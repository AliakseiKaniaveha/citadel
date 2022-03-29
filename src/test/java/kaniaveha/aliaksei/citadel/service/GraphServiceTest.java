package kaniaveha.aliaksei.citadel.service;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GraphServiceTest {

  @InjectMocks private final GraphService testee = new GraphService();

  @Mock private GraphService.Renderer graphServiceRenderer;

  @Test
  void buildsGraphFromClassesDependencies() {
    // given
    ClassDefinition classDefinition1 = new ClassDefinition("class1",
            new HashSet<>(Arrays.asList("dependency1", "dependency2")));
    ClassDefinition classDefinition2 = new ClassDefinition("class2",
            new HashSet<>(Arrays.asList("dependency1", "dependency3")));

    // when
    Graph actualGraph = testee.buildGraph(asList(classDefinition1, classDefinition2));

    //then
    assertThat(actualGraph.toString(), equalTo("""
            digraph {
            "class1" -> "dependency1"
            "class1" -> "dependency2"
            "class2" -> "dependency1"
            "class2" -> "dependency3"
            }"""));
  }

  @Test
  void rendersGraphInGivenFormat() {
    // given
    Graph graph = mock(Graph.class);
    given(graphServiceRenderer.render(graph, Format.SVG)).willReturn("""
            <svg width="15588px" height="404px" viewBox="0.00 0.00 15588.06 404.00"
            xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
            <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">
            <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15584.06,-400 15584.06,4 -4,4"/>
            </g>
            </svg>
            """);

    // when
    String actualSvg = testee.render(graph, "svg");

    // then
    assertThat(actualSvg, equalTo("""
            <svg id="svg" class="svg" preserveaspectratio="xMidYMid meet" width="15588px" height="404px" viewbox="0.00 0.00 15588.06 404.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"> <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">\s
              <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15584.06,-400 15584.06,4 -4,4" />\s
             </g>\s
            </svg>"""));
  }
}
