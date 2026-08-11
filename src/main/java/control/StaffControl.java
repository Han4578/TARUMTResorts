/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import boundary.StaffBoundary;

/**
 *
 * @author Liew Zheng Han
 */
public class StaffControl {
    private final TierControl tierControl;
    private final StaffBoundary staffBoundary = new StaffBoundary();
    
    public StaffControl(TierControl tierControl) {
        this.tierControl = tierControl;
    }
    
    public void start() {
        while (true) {
            switch (this.staffBoundary.getMenuInput()) {
                case 3 -> this.tierControl.start();
                case 6 -> { return; }
                default -> {System.out.println("Not done yet");}
            }
        }
    }
}
