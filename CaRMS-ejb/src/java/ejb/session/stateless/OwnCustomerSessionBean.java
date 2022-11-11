/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author andre
 */
@Stateless
public class OwnCustomerSessionBean implements OwnCustomerSessionBeanRemote, OwnCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OwnCustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewOwnCustomer(OwnCustomer newOwnCustomer) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<OwnCustomer>> constraintViolations = validator.validate(newOwnCustomer);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newOwnCustomer);
                em.flush();
                
                //sub class things
                return newOwnCustomer.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerEmailExistException();
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
    public List<OwnCustomer> retrieveAllOwnCustomers() {
        Query query = em.createQuery("SELECT oc FROM OwnCustomer oc");
        
        return query.getResultList();
    }
    
    @Override
    public OwnCustomer retrieveOwnCustomerByCustomerId(Long ownCustomerId) throws CustomerNotFoundException {
        OwnCustomer ownCustomer = em.find(OwnCustomer.class, ownCustomerId);
        
        if (ownCustomer != null) {
            return ownCustomer;
        } else {
            throw new CustomerNotFoundException("Own Customer ID " + ownCustomerId + " does not exist!");
        }
    }
    
    @Override
    public void updateOwnCustomer(OwnCustomer ownCustomer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (ownCustomer != null && ownCustomer.getCustomerId() != null) {
            Set<ConstraintViolation<OwnCustomer>> constraintViolations = validator.validate(ownCustomer);
            
            if (constraintViolations.isEmpty()) {
                OwnCustomer ownCustomerToUpdate = retrieveOwnCustomerByCustomerId(ownCustomer.getCustomerId());
                
                if (ownCustomerToUpdate.getPassportNumber().equals(ownCustomer.getPassportNumber())) {
                    ownCustomerToUpdate.setCreditCard(ownCustomer.getCreditCard());
                    ownCustomerToUpdate.setCustomerEmail(ownCustomer.getCustomerEmail());
                    ownCustomerToUpdate.setCustomerName(ownCustomer.getCustomerName());
                    ownCustomerToUpdate.setCustomerPassword(ownCustomer.getCustomerPassword());
                    ownCustomerToUpdate.setCustomerPhoneNum(ownCustomer.getCustomerPhoneNum());
                    // able to update everything except partner cause no partner
                } else {
                    throw new UpdateCustomerException("UpdateCustomerException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("CustomerNotFoundException");
        }
    }
    
    @Override
    public void deleteOwnCustomer(Long ownCustomerId) throws CustomerNotFoundException, DeleteCustomerException {
        OwnCustomer ownCustomerToRemove = retrieveOwnCustomerByCustomerId(ownCustomerId);
        //if remove OwnCustomer need to remove credit card also.
        if (ownCustomerToRemove.getCreditCard()==null) {
            em.remove(ownCustomerToRemove);
        } else {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteCustomerException("Customer ID " + ownCustomerId + "cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OwnCustomer>> constraintViolations) {
        String msg = "Input data validation error!:";
        
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}

