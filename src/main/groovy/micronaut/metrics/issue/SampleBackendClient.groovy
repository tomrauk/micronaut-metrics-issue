package micronaut.metrics.issue

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client('${backend-endpoints.sample.base-url}')
interface SampleBackendClient {

    @Get('${backend-endpoints.sample.path}')
    String getResponse()
}
