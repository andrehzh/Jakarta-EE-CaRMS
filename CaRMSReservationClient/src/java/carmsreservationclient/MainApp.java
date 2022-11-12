package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.OwnCustomer;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author andre
 */
public class MainApp {

    private CarSessionBeanRemote carSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private OwnCustomer currentOwnCustomer;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CarSessionBeanRemote carSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) {
        this();

        this.carSessionBeanRemote = carSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Reservation Client ***\n");
            System.out.println("1: Existing Customer Login");
            System.out.println("2: Register as a new Customer");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doCustomerLogin();
                        System.out.println("*** Successfull Logged In! ***\n");
                    } catch (InvalidLoginCredentialException ex) {
                        ex.printStackTrace();
                    }
                } else if (response == 2) {
                    doRegisterNewCustomer();
                } else if (response == 3) {
                    System.out.println("doSearchCar()");
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    private void doCustomerLogin() throws InvalidLoginCredentialException {
        if (currentOwnCustomer == null) {

            Scanner scanner = new Scanner(System.in);
            String passportNumber = "";
            String password = "";

            System.out.println("*** CaRMS Reservation Client :: Login ***\n");
            System.out.print("Enter passsport number> ");
            passportNumber = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();

            if (passportNumber.length() > 0 && password.length() > 0) {
                currentOwnCustomer = customerSessionBeanRemote.customerLogin(passportNumber, password);
            } else {
                throw new InvalidLoginCredentialException("Missing Login Credentials!");
            }
        } else {
            throw new InvalidLoginCredentialException("Already Logged In!");
        }

    }

    private void doRegisterNewCustomer() {
        Scanner scanner = new Scanner(System.in);
        OwnCustomer newOwnCustomer = new OwnCustomer();

        System.out.println("*** CaRMS Reservation Client :: Registration ***\n");
        System.out.print("Enter name> ");
        newOwnCustomer.setCustomerName(scanner.nextLine().trim());
        System.out.print("Enter email> ");
        newOwnCustomer.setCustomerEmail(scanner.nextLine().trim());
        System.out.print("Enter password> ");
        newOwnCustomer.setCustomerPassword(scanner.nextLine().trim());
        System.out.print("Enter passport number> ");
        newOwnCustomer.setPassportNumber(scanner.nextLine().trim());
        System.out.print("Enter phone number> ");
        newOwnCustomer.setCustomerPhoneNum(scanner.nextLine().trim());

        Set<ConstraintViolation<OwnCustomer>> constraintViolations = validator.validate(newOwnCustomer);

        if (constraintViolations.isEmpty()) {
            try {
                Long newOwnCustomerId = customerSessionBeanRemote.createNewOwnCustomer(newOwnCustomer);
                System.out.println("New Customer created successfully!: " + newOwnCustomerId + "\n");
            } catch (CustomerEmailExistException ex) {
                System.out.println("An error has occurred while creating the new Customer!: The customer email already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new staff!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForOwnCustomer(constraintViolations);
        }
    }

    private void showInputDataValidationErrorsForOwnCustomer(Set<ConstraintViolation<OwnCustomer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
