import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GraphSpec extends Specification {

    def 'Visualises classes graph in svg'() {
        given:
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/rest/v1/classes-graph/visualisation")).GET().build()

        when:
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        then:
        response.statusCode() == 200
        response.body().startsWith("""<svg id="svg" class="svg" preserveaspectratio="xMidYMid meet\"""");
    }

    def 'Builds correct classes graph'() {
        given: 'app up and running'
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/rest/v1/classes-graph/visualisation?format=dot")).GET().build()

        when: 'graph visualization requested'
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        then: 'application successfully responded with graph visualization'
        response.statusCode() == 200

        and: 'correct links built'
        response.body().contains(""""kaniaveha.aliaksei.citadel.AppWebController" -> "kaniaveha.aliaksei.citadel.AppRestController\"""");
        response.body().contains(""""kaniaveha.aliaksei.citadel.AppWebController" -> "org.springframework.web.bind.annotation.RequestParam\"""");
        response.body().contains(""""kaniaveha.aliaksei.citadel.AppRestController" -> "org.springframework.web.bind.annotation.RequestParam\"""");

        and: 'nodes has expected style'
        response.body().contains(""""kaniaveha.aliaksei.citadel.AppWebController" ["style"="filled","fixedsize"="true","width"="3.0","height"="3.0","color"="#ab46d8ff"]""");
        response.body().contains(""""kaniaveha.aliaksei.citadel.AppRestController" ["style"="filled","fixedsize"="true","width"="3.0","height"="3.0","color"="#ab46d8ff"]""");
        response.body().contains(""""org.springframework.web.bind.annotation.RequestParam" ["style"="filled","fixedsize"="true","width"="3.0","height"="3.0","color"="#49673f32"]""");

    }


}