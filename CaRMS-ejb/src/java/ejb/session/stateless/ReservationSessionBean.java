/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
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
import util.exception.DeleteReservationException;
import util.exception.InputDataValidationException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservationNumberExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReservationException;

/**
 *
 * @author andre
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewReservation(Reservation newReservation) throws ReservationNumberExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newReservation);
                em.flush();
                
                return newReservation.getReservationId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ReservationNumberExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        
        return query.getResultList();
    }
    
    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
    }
    
    @Override
    public void updateReservation(Reservation reservation) throws ReservationNotFoundException, UpdateReservationException, InputDataValidationException {
        if (reservation != null && reservation.getReservationId() != null) {
            Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
            
            if (constraintViolations.isEmpty()) {
                Reservation reservationToUpdate = retrieveReservationByReservationId(reservation.getReservationId());
                
                if (reservationToUpdate.getReservationNumber().equals(reservation.getReservationNumber())) {
                    reservationToUpdate.setReservationDate(reservation.getReservationDate());
                    reservationToUpdate.setPickUpTime(reservation.getPickUpTime());
                    reservationToUpdate.setDropOffTime(reservation.getDropOffTime());
                    reservationToUpdate.setPartner(reservation.getPartner());
                    reservationToUpdate.setOwnCustomer(reservation.getOwnCustomer());
                    reservationToUpdate.setReservationTransaction(reservation.getReservationTransaction());
                    reservationToUpdate.setRentalRates(reservation.getRentalRates());
                    reservationToUpdate.setOutlets(reservation.getOutlets());
                    // able to update everything except number cause unique
                } else {
                    throw new UpdateReservationException("UpdateReservationException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ReservationNotFoundException("ReservationNotFoundException");
        }
    }
    
    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, DeleteReservationException {
        Reservation reservationToRemove = retrieveReservationByReservationId(reservationId);

        if (reservationToRemove.getCar() == null) {
            em.remove(reservationToRemove);
        } else {
    
            throw new DeleteReservationException("Reservation ID " + reservationId + "cannot be as there is a car tagged to it deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>> constraintViolations) {
        String msg = "Input data validation error!:";
        
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
