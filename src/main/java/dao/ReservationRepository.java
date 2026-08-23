/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.Serializable;
import adt.DictionaryInterface;
import adt.HashedDictionary;
import entity.Reservation;
import entity.Room;
import adt.ArrayList;
import adt.ListInterface;


/**
 *
 * @author chao_
 */
public class ReservationRepository implements Serializable {
    private final DictionaryInterface<String, Room> roomTable = new HashedDictionary<>();
    private final DictionaryInterface<String, Reservation> reservationTable = new HashedDictionary<>();
    
    public void addToResersevationTable(String confirmNo, Reservation reservation) {
        this.reservationTable.insert(confirmNo, reservation);
    }
    
    public void addToRoomTable(String confirmNo, Room room) {
        this.roomTable.insert(confirmNo, room);
    }
    
    public void removeFromRoomTable(String confirmNo) {
        this.roomTable.remove(confirmNo);
    }
            
    public DictionaryInterface<String, Reservation> getReservationTable() {
        return this.reservationTable;
    }
    
    public DictionaryInterface<String, Room> getRoomTable() {
        return this.roomTable;
    }
    
}

