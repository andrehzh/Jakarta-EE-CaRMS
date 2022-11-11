/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import util.exception.CarModelNotFoundExeception;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

/**
 *
 * @author tian
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public CarModelSessionBean() {
    }

    public CarModel createNewCarModel(CarModel carModel) {
        try {
            em.persist(carModel);
            em.flush();
            return carModel;
        } catch (PersistenceException ex) {
            return null;
            //throw exception error handling
        }
    }

    @Override
    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundExeception {
        CarModel carModel = em.find(CarModel.class, id);
        if (carModel != null) {
            return carModel;
        } else {
            throw new CarModelNotFoundExeception("Car Model " + id.toString() + " does not exist!");
        }
    }

}
