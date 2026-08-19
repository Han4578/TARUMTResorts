/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.LinkedList;
import adt.ListInterface;
import boundary.WalkInBookingBoundary;
import dao.RoomRepository;
import dao.TierRepository;
import dao.UserRepository;
import entity.Customer;
import entity.Reservation;
import entity.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import tarumtresorts.TARUMTResorts;
import utility.Input;

/**
 *
 * @author louos
 */


public class WalkInBookingControl {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final TierRepository tierRepository;
    
    private final ListInterface<Reservation> walkInQueue = new LinkedList<>();
    
    private final WalkInBookingBoundary walkInBookingBoundary = new WalkInBookingBoundary();
    
    public WalkInBookingControl(UserRepository userRepository, RoomRepository roomRepository, TierRepository tierRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.tierRepository = tierRepository;
    }
    
    // for customer to crud reservation
    public void startCustomerFlow(Customer customer) {
        while (true) {
            this.walkInBookingBoundary.bookingStatus(this.getMyReservationStatus(customer.getEmail()));
            
            switch (this.walkInBookingBoundary.getCustomerChoice()) {
                case 1 -> {
                    int days = this.walkInBookingBoundary.getDaysStay();
                    this.walkInBookingBoundary.systemMessage(this.registerWalkIn(customer, days));
                    TARUMTResorts.save();
                }
                case 2 -> {
                    if (this.getMyReservationStatus(customer.getEmail()).contains("do not have")) {
                        this.walkInBookingBoundary.noReservation("update");
                        break;
                    }
                    int days = this.walkInBookingBoundary.getNewDaysStay();
                    this.walkInBookingBoundary.systemMessage(this.updateStayDays(customer.getEmail(), days));
                    TARUMTResorts.save();
                }
                case 3 -> {
                    if (this.getMyReservationStatus(customer.getEmail()).contains("do not have")) {
                        this.walkInBookingBoundary.noReservation("cancel");
                        break;
                    }
                    this.walkInBookingBoundary.systemMessage(this.cancelWalkInReservation(customer));
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
                    this.walkInBookingBoundary.processingQueueTitle();
                    this.walkInBookingBoundary.systemMessage(this.processNextInQueue());
                    TARUMTResorts.save();
                }
                case 2 -> {
                    int sortChoice = this.walkInBookingBoundary.getSortChoice();
                        
                    this.walkInBookingBoundary.showReport(this.generateWaitlistReport(sortChoice));
                }
                case 3 -> {
                    return;
                }
            }
        }
    }
    

    public String getMyReservationStatus(String email) {
        for (int i = 0; i < walkInQueue.size(); i++) {
            Reservation r = walkInQueue.get(i);
            if (r.getCustomer().getEmail().equals(email)) {
                long days = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
                return "You are currently at Position #" + (i + 1) + " in the Walk-in queue. (Requested Stay: " + days + " days)";
            }
        }
        return "You do not have any active reservations in the waiting list.";
    }


    public String registerWalkIn(String name, String email, String phone, int stayDays) {
        Customer customer;
        
        if (userRepository.getUsers().containsKey(email)) {
            customer = (Customer) userRepository.getUsers().get(email);
        } else {
            customer = new Customer(email, "default123", tierRepository.getDefaultTier());
            customer.setName(name);
            customer.setPhoneNumber(phone);
            userRepository.addUser(customer);
        }
        
        return registerWalkIn(customer, stayDays);
    }


    public String registerWalkIn(Customer customer, int stayDays) {
        // avoid duplicate
        for (int i = 0; i < walkInQueue.size(); i++) {
            if (walkInQueue.get(i).getCustomer().getEmail().equals(customer.getEmail())) {
                return "Error: " + customer.getName() + " is already in the waiting list.";
            }
        }
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(stayDays);
        Reservation newReservation = new Reservation(customer, startDate, endDate);
        
        if (roomRepository.checkAvailability(newReservation)) {
            var rooms = roomRepository.getRooms();
            for (Room room: rooms) {
                if (room.canAssign(newReservation)) {
                    room.getReservations().add(newReservation); 
                    return "Success: Room " + room.getRoomNumber() + " assigned to " + customer.getName();
                }
            }
        }
        
        walkInQueue.add(newReservation);
        
        return "Queue: No rooms available. " + customer.getName() + " has been added to the end of the walk-in line.";
    }
    

    public String updateStayDays(String email, int newStayDays) {
        for (int i = 0; i < walkInQueue.size(); i++) {
            Reservation r = walkInQueue.get(i);
            if (r.getCustomer().getEmail().equals(email)) {
                r.setEndDate(r.getStartDate().plusDays(newStayDays));
                return "Success: Stay days updated to " + newStayDays + " for " + r.getCustomer().getName();
            }
        }
        return "Error: Customer with email " + email + " is not currently in the waiting list.";
    }


    public String cancelWalkInReservation(Customer customer) {
        for (int i = 0; i < walkInQueue.size(); i++) {
            if (walkInQueue.get(i).getCustomer().getEmail().equals(customer.getEmail())) {
                walkInQueue.remove(i);
                return "Success: Reservation for " + customer.getEmail() + " has been cancelled.";
            }
        }
        return "Error: Reservation not found.";
    }


    public String processNextInQueue() {
        if (walkInQueue.isEmpty()) {
            return "The waiting list is currently empty.";
        }
        
        int bestIndex = -1;
        Reservation bestReservation = null;
        int highestPriority = Integer.MAX_VALUE;
        
        // find highest tier guest
        for (int i = 0; i < walkInQueue.size(); i++) {
            Reservation r = walkInQueue.get(i);
            int currentPriority = r.getCustomer().getTier().getPriority();
            
            if (currentPriority < highestPriority) {
                highestPriority = currentPriority;
                bestReservation = r;
                bestIndex = i;
            }
        }
        
        if (roomRepository.checkAvailability(bestReservation)) {
             var rooms = roomRepository.getRooms();
             for (Room room: rooms) {
                if (room.canAssign(bestReservation)) {

                    walkInQueue.remove(bestIndex); 
                    room.getReservations().add(bestReservation);
                    return "Success: Room " + room.getRoomNumber() + " assigned to VIP guest " + bestReservation.getCustomer().getName();
                }
            }
        }
        
        return "Still no rooms available for our highest priority guest: " + bestReservation.getCustomer().getName() + ".";
    }
    
    public String generateWaitlistReport(int sortOption) {
        if (walkInQueue.isEmpty()) {
            return "No guests in the waiting list.\n";
        }

        // copy for sorting purpose only, so wont affect original order
        ListInterface<Reservation> list = new LinkedList<>();
        for (int i = 0; i < walkInQueue.size(); i++) {
            list.add(walkInQueue.get(i));
        }
        
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean shouldSwap = false;
                Reservation r1 = list.get(j);
                Reservation r2 = list.get(j + 1);
                
                switch (sortOption) {
                    case 1 -> {
                        // sort by name
                        if (r1.getCustomer().getName().compareToIgnoreCase(r2.getCustomer().getName()) > 0) {
                            shouldSwap = true;
                        }
                    }
                    case 2 -> {
                        // sort from shortest to longest
                        long days1 = ChronoUnit.DAYS.between(r1.getStartDate(), r1.getEndDate());
                        long days2 = ChronoUnit.DAYS.between(r2.getStartDate(), r2.getEndDate());
                        if (days1 > days2) shouldSwap = true;
                    }
                    case 3 -> {
                        // sort by tier
                        if (r1.getCustomer().getTier().getPriority() > r2.getCustomer().getTier().getPriority()) {
                            shouldSwap = true;
                        }
                    }
                }
                
                if (shouldSwap) {
                    list.set(j, r2);
                    list.set(j + 1, r1);
                }
            }
        }
        
        // formatting
        StringBuilder report = new StringBuilder();
        String sortTitle = sortOption == 1 ? "Alphabetically" : (sortOption == 2 ? "By Stay Duration" : "By Tier Priority");
        report.append("=== Walk-In Waiting List Report (Sorted ").append(sortTitle).append(") ===\n");
        
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            long days = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
            report.append(i + 1).append(". Name: ").append(r.getCustomer().getName())
                  .append(" | Tier: ").append(r.getCustomer().getTier().getName())
                  .append(" | Stay: ").append(days).append(" days\n");
        }
        return report.toString();
    }
}