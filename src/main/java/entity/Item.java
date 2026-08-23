/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author chao_
 */
import java.io.Serializable;

public class Item implements Serializable {
    
    private static int tempID = 1;
    private String itemID;
    private String itemName;
    private float price;
    
    
    public Item() {
        this.itemID = String.format("I%04d", tempID); 
        tempID++;
    }
    
    public Item(String itemName, float price) {
        this.itemID = String.format("I%04d", tempID); 
        this.itemName = itemName;
        this.price = price;
        tempID++;
    }
    
    public Item(String itemID, String itemName, float price) {
        this.itemID = itemID; 
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
            
    public static void setTempID(int nextID) {
        tempID = nextID;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && Integer.parseInt(((Item)obj).getItemID()) == Integer.parseInt(this.itemID);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Integer.parseInt(this.itemID);
        return hash;
    }  
}
