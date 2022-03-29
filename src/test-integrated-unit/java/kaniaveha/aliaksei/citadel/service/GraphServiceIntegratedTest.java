package kaniaveha.aliaksei.citadel.service;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.facade.ClassesGraphFacade;
import kaniaveha.aliaksei.citadel.integratedtest.Toolbox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class GraphServiceIntegratedTest {

  @Autowired GraphService testee;
  @Autowired ClassesGraphFacade classesGraphFacade;
  @MockBean GraphService.Renderer graphServiceRenderer;

  @Test
  void rendersGraphInGivenFormat() {
    // given
    Graph graph = classesGraphFacade.buildGraph(Toolbox.getTestResource("twoClasses.jar"));
    given(graphServiceRenderer.render(graph, Format.SVG)).willReturn("""
            <svg attr="val"></svg>""");

    // when
    String actualSvg = testee.render(graph, "svg");

    // then
    assertThat(
        actualSvg,
        equalTo("""
                <svg id="svg" class="svg" preserveaspectratio="xMidYMid meet" attr="val"></svg>"""));
  }
}
