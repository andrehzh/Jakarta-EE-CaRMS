/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import java.util.Set;
import util.exception.CarModelNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarModelSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCarModel(CarModel carModel) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(carModel);
                em.flush();
                return carModel.getCarModelId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    throw new UnknownPersistenceException(ex.getMessage());

                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public CarModel retrieveCarModelById(Long id) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, id);
        if (carModel != null) {
            return carModel;
        } else {
            throw new CarModelNotFoundException("Car Model " + id.toString() + " does not exist!");
        }
    }

    @Override
    public List<CarModel> retrieveAllCarModels() {
        Query query = em.createQuery("SELECT cm FROM CarModel cm");

        return query.getResultList();
    }

    @Override
    public void updateCarModel(CarModel carModel) throws CarModelNotFoundException, InputDataValidationException //, UpdateCarModelException
    {
        if (carModel != null && carModel.getCarModelId() != null) {
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

            if (constraintViolations.isEmpty()) {
                CarModel carModelToUpdate = retrieveCarModelById(carModel.getCarModelId());
                carModelToUpdate.setCarModelBrand(carModel.getCarModelBrand());
                carModelToUpdate.setCarModelName(carModel.getCarModelName());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarModelNotFoundException("Car Model " + carModel.getCarModelId().toString() + " does not exist!");
        }
    }

    @Override
    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException //, DeleteCarModelException
    {
        CarModel carModelToRemove = retrieveCarModelById(carModelId);
        if (carModelToRemove != null) {
            em.remove(carModelToRemove);
        } else {
            throw new CarModelNotFoundException("Car Model " + carModelId.toString() + " does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarModel>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
