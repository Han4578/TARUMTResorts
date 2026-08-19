/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import adt.ArrayList;
import adt.DoubleHashingTable;
import adt.ListInterface;
import adt.TableInterface;
import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class UserRepository implements Serializable {
    private final TableInterface<String, Account> userTable = new DoubleHashingTable<>();
    private final ListInterface<Account> deactivatedUsers = new ArrayList<>();
    private final Account adminAccount = new Account("admin@admin.com", "password");
    
    public UserRepository() {
        this.userTable.insert(this.adminAccount.getEmail(), this.adminAccount);
    }
    
    public void addUser(Account account) {
        this.userTable.insert(account.getEmail(), account);
    }
    
    public void removeUser(Account account) {
        this.deactivatedUsers.add(this.userTable.remove(account.getEmail()));
        if (account instanceof Customer customer) customer.deactivate();
    }
    
    public Account getUser(String email, String password) {
        Account account = this.userTable.get(email);
                
        return (account != null && account.getPassword().equals(Account.hashPassword(password)))? account: null;
    }
    
    public boolean userExists(String email) {
        return this.userTable.containsKey(email);
    }

    public void updateCustomerEmail(Customer customer, String email) {
        this.userTable.remove(customer.getEmail());
        customer.setEmail(email);
        this.addUser(customer);
    }
    
    public TableInterface<String, Account> getUsers() {
        return this.userTable;
    }
}