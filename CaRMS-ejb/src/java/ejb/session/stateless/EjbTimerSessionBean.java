/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.CarStatusEnum;
import util.exception.NoAvailableCarException;

/**
 *
 * @author andre
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @Schedule(hour = "2", info = "allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations() throws NoAvailableCarException {
        //for reservations that are not already on going. filters out any reservation whereby start day passed and end day not here yet.
        //checks all reservations made 24 hours prior 2am to 2am
        //supposed to allocate the most efficient and cost savings car
        //while maximising the capacity of the cars...
        //something like a reshuffling of cars
        //cause during search n reserve they have a preliminary sort of reservation towards a car
        //afterwards it changes/can change..
        //because idk where the car is gonna be in the future??
        //what am i accounting for by reshuffling??
        //possible cancels??
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("********** EjbTimerSessionBean.allocateCarsToCurrentDayReservations(): Timeout at " + timeStamp);

        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservations();
        List<Car> cars = carSessionBeanLocal.retrieveAllCars();

        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.getCarStatus() == CarStatusEnum.AVAILABLE) {
                availableCars.add(car);
            }
        }

        List<Reservation> pendingReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getPickUpDateTime().isAfter(currentDateTime) || reservation.getPickUpDateTime().isEqual(currentDateTime)) {
                pendingReservations.add(reservation);
            }
        }

        //sort pendingReservations in order of urgency:
        //sorting pending reservations.
        Collections.sort(pendingReservations, (Reservation o1, Reservation o2) -> o1.getPickUpDateTime().compareTo(o2.getPickUpDateTime()));

        for (Reservation reservation : pendingReservations) {

            long bestCarId = 0;
            boolean needDispatch = true;

            //alldays in abooking
            long numOfDays = ChronoUnit.DAYS.between(reservation.getPickUpDateTime(), reservation.getDropOffDateTime());
            List<LocalDateTime> allDays = IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDays)
                    .mapToObj(i -> reservation.getPickUpDateTime().plusDays(i))
                    .collect(Collectors.toList());

            for (Car car : availableCars) {
                //need to iterate thru existing reservations
                //check if car is even the correct category
                if (reservation.getCategory() == car.getCarModel().getCategory() || reservation.getCategory() == null) {
                    //check if car is the correct Model
                    if (reservation.getCarModel() == car.getCarModel() || reservation.getCarModel() == null) {

                        List<Reservation> existingFutureReservations = car.getReservations();

                        boolean noConflict = true;
                        boolean noMoreCarsChosen = false;

                        while (noConflict == true && noMoreCarsChosen == false) {
                            //check the most broad range for conflict +2 -2 hrs
                            for (LocalDateTime day : allDays) {

                                for (Reservation futureReservation : existingFutureReservations) {
                                    //check that is just nice that means to update best car it must be at the same outlet for pick up 
                                    //focus on pick up onlyyy

                                    LocalDateTime pickUpDateTimeWithDispatch = reservation.getPickUpDateTime().plusHours(-2);
                                    LocalDateTime dropOffDateTimeWithDispatch = reservation.getDropOffDateTime().plusHours(2);

                                    //i reached the last day with no conflict at all on the dates
                                    if (day.toLocalDate().equals(reservation.getDropOffDateTime().toLocalDate())) {
                                        //check if at the same outlet and if a dispatch is still needed
                                        if (reservation.getPickUpOutlet().equals(car.getOutlet()) && needDispatch == true) {
                                            bestCarId = car.getCarId();
                                            needDispatch = false;
                                            //breaks the loop
                                            noMoreCarsChosen = true;
                                        } else if (needDispatch && bestCarId == 0) {
                                            bestCarId = car.getCarId();
                                        }
                                        //account for edge cases
                                        //either or of the days equal to each other but must be first or last ONLY
                                        //first day is same but have enough time to dispatch
                                    } else if (reservation.getPickUpDateTime().toLocalDate().equals(futureReservation.getDropOffDateTime().toLocalDate())
                                            && reservation.getPickUpDateTime().isAfter(futureReservation.getDropOffDateTime())) {
                                        //need to account for first day looks good but what is subsequent days cmi.
                                        if (reservation.getPickUpOutlet().equals(car.getOutlet()) && needDispatch == true) {
                                            //if im only checking the first day i can chill first no need to do anything so j update the dispatch status
                                            needDispatch = false;
                                            //supposed to still go thru all the days
                                        } else if (futureReservation.getDropOffDateTime().isBefore(pickUpDateTimeWithDispatch) || futureReservation.getDropOffDateTime().equals(pickUpDateTimeWithDispatch)) {
                                            needDispatch = true;
                                        }
                                    } else if (reservation.getDropOffDateTime().toLocalDate().equals(futureReservation.getPickUpDateTime().toLocalDate())
                                            && reservation.getDropOffDateTime().isBefore(futureReservation.getPickUpDateTime())) {
                                        //check until last day then got issue.... but possible cause same outlet or enough time
                                        if (reservation.getPickUpOutlet().equals(car.getOutlet()) && needDispatch == true) {
                                            bestCarId = car.getCarId();
                                            needDispatch = false;
                                            //breaks the loop cause last day to check
                                            noMoreCarsChosen = true;
                                        } else if ((futureReservation.getPickUpDateTime().isAfter(dropOffDateTimeWithDispatch) || futureReservation.getPickUpDateTime().equals(dropOffDateTimeWithDispatch))
                                                && bestCarId == 0) {
                                            bestCarId = car.getCarId();
                                            noMoreCarsChosen = true;
                                        }
                                    } else {
                                        noConflict = false;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
