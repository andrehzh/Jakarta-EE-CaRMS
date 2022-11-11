/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import util.enumeration.TransactionStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andre
 */
@Entity
public class ReservationTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationTransactionId;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal transactionAmount;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date transactionDate;
    @Column(nullable = false)
    @NotNull
    private TransactionStatusEnum transactionStaus;

    public ReservationTransaction() {
    }

    public ReservationTransaction(BigDecimal transactionAmount, Date transactionDate, TransactionStatusEnum transactionStaus) {
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionStaus = transactionStaus;
    }

    public Long getReservationTransactionId() {
        return reservationTransactionId;
    }

    public void setReservationTransactionId(Long reservationTransactionId) {
        this.reservationTransactionId = reservationTransactionId;
    }

    /**
     * @return the transactionAmount
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the transactionStaus
     */
    public TransactionStatusEnum getTransactionStaus() {
        return transactionStaus;
    }

    /**
     * @param transactionStaus the transactionStaus to set
     */
    public void setTransactionStaus(TransactionStatusEnum transactionStaus) {
        this.transactionStaus = transactionStaus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationTransactionId != null ? reservationTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationTransactionId fields are not set
        if (!(object instanceof ReservationTransaction)) {
            return false;
        }
        ReservationTransaction other = (ReservationTransaction) object;
        if ((this.reservationTransactionId == null && other.reservationTransactionId != null) || (this.reservationTransactionId != null && !this.reservationTransactionId.equals(other.reservationTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationTransaction[ id=" + reservationTransactionId + " ]";
    }
    
}
