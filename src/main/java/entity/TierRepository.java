/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.BucketPriorityQueue;
import adt.PriorityQueueInterface;
import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class TierRepository implements Serializable {
    private final PriorityQueueInterface<Reservation> queue = new BucketPriorityQueue<>();
    private final SortedListInterface<Tier> tiers = new SortedArrayList<>();
    private final Tier defaultTier = new Tier("Regular", 0);
    private final UserRepository userRepository;
    
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
        this.tiers.add(tier);
        if (shiftPriority) { // All tiers >= newPriority += 1
            this.queue.movePriority(currentPriority, newPriority, t -> t.getCustomer().getTier().equals(tier));

            for (int i = this.tiers.binarySearch(tier); i < this.tiers.size(); ++i) {
                this.tiers.get(i).setPriority(newPriority++);
            }
        } else this.queue.mergePriority(currentPriority, newPriority, t -> t.getCustomer().getTier().equals(tier));
    }
    
    public boolean tierOccupied(int priority) {
        int index = this.tiers.binarySearch(tier -> Integer.compare(priority, tier.getPriority()) <= 0);
        
        return index < this.tiers.size() && this.tiers.get(index).getPriority() == priority;
    }

    public void addTier(Tier tier, boolean shiftOtherTierPriorities) {
        
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
