package micronaut.metrics.issue

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.exceptions.HttpClientResponseException

import javax.inject.Inject

@Controller('/example')
class ExampleController {

    @Inject
    SampleBackendClient sampleBackendClient

    @Get
    String getResponse() {
        try {
            return sampleBackendClient.getResponse()
        } catch (HttpClientResponseException exception) {
            return "Failure: ${exception.message}"
        }
    }


}
