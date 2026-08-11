/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Liew Zheng Han
 */
public class Reservation implements Serializable, Comparable<Reservation> {
    private Customer customer;
    private Date startDate;
    private Date endDate;
    
    public Reservation(Customer customer, Date startDate, Date endDate) {
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Customer getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int compareTo(Reservation o) {
        return this.startDate.compareTo(o.getStartDate());
    }
    
}
