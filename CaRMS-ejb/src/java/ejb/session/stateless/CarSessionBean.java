/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarNotFoundException;
import util.exception.CarPlateExistsException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author tian
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCar(Car car) throws UnknownPersistenceException, InputDataValidationException, CarPlateExistsException {
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(car);
                em.flush();
                return car.getCarId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CarPlateExistsException();
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
    public Car retrieveCarById(Long id) throws CarNotFoundException {
        Car car = em.find(Car.class, id);
        if (car != null) {
            return car;
        } else {
            throw new CarNotFoundException("Car " + id.toString() + " does not exist!");
        }
    }

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c");

        return query.getResultList();
    }

    @Override
    public Car retrieveCarByCarPlate(String carPlate) throws CarNotFoundException {
        try {
            Query query = em.createQuery("SELECT c FROM Car c WHERE c.carPlate = :inCarPlate");
            query.setParameter("inCarPlate", carPlate);

            return (Car) query.getSingleResult();
        } catch (PersistenceException ex) {
            throw new CarNotFoundException();
        }

    }

    @Override
    public void updateCar(Car car) throws CarNotFoundException, InputDataValidationException, UpdateCarException {
        if (car != null && car.getCarId() != null) {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                Car carToUpdate = retrieveCarById(car.getCarId());
                if (carToUpdate.getCarPlateNumber().equals(car.getCarPlateNumber())) {
                    carToUpdate.setCarColor(car.getCarColor());
                    carToUpdate.setCarStatus(car.getCarStatus());
                } else {
                    throw new UpdateCarException("UpdateCarException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarNotFoundException("Car " + car.getCarId().toString() + " does not exist!");
        }
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException, DeleteCarException {
        Car carToRemove = retrieveCarById(carId);
        if (carToRemove.getReservations().isEmpty()) {
            throw new DeleteCarException("Car " + carId.toString() + " is associated with an existing reservation and cannot be deleted!");
        } else {
            em.remove(carToRemove);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
