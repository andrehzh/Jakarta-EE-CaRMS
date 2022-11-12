/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andre
 */
@Entity
public class OwnCustomer extends Customer {

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String customerPassword;
    @Column(nullable = false, unique = true,length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String passportNumber;
    
    @OneToMany(mappedBy = "ownCustomer")
    private List<Reservation> reservations;

    public OwnCustomer() {
        reservations = new ArrayList<>();
    }

    public OwnCustomer(String customerPassword, String passportNumber, String customerEmail, String customerName, String customerPhoneNum) {       
        super(customerEmail, customerName, customerPhoneNum);

        this.customerPassword = customerPassword;
        this.passportNumber = passportNumber;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

}
