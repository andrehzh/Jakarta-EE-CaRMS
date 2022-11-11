/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author andre
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewRentalRate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newRentalRate);
                em.flush();

                return newRentalRate.getRentalRateId();
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
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr");

        return query.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    //does it match the time stamp?
    @Override
    public List<RentalRate> retrieveRentalRatesByDate(Date date) throws RentalRateNotFoundException {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.date = :inDate");
        query.setParameter("inDate", date);

        try {
            return query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RentalRateNotFoundException("Rental Rate date: " + date + " does not exist!");
        }
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                RentalRate rentalRateToUpdate = retrieveRentalRateByRentalRateId(rentalRate.getRentalRateId());

                if (rentalRateToUpdate.getRentalRateName().equals(rentalRate.getRentalRateName())) {
                    rentalRateToUpdate.setRentalAmount(rentalRate.getRentalAmount());
                    rentalRateToUpdate.setCategory(rentalRate.getCategory());
                    rentalRateToUpdate.setRentalDate(rentalRate.getRentalDate());
                    //  can update everything about a rental rate except name?
                } else {
                    throw new UpdateRentalRateException("UpdateRentalRateException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new RentalRateNotFoundException("RentalRateNotFoundException");
        }
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException {
        RentalRate rentalRateToRemove = retrieveRentalRateByRentalRateId(rentalRateId);

        try {
            //no need to remove from category cause em will do it
            em.remove(rentalRateToRemove);
        } catch (PersistenceException ex) {
            throw new DeleteRentalRateException();
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
