/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.BucketPriorityQueue;
import adt.PriorityQueueInterface;
import adt.SortedArrayList;
import adt.SortedListInterface;
import entity.Account;
import entity.Customer;
import entity.Reservation;
import entity.Tier;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class TierRepository implements Serializable {
    private final PriorityQueueInterface<Reservation> queue = new BucketPriorityQueue<>();
    private final SortedListInterface<Tier> tiers = new SortedArrayList<>();
    private final Tier defaultTier = new Tier("Regular", 3);
    private final UserRepository userRepository;
    private int lastTierId = 1;
    
    public TierRepository(UserRepository userRepository) {
        this.tiers.add(this.defaultTier);
        this.userRepository = userRepository;
    }
    
    public SortedListInterface<Tier> getTiers() {
        return this.tiers;
    }

    public Tier getDefaultTier() {
        return this.defaultTier;
    }
    
    public void updateTierPriority(Tier tier, int newPriority, boolean shiftPriority) {
        int currentPriority = tier.getPriority();
        this.tiers.remove(tier);
        tier.setPriority(newPriority);
        if (shiftPriority) { // All tiers >= newPriority += 1
            this.queue.movePriority(currentPriority, newPriority, t -> t.getCustomer().getTier().equals(tier));

            for (int i = this.tiers.binarySearch(t -> newPriority <= t.getPriority()); i < this.tiers.size(); ++i) {
                Tier t = this.tiers.get(i);
                t.setPriority(t.getPriority() + 1);
            }
        } else this.queue.mergePriority(currentPriority, newPriority, t -> t.getCustomer().getTier().equals(tier));
        this.tiers.add(tier);
    }
    
    public boolean tierOccupied(int priority) {
        int index = this.tiers.binarySearch(tier -> Integer.compare(priority, tier.getPriority()) <= 0);
        
        return index < this.tiers.size() && this.tiers.get(index).getPriority() == priority;
    }

    public void addTier(Tier tier, boolean shiftOtherTierPriorities) {
        tier.setTierId(this.lastTierId++);
        
        if (shiftOtherTierPriorities) {
            int insertPriority = (this.tiers.isEmpty())? 0: this.tiers.get(-1).getPriority() + 1;
            
            this.queue.movePriority(insertPriority, tier.getPriority());
            
            for (int i = this.tiers.binarySearch(tier); i < this.tiers.size(); ++i) {
                this.tiers.get(i).setPriority(this.tiers.get(i).getPriority() + 1);
            }
        }
        
        this.tiers.add(tier);
    }
    
    public void removeTier(Tier tier) {
        if (tier.equals(this.defaultTier)) return;
        
        this.tiers.remove(tier);
        // Move tier appointments to default tier
        this.queue.movePriority(tier.getPriority(), this.defaultTier.getPriority(), appointment -> appointment.getCustomer().getTier().equals(tier));
        
        for (Account account: this.userRepository.getUsers().getValues()) {
            if (account instanceof Customer customer && customer.getTier().equals(tier)) {
                customer.setTier(this.defaultTier);
            }
        }
    }
    
    public PriorityQueueInterface<Reservation> getQueue() {
        return this.queue;
    }
}
