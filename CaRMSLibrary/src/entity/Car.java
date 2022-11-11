/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import util.enumeration.CarStatusEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
//really double checking

/**
 *
 * @author tian
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, unique = true, length = 8)
    @NotNull
    @Size(min = 1, max = 8)
    private String carPlateNumber;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String carColor;
    @Column(nullable = false)
    @NotNull
    private CarStatusEnum carStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarModel carModel;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations;
    
    

    public Car() {
        reservations = new ArrayList<>();
    }

    public Car(String carPlateNumber, String carColor, CarStatusEnum carStatus) {
        this();
        
        this.carPlateNumber = carPlateNumber;
        this.carColor = carColor;
        this.carStatus = carStatus;
    }

    public Car(String carPlateNumber, CarStatusEnum carStatus, CarModel carModel, Outlet outlet) {
        this();
        
        this.carPlateNumber = carPlateNumber;
        this.carStatus = carStatus;
        this.carModel = carModel;
        this.outlet = outlet;
        this.carColor = "Orangey Black";
    }
    
    

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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
     * @return the outlet
     */
    public Outlet getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }

    /**
     * @return the carPlateNumber
     */
    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    /**
     * @param carPlateNumber the carPlateNumber to set
     */
    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    /**
     * @return the carColor
     */
    public String getCarColor() {
        return carColor;
    }

    /**
     * @param carColor the carColor to set
     */
    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    /**
     * @return the carStatus
     */
    public CarStatusEnum getCarStatus() {
        return carStatus;
    }

    /**
     * @param carStatus the carStatus to set
     */
    public void setCarStatus(CarStatusEnum carStatus) {
        this.carStatus = carStatus;
    }

}
