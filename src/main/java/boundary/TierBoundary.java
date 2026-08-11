/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import adt.SortedListInterface;
import entity.Tier;
import utility.Input;

/**
 *
 * @author Liew Zheng Han
 */
public class TierBoundary {
    public int getMenuChoice() {
        return Input.getIntInput(
                """
                1. View Tiers
                2. Add Tier
                3. Edit Tier
                4. Generate Report
                5. Back
                
                Input: \
                """, 1, 5
        );
    }

    public void showTiers(SortedListInterface<Tier> tiers) {
        if (tiers.isEmpty()) System.out.println("No tiers added");
        
        System.out.println("%-6s %-10s %-8s".formatted("No.", "Name", "Priority"));
        for (int i = 0; i < tiers.size(); ++i) {
            Tier tier = tiers.get(i);
            System.out.println("%-6s %-10s %-8s".formatted("" + (i + 1), tier.getName(), tier.getPriority()));
        }
    }
    
    public String getName() {
        return Input.getStringInput("Enter tier name: ");
    }
    
    public String getNewName() {
        return Input.getStringInput("Enter new tier name: ");
    }
    
    public int getPriority(int maxPriority) {
        return Input.getIntInput("Enter tier priority between 0 (highest) to %d (lowest): ".formatted(maxPriority), 0, maxPriority);
    }
    
    public int getNewPriority(int maxPriority) {
        return Input.getIntInput("Enter new tier priority between 0 (highest) to %d (lowest): ".formatted(maxPriority));
    }
    
    public int getIndex(int max) {
        return Input.getIntInput("Enter tier number [1 to %d]: ".formatted(max));
    }
    
    public boolean askShift() {
        return Input.getBooleanInput("Another tier has the same priority, would you like to shift the other tiers back? [y/n]: ");
    }

    public void tierAdded() {
        System.out.println("Tier added");
    }

    public int getEditMenuChoice() {
        return Input.getIntInput(
                """
                1. Edit Name
                2. Edit Priority
                3. Delete Tier
                4. Back                                 
                """, 1, 4);
    }

    public void nameUpdated() {
        System.out.println("Name has been updated");
    }

    public void priorityUpdated() {
        System.out.println("Priority has been updated");
    }

    public boolean confirmDelete() {
        return Input.getBooleanInput("Are you sure you want to delete this tier? All appointments of this tier in queue will return to default priority [y/n]:");
    }

    public void tierDeleted() {
        System.out.println("Tier has been deleted");
    }

    public void cannotDeleteDefaultTier() {
        System.out.println("Default tier cannot be deleted");
    }
}
