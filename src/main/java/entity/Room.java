/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class Room implements Serializable, Comparable<Room> {
    private int roomNumber;
    private final SortedListInterface<Reservation> reservations = new SortedArrayList<>();
    
    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
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
        int index = this.reservations.binarySearch(r -> reservation.getStartDate().compareTo(r.getEndDate()) <= 0);
                
        Reservation reservation2 = this.reservations.get(index);

        return (reservation.getEndDate().compareTo(reservation2.getStartDate()) < 0);
    }

    void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
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
