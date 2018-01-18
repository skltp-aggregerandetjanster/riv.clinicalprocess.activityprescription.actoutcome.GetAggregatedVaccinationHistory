/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.util.ThreadSafeSimpleDateFormat;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryImpl implements RequestListFactory {

    private static final Logger log = LoggerFactory.getLogger(RequestListFactoryImpl.class);
    private static final ThreadSafeSimpleDateFormat df = new ThreadSafeSimpleDateFormat("YYYYMMDDhhmmss");

    /**
     * Filtrera svarsposter från i EI (ei-engagement) baserat parametrar i GetVaccinationHistory requestet (req). 
     * 
     * Följande villkor måste vara sanna för att en svarspost från EI skall tas med i svaret:
     *
     * Svarsposter från EI som passerat filtreringen grupperas på fältet sourceSystem 
     * samt postens fält logicalAddress (= PDL-enhet) samlas i listan careUnitId per varje sourceSystem
     *
     * Ett anrop görs per funnet sourceSystem med följande värden i anropet:
     *
     * 1. logicalAddress  = sourceSystem (systemadressering) 
     * 2. subjectOfCareId = original-request.subjectOfCareId
     */
    public List<Object[]> createRequestList(QueryObject qo, FindContentResponseType src) {
        final GetVaccinationHistoryType originalRequest = (GetVaccinationHistoryType) qo.getExtraArg();
        final String reqCareUnit = originalRequest.getSourceSystemHSAid();

        FindContentResponseType eiResp = (FindContentResponseType) src;
        List<EngagementType> inEngagements = eiResp.getEngagement();

        log.info("Got {} hits in the engagement index", inEngagements.size());

        Map<String, List<String>> sourceSystem_pdlUnitList_map = new HashMap<String, List<String>>();
        
        for (EngagementType inEng : inEngagements) {
            // discard engagements that are not the same as the request HSAid (if specified)
            if (isPartOf(reqCareUnit, inEng.getLogicalAddress())) {
                log.debug("Added source system: {} for PDL unit: {}", inEng.getSourceSystem(), inEng.getLogicalAddress());
                addPdlUnitToSourceSystem(sourceSystem_pdlUnitList_map, inEng.getSourceSystem(), inEng.getLogicalAddress());
            }
        }

        // Prepare the result of the transformation as a list of request-payloads,
        // one payload for each unique logical-address (e.g. source system since we are using system addressing),
        // each payload built up as an object-array according to the JAX-WS signature for the method in the service interface
        List<Object[]> reqList = new ArrayList<Object[]>();
        for (Entry<String, List<String>> entry : sourceSystem_pdlUnitList_map.entrySet()) {
            final String sourceSystem = entry.getKey();
            final GetVaccinationHistoryType request = originalRequest;
            log.info("Calling source system using logical address {} for subject of care {}", sourceSystem, originalRequest.getPatientId().getId());
            Object[] reqArr = new Object[] { sourceSystem, request };
            reqList.add(reqArr);
        }
        log.debug("Transformed payload: {}", reqList);
        return reqList;
    }

    boolean isPartOf(final String careUnitId, final String careUnit) {
        log.debug("Check careunit {} equals expected {}", careUnitId, careUnit);
        if (StringUtils.isBlank(careUnitId))
            return true;
        return careUnitId.equals(careUnit);
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
        log.debug("Is {} between {} and ", new Object[] { tsStr, from, to });
        try {
            Date ts = df.parse(tsStr);
            if (from != null && from.after(ts))
                return false;
            if (to != null && to.before(ts))
                return false;
            return true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isPartOf(List<String> careUnitIdList, String careUnit) {
        log.debug("Check presence of {} in {}", careUnit, careUnitIdList);
        if (careUnitIdList == null || careUnitIdList.size() == 0)
            return true;
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
