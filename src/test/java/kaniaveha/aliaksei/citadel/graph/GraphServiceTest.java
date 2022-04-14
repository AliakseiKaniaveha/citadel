package kaniaveha.aliaksei.citadel.graph;

import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import kaniaveha.aliaksei.citadel.classes.ClassDefinition;
import kaniaveha.aliaksei.citadel.graph.GraphService;
import kaniaveha.aliaksei.citadel.toolbox.test.Toolbox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static kaniaveha.aliaksei.citadel.toolbox.test.Toolbox.Grpahs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GraphServiceTest {

  @InjectMocks private final GraphService testee = new GraphService();

  @Mock private GraphService.Renderer graphServiceRenderer;

  @Test
  void buildsGraphFromClassesDependencies() {
    // given
    ClassDefinition classDefinition1 = new ClassDefinition("internal.package.Class1",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "external.package.Dependency2")));
    ClassDefinition classDefinition2 = new ClassDefinition("internal.package.Class2",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "another.external.package.Dependency3", "internal.package.Class1")));

    // when
    MutableGraph actualGraph = (MutableGraph) testee.buildGraph(asList(classDefinition1, classDefinition2));

    //then
    Collection<MutableNode> nodes = actualGraph.rootNodes();
    assertThat(toNames(nodes), containsInAnyOrder("internal.package.Class1", "internal.package.Class2"));

    MutableNode node1 = findNode("internal.package.Class1", nodes);
    assertThat(toNames(getLinkedNodes(node1)),
            containsInAnyOrder("external.package.Dependency1", "external.package.Dependency2"));

    MutableNode node2 = findNode("internal.package.Class2", nodes);
    assertThat(toNames(getLinkedNodes(node2)),
            containsInAnyOrder("external.package.Dependency1", "another.external.package.Dependency3", "internal.package.Class1"));
  }

  @Test
  void paintsNodesInColorsDependingOnPackage() {
    // given
    ClassDefinition classDefinition1 = new ClassDefinition("internal.package.Class1",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "external.package.Dependency2")));
    ClassDefinition classDefinition2 = new ClassDefinition("internal.package.Class2",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "another.external.package.Dependency3", "internal.package.Class1")));

    // when
    MutableGraph actualGraph = (MutableGraph) testee.buildGraph(asList(classDefinition1, classDefinition2));

    //then
    Collection<MutableNode> nodes = actualGraph.rootNodes();

    MutableNode node1 = findNode("internal.package.Class1", nodes);
    assertThat(node1.get("color"), equalTo("#60ce0032"));
    List<MutableNode> node1Links = getLinkedNodes(node1);
    assertThat(findNode("external.package.Dependency1", node1Links).get("color"), equalTo("#66ce0032"));
    assertThat(findNode("external.package.Dependency2", node1Links).get("color"), equalTo("#66ce0032"));


    MutableNode node2 = findNode("internal.package.Class2", nodes);
    List<MutableNode> node2Links = getLinkedNodes(node2);
    assertThat(node2.get("color"), equalTo("#60ce0032"));
    assertThat(findNode("external.package.Dependency1", node2Links).get("color"), equalTo("#66ce0032"));
    assertThat(findNode("another.external.package.Dependency3", node2Links).get("color"), equalTo("#f366ce32"));
    assertThat(findNode("internal.package.Class1", node2Links).get("color"), equalTo("#60ce0032"));
  }

  @Test
  void setsStyleToAllNodes() {
    // given
    ClassDefinition classDefinition1 = new ClassDefinition("internal.package.Class1",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "external.package.Dependency2")));
    ClassDefinition classDefinition2 = new ClassDefinition("internal.package.Class2",
            new HashSet<>(Arrays.asList("external.package.Dependency1", "another.external.package.Dependency3", "internal.package.Class1")));

    // when
    MutableGraph actualGraph = (MutableGraph) testee.buildGraph(asList(classDefinition1, classDefinition2));

    //then
    Collection<MutableNode> nodes = actualGraph.rootNodes();
    Stream<MutableNode> allGraphNodes = Stream.concat(nodes.stream(),
            nodes.stream().map(Toolbox.Grpahs::getLinkedNodes).flatMap(Collection::stream));
    allGraphNodes.forEach(node -> {
      assertThat(node.get("style"), equalTo("filled"));
      assertThat(node.get("fixedsize"), equalTo("true"));
      assertThat(node.get("width"), equalTo(3.0));
      assertThat(node.get("height"), equalTo(3.0));
    });
  }

  @Test
  void paintsOwnClassesNodesWithNoTransparencyOthersWithTransparency80Percents() {
    // given
    ClassDefinition classDefinition = new ClassDefinition("kaniaveha.package.Class1",
            new HashSet<>(List.of("external.package.Dependency")));

    // when
    MutableGraph actualGraph = (MutableGraph) testee.buildGraph(List.of(classDefinition));

    // then
    Collection<MutableNode> nodes = actualGraph.rootNodes();

    assertThat(nodes, iterableWithSize(1));
    MutableNode node = nodes.iterator().next();
    assertThat(node.get("color"), equalTo("#abce00ff"));  //ff for 255 in hex - most bright color

    List<MutableNode> linkedNodes = getLinkedNodes(node);
    assertThat(linkedNodes, iterableWithSize(1));
    assertThat(linkedNodes.iterator().next().get("color"), equalTo("#66ce0032")); //32 for 50 in he - ~20% bright
  }

  @Test
  void addsToSvgAttributesRequiredForCorrectDisplay() {
    // given
    Graph graph = mock(Graph.class);
    given(graphServiceRenderer.render(graph, Engine.DOT, Format.SVG)).willReturn("""
            <svg width="15588px" height="404px" viewBox="0.00 0.00 15588.06 404.00"
            xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
            <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">
            <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15584.06,-400 15584.06,4 -4,4"/>
            </g>
            </svg>
            """);

    // when
    String actualSvg = testee.render(graph, "dot", "svg");

    // then
    assertThat(actualSvg, containsString("id=\"svg\""));
    assertThat(actualSvg, containsString("class=\"svg\""));
    assertThat(actualSvg, containsString("preserveaspectratio=\"xMidYMid meet\""));
    assertThat(actualSvg, equalTo("""
            <svg id="svg" class="svg" preserveaspectratio="xMidYMid meet" width="15588px" height="404px" viewbox="0.00 0.00 15588.06 404.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"> <g id="graph0" class="graph" transform="scale(1.0 1.0) rotate(0.0) translate(4.0 400.0)">\s
              <polygon fill="white" stroke="transparent" points="-4,4 -4,-400 15584.06,-400 15584.06,4 -4,4" />\s
             </g>\s
            </svg>"""));
  }

}
