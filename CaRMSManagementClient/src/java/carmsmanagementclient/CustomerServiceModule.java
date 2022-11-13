/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.Reservation;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author tian
 */
public class CustomerServiceModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private Employee currentEmployee;

    public CustomerServiceModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceModule(CustomerSessionBeanRemote customerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, Employee currentEmployee) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuCustomerService() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() == EmployeeAccessRightEnum.CS_EXECUTIVE || currentEmployee.getAccessRight() == EmployeeAccessRightEnum.SYSTEM_ADMINISTRATOR) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            while (true) {
                System.out.println("*** CaRMS Management Client :: Customer Service ***\n");
                System.out.println("1: Pickup Car");
                System.out.println("2: Return Car");
                System.out.println("3: Back\n");
                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doPickupCar();
                    } else if (response == 2) {
                        doReturnCar();
                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 3) {
                    break;
                }
            }
        } else {
            throw new InvalidAccessRightException("You don't have rights to access the Customer Service module.");
        }
    }

    private void doPickupCar() {
        Scanner scanner = new Scanner(System.in);
        Customer cust = new Customer();
        Car car = new Car();

        System.out.println("*** CaRMS Management Client :: Customer Service :: Pickup Car ***\n");
        System.out.print("Enter Customer ID> ");
        Long custID = scanner.nextLong();
        try {
            cust = customerSessionBeanRemote.retrieveCustomerByCustomerId(custID);
        } catch (CustomerNotFoundException ex) {
            System.out.println("Could not find Customer!");
        }

//      TODO: If rental fee payment is deferred during online reservation, it must be paid before the car can be collected.
        System.out.print("Enter Car Plate Number> ");
        String cp = scanner.next();
        try {
            car = carSessionBeanRemote.retrieveCarByCarPlate(cp);
            if (car.isIsDisabled()) {
                System.out.println("This car is disabled!");
                return;
            }

            car.setCarStatus(CarStatusEnum.RESERVED);
            car.setOutlet(null);
//            car.setCustomer
        } catch (CarNotFoundException ex) {
            System.out.println("Could not find Car!");
        }

    }

    private void doReturnCar() {
        Scanner scanner = new Scanner(System.in);
        Customer cust = new Customer();
        Car car = new Car();
        Outlet outlet = new Outlet();

        System.out.println("*** CaRMS Management Client :: Customer Service :: Return Car ***\n");
        System.out.print("Enter Customer ID> ");
        Long custID = scanner.nextLong();
        try {
            cust = customerSessionBeanRemote.retrieveCustomerByCustomerId(custID);
        } catch (CustomerNotFoundException ex) {
            System.out.println("Could not find Customer!");
        }

        System.out.print("Enter Car Plate Number> ");
        String cp = scanner.next();
        try {
            car = carSessionBeanRemote.retrieveCarByCarPlate(cp);
            if (car.isIsDisabled()) {
                System.out.println("This car is disabled!");
                return;
            }
            System.out.print("Enter Outlet to return to> ");
            String o = scanner.nextLine();
            try {
                outlet = outletSessionBeanRemote.retrieveOutletByOutletName(o);
                car.setCarStatus(CarStatusEnum.AVAILABLE);
                car.setOutlet(outlet);
            } catch (OutletNotFoundException ex) {
                System.out.println("Could not find Outlet!");
                return;
            }
        } catch (CarNotFoundException ex) {
            System.out.println("Could not find Car!");
        }
    }

}
