/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.SortedArrayList;
import adt.SortedListInterface;

/**
 *
 * @author Liew Zheng Han
 */
public class Customer extends Account {
    private String phoneNumber = "";
    private String paymentMethod = "";
    private Tier tier;
    private boolean deactivated = false;
    SortedListInterface<Reservation> reservations = new SortedArrayList<>();
    
    public Customer(String email, String password, Tier tier) {
        super(email, password);
        this.name = "New Guest";
        this.tier = tier;
    }
    
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }    
    
    public int getTierId() {
        return this.tier.getTierId();
    }
    
    public Tier getTier() {
        return this.tier;
    }
    
    public void deactivate() {
        this.deactivated = true;
    }
    
    public boolean isDeactivated() {
        return this.deactivated;
    }
    
    public void setTier(Tier tier) {
        this.tier = tier;
    }    

    @Override
    public String toString() {
        return """
               Name: %s
               Email: %s
               Phone Number: %s
               Payment Method: %s
               """.formatted(this.name, this.email, this.phoneNumber, this.paymentMethod);
    }
}
