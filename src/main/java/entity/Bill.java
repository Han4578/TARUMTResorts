/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author chao_
 */

import java.time.temporal.ChronoUnit;
import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;

public class Bill implements Serializable{
    
    private static int tempID = 1;
    private String billID;
    private float roomPrice;
    private SortedListInterface<Order> orderList;
    private boolean paid = false;
    
    public Bill() {
        this.billID = String.format("B%04d", tempID);
        this.orderList = new SortedArrayList<>();
        tempID++;
    }
    
    public Bill(String billID, SortedArrayList<Order> orderList) {
        this.billID = billID;
        this.orderList = orderList;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public SortedListInterface<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(SortedListInterface<Order> orderList) {
        this.orderList = orderList;
    }
    
    public float getBillAmount() {
        return calculateBillAmount();
    }
    
    private float calculateBillAmount() {
        float billAmount = 0;
        billAmount += getRoomPrice();
        for(int i = 0; i < orderList.size(); i++) {
            billAmount += orderList.get(i).getSubTotal();
        }
        
        return billAmount;
    }
    
    public float getOrderTotalAmount() {
        float orderTotalAmount = 0;
        for(int i = 0; i < orderList.size(); i++) {
            orderTotalAmount += orderList.get(i).getSubTotal();
        }
        
        return orderTotalAmount;
    }
    
    public void setRoomPrice(long numberOfNights) {
        float roomRate = 150;
        this.roomPrice = numberOfNights * roomRate;
    }
    
    public float getRoomPrice() {
        return roomPrice;
    }
    
    public boolean isPaid() {
        return paid;
    }
    
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    
    public static void setTempID(int nextID) {
        tempID = nextID;
    }
}
