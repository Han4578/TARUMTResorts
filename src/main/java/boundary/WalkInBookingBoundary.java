/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import adt.ListInterface;
import entity.Room;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public LocalDate getStartDate() {
        while (true) {
            try {
                String date = Input.getStringInput("Enter end date (dd/mm/yyyy): ");
                DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(date, format);
            } catch (DateTimeParseException e){}
        }
    }
    
    public void showRooms(ListInterface<Room> rooms) {
        System.out.println("No. Room Number Status");
        
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println("%-3s %-11d %s".formatted(i + 1, room.getRoomNumber(), room.getStatus()));
        }
    }
    
    public int getAvailableRoomChoice(ListInterface<Room> rooms) {
        this.showRooms(rooms);
        return Input.getIntInput("\nChoose an available room, 0 to cancel [0-%d]: ".formatted(rooms.size()), 0, rooms.size());
    }
}