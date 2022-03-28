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

    File graphFile = mock(File.class);
    given(graphService.render(graph, "FORMAT")).willReturn(graphFile);
    given(graphFile.getPath()).willReturn("path/to/rendered/graph.file");

    // when
    String actual = testee.visualiseOwnClasses("FORMAT");

    // then
    assertThat(actual, equalTo("path/to/rendered/graph.file"));
  }
}
