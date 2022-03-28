package kaniaveha.aliaksei.citadel.facade;

import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.Toolbox;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
class ClassesGraphFacadeIntegratedTest {

  @Autowired ClassesGraphFacade testee;

  @Test
  void buildsClassesGraphFromJar() {
    // given
    File jar = Toolbox.getTestResource("twoClasses.jar");

    // when
    Graph graph = testee.buildGraph(jar);

    // then
    assertThat(graph.toString(), equalTo("""
            digraph {
            "test.dependencies.ClassWithDependencies" -> "java.io.File"
            "test.dependencies.ClassWithDependencies" -> "javax.tools.ToolProvider"
            "test.dependencies.ClassWithDependencies" -> "java.nio.file.Path"
            "test.dependencies.ClassWithDependencies" -> "java.util.LinkedList"
            "test.dependencies.ClassWithDependencies" -> "java.lang.String"
            "test.dependencies.ClassWithDependencies" -> "java.io.IOException"
            "test.dependencies.ClassWithDependencies" -> "java.util.Collection"
            "test.dependencies.ClassWithDependencies" -> "javax.tools.OptionChecker"
            "test.dependencies.ClassWithoutDependencies" -> "java.lang.Object"
            }"""));
  }
}
