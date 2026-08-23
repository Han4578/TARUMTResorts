/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author Tan Kah Chao
 */
public class CheckIn implements Serializable{
    
    private static int tempID = 1;
    private String checkInID;
    private Reservation reservation;
    private Room room;
    private Bill bill;
    private String remark;
    private boolean checkOut = false;
    
    
    public CheckIn() {
        this.checkInID = String.format("B%04d", tempID);
        tempID++;
        
    }
    
    public CheckIn(Reservation reservation, Room room, Bill bill) {
        this.checkInID = String.format("B%04d", tempID);
        this.reservation = reservation;
        this.room = room;
        this.bill = bill;
        tempID++;
    }
    
    public String getCheckInID() {
        return checkInID;
    }

    public void setCheckInID(String checkInID) {
        this.checkInID = checkInID;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Bill getBill() {
        return bill;
    }
    
    public void setBill(Bill bill) {
        this.bill = bill;
    }
   
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public boolean isCheckout() {
        return checkOut;
    }
    
    public void setCheckOut(boolean checkOut) {
        this.checkOut = checkOut;
    }
    
    public static void setTempID(int nextID) {
        tempID = nextID;
    }
    
}
