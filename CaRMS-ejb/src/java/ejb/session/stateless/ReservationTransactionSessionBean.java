/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationTransaction;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteReservationTransactionException;
import util.exception.InputDataValidationException;
import util.exception.ReservationTransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReservationTransactionException;

/**
 *
 * @author andre
 */
@Stateless
public class ReservationTransactionSessionBean implements ReservationTransactionSessionBeanRemote, ReservationTransactionSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationTransactionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewReservationTransaction(ReservationTransaction newReservationTransaction) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<ReservationTransaction>> constraintViolations = validator.validate(newReservationTransaction);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newReservationTransaction);
                em.flush();

                return newReservationTransaction.getReservationTransactionId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {

                    throw new UnknownPersistenceException(ex.getMessage());

                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ReservationTransaction> retrieveAllReservationTransactions() {
        Query query = em.createQuery("SELECT rt FROM ReservationTransaction rt");

        return query.getResultList();
    }

    @Override
    public ReservationTransaction retrieveReservationTransactionByReservationTransactionId(Long reservationTransactionId) throws ReservationTransactionNotFoundException {
        ReservationTransaction reservationTransaction = em.find(ReservationTransaction.class, reservationTransactionId);

        if (reservationTransaction != null) {
            return reservationTransaction;
        } else {
            throw new ReservationTransactionNotFoundException("ReservationTransaction ID " + reservationTransactionId + " does not exist!");
        }
    }

    @Override
    public void updateReservationTransaction(ReservationTransaction reservationTransaction) throws ReservationTransactionNotFoundException, UpdateReservationTransactionException, InputDataValidationException {
        if (reservationTransaction != null && reservationTransaction.getReservationTransactionId() != null) {
            Set<ConstraintViolation<ReservationTransaction>> constraintViolations = validator.validate(reservationTransaction);

            if (constraintViolations.isEmpty()) {
                ReservationTransaction reservationTransactionToUpdate = retrieveReservationTransactionByReservationTransactionId(reservationTransaction.getReservationTransactionId());

                try {
                    reservationTransactionToUpdate.setTransactionAmount(reservationTransaction.getTransactionAmount());
                    reservationTransactionToUpdate.setTransactionDate(reservationTransaction.getTransactionDate());
                    reservationTransactionToUpdate.setTransactionStaus(reservationTransaction.getTransactionStaus());
                    // able to update everything 
                } catch(PersistenceException ex) {
                    throw new UpdateReservationTransactionException("UpdateReservationTransactionException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ReservationTransactionNotFoundException("ReservationTransactionNotFoundException");
        }
    }

    @Override
    public void deleteReservationTransaction(Long reservationTransactionId) throws ReservationTransactionNotFoundException, DeleteReservationTransactionException {
        ReservationTransaction reservationTransactionToRemove = retrieveReservationTransactionByReservationTransactionId(reservationTransactionId);
        
        try {
            em.remove(reservationTransactionToRemove);
        } catch (PersistenceException ex) {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteReservationTransactionException("ReservationTransaction ID " + reservationTransactionId + " is associated with existing credit card and cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationTransaction>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
