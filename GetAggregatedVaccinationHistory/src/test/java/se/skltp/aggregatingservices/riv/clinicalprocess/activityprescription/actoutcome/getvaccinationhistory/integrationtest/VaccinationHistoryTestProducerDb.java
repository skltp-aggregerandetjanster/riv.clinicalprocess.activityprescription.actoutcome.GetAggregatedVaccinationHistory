package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.AdministrationRecordType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.HealthcareProfessionalType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.IIType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.LegalAuthenticatorType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.OrgUnitType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientSummaryHeaderType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PersonIdType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.RegistrationRecordType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordBodyType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordType;
import se.skltp.agp.test.producer.TestProducerDb;

public class VaccinationHistoryTestProducerDb extends TestProducerDb {

    private static final Logger log = LoggerFactory.getLogger(VaccinationHistoryTestProducerDb.class);

    @Override
    public Object createResponse(Object... responseItems) {
        log.debug("Creates a response with {} items", responseItems);
        GetVaccinationHistoryResponseType response = new GetVaccinationHistoryResponseType();
        for (int i = 0; i < responseItems.length; i++) {
            response.getVaccinationMedicalRecord().add((VaccinationMedicalRecordType)responseItems[i]);
        }
        return response;
    }

    public static final String TEST_REASON_DEFAULT = "default reason";
    public static final String TEST_REASON_UPDATED = "updated reason";

    @Override
    public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {

        log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
                new Object[] {logicalAddress, registeredResidentId, businessObjectId});

        VaccinationMedicalRecordType response = new VaccinationMedicalRecordType();
        PatientSummaryHeaderType header = new PatientSummaryHeaderType();
        PersonIdType patientId = new PersonIdType();
        patientId.setId(registeredResidentId);
        patientId.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientId);
        header.setApprovedForPatient(true);
        LegalAuthenticatorType legalAuthenticator = new LegalAuthenticatorType();
        legalAuthenticator.setSignatureTime("20130707070707");
        header.setLegalAuthenticator(legalAuthenticator);
        header.setSourceSystemHSAId(logicalAddress);
        header.setDocumentId(businessObjectId);
        header.setDocumentTime(time);
        
        // TODO: Set stuff

        VaccinationMedicalRecordBodyType body = new VaccinationMedicalRecordBodyType();
        
        AdministrationRecordType adminRecord = new AdministrationRecordType();
        IIType vaccUniqueRef = new IIType();
        vaccUniqueRef.setExtension("extension");
        vaccUniqueRef.setRoot("root");
        adminRecord.setVaccinationUniqueReference(vaccUniqueRef);
        
        body.getAdministrationRecord().add(adminRecord);
        
        RegistrationRecordType regRecord = new RegistrationRecordType();
        
        body.setRegistrationRecord(regRecord);
        
        HealthcareProfessionalType author = new HealthcareProfessionalType();
        author.setHealthcareProfessionalCareUnitHSAId(logicalAddress); // TODO ???
        OrgUnitType orgUnit = new OrgUnitType();
        if(TestProducerDb.TEST_LOGICAL_ADDRESS_1.equals(logicalAddress)){
            orgUnit.setOrgUnitName("Vårdcentralen Kusten, Kärna"); // TODO ???
        } else if(TestProducerDb.TEST_LOGICAL_ADDRESS_2.equals(logicalAddress)){
        	orgUnit.setOrgUnitName("Vårdcentralen Molnet");
        } else {
        	orgUnit.setOrgUnitName("Vårdcentralen Stacken");
        }
        author.setHealthcareProfessionalOrgUnit(orgUnit);
        header.setAccountableHealthcareProfessional(author);
        response.setVaccinationMedicalRecordHeader(header);
        response.setVaccinationMedicalRecordBody(body);
        return response;
    }
}
