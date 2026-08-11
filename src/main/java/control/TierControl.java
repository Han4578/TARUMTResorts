/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.SortedListInterface;
import boundary.TierBoundary;
import entity.Tier;
import entity.TierRepository;
import tarumtresorts.TARUMTResorts;

/**
 *
 * @author Liew Zheng Han
 */
public class TierControl {
    private final TierBoundary tierBoundary = new TierBoundary();
    private final TierRepository tierRepository;
    
    public TierControl(TierRepository tierRepository) {
        this.tierRepository = tierRepository;
    }
    
    public void start() {
        while (true) {
            switch (this.tierBoundary.getMenuChoice()) {
                case 1 -> this.tierBoundary.showTiers(this.tierRepository.getTiers());
                case 2 -> this.addTier();
                case 3 -> this.editTier();
                case 4 -> this.generateReport();
                case 5 -> { return; }
            }
        }
    }
    
    private void addTier() {
        String name = this.tierBoundary.getName();
        int priority = this.tierBoundary.getPriority(this.tierRepository.getTiers().get(-1).getPriority() + 1);        
        
        this.tierRepository.addTier(new Tier(name, priority), this.tierRepository.tierOccupied(priority) && this.tierBoundary.askShift());
        TARUMTResorts.save();
        this.tierBoundary.tierAdded();
    }

    private void editTier() {
        SortedListInterface<Tier> tiers = this.tierRepository.getTiers();
        this.tierBoundary.showTiers(tiers);
        if (tiers.isEmpty()) return;
        
        Tier tier = tiers.get(this.tierBoundary.getIndex(tiers.size()) - 1);
        
        while (true) {
            switch (this.tierBoundary.getEditMenuChoice()) {
                case 1 -> {
                    String name = this.tierBoundary.getNewName();
                    tier.setName(name);
                    this.tierBoundary.nameUpdated();
                    TARUMTResorts.save();
                }
                case 2 -> {
                    int priority = this.tierBoundary.getNewPriority(this.tierRepository.getTiers().get(-1).getPriority() + 1);
                    if (priority != tier.getPriority()) this.tierRepository.updateTierPriority(tier, priority, this.tierRepository.tierOccupied(priority) && this.tierBoundary.askShift());
                    this.tierBoundary.priorityUpdated();
                    TARUMTResorts.save();
                }
                case 3 -> {
                    if (tier.equals(this.tierRepository.getDefaultTier())) {
                        this.tierBoundary.cannotDeleteDefaultTier();
                        break;
                    }
                    if (!this.tierBoundary.confirmDelete()) break;

                    this.tierRepository.removeTier(tier);
                    this.tierBoundary.tierDeleted();
                    TARUMTResorts.save();
                    return;
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    private void generateReport() {
        System.out.println("Not done yet");
    }
}
