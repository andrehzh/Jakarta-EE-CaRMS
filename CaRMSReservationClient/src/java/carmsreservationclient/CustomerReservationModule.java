/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import entity.OwnCustomer;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InvalidLoginCredentialException;

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
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CustomerReservationModule customerReservationModule;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private OwnCustomer currentOwnCustomer;

    public CustomerReservationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerReservationModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ReservationTransactionSessionBeanRemote reservationTransactionSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, CustomerReservationModule customerReservationModule, OwnCustomer currentOwnCustomer) {
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
                    System.out.println("doViewAllReservations()");
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

}
