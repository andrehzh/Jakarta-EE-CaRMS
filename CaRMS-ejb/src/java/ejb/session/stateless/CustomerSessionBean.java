/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
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
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(newCustomer);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCustomer);
                em.flush();
                
                return newCustomer.getCustomerId();
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
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        
        return query.getResultList();
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        
        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (customer != null && customer.getCustomerId() != null) {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
            
            if (constraintViolations.isEmpty()) {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());
                
                if (customerToUpdate.getCustomerEmail().equals(customer.getCustomerEmail())) {
                    customerToUpdate.setCreditCard(customer.getCreditCard());
                    customerToUpdate.setCustomerName(customer.getCustomerName());
                    customerToUpdate.setCustomerPhoneNum(customer.getCustomerPhoneNum());
                    customerToUpdate.setPartner(customer.getPartner());
                    // able to update everything except email cause unique
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
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException {
        Customer customerToRemove = retrieveCustomerByCustomerId(customerId);
            //if remove customer need to remove credit card also.
        if(customerToRemove.getCreditCard() == null)
        {
            em.remove(customerToRemove);
        }
        else
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteCustomerException("Customer ID " + customerId + " is associated with existing credit card and cannot be deleted!");
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";
        
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
