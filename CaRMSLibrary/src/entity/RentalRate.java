/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andre
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String rentalRateName;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal rentalAmount;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date rentalDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;
    
    public RentalRate() {
    }

    public RentalRate(String rentalRateName, BigDecimal rentalAmount, Date rentalDate) {
        this.rentalRateName = rentalRateName;
        this.rentalAmount = rentalAmount;
        this.rentalDate = rentalDate;
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
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
     * @return the rentalRateName
     */
    public String getRentalRateName() {
        return rentalRateName;
    }

    /**
     * @param rentalRateName the rentalRateName to set
     */
    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
    }

    /**
     * @return the rentalAmount
     */
    public BigDecimal getRentalAmount() {
        return rentalAmount;
    }

    /**
     * @param rentalAmount the rentalAmount to set
     */
    public void setRentalAmount(BigDecimal rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    /**
     * @return the rentalDate
     */
    public Date getRentalDate() {
        return rentalDate;
    }

    /**
     * @param rentalDate the rentalDate to set
     */
    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + rentalRateId + " ]";
    }

}
