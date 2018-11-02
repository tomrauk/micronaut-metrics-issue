package micronaut.metrics.issue

import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Produces
import io.micronaut.management.endpoint.annotation.Endpoint
import io.micronaut.management.endpoint.annotation.Read

import javax.inject.Inject

@Endpoint(id = "prometheus", value = "/prometheus", defaultEnabled = true, defaultSensitive = false)
class PrometheusEndpoint {

    @Inject
    PrometheusMeterRegistry prometheusMeterRegistry

    @Read
    @Produces(MediaType.TEXT_PLAIN)
    String scrape(){
        return prometheusMeterRegistry.scrape()
    }
}