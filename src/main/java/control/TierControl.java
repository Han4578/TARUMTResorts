/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.SortedListInterface;
import boundary.TierBoundary;
import entity.Reservation;
import entity.Room;
import entity.RoomRepository;
import entity.Tier;
import entity.TierRepository;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import tarumtresorts.TARUMTResorts;
import utility.Util;

/**
 *
 * @author Liew Zheng Han
 */
public class TierControl {
    private final TierBoundary tierBoundary = new TierBoundary();
    private final TierRepository tierRepository;
    private final RoomRepository roomRepository;
    
    public TierControl(TierRepository tierRepository, RoomRepository roomRepository) {
        this.tierRepository = tierRepository;
        this.roomRepository = roomRepository;
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
        LocalDate startDate = this.tierBoundary.getStartDate();
        LocalDate endDate = this.tierBoundary.getEndDate();
        
        if (startDate.isAfter(endDate)) {
            this.tierBoundary.invalidDateRange();
            return;
        }
        
        SortedListInterface<Tier> tiers = this.tierRepository.getTiers();
        
        int[] reservationCount = new int[tiers.size()];
        int[] reservationDaysTotal = new int[tiers.size()];
        
        for (Room room: this.roomRepository.getRooms()) {
            SortedListInterface<Reservation> reservations = room.getReservations();
            
            for (int i = reservations.binarySearch(r -> !startDate.isAfter(r.getEndDate())); i < reservations.size(); ++i) {
                Reservation reservation = reservations.get(i);
                if (reservation.getStartDate().isAfter(endDate)) break;
                
                int j = tiers.indexOf(reservation.getCustomer().getTier());
                
                reservationCount[j] += 1;
                reservationDaysTotal[j] += (reservation.getStartDate().isBefore(startDate)? startDate: reservation.getStartDate()).until(reservation.getEndDate().isBefore(endDate)? reservation.getEndDate(): endDate, DAYS) + 1;
            }
        }
        
        ReportTier[] reportTiers = new ReportTier[tiers.size()];
        
        for (int i = 0; i < tiers.size(); i++) {
            reportTiers[i] = new ReportTier(tiers.get(i), reservationCount[i], reservationDaysTotal[i], (float) reservationDaysTotal[i] / tiers.size(), reservationCount[i] > 0? (float) reservationDaysTotal[i] / reservationCount[i]: 0);
        }
        
        boolean ascending = true;
        int lastChoice = -1;
        
        while (true) {
            this.tierBoundary.showAnnualReport(reportTiers, startDate, endDate);
            
            int choice = this.tierBoundary.getReportOption();
            if (lastChoice != choice) ascending = true;
            else ascending = ! ascending;
            lastChoice = choice;
            
            switch (choice) {
                case 1 -> { // Sort by name
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.tier.getName().compareTo(b.tier.getName()) <= 0);
                   else Util.bubbleSort(reportTiers, (a, b) -> a.tier.getName().compareTo(b.tier.getName()) >= 0);
                }
                case 2 -> { // Sort by priority
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.tier.getPriority() <= b.tier.getPriority());
                   else Util.bubbleSort(reportTiers, (a, b) -> a.tier.getPriority() >= b.tier.getPriority());
                }
                case 3 -> { // Sort by reservation count
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.reservationCount <= b.reservationCount);
                   else Util.bubbleSort(reportTiers, (a, b) -> a.reservationCount >= b.reservationCount);
                }
                case 4 -> { // Sort by total reservation days
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.totalReservationDays <= b.totalReservationDays);
                   else Util.bubbleSort(reportTiers, (a, b) -> a.totalReservationDays >= b.totalReservationDays);
                }
                case 5 -> { // Sort by average reservation days per room
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.averageDaysRoom <= b.averageDaysRoom);
                   else Util.bubbleSort(reportTiers, (a, b) -> a.averageDaysRoom >= b.averageDaysRoom);
                }
                case 6 -> { // Sort by average reservation days per reservation
                   if (ascending) Util.bubbleSort(reportTiers, (a, b) -> a.averageDaysReservation <= b.averageDaysReservation);
                   else Util.bubbleSort(reportTiers, (a, b) -> a.averageDaysReservation >= b.averageDaysReservation);
                }
                case 7 -> { return; }
            }
        }
    }
    
    public record ReportTier(
            Tier tier,
            int reservationCount, 
            int totalReservationDays,
            float averageDaysRoom,
            float averageDaysReservation
        ) {};
}
