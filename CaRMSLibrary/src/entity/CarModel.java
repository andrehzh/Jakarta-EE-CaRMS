/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

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
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String carModelBrand;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String carModelName;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "carModel")
    private List<Car> cars;

    public CarModel() {
        cars = new ArrayList<>();
    }

    public CarModel(String carModelBrand, String carModelName) {
        this();
        
        this.carModelBrand = carModelBrand;
        this.carModelName = carModelName;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
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
