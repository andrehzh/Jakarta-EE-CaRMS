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
import util.exception.DeleteCarModelException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarModelException;

/**
 *
 * @author tian
 */
@Remote
public interface CarModelSessionBeanRemote {

    public Long createNewCarModel(CarModel carModel) throws UnknownPersistenceException, InputDataValidationException;

    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundException;

    public List<CarModel> retrieveAllCarModels();

    public void updateCarModel(CarModel carModel) throws CarModelNotFoundException, InputDataValidationException, UpdateCarModelException;

    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException, DeleteCarModelException;
}
