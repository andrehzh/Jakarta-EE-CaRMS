/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author andre
 */
@Remote
public interface RentalRateSessionBeanRemote {

    public Long createNewRentalRate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException;

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

    public List<RentalRate> retrieveRentalRatesByDate(LocalDateTime date) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException;

    public RentalRate retrieveRentalRateByRentalRateName(String rentalRateName) throws RentalRateNotFoundException;
}
