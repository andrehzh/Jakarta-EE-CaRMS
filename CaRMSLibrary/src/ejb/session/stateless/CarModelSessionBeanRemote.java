/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import entity.RentalRate;
import java.util.List;
import util.exception.CarModelNotFoundException;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Remote
public interface CarModelSessionBeanRemote {

    Long createNewCarModel(CarModel carModel) throws UnknownPersistenceException, InputDataValidationException;

    CarModel retrieveCarModelById(Long id) throws CarModelNotFoundException;

    List<CarModel> retrieveAllCarModels();

    void updateCarModel(CarModel carModel) throws CarModelNotFoundException, InputDataValidationException;

    void deleteCarModel(Long carModelId) throws CarModelNotFoundException;
}
