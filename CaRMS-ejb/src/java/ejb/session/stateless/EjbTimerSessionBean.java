/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarModel;
import entity.Category;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableCarException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author andre
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    

    @Schedule(hour = "2", info = "allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations() throws NoAvailableCarException {
        //go through all reservations
        //sort those which are happening on the day of execution
        //iterate through the car list once only once then if take the first yes
        //if there are no Y take the first YN

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("********** EjbTimerSessionBean.allocateCarsToCurrentDayReservations(): Timeout at " + timeStamp);

        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservations();
        List<Car> cars = carSessionBeanLocal.retrieveAllCars();

        //get a list of all the requests
        //go through the list to see which requests we have to process
        //1. it is ongoing car must be unavailable -> ongoing means that the current day is within their range of days
        //1b. have to update cars which completed yesterday?
        //2. it is within TODAY 1.59am - next day 2.00 am must allocate the best AVAILABLE car -> today is equal to reservation's start day
        //3. after allocating the best car to reservation
        //4. update car to unavailable
        //5. done
        //all requests at the moment
        List<Reservation> allReservations = reservationSessionBeanLocal.retrieveAllReservations();

        List<Reservation> todayReservations = new ArrayList<>(); //no need to update ongoing reservations cause it is done by use case 26 & 27

        for (Reservation reservation : allReservations) {
            LocalDateTime startDate = reservation.getPickUpDateTime();
            LocalDateTime endDate = reservation.getDropOffDateTime();
            if (startDate.isAfter(currentDateTime.minusMinutes(1)) && startDate.isBefore(currentDateTime.plusDays(1))) {
                todayReservations.add(reservation);
            }
        }
        System.out.println(todayReservations.toString());

        List<Car> allCars = carSessionBeanLocal.retrieveAllCars();

        List<Car> availableCars = new ArrayList<>();

        for (Car car : allCars) {
            if (car.getCarStatus() == CarStatusEnum.AVAILABLE) {
                availableCars.add(car);
            }
        }
        //to allocate best car
        //iterate through list of car till get a car which is best then break;
        //remove from the list
        //if iterate through list of car still no good car then YN car allocated;
        try {
            for (Reservation reservation : todayReservations) {
                Category categoryRequirement = reservation.getCategory();
                CarModel cmRequirement = reservation.getCarModel();
                Outlet outletPickUp = reservation.getPickUpOutlet();
                Outlet outletDropOff = reservation.getDropOffOutlet();
                LocalDateTime pickUpDate = reservation.getPickUpDateTime();
                LocalDateTime dropOffDate = reservation.getDropOffDateTime();

                List<Car> allocatedBestCars = new ArrayList<>(); //just keep adding later trim to size (1)
                List<Car> allocatedCarsNeedDispatch = new ArrayList<>(); //only access if allocatedCars is empty.
                List<Car> allocatedBestCar = new ArrayList<>();// this is the allocated Car
                List<Car> allocatedCarNeedDispatch = new ArrayList<>();
                Car allocatedCar = null;
                System.out.println(allocatedBestCars.toString());
                //just completely iterate through the list 
                for (Car car : availableCars) {
                    if (categoryRequirement == null || categoryRequirement.equals(car.getCarModel().getCategory())) {
                        if (cmRequirement == null || cmRequirement.equals(car.getCarModel())) {
                            if (car.getOutlet().equals(outletPickUp)) {
                                //by default it is available because no reservation assigned as of yet.
                                allocatedBestCars.add(car);
                            } else {
                                allocatedCarsNeedDispatch.add(car);
                            }
                        }
                    }
                }
                System.out.println(allocatedBestCars.toString());
                System.out.println(allocatedCarsNeedDispatch.toString());
                //after looping thorough i check if there is any cars in allocated Best Cars
                if (!allocatedBestCars.isEmpty()) {
                    allocatedBestCar.add(allocatedBestCars.get(0));
                    System.out.println(allocatedBestCar.toString());

                    //time for allocation j change the status to reserved no harm.
                    allocatedCar = allocatedBestCar.get(0);
                    
                    //remove Best car from available car list
                    availableCars.removeAll(allocatedBestCar);
                    System.out.println(availableCars.toString());
                } else if (!allocatedCarsNeedDispatch.isEmpty()) {
                    allocatedCarNeedDispatch.add(allocatedCarsNeedDispatch.get(0));
                    System.out.println(allocatedCarNeedDispatch.toString());
                    
                    allocatedCar = allocatedCarNeedDispatch.get(0);
                    
                    availableCars.removeAll(allocatedCarNeedDispatch);
                    System.out.println(availableCars.toString());
                    
                    //have to create a new transit driver dispatch record
                    TransitDriverDispatchRecord newTransitDriverDispatchRecord = new TransitDriverDispatchRecord();
                    newTransitDriverDispatchRecord.setIsCompleted(false);
                    //already minus 2 hours for the dispatch
                    newTransitDriverDispatchRecord.setDispatchDate(reservation.getPickUpDateTime().minusHours(2));
                    newTransitDriverDispatchRecord.setPickUpOutlet(allocatedCar.getOutlet());
                    newTransitDriverDispatchRecord.setDropOffOutlet(reservation.getPickUpOutlet());
                    
                    Long dispatchId = transitDriverDispatchRecordSessionBeanLocal.createNewTransitDriverDispatchRecord(newTransitDriverDispatchRecord);
                    outletSessionBeanLocal.retrieveOutletById(allocatedCar.getOutlet().getOutletId()).getTransitDriverDispatchRecords().add(transitDriverDispatchRecordSessionBeanLocal.retrieveTransitDriverDispatchRecordById(dispatchId));
                   
                } else {
                    throw new NoAvailableCarException("WE ARE OVER BOOKED SYSTEM IS NOT DOING WELL :(");
                }
                
                //update the car
                carSessionBeanLocal.retrieveCarById(allocatedCar.getCarId()).setReservation(reservationSessionBeanLocal.retrieveReservationByReservationId(reservation.getReservationId()));
                carSessionBeanLocal.retrieveCarById(allocatedCar.getCarId()).setCarStatus(CarStatusEnum.RESERVED);
                
                //update the reservation
                reservationSessionBeanLocal.retrieveReservationByReservationId(reservation.getReservationId()).setCar(carSessionBeanLocal.retrieveCarById(allocatedCar.getCarId()));

            }
        } catch (TransitDriverDispatchRecordNotFoundException | OutletNotFoundException | CarNotFoundException | NoAvailableCarException | ReservationNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }

        //end
    }

    //need something to constantly update the status of cars everyday.
}
