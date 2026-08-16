/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import adt.ListInterface;
import control.RoomAssignControl;
import entity.Reservation;
import entity.Room;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import utility.Input;
import utility.Util;

/**
 *
 * @author Liew Zheng Han
 */
public class RoomAssignBoundary {
    public int getMenuOption() {
        return Input.getIntInput(
                """
                1. Manage Allocation Queue
                2. Assign Next In Line
                3. Assign Next Available
                4. Assign Next Available Without Conflict
                5. Unassign Room
                6. Generate Annual Report
                7. View Room Availability
                8. Back
                
                Input: \
                """, 1, 8);
    }
    
    public int getQueueOptions() {
        return Input.getIntInput(
                """
                1. Load More
                2. Filter By Name
                3. Filter By Email
                4. Filter By Date Range
                5. Reset
                6. Select From List
                7. Select Next Available
                8. Back
                
                Tnput: \
                """, 1, 8);
    }
    
    public int getReservationOption() {
        return Input.getIntInput(
                """
                1. Assign Room
                2. Remove From Queue
                3. Back
                
                Tnput: \
                """, 1, 3);
    }
    
    public void showRooms(ListInterface<Room> rooms) {
        System.out.println("No. Room Numbers");
        
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println("%-3s %d".formatted(i + 1, rooms.get(i).getRoomNumber()));
        }
    }
    
    public int getAvailableRoomChoice(ListInterface<Room> rooms) {
        this.showRooms(rooms);
        return Input.getIntInput("\nChoose an available room, 0 to cancel [0-%d]: ".formatted(rooms.size()), 0, rooms.size());
    }
    
    public void noAvailableRooms() {
        System.out.println("No available rooms");
    }
    
    public void showQueueHeader() {
        System.out.println("%-6s %-10s %-10s %-11s %-11s %-10s %s".formatted(
                "No. ", 
                "Name", 
                "Email", 
                "Start Date", 
                "End Date",
                "Tier",
                "Priority"
        ));
    }
    
    public void showQueue(Reservation reservation, int numbering) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy");
        
        System.out.println("%-6d %-10s %-10s %-11s %-11s %-10s %d".formatted(
                numbering, 
                Util.ellipsis(reservation.getCustomer().getName(), 10), 
                Util.ellipsis(reservation.getCustomer().getEmail(), 10), 
                format.format(reservation.getStartDate()),
                format.format(reservation.getEndDate()),
                Util.ellipsis(reservation.getCustomer().getTier().getName(), 10),
                reservation.getCustomer().getTier().getPriority()
        ));
    }
    
    public void showReservation(Reservation reservation) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy");
        System.out.println(
                """
                Customer name: %s
                Customer email: %s
                Customer tier: %s
                Start date: %s
                End date: %s
                """.formatted(
                reservation.getCustomer().getName(), 
                reservation.getCustomer().getEmail(), 
                reservation.getCustomer().getTier().getName(),
                format.format(reservation.getStartDate()),
                format.format(reservation.getEndDate())
                ));
    }
    
    public int selectFromList(int maxValue) {
        return Input.getIntInput("Enter list number [1 - %d]: ".formatted(maxValue), 1, maxValue);
    }
    
    public String getName() {
        return Input.getStringInput("Enter name: ");
    }
    
    public String getEmail() {
        return Input.getStringInput("Enter email: ");
    }
    
    public LocalDate getStartDate() {
        while (true) {
            try {
                String date = Input.getStringInput("Enter start date (dd/mm/yyyy, optional): ");
                if (date.isBlank()) return LocalDate.MIN;
                
                DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(date, format);
            } catch (DateTimeParseException e){}
        }
    }
    
    public LocalDate getEndDate() {
        while (true) {
            try {
                String date = Input.getStringInput("Enter end date (dd/mm/yyyy, optional): ");
                if (date.isBlank()) return LocalDate.MAX;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(date, format);
            } catch (DateTimeParseException e){}
        }
    }
    
    public int getRoomNumber() {
        return Input.getIntInput("Enter room number: ");
    }
    
    public LocalDate getReservationDate() {
        while (true) {
            try {
                String date = Input.getStringInput("Enter reserved date (dd/mm/yyyy, any date within reservation range, leave blank to cancel): ");
                if (date.isBlank()) return null;
                
                DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(date, format);
            } catch (DateTimeParseException e){}
        }
    }

    public void invalidDateRange() {
        System.out.println("Start date cannot be after end date");
    }
    
    public boolean confirmRemoveQueue() {
        return Input.getBooleanInput("Confirm remove from queue? [y/n]: ");
    }
    
    public void removeQueueSuccess() {
        System.out.println("Reservation removed from queue");
    }
    
    public boolean confirmUnassignRoom() {
        return Input.getBooleanInput("Confirm unassign room? [y/n]: ");
    }
    
    public void unassignRoomSuccess() {
        System.out.println("Room has been unassigned");
    }

    public void roomAssignSuccess() {
        System.out.println("Room has been assigned");
    }

    public void noAvailableReservations() {
        System.out.println("No available reservations found");
    }

    public void reservationNotFound() {
        System.out.println("Reservation not found");
    }

    public void roomNotFound() {
        System.out.println("Room not found");
    }

    public void noReservations() {
        System.out.println("No reservations");
    }

    public boolean confirmFindNext() {
        return Input.getBooleanInput("Find Next? [y/n]: ");
    }

    public int getYear() {
        return Input.getIntInput("Enter year: ", 0, 10000);
    }

    public void showAnnualReport(RoomAssignControl.ReportMonth[] reportMonths, int year) {
        String title = "Annual Room Reservation Report %d".formatted(year);
        System.out.println(" ".repeat(29) + title);
        
        System.out.println("%-5s|%17s|%19s|%21s|%28s".formatted(
                "Month", 
                "Reservation Count", 
                "Total Reserved Days", 
                "Average Days Per Room", 
                "Average Days Per Reservation"
        ));
        
        int reservationCount = 0;
        int totalReservationDays = 0;
        float averageDaysRoom = 0;
        float averageDaysReservation = 0;
        
        for (RoomAssignControl.ReportMonth reportMonth: reportMonths) {
            System.out.println("%-5s|%17d|%19d|%21.02f|%28.02f".formatted(
                    reportMonth.month().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), 
                    reportMonth.reservationCount(), 
                    reportMonth.totalReservationDays(), 
                    reportMonth.averageDaysRoom(), 
                    reportMonth.averageDaysReservation()
            ));
            
            reservationCount += reportMonth.reservationCount();
            totalReservationDays += reportMonth.totalReservationDays();
            averageDaysRoom += reportMonth.averageDaysRoom();
            averageDaysReservation += reportMonth.averageDaysReservation();
        }
        
        System.out.println("-".repeat(94));
        
        System.out.println("%-5s|%17d|%19d|%21.02f|%28.02f".formatted(
            "Total",
            reservationCount,
            totalReservationDays,
            averageDaysRoom,
            averageDaysReservation
        ));
        
    }

    public int getReportOption() {
        return Input.getIntInput(
                """
                1. Sort by month                 
                2. Sort by reservation count                     
                3. Sort by reserved days
                4. Sort by average days per room         
                5. Sort by average days per reservation 
                6. Back
                
                Input: \
                """, 1, 6);
    }
}
