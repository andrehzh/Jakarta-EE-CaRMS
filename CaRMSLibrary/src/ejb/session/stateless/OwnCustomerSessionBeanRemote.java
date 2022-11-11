/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface OwnCustomerSessionBeanRemote {
  
    public Long createNewOwnCustomer(OwnCustomer newOwnCustomer) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException;

    public List<OwnCustomer> retrieveAllOwnCustomers();

    public OwnCustomer retrieveOwnCustomerByCustomerId(Long ownCustomerId) throws CustomerNotFoundException;

    public void updateOwnCustomer(OwnCustomer ownCustomer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public void deleteOwnCustomer(Long ownCustomerId) throws CustomerNotFoundException, DeleteCustomerException;
    
}
