package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.AuthorType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientIdType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientSummaryHeaderType;
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
        PatientIdType patientId = new PatientIdType();
        patientId.setId(registeredResidentId);
        patientId.setType("1.2.752.129.2.1.3.1");
        header.setPatientId(patientId);
        header.setApprovedForPatient(true);
        header.setSourceSystemHSAid(logicalAddress);
        header.setDocumentId(businessObjectId);
        header.setDocumentTime(time);
        
        // TODO: Set stuff

        VaccinationMedicalRecordBodyType body = new VaccinationMedicalRecordBodyType();
        
        RegistrationRecordType regRecord = new RegistrationRecordType();
        // TODO: Set stuff
        
        body.setRegistrationRecord(regRecord);
        
        AuthorType author = new AuthorType();
        author.setCareUnitHSAid(logicalAddress); // TODO ???
        if(TestProducerDb.TEST_LOGICAL_ADDRESS_1.equals(logicalAddress)){
            author.setAuthorOrgUnitName("Vårdcentralen Kusten, Kärna"); // TODO ???
        } else if(TestProducerDb.TEST_LOGICAL_ADDRESS_2.equals(logicalAddress)){
            author.setAuthorOrgUnitName("Vårdcentralen Molnet");
        } else {
            author.setAuthorOrgUnitName("Vårdcentralen Stacken");
        }
        header.setAuthor(author);
        response.setVaccinationMedicalRecordHeader(header);
        response.setVaccinationMedicalRecordBody(body);
        return response;
    }
}
