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
    private final RoomAssignControl roomAssignControl;
    private final StaffBoundary staffBoundary = new StaffBoundary();
    private final WalkInBookingControl walkInBookingControl;
    private final HousekeepingTaskControl housekeepingTaskControl;
    private final FrontDeskControl frontDeskControl;
    
    public StaffControl(TierControl tierControl, RoomAssignControl roomAssignControl, HousekeepingTaskControl housekeepingTaskControl, WalkInBookingControl walkInBookingControl, FrontDeskControl frontDeskControl) {
        this.tierControl = tierControl;
        this.roomAssignControl = roomAssignControl;
        this.walkInBookingControl = walkInBookingControl;
        this.housekeepingTaskControl = housekeepingTaskControl;
        this.frontDeskControl = frontDeskControl;
    }
    
    public void start() {
        while (true) {
            switch (this.staffBoundary.getMenuInput()) {
                case 1 -> this.walkInBookingControl.startStaffFlow();
                case 2 -> this.roomAssignControl.start();
                case 3 -> this.tierControl.start();
                case 4 -> this.housekeepingTaskControl.start();
                case 5 -> this.frontDeskControl.frontDeskMainMenu();
                case 6 -> { return; }
                default -> {System.out.println("Not done yet");}
            }
        }
    }
}
