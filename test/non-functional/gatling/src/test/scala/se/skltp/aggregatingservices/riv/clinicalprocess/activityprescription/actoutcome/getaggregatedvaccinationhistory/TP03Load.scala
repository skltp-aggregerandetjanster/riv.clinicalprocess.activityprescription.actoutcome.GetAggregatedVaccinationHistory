package se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import se.skltp.aggregatingservices.riv.clinicalprocess.activityprescription.actoutcome.getaggregatedvaccinationhistory.scenarios.GetAggregatedVaccinationHistoryScenario

/**
 * Load test VP:GetAggregatedVaccinationHistory.
 */
class TP03Load extends Simulation {

  val baseURL = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) { 
                  System.getProperty("baseUrl") 
                } else {
                  "http://ine-sit-app03.sth.basefarm.net:9016/GetAggregatedVaccinationHistory/service/v1"
                }
  
  val testDuration            = 140 seconds
  val numberOfConcurrentUsers =  45
  val rampDuration            =  10 seconds
  val minWaitDuration         =   2 seconds
  val maxWaitDuration         =   5 seconds
  
  val httpProtocol = http.baseURL(baseURL).disableResponseChunksDiscarding

  val load = scenario("load")
                 .during(testDuration) {
                   exec(session => {
                     session.set("status","200").set("patientid","121212121212").set("name","Tolvan Tolvansson").set("count","3")
                   })    
                   .exec(GetAggregatedVaccinationHistoryScenario.request)
                   .pause(minWaitDuration, maxWaitDuration)
                  }
                 
  setUp (load.inject(rampUsers(numberOfConcurrentUsers) over (rampDuration)).protocols(httpProtocol))
}