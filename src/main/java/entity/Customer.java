/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Liew Zheng Han
 */
public class Customer extends Account {
    private String phoneNumber = "";
    private String paymentMethod = "";
    private int tierId;
    
    public Customer(String email, String password, int tierId) {
        super(email, password);
        this.name = "New Guest";
        this.tierId = tierId;
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
        return this.tierId;
    }
    
    public void setTier(int tierId) {
        this.tierId = tierId;
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
