/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface ReservationSessionBeanLocal {

    public Long createNewReservation(Reservation newReservation) throws ReservationNumberExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    public void updateReservation(Reservation reservation) throws ReservationNotFoundException, UpdateReservationException, InputDataValidationException;

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException, DeleteReservationException;

    public Reservation retrieveReservationByReservationNumber(String reservationNumber) throws ReservationNotFoundException;

    public void updateReservationCustomer(Reservation reservation) throws ReservationNotFoundException, UpdateReservationException, InputDataValidationException;

    public List<Reservation> retrieveReservationsByCustomerId(Long customerId) throws ReservationNotFoundException;
    
}
