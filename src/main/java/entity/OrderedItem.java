/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Tan Kah Chao
 */

import java.io.Serializable;

public class OrderedItem extends Item implements Serializable, Comparable<OrderedItem>  {
    private int orderQty;
    private float total = 0;
    
    public OrderedItem(String itemID, String itemName, float price, int orderQty) {
        super(itemID, itemName, price);
        this.orderQty = orderQty;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }
    
    public float getTotal() {
        total = getPrice() * orderQty;
        return total;
    }
    
    public void addOrderQty(int orderQty) {
        this.orderQty += orderQty;
    }
    
    @Override
    public int compareTo(OrderedItem o) {
        return this.getItemID().compareTo(o.getItemID());
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && ((Item)obj).getItemID().equals(this.getItemID());
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Integer.parseInt(this.getItemID());
        return hash;
    }  
    
}
