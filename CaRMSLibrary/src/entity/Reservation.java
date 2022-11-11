/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andre
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String reservationNumber;
    @Column(nullable = false)
    @NotNull
    private List<Date> reservationDate;
    @Column(nullable = false)
    @NotNull
    private Time pickUpTime;
    @Column(nullable = false)
    @NotNull
    private Time dropOffTime;
    
    @ManyToOne
    private Partner partner;
    
    @ManyToOne
    private OwnCustomer ownCustomer;
    
    @OneToOne
    private ReservationTransaction reservationTransaction;
    
    @OneToMany
    private List<RentalRate> rentalRates;
    
    @ManyToOne
    private Car car;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<Outlet> outlets;

    public Reservation() {
        reservationDate = new ArrayList<>();
        
        rentalRates = new ArrayList<>();
        outlets = new ArrayList<>();
    }

    public Reservation(String reservationNumber, List<Date> reservationDate, Time pickUpTime, Time dropOffTime) {
        this();
        
        this.reservationNumber = reservationNumber;
        this.reservationDate = reservationDate;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    
    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
    
    /**
     * @return the ownCustomer
     */
    public OwnCustomer getOwnCustomer() {
        return ownCustomer;
    }

    /**
     * @param ownCustomer the ownCustomer to set
     */
    public void setOwnCustomer(OwnCustomer ownCustomer) {
        this.ownCustomer = ownCustomer;
    }

    /**
     * @return the reservationTransaction
     */
    public ReservationTransaction getReservationTransaction() {
        return reservationTransaction;
    }

    /**
     * @param reservationTransaction the reservationTransaction to set
     */
    public void setReservationTransaction(ReservationTransaction reservationTransaction) {
        this.reservationTransaction = reservationTransaction;
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    /**
     * @return the outlets
     */
    public List<Outlet> getOutlets() {
        return outlets;
    }

    /**
     * @param outlets the outlets to set
     */
    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    /**
     * @return the reservationNumber
     */
    public String getReservationNumber() {
        return reservationNumber;
    }

    /**
     * @param reservationNumber the reservationNumber to set
     */
    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    /**
     * @return the reservationDate
     */
    public List<Date> getReservationDate() {
        return reservationDate;
    }

    /**
     * @param reservationDate the reservationDate to set
     */
    public void setReservationDate(List<Date> reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * @return the pickUpTime
     */
    public Time getPickUpTime() {
        return pickUpTime;
    }

    /**
     * @param pickUpTime the pickUpTime to set
     */
    public void setPickUpTime(Time pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    /**
     * @return the dropOffTime
     */
    public Time getDropOffTime() {
        return dropOffTime;
    }

    /**
     * @param dropOffTime the dropOffTime to set
     */
    public void setDropOffTime(Time dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

}
