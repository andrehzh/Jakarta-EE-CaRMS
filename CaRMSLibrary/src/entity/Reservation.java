/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private LocalDateTime pickUpDateTime;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime dropOffDateTime;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private OwnCustomer ownCustomer;

    @OneToOne
    private ReservationTransaction reservationTransaction;

    @OneToMany
    private List<RentalRate> rentalRates;

    @OneToOne
    private Car car;

    @ManyToOne
    private Outlet pickUpOutlet;

    @ManyToOne
    private Outlet dropOffOutlet;

    @OneToOne
    private CarModel carModel;

    @OneToOne
    private Category category;

    public Reservation() {

        rentalRates = new ArrayList<>();
    }

    public Reservation(String reservationNumber, LocalDateTime pickUpDateTime, LocalDateTime dropOffDateTime) {
        this();

        this.reservationNumber = reservationNumber;
        this.pickUpDateTime = pickUpDateTime;
        this.dropOffDateTime = dropOffDateTime;
    }

    public Reservation(String reservationNumber, LocalDateTime pickUpDateTime, LocalDateTime dropOffDateTime, Outlet pickUpOutlet, Outlet dropOffOutlet) {
        this(); 
        this.reservationNumber = reservationNumber;
        this.pickUpDateTime = pickUpDateTime;
        this.dropOffDateTime = dropOffDateTime;
        this.pickUpOutlet = pickUpOutlet;
        this.dropOffOutlet = dropOffOutlet;
    }
    
    

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the pickUpOutlet
     */
    public Outlet getPickUpOutlet() {
        return pickUpOutlet;
    }

    /**
     * @param pickUpOutlet the pickUpOutlet to set
     */
    public void setPickUpOutlet(Outlet pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    /**
     * @return the dropOffOutlet
     */
    public Outlet getDropOffOutlet() {
        return dropOffOutlet;
    }

    /**
     * @param dropOffOutlet the dropOffOutlet to set
     */
    public void setDropOffOutlet(Outlet dropOffOutlet) {
        this.dropOffOutlet = dropOffOutlet;
    }

    /**
     * @return the carModel
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    /**
     * @return the pickUpDateTime
     */
    public LocalDateTime getPickUpDateTime() {
        return pickUpDateTime;
    }

    /**
     * @param pickUpDateTime the pickUpDateTime to set
     */
    public void setPickUpDateTime(LocalDateTime pickUpDateTime) {
        this.pickUpDateTime = pickUpDateTime;
    }

    /**
     * @return the dropOffDateTime
     */
    public LocalDateTime getDropOffDateTime() {
        return dropOffDateTime;
    }

    /**
     * @param dropOffDateTime the dropOffDateTime to set
     */
    public void setDropOffDateTime(LocalDateTime dropOffDateTime) {
        this.dropOffDateTime = dropOffDateTime;
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
