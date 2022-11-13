/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Employee;
import entity.RentalRate;
import entity.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author tian
 */
public class SalesManagementModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private Employee currentEmployee;

    public SalesManagementModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, Employee currentEmployee, CategorySessionBeanRemote categorySessionBeanRemote) {
        this();
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() == EmployeeAccessRightEnum.SALES_MANAGER || currentEmployee.getAccessRight() == EmployeeAccessRightEnum.SYSTEM_ADMINISTRATOR) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            while (true) {
                System.out.println("*** CaRMS Management Client :: Sales Management ***\n");
                System.out.println("1: Create New Rental Rate");
                System.out.println("2: View All Rental Rates");
                System.out.println("3: View Rental Rate Details");
                System.out.println("4: Back\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateNewRentalRate();
                    } else if (response == 2) {
                        doViewAllRentalRates();
                    } else if (response == 3) {
                        doViewRentalRateDetails();
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
        } else {
            throw new InvalidAccessRightException("You don't have rights to access the Sales Management module.");
        }
    }

    private void doCreateNewRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CaRMS Management Client :: Sales Management :: Create New Rental Rate ***\n");
        System.out.print("Enter Rental Rate Name> ");
        newRentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter Rental Rate Type> ");
        newRentalRate.setRentalRateType(scanner.nextLine().trim());

        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;

        System.out.print("Enter Car Category> ");
        String categoryName = scanner.nextLine().trim();
        try {
            Category cat = categorySessionBeanRemote.retrieveCategoryByCategoryName(categoryName);
            newRentalRate.setCarCategory(cat);
        } catch (CategoryNotFoundException ex) {
            System.out.println("Could not find Category!");
        }

        System.out.print("Enter Rate per Day> $");
        newRentalRate.setRentalAmount(scanner.nextBigDecimal());

        System.out.println("*** Please enter the Validity Start Details Accordingly ***");
        System.out.print("Enter Year(YYYY)> ");
        year = scanner.nextInt();
        System.out.print("Enter Month(MM)> ");
        month = scanner.nextInt();
        System.out.print("Enter Day(DD)> ");
        day = scanner.nextInt();
        System.out.print("Enter Hour(hh)> ");
        hour = scanner.nextInt();
        System.out.print("Enter Minute(mm)> ");
        minute = scanner.nextInt();

        newRentalRate.setStartDateTime(LocalDateTime.of(year, month, day, hour, minute));
        System.out.println("The rental rate START date is: " + newRentalRate.getStartDateTime().toString() + "\n");

        System.out.println("*** Please enter the Validity End Details Accordingly ***");
        System.out.print("Enter Year(YYYY)> ");
        year = scanner.nextInt();
        System.out.print("Enter Month(MM)> ");
        month = scanner.nextInt();
        System.out.print("Enter Day(DD)> ");
        day = scanner.nextInt();
        System.out.print("Enter Hour(hh)> ");
        hour = scanner.nextInt();
        System.out.print("Enter Minute(mm)> ");
        minute = scanner.nextInt();

        newRentalRate.setEndDateTime(LocalDateTime.of(year, month, day, hour, minute));
        System.out.println("The rental rate END date is: " + newRentalRate.getEndDateTime().toString() + "\n");

        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                Long newRentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(newRentalRate);
                System.out.println("New Rental Rate created successfully!: " + newRentalRateId + "\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new Rental Rate!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }

    private void doViewAllRentalRates() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS Management Client :: Sales Management :: View All Rental Rates ***\n");

        List<RentalRate> RentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%20s%20s%20s%20s%20s%20s%20s\n", "Rental Rate ID", "Name", "Type", "Category", "Daily Rate", "Validity Start Date", "Validity End Date");

        for (RentalRate rentalRate : RentalRates) {
            String n = "-";
            String startDate;
            String endDate;

            if (rentalRate.getStartDateTime() == null) {
                startDate = n;
            } else {
                startDate = rentalRate.getStartDateTime().toString();
            }
            if (rentalRate.getEndDateTime() == null) {
                endDate = n;
            } else {
                endDate = rentalRate.getEndDateTime().toString();
            }

            System.out.printf("%20s%20s%20s%20s%20s%20s%20s\n", rentalRate.getRentalRateId().toString(), rentalRate.getRentalRateName(), rentalRate.getRentalRateType(), rentalRate.getCarCategory().getCategoryName(), rentalRate.getRentalAmount().toString(), startDate, endDate);
        }

        System.out.print("Press ENTER to continue...> ");
        scanner.nextLine();
    }

    private void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Rental Rate Details ***\n");
        System.out.print("Enter Rental Rate ID> ");
        try {
            Long rrID = scanner.nextLong();
            try {
                RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rrID);
                String n = "-";
                String startDate;
                String endDate;

                if (rentalRate.getStartDateTime() == null) {
                    startDate = n;
                } else {
                    startDate = rentalRate.getStartDateTime().toString();
                }
                if (rentalRate.getEndDateTime() == null) {
                    endDate = n;
                } else {
                    endDate = rentalRate.getEndDateTime().toString();
                }
                System.out.printf("%20s%20s%20s%20s%20s%20s%20s\n", "Rental Rate ID", "Name", "Type", "Category", "Daily Rate", "Validity Start Date", "Validity End Date");
                System.out.printf("%20s%20s%20s%20s%20s%20s%20s\n", rentalRate.getRentalRateId().toString(), rentalRate.getRentalRateName(), rentalRate.getRentalRateType(), rentalRate.getCarCategory().getCategoryName(), rentalRate.getRentalAmount().toString(), startDate, endDate);
                System.out.println("------------------------");
                System.out.println("1: Update Rental Rate");
                System.out.println("2: Delete Rental Rate");
                System.out.println("3: Back\n");
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    doUpdateRentalRate(rentalRate);
                } else if (response == 2) {
                    doDeleteRentalRate(rentalRate);
                }
            } catch (RentalRateNotFoundException ex) {
                System.out.println("An error has occurred while retrieving Rental Rate: " + ex.getMessage() + "\n");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Input not valid!");
        }

    }

    private void doUpdateRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Integer integerInput;
        BigDecimal bigDecimalInput;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Rental Rate Details :: Update Rental Rate ***\n");
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            rentalRate.setRentalRateName(input);
        }

        System.out.print("Enter Rental Rate Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            rentalRate.setRentalRateType(input);
        }

        System.out.print("Enter Car Category (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            try {
                Category cat = categorySessionBeanRemote.retrieveCategoryByCategoryName(input);
                rentalRate.setCarCategory(cat);
            } catch (CategoryNotFoundException ex) {
                System.out.println("Could not find Category!");
            }
        }

        System.out.print("Enter Rate per Day (zero or negative number if no change)> $");
        bigDecimalInput = scanner.nextBigDecimal();
        scanner.nextLine();
        if (bigDecimalInput.compareTo(BigDecimal.ZERO) > 0) {
            rentalRate.setRentalAmount(bigDecimalInput);
        }

        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        
        System.out.print("Change Validity Start Date? (Enter Y to edit or leave blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.equals("y") || input.equals("Y")) {
            System.out.println("*** Please enter the Validity Start Details Accordingly ***");
            System.out.print("Enter Year(YYYY)> ");
            year = scanner.nextInt();
            System.out.print("Enter Month(MM)> ");
            month = scanner.nextInt();
            System.out.print("Enter Day(DD)> ");
            day = scanner.nextInt();
            System.out.print("Enter Hour(hh)> ");
            hour = scanner.nextInt();
            System.out.print("Enter Minute(mm)> ");
            minute = scanner.nextInt();

            rentalRate.setStartDateTime(LocalDateTime.of(year, month, day, hour, minute));
            System.out.println("The new rental rate START date is: " + rentalRate.getStartDateTime().toString() + "\n");
        }

        System.out.print("Change Validity End Date? (Enter Y to edit or leave blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.equals("y") || input.equals("Y")) {
            System.out.println("*** Please enter the Validity End Details Accordingly ***");
            System.out.print("Enter Year(YYYY)> ");
            year = scanner.nextInt();
            System.out.print("Enter Month(MM)> ");
            month = scanner.nextInt();
            System.out.print("Enter Day(DD)> ");
            day = scanner.nextInt();
            System.out.print("Enter Hour(hh)> ");
            hour = scanner.nextInt();
            System.out.print("Enter Minute(mm)> ");
            minute = scanner.nextInt();

            rentalRate.setEndDateTime(LocalDateTime.of(year, month, day, hour, minute));
            System.out.println("The new rental rate END date is: " + rentalRate.getEndDateTime().toString() + "\n");
        }

        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                rentalRateSessionBeanRemote.updateRentalRate(rentalRate);
                System.out.println("Rental Rate updated successfully!\n");
            } catch (RentalRateNotFoundException | UpdateRentalRateException ex) {
                System.out.println("An error has occurred while updating Rental Rate: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }

    private void doDeleteRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** CaRMS Management Client :: Sales Management :: View Rental Rate Details :: Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Rental Rate %s (Type: %s) (Enter 'Y' to Delete)> ", rentalRate.getRentalRateName(), rentalRate.getRentalRateType());
        input = scanner.nextLine().trim();

        if (input.equals("y") || input.equals("Y")) {
            try {
                rentalRateSessionBeanRemote.deleteRentalRate(rentalRate.getRentalRateId());
                System.out.println("Rental Rate deleted successfully!\n");
            } catch (RentalRateNotFoundException ex) {
                System.out.println("Rental Rate is not found!\n");
            } catch (DeleteRentalRateException ex) {
                System.out.println("Rental Rate is in use!\n");
                System.out.println("Rental Rate has been marked as disabled.\n");
            }
        } else {
            System.out.println("RentalRate NOT deleted!\n");
        }
    }

    private void showInputDataValidationErrorsForRentalRate(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
