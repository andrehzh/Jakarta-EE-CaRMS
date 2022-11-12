/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author andre
 */
public class Main {

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB
    private static CreditCardSessionBeanRemote creditCardSessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carSessionBeanRemote, partnerSessionBeanRemote, creditCardSessionBeanRemote, customerSessionBeanRemote);
        mainApp.runApp(); 
        
    }
    
}
