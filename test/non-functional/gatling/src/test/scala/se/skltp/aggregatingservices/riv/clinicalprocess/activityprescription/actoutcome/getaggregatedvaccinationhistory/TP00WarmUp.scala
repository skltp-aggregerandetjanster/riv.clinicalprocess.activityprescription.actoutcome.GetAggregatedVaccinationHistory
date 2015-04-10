package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.scenarios.GetAggregatedVaccinationHistoryScenario

/**
 * Simple requests to warm up service.
 */
class TP00WarmUp extends Simulation {

  val baseURL           = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) System.getProperty("baseUrl") else "http://33.33.33.33:8084/GetAggregatedVaccinationHistory/service/v1"

  val testDuration      = 60 seconds
  val minWaitDuration   =  2 seconds
  val maxWaitDuration   =  5 seconds
  val times:Int         =  1

  val httpProtocol = http.baseURL(baseURL).disableResponseChunksDiscarding

  val warmUp = scenario("warm up")
                 .repeat(times) {
//                 feed(csv("patients.csv").queue)
                   exec(session => {
                     session.set("status","200").set("patientid","121212121212").set("name","Tolvan Tolvansson").set("count","3")
                   })
                   .exec(GetAggregatedVaccinationHistoryScenario.request)
                   .pause(1 second)
                  }

  setUp (warmUp.inject(atOnceUsers(1)).protocols(httpProtocol))
}
