/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
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
public interface CustomerSessionBeanRemote {

    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Customer> retrieveAllCustomers();

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException;

}
