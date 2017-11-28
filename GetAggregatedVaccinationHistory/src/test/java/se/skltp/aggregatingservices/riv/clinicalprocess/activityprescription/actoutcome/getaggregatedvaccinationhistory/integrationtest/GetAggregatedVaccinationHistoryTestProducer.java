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
package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.integrationtest;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v2.rivtabp21.GetVaccinationHistoryResponderInterface;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName     = "GetVaccinationHistoryResponderService", 
            portName        = "GetVaccinationHistoryResponderPort", 
            targetNamespace = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistory:2:rivtabp21", 
            name            = "GetVaccinationHistoryInteraction")
public class GetAggregatedVaccinationHistoryTestProducer implements GetVaccinationHistoryResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedVaccinationHistoryTestProducer.class);

    private TestProducerDb testDb;
    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    @Override
    public GetVaccinationHistoryResponseType getVaccinationHistory(String logicalAddress, GetVaccinationHistoryType request) {
        log.info("#### Virtual service for GetVaccinationHistory call the source system with logical address: {} and patinentId: {}", 
                  logicalAddress, request.getPatientId().getId());
        final GetVaccinationHistoryResponseType response 
              = (GetVaccinationHistoryResponseType) testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
            return new GetVaccinationHistoryResponseType();
        }
        log.info("### Virtual service got {} in the reply from the source system with logical address: {} and patientId: {}", 
                  new Object[] {response.getVaccinationMedicalRecord().size(), logicalAddress, request.getPatientId().getId() });
        return response;
    }
}