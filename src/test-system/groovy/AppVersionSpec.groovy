import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class AppVersionSpec extends Specification {

    def 'Knows own version'() {
        given:
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/rest/v1/version")).GET().build()

        when:
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        then:
        response.statusCode() == 200
        response.body().matches('^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)?$')
    }

}