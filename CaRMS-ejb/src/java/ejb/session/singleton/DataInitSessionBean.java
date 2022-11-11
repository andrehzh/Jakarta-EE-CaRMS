/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarModel;
import entity.Category;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.PersistenceException;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.CarModelNameExistException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.CarPlateExistsException;
import util.exception.CategoryNameExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.EmployeeEmailExistsException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNameExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author andre
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A");
        } catch (OutletNotFoundException ex) {
        initData();
       }
    }

    private void initData() {
        try {

            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet A", (LocalTime) null, (LocalTime) null));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet B", (LocalTime) null, (LocalTime) null));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet C", LocalTime.of(8, 0), LocalTime.of(22, 0)));

            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A1", "employeea1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A2", "employeea2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A3", "employeea3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A4", "employeea4", "password", EmployeeAccessRightEnum.EMPLOYEE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A5", "employeea5", "password", EmployeeAccessRightEnum.EMPLOYEE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea1"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea2"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea3"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea4"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea5"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B1", "employeeb1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B2", "employeeb2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B3", "employeeb3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb1"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb2"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb3"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C1", "employeec1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C2", "employeec2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C3", "employeec3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec1"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec2"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec3"));

            categorySessionBeanLocal.createNewCategory(new Category("Standard Sedan", "Standard Sedan"));
            categorySessionBeanLocal.createNewCategory(new Category("Family Sedan", "Family Sedan"));
            categorySessionBeanLocal.createNewCategory(new Category("Luxury Sedan", "Luxury Sedan"));
            categorySessionBeanLocal.createNewCategory(new Category("SUV and Minivan", "SUV and Minivan"));

            carModelSessionBeanLocal.createNewCarModel(new CarModel("Toyota", "Corolla", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
            carModelSessionBeanLocal.createNewCarModel(new CarModel("Honda", "Civic", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
            carModelSessionBeanLocal.createNewCarModel(new CarModel("Nissan", "Sunny", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
            carModelSessionBeanLocal.createNewCarModel(new CarModel("Mercedes", "E Class", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            carModelSessionBeanLocal.createNewCarModel(new CarModel("BMW", "5 Series", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            carModelSessionBeanLocal.createNewCarModel(new CarModel("Audi", "A6", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("E Class"));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("5 Series"));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("A6"));


            carSessionBeanLocal.createNewCar(new Car("SS00A1TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            carSessionBeanLocal.createNewCar(new Car("SS00A2TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            carSessionBeanLocal.createNewCar(new Car("SS00A3TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            carSessionBeanLocal.createNewCar(new Car("LS00A4ME", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("E Class"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A1TC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A2TC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A3TC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("LS00A4ME"));
            carSessionBeanLocal.createNewCar(new Car("SS00B1HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            carSessionBeanLocal.createNewCar(new Car("SS00B2HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            carSessionBeanLocal.createNewCar(new Car("SS00B3HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            carSessionBeanLocal.createNewCar(new Car("LS00B4B5", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("5 Series"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B1HC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B2HC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B3HC"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A3TC"));
            carSessionBeanLocal.createNewCar(new Car("SS00C1NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            carSessionBeanLocal.createNewCar(new Car("SS00C2NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            carSessionBeanLocal.createNewCar(new Car("SS00C3NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            carSessionBeanLocal.createNewCar(new Car("LS00C4A6", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("A6"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C1NS"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C2NS"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C3NS"));
            outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("LS00C4A6"));

            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(100.00), (LocalDateTime) null, (LocalDateTime) null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Weekend Promo", "Promotion", BigDecimal.valueOf(80.00), LocalDateTime.of(2022, Month.DECEMBER, 9, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 11, 0, 0), categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(200.00), (LocalDateTime) null, (LocalDateTime) null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Family Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(300.00), (LocalDateTime) null, (LocalDateTime) null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Monday", "Peak", BigDecimal.valueOf(310.00), LocalDateTime.of(2022, Month.DECEMBER, 5, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 5, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Tuesday", "Peak", BigDecimal.valueOf(320.00), LocalDateTime.of(2022, Month.DECEMBER, 6, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 6, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Wednesday", "Peak", BigDecimal.valueOf(330.00), LocalDateTime.of(2022, Month.DECEMBER, 7, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 7, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Weekday Promo", "Promotion", BigDecimal.valueOf(250.00), LocalDateTime.of(2022, Month.DECEMBER, 7, 12, 0), LocalDateTime.of(2022, Month.DECEMBER, 8, 12, 0), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
            rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(400.00), (LocalDateTime) null, (LocalDateTime) null, categorySessionBeanLocal.retrieveCategoryByCategoryName("SUV and Minivan")));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(1L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(2L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Family Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(3L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(4L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(5L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(6L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(7L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(8L));
            categorySessionBeanLocal.retrieveCategoryByCategoryName("SUV and Minivan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(9L));

            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com"));
            //} catch (CategoryNotFoundException | RentalRateNotFoundException | CarNotFoundException | CarModelNotFoundException | OutletNotFoundException | CarPlateExistsException | UnknownPersistenceException | PersistenceException | InputDataValidationException ex) {
        } catch ( CarModelNameExistException | OutletNameExistException | CategoryNotFoundException | RentalRateNotFoundException | CarNotFoundException | CarModelNotFoundException | OutletNotFoundException | EmployeeNotFoundException | PartnerNameExistException | CarPlateExistsException | CategoryNameExistsException | EmployeeEmailExistsException | UnknownPersistenceException | PersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }

//    private void initData() {
//        try {
//
//            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet A", (LocalTime) null, (LocalTime) null));
//            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet B", (LocalTime) null, (LocalTime) null));
//            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet C", LocalTime.of(8, 0), LocalTime.of(22, 0)));
//            try {
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A1", "employeea1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A2", "employeea2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A3", "employeea3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A4", "employeea4", "password", EmployeeAccessRightEnum.EMPLOYEE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A5", "employeea5", "password", EmployeeAccessRightEnum.EMPLOYEE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea1"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea2"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea3"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea4"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeea5"));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B1", "employeeb1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B2", "employeeb2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B3", "employeeb3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb1"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb2"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeeb3"));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C1", "employeec1", "password", EmployeeAccessRightEnum.SALES_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C2", "employeec2", "password", EmployeeAccessRightEnum.OPS_MANAGER, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C3", "employeec3", "password", EmployeeAccessRightEnum.CS_EXECUTIVE, outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec1"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec2"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getEmployees().add(employeeSessionBeanLocal.retrieveEmployeeByEmployeeEmail("employeec3"));
//            } catch (OutletNotFoundException | EmployeeNotFoundException ex) {
//                ex.printStackTrace();
//            }
//
//            categorySessionBeanLocal.createNewCategory(new Category("Standard Sedan", "Standard Sedan"));
//            categorySessionBeanLocal.createNewCategory(new Category("Family Sedan", "Family Sedan"));
//            categorySessionBeanLocal.createNewCategory(new Category("Luxury Sedan", "Luxury Sedan"));
//            categorySessionBeanLocal.createNewCategory(new Category("SUV and Minivan", "SUV and Minivan"));
//            try {
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("Toyota", "Corolla", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("Honda", "Civic", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("Nissan", "Sunny", categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("Mercedes", "E Class", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("BMW", "5 Series", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                carModelSessionBeanLocal.createNewCarModel(new CarModel("Audi", "A6", categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("E Class"));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("5 Series"));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getCarModels().add(carModelSessionBeanLocal.retrieveCarModelByCarModelName("A6"));
//            } catch (CategoryNotFoundException | CarModelNotFoundException ex) {
//                ex.printStackTrace();
//            }
//
//            try {
//                carSessionBeanLocal.createNewCar(new Car("SS00A1TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                carSessionBeanLocal.createNewCar(new Car("SS00A2TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                carSessionBeanLocal.createNewCar(new Car("SS00A3TC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Corolla"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                carSessionBeanLocal.createNewCar(new Car("LS00A4ME", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("E Class"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A1TC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A2TC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A3TC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet A").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("LS00A4ME"));
//                carSessionBeanLocal.createNewCar(new Car("SS00B1HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                carSessionBeanLocal.createNewCar(new Car("SS00B2HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                carSessionBeanLocal.createNewCar(new Car("SS00B3HC", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Civic"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                carSessionBeanLocal.createNewCar(new Car("LS00B4B5", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("5 Series"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B1HC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B2HC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00B3HC"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet B").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00A3TC"));
//                carSessionBeanLocal.createNewCar(new Car("SS00C1NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                carSessionBeanLocal.createNewCar(new Car("SS00C2NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                carSessionBeanLocal.createNewCar(new Car("SS00C3NS", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("Sunny"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                carSessionBeanLocal.createNewCar(new Car("LS00C4A6", CarStatusEnum.AVAILABLE, carModelSessionBeanLocal.retrieveCarModelByCarModelName("A6"), outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C")));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C1NS"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C2NS"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("SS00C3NS"));
//                outletSessionBeanLocal.retrieveOutletByOutletName("Outlet C").getCars().add(carSessionBeanLocal.retrieveCarByCarPlate("LS00C4A6"));
//            } catch (OutletNotFoundException | CarModelNotFoundException | CarNotFoundException ex) {
//                ex.printStackTrace();
//            }
//
//            try {
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(100.00), (LocalDateTime)null, (LocalDateTime)null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Weekend Promo", "Promotion", BigDecimal.valueOf(80.00), LocalDateTime.of(2022, Month.DECEMBER, 9, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 11, 0, 0), categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(200.00), (LocalDateTime)null, (LocalDateTime)null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Family Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(300.00), (LocalDateTime)null, (LocalDateTime)null, categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Monday", "Peak", BigDecimal.valueOf(310.00), LocalDateTime.of(2022, Month.DECEMBER, 5, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 5, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Tuesday", "Peak", BigDecimal.valueOf(320.00), LocalDateTime.of(2022, Month.DECEMBER, 6, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 6, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Wednesday", "Peak", BigDecimal.valueOf(330.00), LocalDateTime.of(2022, Month.DECEMBER, 7, 0, 0), LocalDateTime.of(2022, Month.DECEMBER, 7, 23, 59), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Weekday Promo", "Promotion", BigDecimal.valueOf(250.00), LocalDateTime.of(2022, Month.DECEMBER, 7, 12, 0), LocalDateTime.of(2022, Month.DECEMBER, 8, 12, 0), categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan")));
//                rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Default", "Default", BigDecimal.valueOf(400.00), (LocalDateTime)null, (LocalDateTime)null, categorySessionBeanLocal.retrieveCategoryByCategoryName("SUV and Minivan")));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(1L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Standard Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(2L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Family Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(3L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(4L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(5L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(6L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(7L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("Luxury Sedan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(8L));
//                categorySessionBeanLocal.retrieveCategoryByCategoryName("SUV and Minivan").getRentalRates().add(rentalRateSessionBeanLocal.retrieveRentalRateByRentalRateId(9L));
//            } catch (CategoryNotFoundException | RentalRateNotFoundException ex) {
//                ex.printStackTrace();
//            }
//
//            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com"));
//
//        } catch (PartnerNameExistException | CarPlateExistsException | CategoryNameExistsException | EmployeeEmailExistsException | UnknownPersistenceException | PersistenceException | InputDataValidationException ex) {
//            ex.printStackTrace();
//        }
//    }
}
