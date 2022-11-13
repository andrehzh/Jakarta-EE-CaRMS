/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
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
import util.exception.CardNumberExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author andre
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanRemote, CreditCardSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCreditCard(CreditCard newCreditCard) throws CardNumberExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(newCreditCard);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCreditCard);
                em.flush();

                return newCreditCard.getCreditCardId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CardNumberExistException();
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
    public List<CreditCard> retrieveAllCreditCards() {
        Query query = em.createQuery("SELECT c FROM CreditCard c");

        return query.getResultList();
    }

    @Override
    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException {
        CreditCard creditCard = em.find(CreditCard.class, creditCardId);

        if (creditCard != null) {
            return creditCard;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + creditCardId + " does not exist!");
        }
    }
    
    @Override
    public CreditCard retrieveCreditCardByCustomerId(Long customerId) throws CreditCardNotFoundException {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.customer.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);

        try {
            return (CreditCard) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CreditCardNotFoundException("Customer " + customerId + " does not exist!");
        }
    }

    @Override
    public void updateCreditCard(CreditCard creditCard) throws CreditCardNotFoundException, UpdateCreditCardException, InputDataValidationException {
        if (creditCard != null && creditCard.getCreditCardId() != null) {
            Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(creditCard);

            if (constraintViolations.isEmpty()) {
                CreditCard creditCardToUpdate = retrieveCreditCardByCreditCardId(creditCard.getCreditCardId());

                if (creditCardToUpdate.getCardNumber().equals(creditCard.getCardNumber())) {
                    creditCardToUpdate.setNameOnCard(creditCard.getNameOnCard());
                    // note able to update cardNum, cvv, expirydate
                } else {
                    throw new UpdateCreditCardException("UpdateCreditCardException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CreditCardNotFoundException("CreditCardNotFoundException");
        }
    }

    @Override
    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException {
        CreditCard creditCardToRemove = retrieveCreditCardByCreditCardId(creditCardId);
        try {
            //no need to remove from customer cause em will do it
            em.remove(creditCardToRemove);
        } catch (PersistenceException ex) {
            throw new DeleteCreditCardException();
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
