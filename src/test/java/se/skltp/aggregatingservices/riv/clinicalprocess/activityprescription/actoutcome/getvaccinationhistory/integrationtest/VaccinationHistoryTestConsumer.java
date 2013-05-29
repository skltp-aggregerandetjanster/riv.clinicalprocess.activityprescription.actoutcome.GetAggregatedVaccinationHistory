package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v1.rivtabp21.GetVaccinationHistoryResponderInterface;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.PatientIdType;
import se.skltp.aggregatingservices.VaccinationHistoryMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

public class VaccinationHistoryTestConsumer extends AbstractTestConsumer<GetVaccinationHistoryResponderInterface>{

    private static final Logger log = LoggerFactory.getLogger(VaccinationHistoryTestConsumer.class);

    public static void main(String[] args) {
        log.info("URL: " + VaccinationHistoryMuleServer.getAddress("SERVICE_INBOUND_URL"));
        String serviceAddress = VaccinationHistoryMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        VaccinationHistoryTestConsumer consumer = new VaccinationHistoryTestConsumer(serviceAddress, SAMPLE_ORIGINAL_CONSUMER_HSAID);
        Holder<GetVaccinationHistoryResponseType> responseHolder = new Holder<GetVaccinationHistoryResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        long now = System.currentTimeMillis();
        consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);
        log.info("Returned #vaccination record= " + responseHolder.value.getVaccinationMedicalRecord().size() + " in " + (System.currentTimeMillis() - now) + " ms.");	
    }

    public VaccinationHistoryTestConsumer(String serviceAddress, String originalConsumerHsaId) {
        // Setup a web service proxy for communication using HTTPS with Mutual Authentication
        super(GetVaccinationHistoryResponderInterface.class, serviceAddress, originalConsumerHsaId); 
    }

    public void callService(String logicalAddress, String id, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetVaccinationHistoryResponseType> responseHolder) {
        log.debug("Calling GetAggregatedVaccinationHistory-soap-service with id = {}", id);

        GetVaccinationHistoryType request = new GetVaccinationHistoryType();
        PatientIdType patientId = new PatientIdType();
        patientId.setId(id);
        request.setPatientId(patientId);

        GetVaccinationHistoryResponseType response = _service.getVaccinationHistory(logicalAddress, request);
        responseHolder.value = response;

        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}