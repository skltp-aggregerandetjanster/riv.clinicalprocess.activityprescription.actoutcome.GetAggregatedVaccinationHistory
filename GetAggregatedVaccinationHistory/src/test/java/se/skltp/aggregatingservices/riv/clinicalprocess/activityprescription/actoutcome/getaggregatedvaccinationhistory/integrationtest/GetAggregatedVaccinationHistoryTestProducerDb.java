package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.integrationtest;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.ThreadSafeSimpleDateFormat;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.ActorType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.CVType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.HealthcareProfessionalType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.LegalAuthenticatorType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.OrgUnitType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.PatientSummaryHeaderType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.PersonIdType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.RegistrationRecordType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordBodyType;
import riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedVaccinationHistoryTestProducerDb extends TestProducerDb {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedVaccinationHistoryTestProducerDb.class);
    private static final String TEST_CODE_SYSTEM = "1.2.752.129.2.1.3.1";

    private static final ThreadSafeSimpleDateFormat df = new ThreadSafeSimpleDateFormat("yyyyMMddHHmmss");
    private static final ThreadSafeSimpleDateFormat dateType = new ThreadSafeSimpleDateFormat("yyyyMMdd");

    @Override
    public Object createResponse(Object... responseItems) {
        log.debug("Creates a response with {} items", responseItems);
        final GetVaccinationHistoryResponseType response = new GetVaccinationHistoryResponseType();
        for (int i = 0; i < responseItems.length; i++) {
            response.getVaccinationMedicalRecord().add((VaccinationMedicalRecordType) responseItems[i]);
        }
        return response;
    }

    @Override
    public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
        log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}", 
                   new Object[] {logicalAddress, registeredResidentId, businessObjectId });
        final VaccinationMedicalRecordType response = new VaccinationMedicalRecordType();
        response.setVaccinationMedicalRecordBody(body(registeredResidentId, logicalAddress));
        response.setVaccinationMedicalRecordHeader(header(registeredResidentId, logicalAddress));
        return response;
    }

    protected VaccinationMedicalRecordBodyType body(final String patientId, final String logicalAddress) {
        final VaccinationMedicalRecordBodyType type = new VaccinationMedicalRecordBodyType();
        type.setRegistrationRecord(record());
        return type;
    }

    protected RegistrationRecordType record() {
        final RegistrationRecordType type = new RegistrationRecordType();
        type.setCareGiverContact(actor());
        type.setCareGiverOrg(org());
        type.setDate(dateType.format(new Date()));
        type.setPatientPostalCode("446 32");
        type.setSourceSystemContact(actor());
        type.setSourceSystemName("Take off");
        type.setVaccinationUnstructuredNote("Unstructured note");
        return type;
    }

    protected OrgUnitType org() {
        final OrgUnitType type = new OrgUnitType();
        type.setOrgUnitAddress("Testvagen 3");
        type.setOrgUnitEmail("test@test.se");
        type.setOrgUnitHSAid("TEST-HSAId-1234");
        type.setOrgUnitLocation("Testby");
        type.setOrgUnitName("Testhuset");
        type.setOrgUnitTelecom("00-0000000");
        return type;
    }

    protected ActorType actor() {
        final ActorType type = new ActorType();
        type.setHsaid("TEST-HSAId-1234");
        type.setPersonAddress("Testvagen 2");
        type.setPersonEmail("test@test.se");
        type.setPersonName("Test Testsson");
        type.setPersonTelecom("00-0000000");
        return type;
    }

    protected CVType cvType() {
        final CVType type = new CVType();
        type.setCode("0");
        type.setCodeSystem(TEST_CODE_SYSTEM);
        return type;
    }

    protected PatientSummaryHeaderType header(final String patientId, final String logicalAddress) {
        final PatientSummaryHeaderType type = new PatientSummaryHeaderType();
        final String careContact = "TEST-Contact-1234";
        type.setAccountableHealthcareProfessional(hp(careContact));
        type.setApprovedForPatient(true);
        type.setCareContactId(careContact);
        type.setDocumentId(UUID.randomUUID().toString());
        type.setDocumentTime(df.format(new Date()));
        type.setDocumentTitle("TEST-Document-Title");
        type.setLegalAuthenticator(legal());
        type.setNullified(false);
        type.setNullifiedReason(false);
        type.setSourceSystemHSAId(logicalAddress);
        type.setPatientId(person(patientId));
        return type;
    }

    protected LegalAuthenticatorType legal() {
        final LegalAuthenticatorType type = new LegalAuthenticatorType();
        type.setLegalAuthenticatorHSAid("TEST-HSAId");
        type.setLegalAuthenticatorName("Test Testsson");
        type.setSignatureTime(df.format(new Date()));
        return type;
    }

    protected HealthcareProfessionalType hp(final String careContact) {
        final HealthcareProfessionalType type = new HealthcareProfessionalType();
        type.setAuthorTime(df.format(new Date()));
        return type;
    }

    protected PersonIdType person(final String patientId) {
        final PersonIdType type = new PersonIdType();
        type.setId(patientId);
        type.setType(TEST_CODE_SYSTEM);
        return type;
    }

    protected static String dateOfBirth(final String patientId) {
        try {
            return "19" + patientId.substring(0, 6);
        } catch (Exception err) {
            return null;
        }
    }
}