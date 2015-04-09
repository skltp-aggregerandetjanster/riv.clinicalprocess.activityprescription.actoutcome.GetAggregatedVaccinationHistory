package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.ObjectFactory;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

    private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetVaccinationHistoryResponseType.class, ProcessingStatusType.class);
    private static final ObjectFactory OF = new ObjectFactory();

    @Override
    public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
        final GetVaccinationHistoryResponseType aggregatedResponse = new GetVaccinationHistoryResponseType();
        for (Object object : aggregatedResponseList) {
            final GetVaccinationHistoryResponseType response = (GetVaccinationHistoryResponseType) object;
            aggregatedResponse.getVaccinationMedicalRecord().addAll(response.getVaccinationMedicalRecord());
        }
        if (log.isInfoEnabled()) {
            String subjectOfCareId = queryObject.getFindContent().getRegisteredResidentIdentification();
            log.info("Returning {} aggregated alert informations for subject of care id {}", 
                      aggregatedResponse.getVaccinationMedicalRecord().size(), subjectOfCareId);
        }

        // Since the class GetAlertInformationResponseType doesn't have an @XmlRootElement annotation
        // we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetVaccinationHistoryResponse(aggregatedResponse));
    }
}
