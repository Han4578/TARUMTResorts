/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import boundary.CustomerBoundary;
import entity.Customer;
import dao.TierRepository;
import dao.UserRepository;
import tarumtresorts.TARUMTResorts;
import utility.Input;

/**
 *
 * @author Liew Zheng Han
 */
public class CustomerControl {
    private final UserRepository userRepository;
    private final CustomerBoundary customerUI = new CustomerBoundary();
    private final WalkInBookingControl walkInBookingControl;
    private Customer customer;
    private final TierRepository tierRepository;
    
    public CustomerControl(UserRepository userRepository, TierRepository tierRepository, WalkInBookingControl walkInBookingControl) {
        this.userRepository = userRepository;
        this.tierRepository = tierRepository;
        this.walkInBookingControl = walkInBookingControl;
    }
    
    public void start(Customer customer) {
        this.customer = customer;
        
        while (true) {
            switch (this.customerUI.getCustomerMenuChoice()) {
                case 1 -> this.walkInBookingControl.startCustomerFlow(this.customer);
                case 2 -> this.customerUI.showProfile(this.customer);
                case 3 -> updateProfile();
                case 4 -> {
                    if (deleteAccount()) return;
                }
                case 5 -> {
                    return;
                }                    
            }
        }
    }
    
    private void updateProfile() {
        while (true) {
            
            switch (this.customerUI.getUpdateProfileChoice()) {
                case 1 -> {
                    String name = this.customerUI.getName();
                    this.customer.setName(name);
                    TARUMTResorts.save();
                    this.customerUI.nameUpdated();
                }
                case 2 -> {
                    String email = this.customerUI.getEmail();
                    if (!Input.isValidEmail(email)) this.customerUI.invalidEmail();
                    else if (userRepository.userExists(email)) this.customerUI.emailTaken();
                    else {
                        this.userRepository.updateCustomerEmail(this.customer, email);
                        TARUMTResorts.save();
                        this.customerUI.emailUpdated();
                    }

                }
                case 3 -> {
                    String phoneNumber = this.customerUI.getPhoneNumber();
                    try {
                        Integer.valueOf(phoneNumber);
                        this.customer.setPhoneNumber(phoneNumber);
                        TARUMTResorts.save();
                        this.customerUI.phoneNumberUpdated();
                    } catch (NumberFormatException e) {
                        this.customerUI.invalidPhoneNumber();
                    }
                }
                case 4 -> {
                    String paymentMethod = this.customerUI.getPaymentMethod();
                    this.customer.setPaymentMethod(paymentMethod);
                    TARUMTResorts.save();
                    this.customerUI.paymentMethodUpdated();
                }
                case 5 -> {
                    return;
                }
            }
        }
    }
    
    private boolean deleteAccount() {
        if (!this.customerUI.confirmDeleteAccount()) return false;
        
        this.userRepository.removeUser(this.customer);
        this.tierRepository.getQueue().clear(this.customer.getTier().getPriority(), r -> r.getCustomer().equals(this.customer));
        TARUMTResorts.save();
        this.customerUI.deleteSuccess();
        return true;
    }
}
