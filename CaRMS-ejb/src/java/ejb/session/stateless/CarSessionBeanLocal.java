/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundExeception;
import util.exception.CarPlateExistsException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Local
public interface CarSessionBeanLocal {

    public Long createNewCar(Car car) throws UnknownPersistenceException, InputDataValidationException, CarPlateExistsException;

    public Car retrieveCarById(Long id) throws CarNotFoundExeception;

    public List<Car> retrieveAllCars();
    
}
