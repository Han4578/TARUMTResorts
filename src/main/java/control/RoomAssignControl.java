/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.ArrayList;
import adt.ListInterface;
import adt.PriorityQueueInterface;
import adt.SortedListInterface;
import boundary.RoomAssignBoundary;
import dao.RoomRepository;
import dao.TierRepository;
import entity.Reservation;
import entity.Room;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import tarumtresorts.TARUMTResorts;
import utility.Util;

/**
 *
 * @author Liew Zheng Han
 */
public class RoomAssignControl {
    private final RoomAssignBoundary roomAssignBoundary = new RoomAssignBoundary();
    private final RoomRepository roomRepository;
    private final TierRepository tierRepository;
    
    public RoomAssignControl(RoomRepository roomRepository, TierRepository tierRepository) {
        this.roomRepository = roomRepository;
        this.tierRepository = tierRepository;
    }
    
    public void start() {
        while (true) {
            switch (this.roomAssignBoundary.getMenuOption()) {
                case 1 -> this.startQueueMenu();
                case 2 -> this.assignNextInLine();
                case 3 -> this.assignNextAvailable();
                case 4 -> this.assignNextAvailableWithoutConflict();
                case 5 -> this.unassignRoom();
                case 6 -> this.generateAnnualReport();
                case 7 -> this.viewRoomAvailability();
                case 8 -> { return; }
            }
        }
    }
    
    private void startQueueMenu() {
        PriorityQueueInterface<Reservation> queue = this.tierRepository.getQueue();
        ListInterface<Reservation> reservations = new ArrayList<>(queue.size());
        int index = 0;
        
        for (Reservation reservation: queue) reservations.add(reservation);
        
        while (true) {
            if (index + 10 > reservations.size()) index = Integer.max(0, reservations.size() - 10);
                
            this.roomAssignBoundary.showQueueHeader();
        
            for (int j = 0; j < 10 && index < reservations.size(); ++j) { // Show up to 10 reservations
                this.roomAssignBoundary.showQueue(reservations.get(index), index + 1);
                ++index;
            }

            this.roomAssignBoundary.showQueueFooter();
            
            switch (this.roomAssignBoundary.getQueueOptions()) {
                case 1 -> { // Load more
                }
                case 2 -> { // Filter by name
                    String name = this.roomAssignBoundary.getName().toLowerCase();
                    
                    ListInterface<Reservation> filteredReservations = new ArrayList<>();
                    
                    for (Reservation reservation: reservations) {
                        if (reservation.getCustomer().getName().toLowerCase().contains(name)) filteredReservations.add(reservation);
                    }
                    
                    reservations = filteredReservations;
                    index = 0;
                }
                case 3 -> { // Filter by email
                    String email = this.roomAssignBoundary.getEmail().toLowerCase();
                    
                    ListInterface<Reservation> filteredReservations = new ArrayList<>();
                    
                    for (Reservation reservation: reservations) {
                        if (reservation.getCustomer().getEmail().contains(email)) filteredReservations.add(reservation);
                    }
                    
                    reservations = filteredReservations;
                    index = 0;
                }
                case 4 -> { // Filter by date range
                    LocalDate startDate = this.roomAssignBoundary.getStartDate();
                    LocalDate endDate = this.roomAssignBoundary.getEndDate();
                    
                    if (startDate.isAfter(endDate)) {
                        this.roomAssignBoundary.invalidDateRange();
                        continue;
                    }
                    
                    ListInterface<Reservation> filteredReservations = new ArrayList<>();
                    
                    for (Reservation reservation: reservations) {
                        // If fully within date range
                        if (!reservation.getStartDate().isBefore(startDate) && !reservation.getEndDate().isAfter(endDate)) 
                            filteredReservations.add(reservation);
                    }
                    
                    reservations = filteredReservations;
                    index = 0;
                }
                case 5 -> { // Reset
                    reservations = new ArrayList<>(queue.size());
                    
                    for (Reservation reservation: queue) reservations.add(reservation);
                    
                    index = 0;
                }
                case 6 -> { // Select from list
                    if (index == 0) {
                        this.roomAssignBoundary.noReservations();
                        continue;
                    }
                    Reservation chosen = reservations.get(this.roomAssignBoundary.selectFromList(index) - 1);
                    
                    if (this.reservationActions(chosen)) return;
                }
                case 7 -> { // Select next available
                    boolean found = false;
                    for (Reservation reservation: reservations) {
                        if (roomRepository.checkAvailability(reservation)) {
                            if (this.reservationActions(reservation)) return;
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) this.roomAssignBoundary.noAvailableReservations();
                }
                case 8 -> { return; }
            }
        }
    }
        
    private boolean reservationActions(Reservation reservation) {
        while (true) {
            this.roomAssignBoundary.showReservation(reservation);
            
            switch (this.roomAssignBoundary.getReservationOption()) {
                case 1 -> { // Assign room
                    if (this.assignRoom(reservation)) {
                        this.tierRepository.getQueue().clear(reservation.getCustomer().getTier().getPriority(), r -> r.equals(reservation));
                        TARUMTResorts.save();
                        this.roomAssignBoundary.roomAssignSuccess();
                        return true;
                    }
                }
                case 2 -> { // Remove from queue
                    if (!this.roomAssignBoundary.confirmRemoveQueue()) continue;
                    
                    this.tierRepository.getQueue().clear(reservation.getCustomer().getTier().getPriority(), r -> r.equals(reservation));
                        TARUMTResorts.save();
                    this.roomAssignBoundary.removeQueueSuccess();
                    return true;
                }
                case 3 -> { return false; }
            }
        }
    }
    
    private void assignNextAvailable() {
        Iterator<Reservation> iterator = this.tierRepository.getQueue().iterator();
        
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            
            if (this.roomRepository.checkAvailability(reservation)) {
                this.roomAssignBoundary.showReservation(reservation);
                
                if (this.assignRoom(reservation)) {
                    iterator.remove();
                    TARUMTResorts.save();
                    this.roomAssignBoundary.roomAssignSuccess();
                }
                
                if (!this.roomAssignBoundary.confirmFindNext()) return;
            }
        }
        
        this.roomAssignBoundary.noAvailableReservations();
    }

    private void assignNextAvailableWithoutConflict() {
        Iterator<Reservation> iterator = this.tierRepository.getQueue().iterator();
        ListInterface<DateRange> conflictDateRanges = new ArrayList<>();
        
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            
            boolean conflict = !this.roomRepository.checkAvailability(reservation);
            
            if (!conflict) {
                for (DateRange range: conflictDateRanges) {
                    if (range.start.isAfter(reservation.getEndDate()) || range.end.isBefore(reservation.getStartDate())) continue;
                    conflict = true;
                    break;
                }
            }
            
            if (!conflict) {
                this.roomAssignBoundary.showReservation(reservation);
                
                if (this.assignRoom(reservation)) {
                    iterator.remove();
                    TARUMTResorts.save();
                    this.roomAssignBoundary.roomAssignSuccess();
                    
                } else conflict = true;
                
                if (!this.roomAssignBoundary.confirmFindNext()) return;
            } 
            
            if (conflict) {
                boolean found = false;
                for (DateRange range: conflictDateRanges) {
                    if (range.start.isAfter(reservation.getEndDate()) || range.end.isBefore(reservation.getStartDate())) continue;
                    
                    // If any range overlaps, update the range to include reservation range. Only 1 range needs to be updated for conflict coverage
                    found = true;
                    if (range.start.isAfter(reservation.getStartDate())) range.start = reservation.getStartDate();
                    if (range.end.isBefore(reservation.getEndDate())) range.end = reservation.getEndDate();
                    break;
                }
                
                if (!found) conflictDateRanges.add(new DateRange(reservation.getStartDate(), reservation.getEndDate()));
            }
        }
        
        this.roomAssignBoundary.noAvailableReservations();
    }
    
    private void unassignRoom() {        
        Room room = this.roomRepository.getRoom(this.roomAssignBoundary.getRoomNumber());
        
        if (room == null) {
            this.roomAssignBoundary.roomNotFound();
            return;
        }
        
        LocalDate reservationDate = this.roomAssignBoundary.getReservationDate();
        
        if (reservationDate == null) return;
        
        SortedListInterface<Reservation> reservations = room.getReservations();
        
        int index = reservations.binarySearch(r -> !reservationDate.isAfter(r.getEndDate()));

        if (index == reservations.size() || reservations.get(index).getStartDate().isAfter(reservationDate)) {
            this.roomAssignBoundary.reservationNotFound();
            return;
        }
        
        this.roomAssignBoundary.showReservation(reservations.get(index));
        
        if (!this.roomAssignBoundary.confirmUnassignRoom()) return;
        
        reservations.remove(index);
        TARUMTResorts.save();
        this.roomAssignBoundary.unassignRoomSuccess();
    }
    
    private void viewRoomAvailability() {
        LocalDate minDate = this.roomAssignBoundary.getStartDate();
        LocalDate maxDate = this.roomAssignBoundary.getEndDate();
        
        ListInterface<Room> availableRooms = new ArrayList<>();
        
        for (Room room: this.roomRepository.getRooms()) {
            if (room.canAssign(minDate, maxDate)) availableRooms.add(room);
        }
        
        this.roomAssignBoundary.showRooms(availableRooms);
    }

    private void assignNextInLine() {
        PriorityQueueInterface<Reservation> queue = this.tierRepository.getQueue();
        
        if (!queue.isEmpty()) {
            Reservation reservation = queue.peek();
            
            this.roomAssignBoundary.showReservation(reservation);
            
            if (this.assignRoom(reservation)) {
                queue.pop();
                TARUMTResorts.save();
                this.roomAssignBoundary.roomAssignSuccess();
            }
        } else this.roomAssignBoundary.noReservations();
    }
    
    public boolean assignRoom(Reservation reservation) {
        ListInterface<Room> availableRooms = new ArrayList<>();
                    
        for (Room room: this.roomRepository.getRooms()) {
            if (room.canAssign(reservation)) availableRooms.add(room);
        }
        
        if (availableRooms.isEmpty()) {
            this.roomAssignBoundary.noAvailableRooms();
            return false;
        }

        int choice = this.roomAssignBoundary.getAvailableRoomChoice(availableRooms);

        if (choice == 0) return false;

        availableRooms.get(choice - 1).addReservation(reservation);
        
        return true;
    }

    private void generateAnnualReport() {
        int year = this.roomAssignBoundary.getYear();
                
        int[] reservationCount = new int[12];
        int[] reservationDaysTotal = new int[12];
        LocalDate[] months = new LocalDate[13];
        
        months[0] = LocalDate.of(year, Month.JANUARY, 1);
        
        for (int i = 1; i < 13; i++) {
            months[i] = months[i - 1].plusMonths(1);
        }
        
        for (Room room: this.roomRepository.getRooms()) {
            SortedListInterface<Reservation> reservations = room.getReservations();
            
            for (int i = reservations.binarySearch(r -> !months[0].isAfter(r.getEndDate())); i < reservations.size(); ++i) {
                Reservation reservation = reservations.get(i);
                if (!reservation.getStartDate().isBefore(months[12])) break;
                
                for (int m = reservation.getStartDate().getMonthValue() - 1; m < reservation.getEndDate().getMonthValue(); ++m) {
                    reservationCount[m] += 1;
                    reservationDaysTotal[m] += (reservation.getEndDate().isBefore(months[m + 1])? reservation.getEndDate(): months[m + 1].minusDays(1)).getDayOfMonth() - (reservation.getStartDate().isBefore(months[m])? months[m]: reservation.getStartDate()).getDayOfMonth() + 1;
                }
            }
        }
        
        ReportMonth[] reportMonths = new ReportMonth[12];
        int roomCount = this.roomRepository.getRooms().size();
        
        for (int i = 0; i < 12; i++) {
            reportMonths[i] = new ReportMonth(months[i], reservationCount[i], reservationDaysTotal[i], (float) reservationDaysTotal[i] / roomCount, reservationCount[i] > 0? (float) reservationDaysTotal[i] / reservationCount[i]: 0);
        }
        
        boolean ascending = true;
        int lastChoice = -1;
        
        while (true) {
            this.roomAssignBoundary.showAnnualReport(reportMonths, year);
            
            int choice = this.roomAssignBoundary.getReportOption();
            if (lastChoice != choice) ascending = true;
            else ascending = ! ascending;
            lastChoice = choice;
            
            switch (choice) {
                case 1 -> { // Sort by months
                   if (ascending) Util.bubbleSort(reportMonths, (a, b) -> a.month.isBefore(b.month));
                   else Util.bubbleSort(reportMonths, (a, b) -> a.month.isAfter(b.month));
                }
                case 2 -> { // Sort by reservation count
                   if (ascending) Util.bubbleSort(reportMonths, (a, b) -> a.reservationCount <= b.reservationCount);
                   else Util.bubbleSort(reportMonths, (a, b) -> a.reservationCount >= b.reservationCount);
                }
                case 3 -> { // Sort by total reservation days
                   if (ascending) Util.bubbleSort(reportMonths, (a, b) -> a.totalReservationDays <= b.totalReservationDays);
                   else Util.bubbleSort(reportMonths, (a, b) -> a.totalReservationDays >= b.totalReservationDays);
                }
                case 4 -> { // Sort by average reservation days per room
                   if (ascending) Util.bubbleSort(reportMonths, (a, b) -> a.averageDaysRoom <= b.averageDaysRoom);
                   else Util.bubbleSort(reportMonths, (a, b) -> a.averageDaysRoom >= b.averageDaysRoom);
                }
                case 5 -> { // Sort by average reservation days per reservation
                   if (ascending) Util.bubbleSort(reportMonths, (a, b) -> a.averageDaysReservation <= b.averageDaysReservation);
                   else Util.bubbleSort(reportMonths, (a, b) -> a.averageDaysReservation >= b.averageDaysReservation);
                }
                case 6 -> { return; }
            }
        }
    }
    
    
    
    private class DateRange {
        LocalDate start;
        LocalDate end;
        
        private DateRange(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }
    }
    
    public record ReportMonth(
                LocalDate month, 
                int reservationCount, 
                int totalReservationDays,
                float averageDaysRoom,
                float averageDaysReservation
            ) {};
}
