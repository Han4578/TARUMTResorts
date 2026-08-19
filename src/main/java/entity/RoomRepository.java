/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.DoubleHashingTable;
import adt.TableInterface;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class RoomRepository implements Serializable {
    private final TableInterface<Integer, Room> rooms = new DoubleHashingTable<>();
    
    public RoomRepository() {
        for (int roomNumber = 101; roomNumber <= 110; ++roomNumber) {
            this.rooms.insert(roomNumber, new Room(roomNumber));
        }
    }

    public boolean checkAvailability(Reservation reservation) {
        for (Room room: this.rooms.getValues()) {
            if (room.canAssign(reservation)) return true;
        }
        
        return false;
    }

    public TableInterface<Integer, Room> getRooms() {
        return this.rooms;
    }

    public Room getRoom(int roomNumber) {
        return this.rooms.get(roomNumber);
    }
}
