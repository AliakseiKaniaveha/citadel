package kaniaveha.aliaksei.citadel.facade;

import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import kaniaveha.aliaksei.citadel.service.ByteCodeEngineeringService;
import kaniaveha.aliaksei.citadel.service.ClassesSourceService;
import kaniaveha.aliaksei.citadel.service.GraphService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ClassesGraphFacadeTest {

  @InjectMocks ClassesGraphFacade testee = new ClassesGraphFacade();

  @Mock private ClassesSourceService classesSourceService;
  @Mock private ByteCodeEngineeringService byteCodeEngineeringService;
  @Mock private GraphService graphService;

  @Test
  void visualizesOwnClassesGraphInGivenFormat() {
    // given
    File ownClassesJar = mock(File.class);
    given(classesSourceService.getMyOwnJar()).willReturn(ownClassesJar);

    byte[] class1Bytecode = {1};
    byte[] class2Bytecode = {2};
    given(classesSourceService.getClasses(ownClassesJar))
        .willReturn(asList(class1Bytecode, class2Bytecode));

    ClassDefinition classDefinition1 = new ClassDefinition("c1", new HashSet<>(asList("d1", "d2")));
    ClassDefinition classDefinition2 = new ClassDefinition("c2", new HashSet<>(asList("d1", "d3")));
    given(byteCodeEngineeringService.toClassDefinition(class1Bytecode)).willReturn(classDefinition1);
    given(byteCodeEngineeringService.toClassDefinition(class2Bytecode)).willReturn(classDefinition2);

    Graph graph = mock(Graph.class);
    given(graphService.buildGraph(asList(classDefinition1, classDefinition2))).willReturn(graph);

    given(graphService.render(graph,  "dot", "svg"))
        .willReturn(
            """
            <svg>
              <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">
                <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15029.87,-400 15029.87,4 -4,4"/>
              </g>
            </svg>
            """);

    // when
    String actual = testee.visualiseOwnClasses("dot", "svg");

    // then
    assertThat(actual, equalTo("""
            <svg>
              <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">
                <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15029.87,-400 15029.87,4 -4,4"/>
              </g>
            </svg>
            """));
  }
}
