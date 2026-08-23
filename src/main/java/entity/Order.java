/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


/**
 *
 * @author chao_
 */

import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

        
public class Order implements Serializable, Comparable<Order> {
    
    private static int tempID = 1;
    private String orderID;
    private LocalDateTime orderTime;
    private SortedListInterface<OrderedItem> itemList;
    private String status; 
    
    public Order(){
        this.orderID = String.format("O%05d", tempID);
        this.orderTime = LocalDateTime.now();
        this.status = "Pending";
        tempID++;
    }
    
    public Order(SortedListInterface<OrderedItem> itemList) {
        this.orderID = String.format("O%05d", tempID);
        this.orderTime = LocalDateTime.now();
        this.itemList = itemList;
        this.status = "Pending";
        tempID++;
    }
    public Order(String orderID, LocalDateTime orderTime, SortedArrayList<OrderedItem> itemList) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.itemList = itemList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public SortedListInterface<OrderedItem> getItemList() {
        return itemList;
    }

    public void setItemList(SortedListInterface<OrderedItem> itemList) {
        this.itemList = itemList;
    }
    
    public float getSubTotal() {
        float subTotal = 0;
        for (int i = 0; i < itemList.size(); i++) {
            subTotal += itemList.get(i).getTotal();
        }
        return subTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void updateOrderStatus() {
        LocalDateTime now = LocalDateTime.now();

        long minutesPassed = Duration.between(orderTime, now).toMinutes();

        if (minutesPassed >= 20) {
            status = "Delivered";
        } else if (minutesPassed >= 5) {
            status = "Preparing";
        }
    }
    
    public static void setTempID(int nextID) {
        tempID = nextID;
    }
        
    @Override
    public int compareTo(Order o) {
        return this.getOrderID().compareTo(o.getOrderID());
    }
    
     @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        return true;
    }

    if (!(obj instanceof Order)) {
        return false;
    }

    Order other = (Order) obj;

    return this.orderID.equals(other.orderID);
    }
    
    @Override
    public int hashCode() {
        return orderID.hashCode();
    }
    
}
