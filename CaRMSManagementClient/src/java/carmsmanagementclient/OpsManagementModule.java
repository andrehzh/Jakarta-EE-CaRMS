/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Employee;
import entity.CarModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.CarModelNameExistException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.CarPlateExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteCarException;
import util.exception.DeleteCarModelException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;
import util.exception.UpdateCarModelException;

/**
 *
 * @author tian
 */
public class OpsManagementModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private Employee currentEmployee;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;

    public OpsManagementModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OpsManagementModule(CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote, Employee currentEmployee, EmployeeSessionBeanRemote employeeSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this();
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }

    public void menuOpsManagement() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() == EmployeeAccessRightEnum.OPS_MANAGER || currentEmployee.getAccessRight() == EmployeeAccessRightEnum.SYSTEM_ADMINISTRATOR) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            while (true) {
                System.out.println("*** CaRMS Management Client :: Operations Management ***\n");
                System.out.println("1: Create New Car Model");
                System.out.println("2: View All Car Models");
                System.out.println("3: Update Car Model");
                System.out.println("--------------------------");
                System.out.println("4: Delete Car Model");
                System.out.println("5: Create New Car");
                System.out.println("6: View All Cars");
                System.out.println("--------------------------");
                System.out.println("7: View Car Details");
                System.out.println("8: View Transit Driver Dispatch Records for Current Day Reservations");
                System.out.println("9: Assign Transit Driver");
                System.out.println("--------------------------");
                System.out.println("10: Update Transit As Completed");
                System.out.println("11: Back\n");
                response = 0;

                while (response < 1 || response > 11) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateNewCarModel();
                    } else if (response == 2) {
                        doViewAllCarModels();
                    } else if (response == 3) {
                        doUpdateCarModel();
                    } else if (response == 4) {
                        doDeleteCarModel();
                    } else if (response == 5) {
                        System.out.println("doCreateNewCar()");
                    } else if (response == 6) {
                        System.out.println("doViewAllCars()");
                    } else if (response == 7) {
                        System.out.println("doViewCarDetails()");
                    } else if (response == 8) {
                        System.out.println("doViewTransitDriverDispatchRecords()");
                    } else if (response == 9) {
                        System.out.println("doAssignTransitDriver()");
                    } else if (response == 10) {
                        System.out.println("doUpdateTransit()");
                    } else if (response == 11) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 11) {
                    break;
                }
            }
        } else {
            throw new InvalidAccessRightException("You don't have rights to access the Operations Management module.");
        }
    }

    private void doCreateNewCarModel() {
        Scanner scanner = new Scanner(System.in);
        CarModel newCarModel = new CarModel();
        System.out.println("*** CaRMS Management Client :: Operations Management :: Create New Car Model ***\n");
        System.out.print("Enter Car Make> ");
        newCarModel.setCarModelBrand(scanner.nextLine().trim());
        System.out.print("Enter Car Model> ");
        newCarModel.setCarModelName(scanner.nextLine().trim());

        System.out.print("Enter Car Category> ");
        String categoryName = scanner.nextLine().trim();
        try {
            Category cat = categorySessionBeanRemote.retrieveCategoryByCategoryName(categoryName);
            newCarModel.setCategory(cat);
        } catch (CategoryNotFoundException ex) {
            System.out.println("Could not find Category!");
        }

        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(newCarModel);

        if (constraintViolations.isEmpty()) {
            try {
                Long newCarModelId = carModelSessionBeanRemote.createNewCarModel(newCarModel);
                System.out.println("New Car Model created successfully!: " + newCarModelId + "\n");
            } catch (CarModelNameExistException ex) {
                System.out.println("Car Model Name exists!\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new car model!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCarModel(constraintViolations);
        }
    }

    private void doViewAllCarModels() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Operations Management :: View All Car Models ***\n");

        List<CarModel> CarModels = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.printf("%8s%20s%10s%20s\n", "Car Model ID", "Make", "Model", "Category");

        for (CarModel carModel : CarModels) {
            System.out.printf("%8s%20s%10s%20s\n", carModel.getCarModelId().toString(), carModel.getCarModelBrand(), carModel.getCarModelName(), carModel.getCategory().getCategoryName());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateCarModel() {
        Scanner scanner = new Scanner(System.in);
        String input;
        Integer integerInput;
        BigDecimal bigDecimalInput;
        CarModel carModel;
        System.out.println("*** CaRMS Management Client :: Operations Management :: Update Car Model ***\n");
        System.out.print("Enter Car Model ID> ");
        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelById(scanner.nextLong());

            System.out.print("Enter Car Make (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                carModel.setCarModelBrand(input);
            }

            System.out.print("Enter Car Model (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                carModel.setCarModelName(input);
            }

            System.out.print("Enter Car Category (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                try {
                    Category cat = categorySessionBeanRemote.retrieveCategoryByCategoryName(input);
                    carModel.setCategory(cat);
                } catch (CategoryNotFoundException ex) {
                    System.out.println("Could not find Category!");
                }
            }
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

            if (constraintViolations.isEmpty()) {
                try {
                    carModelSessionBeanRemote.updateCarModel(carModel);
                    System.out.println("Car Model updated successfully!\n");
                } catch (CarModelNotFoundException | UpdateCarModelException ex) {
                    System.out.println("An error has occurred while updating Car Model: " + ex.getMessage() + "\n");
                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForCarModel(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Could not find Car Model!");
        }

    }

    private void doDeleteCarModel() {
        Scanner scanner = new Scanner(System.in);
        String input;
        CarModel carModel;
        System.out.println("*** CaRMS Management Client :: Operations Management :: Delete Car Model ***\n");
        System.out.print("Enter Car Model ID> ");
        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelById(scanner.nextLong());

            System.out.printf("Confirm Delete Car Model %s %s (Enter 'Y' to Delete)> ", carModel.getCarModelBrand(), carModel.getCarModelName());
            input = scanner.nextLine().trim();

            if (input.equals("Y")) {
                try {
                    carModelSessionBeanRemote.deleteCarModel(carModel.getCarModelId());
                    System.out.println("Car Model deleted successfully!\n");
                } catch (CarModelNotFoundException | DeleteCarModelException ex) {
                    System.out.println("An error has occurred while deleting Car Model: " + ex.getMessage() + "\n");
                }
            } else {
                System.out.println("CarModel NOT deleted!\n");
            }
        } catch (CarModelNotFoundException ex) {
            System.out.println("Could not find Car Model!");
        }
    }

    private void doCreateNewCar() {
        Scanner scanner = new Scanner(System.in);
        Car newCar = new Car();
        String modelBrand;
        String modelName;
        CarModel carModel;

        System.out.println("*** CaRMS Management Client :: Sales Management :: Create New Car ***\n");
        System.out.print("Enter Car Make> ");
        modelBrand = scanner.nextLine().trim();
        System.out.print("Enter Car Model> ");
        modelName = scanner.nextLine().trim();

        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelByBrandAndName(modelBrand, modelName);
            newCar.setCarModel(carModel);
            System.out.print("Enter License Plate Number> ");
            newCar.setCarPlateNumber(scanner.nextLine().trim());
            System.out.print("Enter Car Color> ");
            newCar.setCarColor(scanner.nextLine().trim());

            System.out.println("Select Car Status:");
            System.out.println("1: In Outlet (Available)");
            System.out.println("2: On Rental (Reserved)");
            System.out.println("3: In Transit");
            System.out.println("4: On Repair");
            int response = 0;
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    newCar.setCarStatus(CarStatusEnum.AVAILABLE);
                    System.out.println("Enter Outlet> TODO");
                } else if (response == 2) {
                    newCar.setCarStatus(CarStatusEnum.RESERVED);
                    System.out.println("Enter Rental Customer> TODO");
                } else if (response == 3) {
                    newCar.setCarStatus(CarStatusEnum.IN_TRANSIT);
                } else if (response == 4) {
                    newCar.setCarStatus(CarStatusEnum.SERVICE_REPAIR);
                }
            }

            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {
                try {
                    Long newCarId = carSessionBeanRemote.createNewCar(newCar);
                    System.out.println("New Car created successfully!: " + newCarId + "\n");
                } catch (CarPlateExistsException ex) {
                    System.out.println("A car with license plate " + newCar.getCarPlateNumber() + " already exists!\n");
                } catch (UnknownPersistenceException ex) {
                    System.out.println("An unknown error has occurred while creating the new Car!: " + ex.getMessage() + "\n");
                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }
        } catch (CarModelNotFoundException ex) {
            System.out.println("Could not find Car Model!");
        }

    }

    private void doViewAllCars() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Sales Management :: View All Cars ***\n");

        List<Car> Cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%8s%20s%10s%20s%10s\n", "Car ID", "Category", "Make", "Model", "License Plate");

        for (Car car : Cars) {
            System.out.printf("%8s%20s%10s%20s%10s\n", car.getCarId().toString(), car.getCarModel().getCategory().getCategoryName(), car.getCarModel().getCarModelBrand(), car.getCarModel().getCarModelName(), car.getCarPlateNumber());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Car Details ***\n");
        System.out.print("Enter License Plate Number> ");
        String cp = scanner.nextLine().trim();

        try {
            Car car = carSessionBeanRemote.retrieveCarByCarPlate(cp);
            System.out.printf("%8s%20s%10s%20s%10s\n", "Car ID", "Category", "Make", "Model", "License Plate");
            System.out.printf("%8s%20s%10s%20s%10s\n", car.getCarId().toString(), car.getCarModel().getCategory().getCategoryName(), car.getCarModel().getCarModelBrand(), car.getCarModel().getCarModelName(), car.getCarPlateNumber());
            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateCar(car);
            } else if (response == 2) {
                doDeleteCar(car);
            }
        } catch (CarNotFoundException ex) {
            System.out.println("An error has occurred while retrieving Car: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Integer integerInput;
        BigDecimal bigDecimalInput;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Car Details :: Update Car ***\n");

        String modelBrand;
        String modelName;
        CarModel carModel;

        System.out.print("Enter Car Make (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            modelBrand = scanner.nextLine().trim();
        } else {
            modelBrand = car.getCarModel().getCarModelBrand();
        }

        System.out.print("Enter Car Model (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            modelName = scanner.nextLine().trim();
        } else {
            modelName = car.getCarModel().getCarModelName();
        }

        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelByBrandAndName(modelBrand, modelName);
            car.setCarModel(carModel);

            System.out.print("Enter License Plate Number (blank if no change)> ");
            car.setCarPlateNumber(scanner.nextLine().trim());
            System.out.print("Enter Car Color (blank if no change)> ");
            car.setCarColor(scanner.nextLine().trim());

            System.out.println("Select Car Status:");
            System.out.println("1: In Outlet (Available)");
            System.out.println("2: On Rental (Reserved)");
            System.out.println("3: In Transit");
            System.out.println("4: On Repair");
            System.out.println("5: No change (Current Status: " + car.getCarStatus().toString() + ")");
            int response = 0;
            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    car.setCarStatus(CarStatusEnum.AVAILABLE);
                    System.out.println("Enter Outlet> TODO");
                } else if (response == 2) {
                    car.setCarStatus(CarStatusEnum.RESERVED);
                    System.out.println("Enter Rental Customer> TODO");
                } else if (response == 3) {
                    car.setCarStatus(CarStatusEnum.IN_TRANSIT);
                } else if (response == 4) {
                    car.setCarStatus(CarStatusEnum.SERVICE_REPAIR);
                }
            }

            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                try {
                    carSessionBeanRemote.updateCar(car);
                    System.out.println("Car updated successfully!\n");
                } catch (CarNotFoundException | UpdateCarException ex) {
                    System.out.println("An error has occurred while updating Car: " + ex.getMessage() + "\n");
                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Could not find Car Model!");
        }

    }

    private void doDeleteCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Car Details :: Delete Car ***\n");
        System.out.printf("Confirm Delete %s %s %s (Enter 'Y' to Delete)> ", car.getCarModel().getCarModelBrand(), car.getCarModel().getCarModelName(), car.getCarPlateNumber());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                carSessionBeanRemote.deleteCar(car.getCarId());
                System.out.println("Car deleted successfully!\n");
            } catch (CarNotFoundException | DeleteCarException ex) {
                System.out.println("An error has occurred while deleting Car: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Car NOT deleted!\n");
        }
    }

    private void showInputDataValidationErrorsForCarModel(Set<ConstraintViolation<CarModel>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<Car>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
