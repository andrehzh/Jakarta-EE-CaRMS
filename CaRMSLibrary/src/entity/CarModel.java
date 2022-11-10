/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author tian
 */
@Entity
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carModelId;
    private String carModelBrand;
    private String carModelName;

    public CarModel() {
    }

    public CarModel(String carModelBrand, String carModelName) {
        this.carModelBrand = carModelBrand;
        this.carModelName = carModelName;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carModelId != null ? carModelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carModelId fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.carModelId == null && other.carModelId != null) || (this.carModelId != null && !this.carModelId.equals(other.carModelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarModel[ id=" + carModelId + " ]";
    }

    /**
     * @return the carModelBrand
     */
    public String getCarModelBrand() {
        return carModelBrand;
    }

    /**
     * @param carModelBrand the carModelBrand to set
     */
    public void setCarModelBrand(String carModelBrand) {
        this.carModelBrand = carModelBrand;
    }

    /**
     * @return the carModelName
     */
    public String getCarModelName() {
        return carModelName;
    }

    /**
     * @param carModelName the carModelName to set
     */
    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }
    
}
