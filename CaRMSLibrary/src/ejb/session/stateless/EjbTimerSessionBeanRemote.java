/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.time.LocalDateTime;
import javax.ejb.Remote;
import util.exception.NoAvailableCarException;

/**
 *
 * @author andre
 */
@Remote
public interface EjbTimerSessionBeanRemote {
    public void allocateCarsToCurrentDayReservations() throws NoAvailableCarException;

    public void allocateCarsToCurrentDayReservationsInput(LocalDateTime testDate) throws NoAvailableCarException;
    
}
