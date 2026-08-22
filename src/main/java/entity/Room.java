/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Liew Zheng Han
 */
public class Room implements Serializable, Comparable<Room> {
    private int roomNumber;
    private final SortedListInterface<Reservation> reservations = new SortedArrayList<>();
    private String status;
    
    public Room(int roomNumber) {
        this(roomNumber, "Ready for Check-In");
    }
    
    public Room(int roomNumber, String status) {
        this.roomNumber = roomNumber;
        this.status = status;
    }
    
    public int getRoomNumber() {
        return this.roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public SortedListInterface<Reservation> getReservations() {
        return this.reservations;
    }
    
    public boolean canAssign(Reservation reservation) {
        return this.canAssign(reservation.getStartDate(), reservation.getEndDate());
    }
    
    public boolean canAssign(LocalDate startDate, LocalDate endDate) {
        int index = this.reservations.binarySearch(r -> !startDate.isAfter(r.getEndDate()));
        
        if (index >= this.reservations.size()) return true;
                
        Reservation reservation2 = this.reservations.get(index);

        return (endDate.isBefore(reservation2.getStartDate()));
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + status;
    }

    @Override
    public int compareTo(Room o) {
        return Integer.compare(this.roomNumber, o.getRoomNumber());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Room && ((Room)obj).getRoomNumber() == this.roomNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.roomNumber;
        return hash;
    }    
}
