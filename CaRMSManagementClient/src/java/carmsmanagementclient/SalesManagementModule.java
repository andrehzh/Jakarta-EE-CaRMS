/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Employee;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
public class SalesManagementModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private Employee currentEmployee;

    public SalesManagementModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, Employee currentEmployee) {
        this();
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRightEnum.SALES_MANAGER | currentEmployee.getAccessRight() != EmployeeAccessRightEnum.SYSTEM_ADMINISTRATOR) {
            throw new InvalidAccessRightException("You don't have rights to access the Sales Management module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CaRMS Management Client :: Sales Management ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {

                } else if (response == 3) {

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
    
    private void doCreateNewRentalRate()
    {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        String rentalRateName, BigDecimal rentalAmount, Date rentalDate
        System.out.println("*** CaRMS Management Client :: Sales Management :: Create New Rental Rate ***\n");
        System.out.print("Enter Rental Rate Name> ");
        newRentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter Rental Amount> ");
        newRentalRate.setRentalAmount(scanner.nextBigDecimal());
        System.out.print("Enter Rental Date (DDMMYY)> ");
        newRentalRate.setRentalDate(scanner.());
        
        while(true)
        {
            System.out.print("Select Access Right (1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                newRentalRate.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newRentalRate.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newRentalRate.setPassword(scanner.nextLine().trim());
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(newRentalRate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Long newRentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(newRentalRate);
                System.out.println("New rentalRate created successfully!: " + newRentalRateId + "\n");
            }
            catch(RentalRateUsernameExistException ex)
            {
                System.out.println("An error has occurred while creating the new rentalRate!: The user name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new rentalRate!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }

}
