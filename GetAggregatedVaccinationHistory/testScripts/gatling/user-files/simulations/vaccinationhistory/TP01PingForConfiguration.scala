package vaccinationhistory

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scenarios.GetAggregatedVaccinationHistoryPingForConfigurationScenario

/**
 * Ping for configuration run against remote service - returns ok.
 */
class TP01PingForConfiguration extends Simulation {

  // dev
  val httpProtocol = http.baseURL("http://ine-dit-app02.sth.basefarm.net:9014/agp/getaggregatedvaccinationhistory/itintegration/monitoring/PingForConfiguration/1/rivtabp21").disableResponseChunksDiscarding
  
  val pingForConfiguration = scenario("ping for configuration")
                 .repeat(2) {
                    exec(GetAggregatedVaccinationHistoryPingForConfigurationScenario.request)
                  }
                 
  setUp (pingForConfiguration.inject(atOnceUsers(1)).protocols(httpProtocol))
}