package kaniaveha.aliaksei.citadel.service;

import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class GraphServiceTest {

  private final GraphService testee = new GraphService();

  @Test
  void buildsGraphFromOnClassesDependencies() {
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
}
