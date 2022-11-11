/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationTransaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteReservationTransactionException;
import util.exception.InputDataValidationException;
import util.exception.ReservationTransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReservationTransactionException;

/**
 *
 * @author andre
 */
@Local
public interface ReservationTransactionSessionBeanLocal {

    public Long createNewReservationTransaction(ReservationTransaction newReservationTransaction) throws UnknownPersistenceException, InputDataValidationException;

    public List<ReservationTransaction> retrieveAllReservationTransactions();

    public ReservationTransaction retrieveReservationTransactionByReservationTransactionId(Long reservationTransactionId) throws ReservationTransactionNotFoundException;

    public void updateReservationTransaction(ReservationTransaction reservationTransaction) throws ReservationTransactionNotFoundException, UpdateReservationTransactionException, InputDataValidationException;

    public void deleteReservationTransaction(Long reservationTransactionId) throws ReservationTransactionNotFoundException, DeleteReservationTransactionException;
    
}
