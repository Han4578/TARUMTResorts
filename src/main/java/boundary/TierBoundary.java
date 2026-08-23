/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import adt.SortedListInterface;
import control.TierControl;
import entity.Tier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import utility.Input;
import utility.Util;

/**
 * @author Liew Zheng Han
 */
public class TierBoundary {
    public int getMenuChoice() {
        return Input.getIntInput(
                """
                =====================
                1. View Tiers
                2. Add Tier
                3. Edit Tier
                4. Generate Report
                5. Back
                =====================
                Input: \
                """, 1, 5
        );
    }

    public void showTiers(SortedListInterface<Tier> tiers) {
        if (tiers.isEmpty()) System.out.println("No tiers added");
        
        System.out.println("=".repeat(26));
        System.out.println("%-6s %-10s %-8s".formatted("No.", "Name", "Priority"));
        System.out.println("=".repeat(26));
        for (int i = 0; i < tiers.size(); ++i) {
            Tier tier = tiers.get(i);            
            System.out.println("%-6s %-10s %-8s".formatted("" + (i + 1), Util.ellipsis(tier.getName(), 10), tier.getPriority()));
        }
        System.out.println("=".repeat(26));
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
        System.out.println(
                """
                ===========
                Tier added
                ===========
                """);
    }

    public int getEditMenuChoice() {
        return Input.getIntInput(
                """
                ===================
                1. Edit Name
                2. Edit Priority
                3. Delete Tier
                4. Back                                 
                ===================
                Input: \
                """, 1, 4);
    }

    public void nameUpdated() {
        System.out.println(
                """
                =====================
                Name has been updated
                =====================
                """);
    }

    public void priorityUpdated() {
        System.out.println(
                """
                =========================
                Priority has been updated
                =========================
                """);
    }

    public boolean confirmDelete() {
        return Input.getBooleanInput("Are you sure you want to delete this tier? All reservations of this tier in queue will return to default priority [y/n]:");
    }

    public void tierDeleted() {
        System.out.println(
                """
                ======================
                Tier has been deleted
                ======================
                """);
    }

    public void cannotDeleteDefaultTier() {
        System.out.println(
                """
                ==============================
                Default tier cannot be deleted
                ==============================
                """);
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

    public void showAnnualReport(TierControl.ReportTier[] reportTiers, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String range = "";
        
        if (!startDate.equals(LocalDate.MIN) && endDate.equals(LocalDate.MAX)) range = " From " + format.format(startDate);
        else if (startDate.equals(LocalDate.MIN) && !endDate.equals(LocalDate.MAX)) range = " Until " + format.format(endDate);
        else if (startDate.equals(LocalDate.MIN) && endDate.equals(LocalDate.MAX)) range = " " + format.format(startDate) + " - " + format.format(endDate);
        
        String title = "Annual Tier Reservation Report%s".formatted(range);
        System.out.println(" ".repeat(25) + title);
        
        System.out.println("%-10s|%8s|%17s|%19s|%21s|%28s".formatted(
                "Name", 
                "Priority", 
                "Reservation Count", 
                "Total Reserved Days", 
                "Average Days Per Room", 
                "Average Days Per Reservation"
        ));
        
        int reservationCount = 0;
        int totalReservationDays = 0;
        float averageDaysRoom = 0;
        float averageDaysReservation = 0;
        
        for (TierControl.ReportTier reportTier: reportTiers) {            
            System.out.println("%-10s|%8d|%17d|%19d|%s|%s".formatted(
                    Util.ellipsis(reportTier.tier().getName(), 10), 
                    reportTier.tier().getPriority(), 
                    reportTier.reservationCount(), 
                    reportTier.totalReservationDays(), 
                (reportTier.averageDaysRoom() == 0)? "%21s".formatted("-"): "%21.02f".formatted(reportTier.averageDaysRoom()),
                (reportTier.averageDaysReservation() == 0)? "%28s".formatted("-"): "%28.02f".formatted(reportTier.averageDaysReservation())
            ));
            
            reservationCount += reportTier.reservationCount();
            totalReservationDays += reportTier.totalReservationDays();
            averageDaysRoom += reportTier.averageDaysRoom();
            averageDaysReservation += reportTier.averageDaysReservation();
        }
        
        System.out.println("-".repeat(107));
        
        System.out.println("%-19s|%17d|%19d|%s|%s".formatted(
            "Total",
            reservationCount,
            totalReservationDays,
           (averageDaysRoom == 0)? "%21s".formatted("-"): "%21.02f".formatted(averageDaysRoom),
           (averageDaysReservation == 0)? "%28s".formatted("-"): "%28.02f".formatted(averageDaysReservation)
        ));
            }

    public int getReportOption() {
        return Input.getIntInput(
                """
                =========================================
                1. Sort by name              
                2. Sort by priority              
                3. Sort by reservation count                     
                4. Sort by reserved days
                5. Sort by average days per room         
                6. Sort by average days per reservation 
                7. Back
                =========================================
                Input: \
                """, 1, 7);
    }

    public void invalidDateRange() {
        System.out.println(
                """
                ===================================
                Start date cannot be after end date
                ===================================
                """);
    }
}
