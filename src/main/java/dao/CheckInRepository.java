/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author chao_
 */

import adt.ArrayList;
import adt.DictionaryInterface;
import adt.HashedDictionary;
import adt.ListInterface;
import entity.CheckIn;
import entity.Order;
import java.io.Serializable;

public class CheckInRepository implements Serializable {
    private final ListInterface<CheckIn> checkInList = new ArrayList<>();
    private final ListInterface<Order> orderList = new ArrayList<>();
    private final DictionaryInterface<Integer, CheckIn> checkInTable = new HashedDictionary<>();
    private final DictionaryInterface<String, Order> orderTable = new HashedDictionary<>();
    
    public void addToCheckInTable(int roomNo, CheckIn checkin) {
        this.checkInTable.insert(roomNo, checkin);
    }
    
    public void addToCheckInList(CheckIn checkIn) {
        this.checkInList.add(checkIn);
    }
    
    public void removeFromCheckInTable(int roomNo) {
        this.checkInTable.remove(roomNo);
    }
    
    public void addToOrderTable(String orderID, Order order) {
        this.orderTable.insert(orderID, order);
    }
    
    public void addToOrderList(Order order) {
        this.orderList.add(order);
    }
    
    public void removeFromOrderTable(String orderID) {
        this.orderTable.remove(orderID);
    }
            
    public DictionaryInterface<Integer, CheckIn> getCheckInTable() {
        return this.checkInTable;
    }
    
    public DictionaryInterface<String, Order> getOrderTable() {
        return this.orderTable;
    }
    
    public ListInterface<CheckIn> getCheckInList() {
        return this.checkInList;
    }
    
    public ListInterface<Order> getOrderList() {
        return this.orderList;
    }
}
