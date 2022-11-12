/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
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
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
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
                        throw new CustomerEmailExistException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessageOC(constraintViolations));
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
    public OwnCustomer retrieveOwnCustomerByPassportNumber(String passportNumber) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT oc FROM OwnCustomer oc WHERE oc.passportNumber = :inPassportNumber");
        query.setParameter("inPassportNumber", passportNumber);

        try {
            return (OwnCustomer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Passport Number " + passportNumber + " does not exist!");
        }
    }

    @Override
    public OwnCustomer customerLogin(String passportNumber, String password) throws InvalidLoginCredentialException {
        try {
            OwnCustomer customer = retrieveOwnCustomerByPassportNumber(passportNumber);
            if (customer.getCustomerPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Passport Number does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Invalid Login Credential!");
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
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessageOC(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("CustomerNotFoundException");
        }
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException {
        Customer customerToRemove = retrieveCustomerByCustomerId(customerId);
        //if remove customer need to remove credit card also.
        if (customerToRemove.getCreditCard() == null) {
            em.remove(customerToRemove);
        } else {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteCustomerException("Customer ID " + customerId + " is associated with existing credit card and cannot be deleted!");
        }
    }

    @Override
    public void deleteOwnCustomer(Long ownCustomerId) throws CustomerNotFoundException, DeleteCustomerException {
        OwnCustomer ownCustomerToRemove = retrieveOwnCustomerByCustomerId(ownCustomerId);
        //if remove OwnCustomer need to remove credit card also.
        if (ownCustomerToRemove.getCreditCard() == null) {
            em.remove(ownCustomerToRemove);
        } else {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteCustomerException("Customer ID " + ownCustomerId + "cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
        private String prepareInputDataValidationErrorsMessageOC(Set<ConstraintViolation<OwnCustomer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
