/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import util.exception.CarModelNotFoundException;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Local
public interface CarModelSessionBeanLocal {

    public Long createNewCarModel(CarModel carModel) throws UnknownPersistenceException, InputDataValidationException;

    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundException;

    public List<CarModel> retrieveAllCarModels();

    public void updateCarModel(CarModel carModel) throws CarModelNotFoundException, InputDataValidationException;

    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException;
}
