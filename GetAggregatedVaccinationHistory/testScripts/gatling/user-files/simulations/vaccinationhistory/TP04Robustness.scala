package vaccinationhistory

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scenarios.GetAggregatedVaccinationHistoryScenario

/**
 * Test VP:GetAggregatedVaccinationHistory over 12 hours
 */
class TP04Robustness extends Simulation {

  val baseURL = "https://qa.esb.ntjp.se/vp/clinicalprocess/activityprescription/actoutcome/GetVaccinationHistory/1/rivtabp21"
  
  val testDuration            =  12 hours
  val numberOfConcurrentUsers =   5
  val rampDuration            =   1 minute
  val minWaitDuration         =   2 seconds
  val maxWaitDuration         =   4 seconds
  
  val httpProtocol = http.baseURL(baseURL).disableResponseChunksDiscarding

  val robustness = scenario("robustness")
                  .during(testDuration) {
                    feed(csv("patients.csv").circular)
                   .exec(GetAggregatedVaccinationHistoryScenario.request)
                   .pause(minWaitDuration, maxWaitDuration)
                  }
                 
  setUp (robustness.inject(rampUsers(numberOfConcurrentUsers) over (rampDuration)).protocols(httpProtocol))
}