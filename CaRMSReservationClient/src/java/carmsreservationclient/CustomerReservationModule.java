/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.ReservationTransactionSessionBeanRemote;
import entity.OwnCustomer;
import entity.Reservation;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteReservationException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author andre
 */
public class CustomerReservationModule {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ReservationTransactionSessionBeanRemote reservationTransactionSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CustomerReservationModule customerReservationModule;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private OwnCustomer currentOwnCustomer;

    public CustomerReservationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerReservationModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ReservationTransactionSessionBeanRemote reservationTransactionSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, CustomerReservationModule customerReservationModule, OwnCustomer currentOwnCustomer) {
        this();

        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.reservationTransactionSessionBeanRemote = reservationTransactionSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.customerReservationModule = customerReservationModule;
        this.currentOwnCustomer = currentOwnCustomer;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Customer Reservation Client ***\n");
            System.out.println("1: View All My Reservations");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doViewAllReservations();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Here are your reservations! ***\n");
        try {
            List<Reservation> allReservations = reservationSessionBeanRemote.retrieveReservationByCustomerId(currentOwnCustomer.getCustomerId());
            int i = 1;
            for (Reservation reservation : allReservations) {
                System.out.println(i + ": " + reservation.getReservationNumber());
                i++;
            }
            Integer response = 0;
            System.out.println("\n1: View Reservation Details");
            System.out.println("2: Exit\n");
            response = 0;
            Reservation selectedReservation = null;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    System.out.println("Please select an option!\n");
                    int catRef = 0;
                    for (Reservation reservation : allReservations) {

                        System.out.println(catRef + 1 + ": " + reservation.getReservationNumber());
                        catRef++;

                    }
                    catRef++;
                    System.out.println(catRef + ": Exit\n");
                    response = 0;
                    while (response < 1 || response > catRef) {
                        System.out.print("> ");

                        response = scanner.nextInt();

                        if (response > 0 && response < catRef) {
                            selectedReservation = allReservations.get(response - 1);
                            System.out.println("You have selected Reservation: " + selectedReservation.getReservationNumber() + "!\n");
                            doDisplayReservationDetail(selectedReservation);

                        } else if (response == catRef) {
                            break;
                        } else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                }
            }

        } catch (ReservationNotFoundException ex) {
            Logger.getLogger(CustomerReservationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doDisplayReservationDetail(Reservation reservation) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Reservation Number: " + reservation.getReservationNumber());
        System.out.println("Reservation Pick Up Date: " + reservation.getPickUpDateTime());
        System.out.println("Reservation Drop Off Date: " + reservation.getDropOffDateTime());
        System.out.println("Reservation Pick Up Outlet: " + reservation.getPickUpOutlet().getOutletName());
        System.out.println("Reservation Drop Off Outlet: " + reservation.getDropOffOutlet().getOutletName());
        int response = 0;

        System.out.println("\n*** Select an Option ***");
        System.out.println("1: Cancel Reservation");
        System.out.println("2: Exit\n");
        while (response < 1 || response > 2) {
            System.out.print("> ");

            response = scanner.nextInt();

            if (response == 1) {
                try {
                    reservationSessionBeanRemote.deleteReservation(reservation.getReservationId());
                    System.out.println("Reservation has been deleted successfully!");
                    System.out.println("No Cancellation Fees");
                    break;
                } catch (ReservationNotFoundException | DeleteReservationException ex) {
                    Logger.getLogger(CustomerReservationModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (response == 2) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

    }

}
