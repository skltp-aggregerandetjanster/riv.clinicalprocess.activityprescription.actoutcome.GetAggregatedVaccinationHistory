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

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v2.rivtabp21.GetVaccinationHistoryResponderInterface;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.PersonIdType;
import se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.GetAggregatedVaccinationHistoryMuleServer;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class GetAggregatedVaccinationHistoryTestConsumer extends AbstractTestConsumer<GetVaccinationHistoryResponderInterface> {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedVaccinationHistoryTestConsumer.class);

    public static void main(String[] args) {
        String serviceAddress = GetAggregatedVaccinationHistoryMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        GetAggregatedVaccinationHistoryTestConsumer consumer 
          = new GetAggregatedVaccinationHistoryTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID, SAMPLE_CORRELATION_ID);
        Holder<GetVaccinationHistoryResponseType> responseHolder = new Holder<GetVaccinationHistoryResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

        consumer.callService("logical-adress", personnummer, processingStatusHolder, responseHolder);

        log.info("Returned #records = {}", responseHolder.value.getVaccinationMedicalRecord().size());
    }

    public GetAggregatedVaccinationHistoryTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId, String correlationId) {
        // Setup a web service proxy for communication using HTTPS with mutual authentication
        super(GetVaccinationHistoryResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId, correlationId);
    }

    public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder,
            Holder<GetVaccinationHistoryResponseType> responseHolder) {

        log.debug("Calling GetRequestActivities-soap-service with Registered Resident Id = {}", registeredResidentId);
        GetVaccinationHistoryType request = new GetVaccinationHistoryType();
        final PersonIdType personIdType = new PersonIdType();
        personIdType.setId(registeredResidentId);
        personIdType.setType("1.2.752.129.2.1.3.1");
        request.setPatientId(personIdType);
        
        final GetVaccinationHistoryResponseType response = _service.getVaccinationHistory(logicalAddress, request);
        
        responseHolder.value = response;
        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}