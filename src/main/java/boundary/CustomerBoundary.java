/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import entity.Customer;
import utility.Input;

/**
 *
 * @author Liew Zheng Han
 */
public class CustomerBoundary {
    public int getCustomerMenuChoice() {
        return Input.getIntInput(
                """
                1. Add Reservation
                2. View Profile
                3. Update Profile
                4. Delete Profile
                5. Log Out       
                
                Input: \
                """, 1, 5);
    }
    
    public int getUpdateProfileChoice() {
        return Input.getIntInput(
                """
                1. Update Name
                2. Update Email
                3. Update Phone Number  
                4. Update Payment Method
                5. Cancel
                
                Input: \
                """, 1, 5);
    }
    
    public String getName() {
        return Input.getStringInput("Enter new name: ");
    }
    
    public String getEmail() {
        return Input.getStringInput("Enter new email: ");
    }
    
    public String getPhoneNumber() {
        return Input.getStringInput("Enter new phone number: ");
    }
    
    public String getPaymentMethod() {
        return Input.getStringInput("Enter new payment method: ");
    }

    public void nameUpdated() {
        System.out.println("Name successfully updated");
    }

    public void emailUpdated() {
        System.out.println("Email successfully updated");
    }

    public void phoneNumberUpdated() {
        System.out.println("Phone number successfully updated");
    }

    public void paymentMethodUpdated() {
        System.out.println("Payment method successfully updated");
    }

    public void invalidEmail() {
        System.out.println("Invalid email format");
    }

    public void emailTaken() {
        System.out.println("Email has already been taken");
    }

    public void invalidPhoneNumber() {
        System.out.println("Phone number can only contain digits");
    }

    public boolean confirmDeleteAccount() {
        return Input.getBooleanInput("Are you sure you want to delete your account? [y/n]: ");
    }
    
    public void showProfile(Customer customer) {
        System.out.println(customer);
    }

    public void deleteSuccess() {
        System.out.println("Account successfully deleted. Logging Out");
    }
}
