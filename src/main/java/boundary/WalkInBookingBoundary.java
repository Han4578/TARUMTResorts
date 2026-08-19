/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import utility.Input;
/**
 *
 * @author louos
 */
public class WalkInBookingBoundary {
    public int getCustomerChoice() {
        return Input.getIntInput(
            """

            --- Manage Reservation ---
            1. New Room Booking
            2. Update Stay Days
            3. Cancel Booking
            4. Back

            Input: \
            """, 1, 4);    
    }

    public int getDaysStay() {
        return Input.getIntInput("Enter Number of Days to Stay: ", 1, 365);
    }

    public int getNewDaysStay() {
        return Input.getIntInput("Enter New Number of Days to Stay: ", 1, 365);
    }

    public void systemMessage(String message) {
        System.out.println("[SYSTEM]: " + message);
    }

    public void noReservation(String action) {
        this.systemMessage("You cannot %s because you don't have a reservation!".formatted(action));
    }

    public void bookingStatus(String reservationStatus) {
        System.out.println("\n[YOUR BOOKING STATUS]: " + reservationStatus);
    }

    public void processingQueueTitle() {
        System.out.println("\n--- Processing Queue ---");
    }

    public int getSortChoice() {
        return Input.getIntInput(
            """
            Sort Report By:
            1. Customer Name (A-Z)
            2. Stay Duration (Shortest to Longest)
            3. Tier Priority (VIP First)

            Input: \
            """, 1, 3);
    }

    public void showReport(String report) {
        System.out.println("\nGenerating report...");
        System.out.println(report);
    }
    
    
}