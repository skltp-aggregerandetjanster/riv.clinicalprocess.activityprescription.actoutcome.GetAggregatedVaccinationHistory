package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.ThreadSafeSimpleDateFormat;

import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryImpl implements RequestListFactory {

	private static final Logger log = LoggerFactory.getLogger(RequestListFactoryImpl.class);
	private static final ThreadSafeSimpleDateFormat df = new ThreadSafeSimpleDateFormat("YYYYMMDDhhmmss");

	/**
	 * Filtrera svarsposter fr??n i EI (ei-engagement) baserat parametrar i GetRequestActivities requestet (req).
	 * F??ljande villkor m??ste vara sanna f??r att en svarspost fr??n EI skall tas med i svaret:
	 *
	 * 1. req.fromDate <= ei-engagement.mostRecentContent <= req.toDate
	 * 2. req.careUnitId.size == 0 or req.careUnitId.contains(ei-engagement.logicalAddress)
	 *
	 * Svarsposter fr??n EI som passerat filtreringen grupperas p?? f??ltet sourceSystem samt postens f??lt logicalAddress (= PDL-enhet) samlas i listan careUnitId per varje sourceSystem
	 *
	 * Ett anrop g??rs per funnet sourceSystem med f??ljande v??rden i anropet:
	 *
	 * 1. logicalAddress = sourceSystem (systemadressering)
	 * 2. subjectOfCareId = orginal-request.subjectOfCareId
	 * 3. careUnitId = listan av PDL-enheter som returnerats fr??n EI f??r aktuellt source system)
	 * 4. fromDate = orginal-request.fromDate
	 * 5. toDate = orginal-request.toDate
	 */
	public List<Object[]> createRequestList(QueryObject qo, FindContentResponseType src) {
		return null;
		/**
		GetRequestActivitiesType originalRequest = (GetRequestActivitiesType)qo.getExtraArg();

		Date reqFrom = null;
		Date reqTo   = null;
		List<String> reqCareUnitList = null;

		reqFrom = parseTs(originalRequest.getFromDate());
		reqTo   = parseTs(originalRequest.getToDate());
		reqCareUnitList = originalRequest.getCareUnitId();


		FindContentResponseType eiResp = (FindContentResponseType)src;
		List<EngagementType> inEngagements = eiResp.getEngagement();

		log.info("Got {} hits in the engagement index", inEngagements.size());

		Map<String, List<String>> sourceSystem_pdlUnitList_map = new HashMap<String, List<String>>();

		for (EngagementType inEng : inEngagements) {

			// Filter

			if (isBetween(reqFrom, reqTo, inEng.getMostRecentContent()) &&
				isPartOf(reqCareUnitList, inEng.getLogicalAddress())) {

				// Add pdlUnit to source system
				log.debug("Add SS: {} for PDL unit: {}", inEng.getSourceSystem(), inEng.getLogicalAddress());
				addPdlUnitToSourceSystem(sourceSystem_pdlUnitList_map, inEng.getSourceSystem(), inEng.getLogicalAddress());
			}
		}

		// Prepare the result of the transformation as a list of request-payloads,
		// one payload for each unique logical-address (e.g. source system since we are using systemaddressing),
		// each payload built up as an object-array according to the JAX-WS signature for the method in the service interface
		List<Object[]> reqList = new ArrayList<Object[]>();

		for (Entry<String, List<String>> entry : sourceSystem_pdlUnitList_map.entrySet()) {

			String sourceSystem = entry.getKey();
            GetRequestActivitiesType request = new GetRequestActivitiesType();


			if (log.isInfoEnabled()) log.info("Calling source system using logical address {} for subject of care id {}", sourceSystem, originalRequest.getSubjectOfCareId());

 			List<String> careUnitList = entry.getValue();

			request.setSubjectOfCareId(originalRequest.getSubjectOfCareId());
			request.getCareUnitId().addAll(careUnitList);
			request.setFromDate(originalRequest.getFromDate());
			request.setToDate(originalRequest.getToDate());

			Object[] reqArr = new Object[] {sourceSystem, request};

			reqList.add(reqArr);
		}

		log.debug("Transformed payload: {}", reqList);

		return reqList;
		**/
	}

	Date parseTs(String ts) {
		try {
			if (ts == null || ts.length() == 0) {
				return null;
			} else {
				return df.parse(ts);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	boolean isBetween(Date from, Date to, String tsStr) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Is {} between {} and ", new Object[] {tsStr, from, to});
			}

			Date ts = df.parse(tsStr);
			if (from != null && from.after(ts)) return false;
			if (to != null && to.before(ts)) return false;
			return true;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	boolean isPartOf(List<String> careUnitIdList, String careUnit) {

		log.debug("Check presence of {} in {}", careUnit, careUnitIdList);

		if (careUnitIdList == null || careUnitIdList.size() == 0) return true;

		return careUnitIdList.contains(careUnit);
	}

	void addPdlUnitToSourceSystem(Map<String, List<String>> sourceSystem_pdlUnitList_map, String sourceSystem, String pdlUnitId) {
		List<String> careUnitList = sourceSystem_pdlUnitList_map.get(sourceSystem);
		if (careUnitList == null) {
			careUnitList = new ArrayList<String>();
			sourceSystem_pdlUnitList_map.put(sourceSystem, careUnitList);
		}
		careUnitList.add(pdlUnitId);
	}
}
