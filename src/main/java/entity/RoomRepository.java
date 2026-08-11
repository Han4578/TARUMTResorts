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
public class RoomRepository implements Serializable{
    private final SortedListInterface<Room> rooms = new SortedArrayList<>();
    
    public void assignRoom(Reservation reservation, int roomNumber) {
        int index = this.rooms.binarySearch(room -> room.getRoomNumber() <= roomNumber);
        
        if (index == this.rooms.size() || this.rooms.get(index).getRoomNumber() != roomNumber) throw new IllegalArgumentException("Room number not found");
        
        Room room = this.rooms.get(index);
        
        if (!room.canAssign(reservation)) throw new IllegalArgumentException("Room is occupied");
        
        room.addReservation(reservation);
    }
}
