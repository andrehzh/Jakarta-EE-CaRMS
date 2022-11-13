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
import util.exception.CarModelNameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.DeleteCarModelException;
import util.exception.UpdateCarModelException;

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
    public Long createNewCarModel(CarModel carModel) throws CarModelNameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(carModel);
                em.flush();
                return carModel.getCarModelId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CarModelNameExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
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
        Query query = em.createQuery("SELECT cm FROM CarModel cm ORDER BY cm.category.categoryName, cm.carModelBrand, cm.carModelName");

        return query.getResultList();
    }

    @Override
    public CarModel retrieveCarModelByCarModelName(String carModelName) throws CarModelNotFoundException {
        try {
            Query query = em.createQuery("SELECT cm FROM CarModel cm WHERE cm.carModelName = :inModelName");
            query.setParameter("inModelName", carModelName);

            return (CarModel) query.getSingleResult();
        } catch (PersistenceException ex) {
            throw new CarModelNotFoundException();
        }

    }

    @Override
    public CarModel retrieveCarModelByBrandAndName(String carModelBrand, String carModelName) throws CarModelNotFoundException {
        try {
            Query query = em.createQuery("SELECT cm FROM CarModel cm WHERE cm.carModelBrand = :inModelBrand AND cm.carModelName = :inModelName");
            query.setParameter("inModelBrand", carModelBrand);
            query.setParameter("inModelName", carModelName);

            return (CarModel) query.getSingleResult();
        } catch (PersistenceException ex) {
            throw new CarModelNotFoundException();
        }
    }

    @Override
    public void updateCarModel(CarModel carModel) throws CarModelNotFoundException, InputDataValidationException, UpdateCarModelException {
        if (carModel != null && carModel.getCarModelId() != null) {
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

            if (constraintViolations.isEmpty()) {
                CarModel carModelToUpdate = retrieveCarModelById(carModel.getCarModelId());
                try {
                    carModelToUpdate.setCarModelBrand(carModel.getCarModelBrand());
                    carModelToUpdate.setCarModelName(carModel.getCarModelName());
                    carModelToUpdate.setCategory(carModel.getCategory());
                } catch (PersistenceException ex) {
                    throw new UpdateCarModelException("UpdateCarModelException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarModelNotFoundException("Car Model " + carModel.getCarModelId().toString() + " does not exist!");
        }
    }

    @Override
    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException, DeleteCarModelException {
        CarModel carModelToRemove = retrieveCarModelById(carModelId);
        try {
            em.remove(carModelToRemove);
        } catch (PersistenceException ex) {
            carModelToRemove.setIsDisabled(true);
            throw new DeleteCarModelException("Car Model " + carModelId.toString() + " cannot be deleted and has been disabled instead.");
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
