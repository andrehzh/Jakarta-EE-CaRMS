/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author tian
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchRecordId;
    @Column(nullable = false)
    @NotNull
    private boolean isCompleted;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime dispatchDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Employee employee;

    @OneToOne
    private Outlet pickUpOutlet;
    
    @OneToOne
    private Outlet dropOffOutlet;

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(boolean isCompleted, LocalDateTime dispatchDate) {
        this.isCompleted = isCompleted;
        this.dispatchDate = dispatchDate;
    }

    public TransitDriverDispatchRecord(LocalDateTime dispatchDate, Outlet pickUpOutlet, Outlet dropOffOutlet) {
        this.isCompleted=false;
        this.dispatchDate = dispatchDate;
        this.pickUpOutlet = pickUpOutlet;
        this.dropOffOutlet = dropOffOutlet;
    }



    public Long getTransitDriverDispatchRecordId() {
        return transitDriverDispatchRecordId;
    }

    public void setTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) {
        this.transitDriverDispatchRecordId = transitDriverDispatchRecordId;
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
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchRecordId != null ? transitDriverDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchRecordId == null && other.transitDriverDispatchRecordId != null) || (this.transitDriverDispatchRecordId != null && !this.transitDriverDispatchRecordId.equals(other.transitDriverDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecord[ id=" + transitDriverDispatchRecordId + " ]";
    }

    /**
     * @return the isCompleted
     */
    public boolean isIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the dispatchDate
     */
    public LocalDateTime getDispatchDate() {
        return dispatchDate;
    }

    /**
     * @param dispatchDate the dispatchDate to set
     */
    public void setDispatchDate(LocalDateTime dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

}
