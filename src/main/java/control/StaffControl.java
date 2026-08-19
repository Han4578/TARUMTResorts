/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import boundary.HousekeepingTaskUI;
import boundary.StaffBoundary;
import boundary.WalkInBookingBoundary;

/**
 *
 * @author Liew Zheng Han
 */
public class StaffControl {
    private final TierControl tierControl;
    private final RoomAssignControl roomAssignControl;
    private final StaffBoundary staffBoundary = new StaffBoundary();
    private final WalkInBookingControl walkInBookingControl;
    private final HousekeepingTaskUI housekeepingTaskUI;
    
    public StaffControl(TierControl tierControl, RoomAssignControl roomAssignControl, HousekeepingTaskUI housekeepingTaskUI, WalkInBookingControl walkInBookingControl) {
        this.tierControl = tierControl;
        this.roomAssignControl = roomAssignControl;
        this.walkInBookingControl = walkInBookingControl;
        this.housekeepingTaskUI = housekeepingTaskUI;
    }
    
    public void start() {
        while (true) {
            switch (this.staffBoundary.getMenuInput()) {
                case 1 -> new WalkInBookingBoundary(this.walkInBookingControl).startStaffFlow();
                case 2 -> this.roomAssignControl.start();
                case 3 -> this.tierControl.start();
                case 4 -> this.housekeepingTaskUI.start();
                case 6 -> { return; }
                default -> {System.out.println("Not done yet");}
            }
        }
    }
}
