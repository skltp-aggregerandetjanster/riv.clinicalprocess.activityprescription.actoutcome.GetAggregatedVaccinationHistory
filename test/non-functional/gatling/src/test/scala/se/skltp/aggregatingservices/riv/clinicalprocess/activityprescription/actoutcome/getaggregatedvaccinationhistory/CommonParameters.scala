package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory

trait CommonParameters {
  val serviceName:String     = "VaccinationHistory"
  val urn:String             = "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:2"
  val responseElement:String = "GetVaccinationHistoryResponse"
  val responseItem:String    = "vaccinationMedicalRecord"
  var baseUrl:String         = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) {
                                   System.getProperty("baseUrl")
                               } else {
                                   "http://33.33.33.33:9016/GetAggregatedVaccinationHistory/service/v2"
                               }
}
