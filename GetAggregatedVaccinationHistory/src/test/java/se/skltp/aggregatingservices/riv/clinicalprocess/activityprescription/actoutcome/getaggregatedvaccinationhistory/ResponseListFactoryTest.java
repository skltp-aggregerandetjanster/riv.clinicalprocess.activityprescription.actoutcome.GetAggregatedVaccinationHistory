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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.PatientSummaryHeaderType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.PersonIdType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.VaccinationMedicalRecordBodyType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.VaccinationMedicalRecordType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryTest {

    private final static String SUBJECT_OF_CARE = UUID.randomUUID().toString();
    private final static int NUMBER_OF_RESPONSES = 5;

    private final ResponseListFactoryImpl testObject = new ResponseListFactoryImpl();
    private final List<Object> responseList = new ArrayList<Object>();

    private final QueryObject queryObject = Mockito.mock(QueryObject.class);
    private final FindContentType findContentType = Mockito.mock(FindContentType.class);

    @Before
    public void setup() {
        Mockito.when(findContentType.getRegisteredResidentIdentification()).thenReturn(SUBJECT_OF_CARE);
        Mockito.when(queryObject.getFindContent()).thenReturn(findContentType);

        for (int i = 0; i < NUMBER_OF_RESPONSES; i++) {
            final GetVaccinationHistoryResponseType resp = new GetVaccinationHistoryResponseType();
            final VaccinationMedicalRecordType record = new VaccinationMedicalRecordType();
            record.setVaccinationMedicalRecordBody(new VaccinationMedicalRecordBodyType());
            record.setVaccinationMedicalRecordHeader(new PatientSummaryHeaderType());
            record.getVaccinationMedicalRecordHeader().setPatientId(new PersonIdType());
            record.getVaccinationMedicalRecordHeader().getPatientId().setId(SUBJECT_OF_CARE);
            resp.getVaccinationMedicalRecord().add(record);
            responseList.add(resp);
        }
    }

    @Test
    public void testGetXmlFromAggregatedResponse() {
        final JaxbUtil jaxbUtil = new JaxbUtil(GetVaccinationHistoryResponseType.class);

        final String after = testObject.getXmlFromAggregatedResponse(queryObject, responseList);
        final GetVaccinationHistoryResponseType type = (GetVaccinationHistoryResponseType) jaxbUtil.unmarshal(after);

        assertEquals(NUMBER_OF_RESPONSES, type.getVaccinationMedicalRecord().size());
        for (VaccinationMedicalRecordType r : type.getVaccinationMedicalRecord()) {
            assertEquals(SUBJECT_OF_CARE, r.getVaccinationMedicalRecordHeader().getPatientId().getId());
        }
    }
}
