/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import exceptions.CarModelNotFoundExeception;
import javax.ejb.Local;

/**
 *
 * @author tian
 */
@Local
public interface CarModelSessionBeanLocal {

    public CarModel createNewCarModel(CarModel carModel);
    
    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundExeception;
}
