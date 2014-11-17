package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.integrationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.VaccinationMedicalRecordType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedVaccinationHistoryTestProducerDb extends TestProducerDb {

	private static final Logger log = LoggerFactory.getLogger(GetAggregatedVaccinationHistoryTestProducerDb.class);

	@Override
	public Object createResponse(Object... responseItems) {
		log.debug("Creates a response with {} items", responseItems);
		final GetVaccinationHistoryResponseType response = new GetVaccinationHistoryResponseType();
		for (int i = 0; i < responseItems.length; i++) {
			response.getVaccinationMedicalRecord().add((VaccinationMedicalRecordType)responseItems[i]);
		}
		return response;
	}
	
	@Override
	public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
		
		if (log.isDebugEnabled()) {
			log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
				new Object[] {logicalAddress, registeredResidentId, businessObjectId});
		}

		final VaccinationMedicalRecordType response = new VaccinationMedicalRecordType();

		return response;
	}
}