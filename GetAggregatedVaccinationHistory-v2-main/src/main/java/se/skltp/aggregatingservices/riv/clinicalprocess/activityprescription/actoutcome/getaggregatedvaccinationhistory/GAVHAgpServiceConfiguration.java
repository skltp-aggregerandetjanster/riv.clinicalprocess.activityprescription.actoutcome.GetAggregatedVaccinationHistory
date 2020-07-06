package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v2.rivtabp21.GetVaccinationHistoryResponderInterface;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory.v2.rivtabp21.GetVaccinationHistoryResponderService;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "getaggregatedvaccinationhistory.v2")
public class GAVHAgpServiceConfiguration extends se.skltp.aggregatingservices.configuration.AgpServiceConfiguration {

public static final String SCHEMA_PATH = "/schemas/interactions/GetVaccinationHistoryInteraction/GetVaccinationHistoryInteraction_2.0_RIVTABP21.wsdl";

  public GAVHAgpServiceConfiguration() {

    setServiceName("GetAggregatedVaccinationHistory-v2");
    setTargetNamespace("urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistory:2:rivtabp21");

    // Set inbound defaults
    setInboundServiceURL("http://localhost:9016/GetAggregatedVaccinationHistory/service/v2");
    setInboundServiceWsdl(SCHEMA_PATH);
    setInboundServiceClass(GetVaccinationHistoryResponderInterface.class.getName());
    setInboundPortName(GetVaccinationHistoryResponderService.GetVaccinationHistoryResponderPort.toString());

    // Set outbound defaults
    setOutboundServiceWsdl(SCHEMA_PATH);
    setOutboundServiceClass(GetVaccinationHistoryResponderInterface.class.getName());
    setOutboundPortName(GetVaccinationHistoryResponderService.GetVaccinationHistoryResponderPort.toString());

    // FindContent
    setEiServiceDomain("riv:clinicalprocess:activityprescription:actoutcome");
    setEiCategorization("caa-gvh");

    // TAK
    setTakContract("urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:2");

    // Set service factory
    setServiceFactoryClass(GAVHAgpServiceFactoryImpl.class.getName());
    }


}
