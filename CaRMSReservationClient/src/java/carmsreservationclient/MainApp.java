package carmsreservationclient;

import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.ReservationTransactionSessionBeanRemote;
import entity.Car;
import entity.CarModel;
import entity.Category;
import entity.Outlet;
import entity.OwnCustomer;
import entity.Reservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.exception.CustomerEmailExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAvailableCarException;
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

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ReservationTransactionSessionBeanRemote reservationTransactionSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
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

    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ReservationTransactionSessionBeanRemote reservationTransactionSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) {
        this();

        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.reservationTransactionSessionBeanRemote = reservationTransactionSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.currentOwnCustomer = currentOwnCustomer;
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
                    doSearchCar();
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

    private void doSearchCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            //get customer to enter relevant details such as
            //car category mcq null
            System.out.println("*** Search for a Car! ***\n");
            Category selectedCategory = null;
            CarModel selectedCarModel = null;
            Outlet selectedPickUpOutlet = null;
            Outlet selectedDropOffOutlet = null;
            LocalDateTime selectedStartDateTime = null;
            LocalDateTime selectedEndDateTime = null;
            List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
            System.out.println("Please Select a Car Category or No Preference!");
            int catRef = 0;
            for (Category category : categories) {

                System.out.println(catRef + 1 + ": " + category.getCategoryName());
                catRef++;

            }
            catRef++;
            System.out.println(catRef + ": No Preference\n");
            int response = 0;
            while (response < 1 || response > catRef) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response > 0 && response < catRef) {
                    selectedCategory = categories.get(response - 1);
                    System.out.println("You have selected Category: " + selectedCategory.getCategoryName() + "!\n");
                    break;
                } else if (response == catRef) {
                    System.out.println("You have selected Category: No Preference\n");
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            //now is pulling the car models from the category if have otherwise car model is null also
            if (selectedCategory == null) {
                System.out.println("You have selected Car Model: No Preference\n");
            } else {
                List<CarModel> allCarModels = carModelSessionBeanRemote.retrieveAllCarModels();
                List<CarModel> carModels = new ArrayList<>();
                for (CarModel carModel : allCarModels) {
                    if (carModel.getCategory().equals(selectedCategory)) {
                        carModels.add(carModel);
                    }
                }
                System.out.println("Please Select a Car Model or No Preference!");
                int cmRef = 0;
                for (CarModel carModel : carModels) {

                    System.out.println(cmRef + 1 + ": " + carModel.getCarModelName() + " " + carModel.getCarModelBrand());
                    cmRef++;

                }
                cmRef++;
                System.out.println(cmRef + ": No Preference\n");
                response = 0;
                while (response < 1 || response > cmRef) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response > 0 && response < cmRef) {
                        selectedCarModel = carModels.get(response - 1);
                        System.out.println("You have selected Car Model: " + selectedCarModel.getCarModelName() + " " + selectedCarModel.getCarModelBrand() + "!\n");
                        break;
                    } else if (response == cmRef) {
                        System.out.println("You have selected Car Model: No Preference\n");
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            }

            //after selecting car models and car brand will select outlet pick up + drop off + reservation time
            System.out.println("*** Please enter the Start Date Details Accordingly ***");

            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int minute = 0;

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

            selectedStartDateTime = LocalDateTime.of(year, month, day, hour, minute);
            System.out.println("\n*** Please enter the End Date Details Accordingly ***");

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

            selectedEndDateTime = LocalDateTime.of(year, month, day, hour, minute);
            if (selectedEndDateTime.isBefore(selectedStartDateTime)) {
                try {
                    throw new InvalidInputException("End date later then Start date");
                } catch (InvalidInputException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("You have selected Reservation from " + selectedStartDateTime.toString() + " TO " + selectedEndDateTime.toString());

                //now select an outlet need to account for opening times actually fucckkkkcckkckckckkk
                List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
                catRef = 0;
                System.out.println("\nOur outlets!");
                for (Outlet outlet : outlets) {

                    System.out.println(catRef + 1 + ": " + outlet.getOutletName());
                    catRef++;

                }
                System.out.println("\nSelect a Pick Up Outlet:");
                response = 0;
                catRef++;
                while (response < 1 || response > catRef) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response > 0 && response < catRef) {
                        selectedPickUpOutlet = outlets.get(response - 1);
                        if(selectedPickUpOutlet.getOpeningTime() != null && selectedStartDateTime.toLocalTime().isBefore(selectedPickUpOutlet.getOpeningTime()) 
                                || selectedPickUpOutlet.getClosingTime()!= null && selectedStartDateTime.toLocalTime().isAfter(selectedPickUpOutlet.getClosingTime())) {
                            System.out.println("You have selected Outlet: " + selectedPickUpOutlet.getOutletName() + " it is unavailable at that time please choose again!\n");
                            response = 0;
                        } else {
                        System.out.println("You have selected Outlet: " + selectedPickUpOutlet.getOutletName() + "!\n");
                        break;
                        }
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                System.out.println("Select a Drop Off Outlet:");
                response = 0;
                while (response < 1 || response > catRef) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response > 0 && response < catRef) {
                        selectedDropOffOutlet = outlets.get(response - 1);
                        if(selectedDropOffOutlet.getOpeningTime() != null && selectedEndDateTime.toLocalTime().isBefore(selectedDropOffOutlet.getOpeningTime()) 
                                || selectedDropOffOutlet.getClosingTime()!= null && selectedEndDateTime.toLocalTime().isAfter(selectedDropOffOutlet.getClosingTime())) {
                            System.out.println("You have selected Outlet: " + selectedPickUpOutlet.getOutletName() + " it is unavailable at that time please choose again!\n");
                            response = 0;
                        } else {
                        System.out.println("You have selected Category: " + selectedDropOffOutlet.getOutletName() + "!\n");
                        break;
                        }
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                String categoryStr = "";
                String cmStr = "";
                if (selectedCategory == null) {
                    categoryStr = "No Preferance";
                } else {
                    categoryStr = selectedCategory.getCategoryName();
                }
                if (selectedCarModel == null) {
                    cmStr = "No Preferance";
                } else {
                    cmStr = selectedCarModel.getCarModelName();
                }

                System.out.println("You have selected! \nCategory> " + categoryStr + "\nCar Model> " + cmStr + "\nStart Date & Time: " + selectedStartDateTime + "\nEnd Date & Time: " + selectedEndDateTime + "\nPick Up Outlet> " + selectedPickUpOutlet.getOutletName() + "\nDrop Off Outlet> " + selectedDropOffOutlet.getOutletName() + "\n");
            }
//NOW IS THE FUCKING CODEEEE
//assuming that i have all the details how should i go about it
//if(have) else(all)
//im going to need my days of availability?or should i use my ejb shit..
//i have to slot the search request in with my other confirmed request at the moment -> all pending requests or request that have not happened yet.
//fake allocate them to each car? i would like to give my code a go actually... so first i fake allocate then once that is done i check for the free dates?
//all cars should be available unless currently executing a request.
//or just check the capacity everyday
//so i have a list of pending requests
//i have a list of available cars
//i have a new request via this search
//allocate all the old requests to the cars first in the efficient manner
//pseudo assign the reservations btw..
//all cars should have no reservations and are available at the moment -> this would be done ultimately
//1. loop thru the pending reservations
//2. assign the reservations to the best car
//3. add in my new requests
//4. loop through all the cars and return what gives me a yes.
            Reservation searchReservation = new Reservation();
            searchReservation.setCategory(selectedCategory);
            searchReservation.setCarModel(selectedCarModel);
            searchReservation.setPickUpDateTime(selectedEndDateTime);
            searchReservation.setDropOffDateTime(selectedEndDateTime);
            searchReservation.setPickUpOutlet(selectedPickUpOutlet);
            searchReservation.setDropOffOutlet(selectedDropOffOutlet);

            List<Reservation> pendingReservations = reservationSessionBeanRemote.retrieveAllReservations();
            List<Car> allCars = carSessionBeanRemote.retrieveAllCars();
            List<Car> availableCars = new ArrayList<>();
            for (Car car : allCars) {
                if (car.getCarStatus() == CarStatusEnum.AVAILABLE) {
                    car.setReservations(new ArrayList<>());
                    availableCars.add(car);
                }
            }

            System.out.println(availableCars.toString());

            //sorts the pending reservations by earliest
            Collections.sort(pendingReservations, (Reservation o1, Reservation o2) -> o1.getPickUpDateTime().compareTo(o2.getPickUpDateTime()));
            System.out.println(pendingReservations.toString());

            //find conflicting reservations
            //loop through the list to find similar or conflicting reservations and get a number of conflicting reservations?
            //i need to be returning a list of Cars which are available so actually i should sort through the list of reservations
            //prelim tag to a car
            //then sort through the remaining list of cars against my requirement
            //sort through all reservations and remove non conflicting reservations
            List<Reservation> conflictingReservations = new ArrayList<>();
            //afterwards just assign at random n return the remainder cars?
            for (Reservation pendingReservation : pendingReservations) {
                //get all the reservations requirements
                Category categoryRequirement = pendingReservation.getCategory();
                CarModel cmRequirement = pendingReservation.getCarModel();
                Outlet outletPickUp = pendingReservation.getPickUpOutlet();
                Outlet outletDropOff = pendingReservation.getDropOffOutlet();
                LocalDateTime pickUpDate = pendingReservation.getPickUpDateTime();
                LocalDateTime dropOffDate = pendingReservation.getDropOffDateTime();

                //check if the category is even the same or null
                //i want to keep all the conflicting reservations
                if (categoryRequirement == null || categoryRequirement.equals(searchReservation.getCategory())) {
                    if (cmRequirement == null || cmRequirement.equals(searchReservation.getCarModel())) {
                        if (pickUpDate.isBefore(searchReservation.getDropOffDateTime()) && dropOffDate.isAfter(searchReservation.getPickUpDateTime())) {

                            conflictingReservations.add(pendingReservation);

                        } else if (dropOffDate.isAfter(searchReservation.getPickUpDateTime()) && pickUpDate.isBefore(searchReservation.getDropOffDateTime())) {

                            conflictingReservations.add(pendingReservation);

                        } else if (pickUpDate.isBefore(searchReservation.getDropOffDateTime().plusHours(2)) && dropOffDate.isAfter(searchReservation.getPickUpDateTime())
                                && (!outletPickUp.equals(searchReservation.getDropOffOutlet()))) {

                            conflictingReservations.add(pendingReservation);

                        } else if (dropOffDate.isAfter(searchReservation.getPickUpDateTime().minusHours(2)) && pickUpDate.isBefore(searchReservation.getDropOffDateTime())
                                && (!outletDropOff.equals(searchReservation.getPickUpOutlet()))) {

                            conflictingReservations.add(pendingReservation);
                        }
                    }
                }
            }

            System.out.println(conflictingReservations.toString());
            //check the cars to see which fits
            //now all i have to do is minus
            List<Car> suitableCars = new ArrayList<>();
            //suitable cars
            for (Car car : availableCars) {
                Category categoryRequirement = searchReservation.getCategory();
                CarModel cmRequirement = searchReservation.getCarModel();
                if (categoryRequirement == null || categoryRequirement.equals(car.getCarModel().getCategory())) {
                    if (cmRequirement == null || cmRequirement.equals(car.getCarModel())) {
                        suitableCars.add(car);
                    }
                }
            }

            List<Car> allocatedCars = new ArrayList<>();
            if (suitableCars.size() > conflictingReservations.size()) {
                int i = 0;
                for (Reservation reservation : conflictingReservations) {
                    allocatedCars.add(suitableCars.get(i));
                    i++;
                }
                suitableCars.removeAll(allocatedCars);

                System.out.println(suitableCars.toString());
                //display the cars in a string and let the user choose
                System.out.println("*** Here is the list of available cars you can choose from! ***\n");
                catRef = 0;
                for (Car car : suitableCars) {

                    System.out.println(catRef + 1 + ": " + car.getCarModel().getCategory().getCategoryName() + " " + car.getCarModel().getCarModelBrand() + " " + car.getCarModel().getCarModelName() + " " + car.getCarColor());
                    catRef++;

                }
                catRef++;
                Car selectedCar = null;
                System.out.println("*** Would you like to make a reservation? ***\n");
                System.out.println("1: Yes (Logged In Customer Only)");
                System.out.println("2: No\n");
                response = 0;
                while (response < 1 || response > 2) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        if (currentOwnCustomer != null) {
                            System.out.println("*** Please select your preferred vehicle! ***\n");
                            catRef = 0;
                            for (Car car : suitableCars) {

                                System.out.println(catRef + 1 + ": " + car.getCarModel().getCategory().getCategoryName() + " " + car.getCarModel().getCarModelBrand() + " " + car.getCarModel().getCarModelName() + " " + car.getCarColor());
                                catRef++;

                            }
                            catRef++;
                            response = 0;
                            while (response < 1 || response > catRef) {
                                System.out.print("> ");

                                response = scanner.nextInt();

                                if (response > 0 && response < catRef - 1) {
                                    selectedCar = suitableCars.get(response - 1);
                                    System.out.println("You have selected Car: " + selectedCar.getCarModel().getCarModelName() + "!\n");
                                    break;
                                } else {
                                    System.out.println("Invalid option, please try again!\n");
                                }
                            }
                            
                            //need to make the new reservation here.
                        } else {
                            throw new InvalidLoginCredentialException("You are not Logged In!");
                        }
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

            } else {
                throw new NoAvailableCarException();
            }

        } catch (NoAvailableCarException | InvalidLoginCredentialException ex) {
            ex.printStackTrace();
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