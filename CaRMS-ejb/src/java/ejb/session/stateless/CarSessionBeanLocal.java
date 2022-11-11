/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.CarPlateExistsException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author tian
 */
@Local
public interface CarSessionBeanLocal {

    public Long createNewCar(Car car) throws UnknownPersistenceException, InputDataValidationException, CarPlateExistsException;

    public Car retrieveCarById(Long id) throws CarNotFoundException;

    public List<Car> retrieveAllCars();

    public void updateCar(Car car) throws CarNotFoundException, InputDataValidationException, UpdateCarException;

    public void deleteCar(Long carId) throws CarNotFoundException;
    
}
