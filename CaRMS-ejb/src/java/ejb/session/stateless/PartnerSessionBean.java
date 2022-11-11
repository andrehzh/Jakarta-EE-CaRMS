/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PartnerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewPartner(Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(newPartner);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newPartner);
                em.flush();

                return newPartner.getPartnerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PartnerNameExistException();
                    }
                    throw new UnknownPersistenceException(ex.getMessage());

                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");

        return query.getResultList();
    }

    @Override
    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }

    @Override
    public void updatePartner(Partner partner) throws PartnerNotFoundException, UpdatePartnerException, InputDataValidationException {
        if (partner != null && partner.getPartnerId() != null) {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(partner);

            if (constraintViolations.isEmpty()) {
                Partner partnerToUpdate = retrievePartnerByPartnerId(partner.getPartnerId());

                if (partnerToUpdate.getPartnerName().equals(partner.getPartnerName())) {
                    partnerToUpdate.setCustomers(partner.getCustomers());
                    partnerToUpdate.setReservations(partner.getReservations());
                    // able to update everything except partnerName
                } else {
                    throw new UpdatePartnerException("UpdatePartnerException");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new PartnerNotFoundException("PartnerNotFoundException");
        }
    }

    @Override
    public void deletePartner(Long partnerId) throws PartnerNotFoundException, DeletePartnerException {
        Partner partnerToRemove = retrievePartnerByPartnerId(partnerId);
        //remove partner considerations idk need see logical data model.
        if (partnerToRemove.getCustomers().isEmpty()) {
            em.remove(partnerToRemove);
        } else {
            throw new DeletePartnerException("Partner ID " + partnerId + " is associated with existing reservations and cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
