package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistory;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v1.GetVaccinationHistoryResponseType;
import se.riv.clinicalprocess.activityprescription.actoutcome.v1.VaccinationMedicalRecordType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryImplTest {
    
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetVaccinationHistoryResponseType.class, ProcessingStatusType.class);
    
    @Test
    public void getXmlFromAggregatedResponse(){
        FindContentType fc = new FindContentType();     
        fc.setRegisteredResidentIdentification("1212121212");
        QueryObject queryObject = new QueryObject(fc, null);
        List<Object> responseList = new ArrayList<Object>(2);
        responseList.add(createGetVaccinationHistoryResponse());
        responseList.add(createGetVaccinationHistoryResponse());
        ResponseListFactoryImpl responseListFactory = new ResponseListFactoryImpl();
        
        String responseXML = responseListFactory.getXmlFromAggregatedResponse(queryObject, responseList);
        GetVaccinationHistoryResponseType response = (GetVaccinationHistoryResponseType)jaxbUtil.unmarshal(responseXML);
        assertEquals(2, response.getVaccinationMedicalRecord().size());
    }
    
    private GetVaccinationHistoryResponseType createGetVaccinationHistoryResponse(){
        GetVaccinationHistoryResponseType getVaccinationHistoryResponse = new GetVaccinationHistoryResponseType();
        getVaccinationHistoryResponse.getVaccinationMedicalRecord().add(new VaccinationMedicalRecordType());
        return getVaccinationHistoryResponse;
    }
}
