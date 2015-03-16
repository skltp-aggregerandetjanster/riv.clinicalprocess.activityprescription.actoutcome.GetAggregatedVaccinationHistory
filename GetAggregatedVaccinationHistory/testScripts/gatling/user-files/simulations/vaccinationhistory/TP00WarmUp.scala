package vaccinationhistory

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scenarios.GetAggregatedVaccinationHistoryScenario

/**
 * Simple requests to warm up service.
 */
class TP00WarmUp extends Simulation {


//val baseURL           = "https://test.esb.ntjp.se/vp/clinicalprocess/activityprescription/actoutcome/GetVaccinationHistory/2/rivtabp21"
//val baseURL           = "http://ine-dit-app02.sth.basefarm.net:9014/GetAggregatedVaccinationHistory/service/v2"
  val baseURL           = "https://qa.esb.ntjp.se/vp/clinicalprocess/activityprescription/actoutcome/GetVaccinationHistory/2/rivtabp21"

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