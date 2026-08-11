/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.SortedArrayList;
import adt.SortedListInterface;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class UserRepository implements Serializable {
    private final SortedListInterface<Account> userList = new SortedArrayList<>();
    private final Account adminAccount = new Account("admin@admin.com", "password");
    
    public UserRepository() {
        this.userList.add(this.adminAccount);
    }
    
    public void addUser(Account account) {
        this.userList.add(account);
    }
    
    public void removeUser(Account account) {
        this.userList.remove(account);
    }
    
    public Account getUser(String email, String password) {
        int index = this.userList.indexOf(new Account(email));
        if (index == -1) return null;
        Account account = this.userList.get(index);
        
        return (account.getEmail().equals(email) && account.getPassword().equals(Account.hashPassword(password)))? account: null;
    }
    
    public boolean userExists(String email) {
        int index = this.userList.indexOf(new Account(email));
        if (index == -1) return false;
        return this.userList.get(index).getEmail().equals(email);
    }

    public void updateCustomerEmail(Customer customer, String email) {
        this.removeUser(customer);
        customer.setEmail(email);
        this.addUser(customer);
    }
}