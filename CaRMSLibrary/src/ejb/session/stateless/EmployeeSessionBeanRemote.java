/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeEmailExistsException;
import util.exception.EmployeeNotFoundExeception;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public Long createNewEmployee(Employee employee) throws UnknownPersistenceException, InputDataValidationException, EmployeeEmailExistsException;

    public Employee retrieveEmployeeById(Long id) throws EmployeeNotFoundExeception;

    public List<Employee> retrieveAllEmployees();

}
