package citadel.graph;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import citadel.toolbox.test.Toolbox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Collection;

import static citadel.toolbox.test.Toolbox.Grpahs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest
class ClassesGraphFacadeIntegratedTest {

  @Autowired ClassesGraphFacade testee;

  @Test
  void buildsClassesGraphFromJar() {
    // given
    File jar = Toolbox.getTestResource("twoClasses.jar");

    // when
    MutableGraph graph = (MutableGraph) testee.buildGraph(jar);

    // then
    Collection<MutableNode> nodes = graph.rootNodes();
    assertThat(
        toNames(nodes),
        containsInAnyOrder(
            "test.dependencies.ClassWithDependencies",
            "test.dependencies.ClassWithoutDependencies"));

    MutableNode withoutDependencies = findNode("test.dependencies.ClassWithoutDependencies", nodes);
    assertThat(
        toNames(getLinkedNodes(withoutDependencies)), containsInAnyOrder("java.lang.Object"));

    MutableNode withDependencies = findNode("test.dependencies.ClassWithDependencies", nodes);
    assertThat(
        toNames(getLinkedNodes(withDependencies)),
        containsInAnyOrder(
            "java.io.File",
            "javax.tools.ToolProvider",
            "java.nio.file.Path",
            "java.util.LinkedList",
            "java.lang.String",
            "java.io.IOException",
            "java.util.Collection",
            "javax.tools.OptionChecker"));
  }
}
