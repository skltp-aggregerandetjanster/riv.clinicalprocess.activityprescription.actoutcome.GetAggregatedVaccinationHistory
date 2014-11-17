package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v2.rivtabp21.GetVaccinationHistoryResponderInterface;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.GetAggregatedVaccinationHistoryMuleServer;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class GetAggregatedVaccinationHistoryTestConsumer extends AbstractTestConsumer<GetVaccinationHistoryResponderInterface> {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedVaccinationHistoryTestConsumer.class);

	public static void main(String[] args) {
		String serviceAddress = GetAggregatedVaccinationHistoryMuleServer.getAddress("SERVICE_INBOUND_URL");
		String personnummer = TEST_RR_ID_ONE_HIT;

		GetAggregatedVaccinationHistoryTestConsumer consumer = 
				new GetAggregatedVaccinationHistoryTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID);
		//Holder<GetRequestActivitiesResponseType> responseHolder = new Holder<GetRequestActivitiesResponseType>();
		Holder<GetVaccinationHistoryResponseType> responseHolder = new Holder<GetVaccinationHistoryResponseType>();
		Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

		//consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);


		//log.info("Returned #timeslots = " + responseHolder.value.getRequestActivity().size());

	}
	public GetAggregatedVaccinationHistoryTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId) {
	    
		// Setup a web service proxy for communication using HTTPS with Mutual Authentication
		//super(GetRequestActivitiesResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
		super(GetVaccinationHistoryResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId);
	}

	
	public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetVaccinationHistoryType> responseHolder) {

		log.debug("Calling GetRequestActivities-soap-service with Registered Resident Id = {}", registeredResidentId);
		
		GetVaccinationHistoryType request = new GetVaccinationHistoryType();


		//request.setSubjectOfCareId(registeredResidentId);
		//request.setPatientId(value);

		//GetRequestActivitiesResponseType response = _service.getRequestActivities(logicalAddress, request);
		//responseHolder.value = response;
		
		processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
	}
}