/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeletePartnerException;
import util.exception.InputDataValidationException;
import util.exception.PartnerNameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePartnerException;

/**
 *
 * @author andre
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Partner> retrieveAllPartners();

    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException;

    public void updatePartner(Partner partner) throws PartnerNotFoundException, UpdatePartnerException, InputDataValidationException;

    public void deletePartner(Long partnerId) throws PartnerNotFoundException, DeletePartnerException;
    
}
