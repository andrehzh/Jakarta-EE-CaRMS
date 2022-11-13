/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EjbTimerSessionBeanRemote;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.NoAvailableCarException;

/**
 *
 * @author andre
 */
public class EJBTimerModule {

    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;

    public EJBTimerModule() {
    }

    public EJBTimerModule(EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote) {
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Car Allocation Module ***\n");
            System.out.println("1: Allocate Cars to Current Day Reservations & Generate Transit Driver Dispatch Records");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doAllocateCars();
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

    private void doAllocateCars() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Allocate Cars to Current Day Reservations & Generate Transit Driver Dispatch Records ***");
        System.out.println("Please Enter The Date of Allocation!");

        int year = 0;
        int month = 0;
        int day = 0;

        System.out.print("Enter Year(YYYY)> ");
        year = scanner.nextInt();
        System.out.print("Enter Month(MM)> ");
        month = scanner.nextInt();
        System.out.print("Enter Day(DD)> ");
        day = scanner.nextInt();
        
        LocalDateTime allocationDate = LocalDateTime.of(year, month, day, 2, 0);
        System.out.println("You are doing car allocation for: " + allocationDate.toString());
        
        try {
            ejbTimerSessionBeanRemote.allocateCarsToCurrentDayReservationsInput(allocationDate);
            System.out.println("*** Please check System Database for Car Allocation & Transit Driver Dispatch Record data! ***\n");
        } catch (NoAvailableCarException ex) {
            ex.printStackTrace();
        }
    }
}
