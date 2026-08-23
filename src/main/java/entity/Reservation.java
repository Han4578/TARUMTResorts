/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;


/**
 *
 * @author Liew Zheng Han
 */
public class Reservation implements Serializable, Comparable<Reservation> {
    private final String confirmNo;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean checkIn = false;
    
    public Reservation(String confirmNo, Customer customer, LocalDate startDate, LocalDate endDate) {
        this.confirmNo = confirmNo;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getConfirmNo() {
        return confirmNo;
    }
    
    public Customer getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public LocalDate getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    @Override
    public int compareTo(Reservation o) {
        return this.startDate.compareTo(o.getStartDate());
    }
    
}
