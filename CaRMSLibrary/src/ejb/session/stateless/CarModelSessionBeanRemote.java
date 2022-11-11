/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import entity.RentalRate;
import java.util.List;
import util.exception.CarModelNotFoundExeception;
import javax.ejb.Remote;

/**
 *
 * @author tian
 */
@Remote
public interface CarModelSessionBeanRemote {

    public Long createNewCarModel(CarModel carModel);
    
    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundExeception;

    public List<CarModel> retrieveAllCarModels();
}
