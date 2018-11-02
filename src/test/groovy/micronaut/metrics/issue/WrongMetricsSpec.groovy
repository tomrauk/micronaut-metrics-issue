package micronaut.metrics.issue

import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

@MicronautTest
class WrongMetricsSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(options().port(9999))

    @Inject
    @Client('/')
    RxHttpClient client

    @Unroll
    def "metrics report status of #status when backend response status of #status code"() {
        setup:
        wireMockRule.stubFor(get(urlEqualTo('/micronaut-projects/micronaut-core/releases/tag/v1.0.0'))
                .willReturn(aResponse()
                .withStatus(status)
                .withHeader('Content-Type', 'application/json')
                .withBody('{"the":"response"}')))

        when: 'call production code'
        client.toBlocking().retrieve(HttpRequest.GET('/example'), String)

        and: 'get metrics'
        String metricResponse = client.toBlocking().retrieve(HttpRequest.GET('/prometheus'), String)
        List<String> metrics = metricResponse.split('\n')
        String metric = metrics.find {it.startsWith('http_client_requests_seconds_sum{') && it.contains('/micronaut-projects/micronaut-core/releases/tag/v1.0.0')}

        then:
        metric.contains("status=\"$status\"")

        where:
        status << HttpStatus.values()*.code.findAll{it > 399 && it != 404}
    }
}
