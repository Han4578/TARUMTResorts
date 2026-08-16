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
public class RoomRepository implements Serializable {
    private final SortedListInterface<Room> rooms = new SortedArrayList<>();

    public boolean checkAvailability(Reservation reservation) {
        for (Room room: this.rooms) {
            if (room.canAssign(reservation)) return true;
        }
        
        return false;
    }

    public SortedListInterface<Room> getRooms() {
        return this.rooms;
    }

    public Room getRoom(int roomNumber) {
        int index = this.getRooms().binarySearch(r -> r.getRoomNumber() <= roomNumber);
        if (index == this.rooms.size() || this.rooms.get(index).getRoomNumber() != roomNumber) return null;
        return this.rooms.get(index);
    }
}
