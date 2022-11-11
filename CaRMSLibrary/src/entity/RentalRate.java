/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andre
 */
@Entity
public class RentalRate implements Serializable {

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

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String rentalRateName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String rentalRateType;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal rentalAmount;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;

    public RentalRate() {
    }

    public RentalRate(String rentalRateName, String rentalRateType, BigDecimal rentalAmount, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.rentalRateName = rentalRateName;
        this.rentalRateType = rentalRateType;
        this.rentalAmount = rentalAmount;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public RentalRate(String rentalRateName, String rentalRateType, BigDecimal rentalAmount, LocalDateTime startDateTime, LocalDateTime endDateTime, Category category) {
        this.rentalRateName = rentalRateName;
        this.rentalRateType = rentalRateType;
        this.rentalAmount = rentalAmount;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.category = category;
    }
    
    

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    /**
     * @return the startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
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
     * @return the rentalRateType
     */
    public String getRentalRateType() {
        return rentalRateType;
    }

    /**
     * @param rentalRateType the rentalRateType to set
     */
    public void setRentalRateType(String rentalRateType) {
        this.rentalRateType = rentalRateType;
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
