package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import scala.util.Random

object GetAggregatedVaccinationHistoryScenario {
  
  val headers = Map(
    "Accept-Encoding"                        -> "gzip,deflate",
    "Content-Type"                           -> "text/xml;charset=UTF-8",
    "SOAPAction"                             -> "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1:GetVaccinationHistory",
    "x-vp-sender-id"                         -> "test",
    "x-rivta-original-serviceconsumer-hsaid" -> "test",
    "Keep-Alive"                             -> "115")
    
  val request = exec(
        http("GetAggregatedVaccinationHistory ${patientid} - ${name}")
          .post("")
          .headers(headers)
          .body(ELFileBody("GetVaccinationHistory.xml"))
          .check(status.is(session => session("status").as[String].toInt))
          .check(xpath("soap:Envelope", List("soap" -> "http://schemas.xmlsoap.org/soap/envelope/")).exists)
          .check(substring("GetVaccinationHistoryResponse"))
          .check(xpath("//ns2:vaccinationMedicalRecord", List("ns2" -> "urn:riv:clinicalprocess:activityprescription:actoutcome:GetVaccinationHistoryResponder:1")).count.is(session => session("count").as[String].toInt))
      )
}
