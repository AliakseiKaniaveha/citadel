package citadel.graph;

import guru.nidi.graphviz.model.Graph;
import citadel.classes.ByteCodeEngineeringService;
import citadel.classes.ClassDefinition;
import citadel.classes.ClassesSourceService;
import citadel.toolbox.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Combines action provided by different services in order to deal with classes graph related stuff.
 */
@Component
public class ClassesGraphFacade {

  @Autowired private ClassesSourceService classesSourceService;
  @Autowired private ByteCodeEngineeringService byteCodeEngineeringService;
  @Autowired private GraphService graphService;

  /**
   * @return visualisation of classes graph
   */
  public String visualiseOwnClasses(String engine, String format) {
    File myOwnJar = classesSourceService.getMyOwnJar();
    Graph graph = buildGraph(myOwnJar);
    return graphService.render(graph, engine, format);
  }

  @VisibleForTesting
  public Graph buildGraph(File myOwnJar) {
    Collection<byte[]> classes = classesSourceService.getClasses(myOwnJar);
    List<ClassDefinition> classDefinitionList =
        classes.stream().map(byteCodeEngineeringService::toClassDefinition).toList();
    return graphService.buildGraph(classDefinitionList);
  }
}
