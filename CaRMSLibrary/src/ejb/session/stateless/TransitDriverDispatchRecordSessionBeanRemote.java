/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Remote
public interface TransitDriverDispatchRecordSessionBeanRemote {

    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) throws UnknownPersistenceException, InputDataValidationException;

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long id) throws TransitDriverDispatchRecordNotFoundException;

    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverDispatchRecords();

    public void updateTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) throws TransitDriverDispatchRecordNotFoundException, InputDataValidationException;

    public void deleteTransitDriverDispatchRecord(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;
}
