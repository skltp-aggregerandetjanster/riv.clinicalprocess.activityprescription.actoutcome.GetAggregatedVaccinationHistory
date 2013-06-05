package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.integrationtest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v1.rivtabp21.GetVaccinationHistoryResponderInterface;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetVaccinationHistoryResponderService", portName = "GetVaccinationHistoryResponderPort", targetNamespace = "urn:riv:clinicalprocess.activityprescription.actoutcome:GetVaccinationHistoryResponder:1:rivtabp21", name = "GetVaccinationHistoryInteraction")
public class VaccinationHistoryTestProducer implements GetVaccinationHistoryResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(VaccinationHistoryTestProducer.class);

    private TestProducerDb testDb;

    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    @Override
    @WebResult(name = "GetVaccinationHistoryResponse", targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1", partName = "parameters")
    @WebMethod(operationName = "GetVaccinationHistory", action = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:2:GetVaccinationHistory")
    public GetVaccinationHistoryResponseType getVaccinationHistory(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetVaccinationHistory", targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1") GetVaccinationHistoryType request) {
        log.info("### Virtual service for GetVaccinationHistory call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getId());

        GetVaccinationHistoryResponseType response = (GetVaccinationHistoryResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
            // Return an empty response object instead of null if nothing is found
            response = new GetVaccinationHistoryResponseType();
        }

        log.info("### Virtual service got {} documents in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getVaccinationMedicalRecord().size(), logicalAddress, request.getPatientId().getId()});

        // We are done
        return response;
    }


}