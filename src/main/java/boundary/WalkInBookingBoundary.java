/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import control.WalkInBookingControl;
import entity.Customer;
import tarumtresorts.TARUMTResorts;
import utility.Input;
/**
 *
 * @author louos
 */
public class WalkInBookingBoundary {
    
    private final WalkInBookingControl control;
    
    public WalkInBookingBoundary(WalkInBookingControl control) {
        this.control = control;
    }
    
    // for customer to crud reservation
    public void startCustomerFlow(Customer customer) {
        while (true) {
            System.out.println("\n[YOUR BOOKING STATUS]: " + control.getMyReservationStatus(customer.getEmail()));
            
            int choice = Input.getIntInput(
                    """
                    
                    --- Manage Reservation ---
                    1. New Room Booking
                    2. Update Stay Days
                    3. Cancel Booking
                    4. Back
                    
                    Input: \
                    """, 1, 4);
            
            switch (choice) {
                case 1 -> {
                    int days = Input.getIntInput("Enter Number of Days to Stay: ", 1, 365);
                    System.out.println("\n[SYSTEM]: " + control.registerWalkIn(customer, days));
                    TARUMTResorts.save();
                }
                case 2 -> {
                    if (control.getMyReservationStatus(customer.getEmail()).contains("do not have")) {
                        System.out.println("\n[SYSTEM]: You cannot update because you don't have a reservation!");
                        break;
                    }
                    int days = Input.getIntInput("Enter new Number of Days to Stay: ", 1, 365);
                    System.out.println("\n[SYSTEM]: " + control.updateStayDays(customer.getEmail(), days));
                    TARUMTResorts.save();
                }
                case 3 -> {
                    if (control.getMyReservationStatus(customer.getEmail()).contains("do not have")) {
                        System.out.println("\n[SYSTEM]: You cannot cancel because you don't have a reservation!");
                        break;
                    }
                    System.out.println("\n[SYSTEM]: " + control.cancelWalkInReservation(customer));
                    TARUMTResorts.save();
                }
                case 4 -> {
                    return; 
                }
            }
        }
    }
    
    // staff/front desk menu
    public void startStaffFlow() {
        while (true) {
            int choice = Input.getIntInput(
                    """
                    
                    --- Manage Walk-in Queue ---
                    1. Process Next Guest in Queue
                    2. Generate Waitlist Report
                    3. Back
                    
                    Input: \
                    """, 1, 3);
            
            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Processing Queue ---");
                    System.out.println("[SYSTEM]: " + control.processNextInQueue());
                    TARUMTResorts.save();
                }
                case 2 -> {
                    int sortChoice = Input.getIntInput(
                        """
                        Sort Report By:
                        1. Customer Name (A-Z)
                        2. Stay Duration (Shortest to Longest)
                        3. Tier Priority (VIP First)
                        
                        Input: \
                        """, 1, 3);
                        
                    System.out.println("\nGenerating report...");
                    System.out.println(control.generateWaitlistReport(sortChoice));
                }
                case 3 -> {
                    return;
                }
            }
        }
    }
}