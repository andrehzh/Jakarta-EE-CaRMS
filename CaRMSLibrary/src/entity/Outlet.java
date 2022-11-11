/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author tian
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String outletName;
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;
    
    @OneToOne(mappedBy = "outlets")
    private TransitDriverDispatchRecord transitDriverDispatchRecord;
    
    @ManyToMany(mappedBy = "outlets")
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy = "outlet")
    private List<Car> cars;

    public Outlet() {
        employees = new ArrayList<>();
        reservations = new ArrayList<>();
        cars = new ArrayList<>();
    }

    public Outlet(String outletName, LocalTime openingTime, LocalTime closingTime) {
        this();
        
        this.outletName = outletName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    /**
     * @return the transitDriverDispatchRecord
     */
    public TransitDriverDispatchRecord getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    /**
     * @param transitDriverDispatchRecord the transitDriverDispatchRecord to set
     */
    public void setTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
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
     * @return the cars
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    /**
     * @return the outletName
     */
    public String getOutletName() {
        return outletName;
    }

    /**
     * @param outletName the outletName to set
     */
    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    /**
     * @return the openingTime
     */
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    /**
     * @param openingTime the openingTime to set
     */
    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    /**
     * @return the closingTime
     */
    public LocalTime getClosingTime() {
        return closingTime;
    }

    /**
     * @param closingTime the closingTime to set
     */
    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

}
