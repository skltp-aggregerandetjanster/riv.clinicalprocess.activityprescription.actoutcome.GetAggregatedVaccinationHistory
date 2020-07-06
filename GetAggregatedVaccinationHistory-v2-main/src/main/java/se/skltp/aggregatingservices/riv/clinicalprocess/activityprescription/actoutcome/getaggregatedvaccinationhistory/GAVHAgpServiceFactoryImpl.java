package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import riv.clinicalprocess.activityprescription.actoutcome.enums.v2.ResultCodeEnum;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryResponseType;
import riv.clinicalprocess.activityprescription.actoutcome.getvaccinationhistoryresponder.v2.GetVaccinationHistoryType;
import riv.clinicalprocess.activityprescription.actoutcome.v2.ResultType;
import se.skltp.aggregatingservices.AgServiceFactoryBase;

@Log4j2
public class GAVHAgpServiceFactoryImpl extends
    AgServiceFactoryBase<GetVaccinationHistoryType, GetVaccinationHistoryResponseType> {

  @Override
  public String getPatientId(GetVaccinationHistoryType queryObject) {
    return queryObject.getPatientId().getId();
  }

  @Override
  public String getSourceSystemHsaId(GetVaccinationHistoryType queryObject) {
    return queryObject.getSourceSystemHSAid();
  }

  @Override
  public GetVaccinationHistoryResponseType aggregateResponse(
      List<GetVaccinationHistoryResponseType> aggregatedResponseList) {

    GetVaccinationHistoryResponseType aggregatedResponse = new GetVaccinationHistoryResponseType();
    for (GetVaccinationHistoryResponseType response : aggregatedResponseList) {
      aggregatedResponse.getVaccinationMedicalRecord()
          .addAll(response.getVaccinationMedicalRecord());
    }

    aggregatedResponse.setResult(new ResultType());
    aggregatedResponse.getResult().setResultCode(ResultCodeEnum.INFO);
    aggregatedResponse.getResult().setLogId("NA");

    return aggregatedResponse;

  }
}

