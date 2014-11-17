package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Node;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryImpl implements QueryObjectFactory {

	private static final Logger log = LoggerFactory.getLogger(QueryObjectFactoryImpl.class);
	private static final JaxbUtil ju = new JaxbUtil(GetVaccinationHistoryType.class);

	private String eiServiceDomain;
	public void setEiServiceDomain(String eiServiceDomain) {
		this.eiServiceDomain = eiServiceDomain;
	}

	private String eiCategorization;
	public void setEiCategorization(String eiCategorization) {
		this.eiCategorization = eiCategorization;
	}

	/**
	 * Transformerar GetRequestActivities request till EI FindContent request enligt:
	 *
	 * 1. subjectOfCareId --> patientId
	 * 2. "riv:clinicalprocess:activityprescription:actoutcome" --> serviceDomain
	 * 3. "caa-gvh" --> categorization
	 * 4. SourceSystemHSAId --> SourceSystem
	 */
	public QueryObject createQueryObject(Node node) {
		final GetVaccinationHistoryType request = (GetVaccinationHistoryType) ju.unmarshal(node);
		if(log.isDebugEnabled() && request.getPatientId() != null) {
			log.debug("Transformed payload for pid: {}", request.getPatientId().getId());
		}
		final FindContentType fc = new FindContentType();
		if(request.getPatientId() != null) {
			fc.setRegisteredResidentIdentification(request.getPatientId().getId());
		}
		fc.setCategorization(eiCategorization);
		fc.setServiceDomain(eiServiceDomain);
		fc.setSourceSystem(request.getSourceSystemHSAid());

		QueryObject qo = new QueryObject(fc, request);
		return qo;
	}
}
