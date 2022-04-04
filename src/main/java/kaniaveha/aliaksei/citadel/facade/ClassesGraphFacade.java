package kaniaveha.aliaksei.citadel.facade;

import guru.nidi.graphviz.model.Graph;
import kaniaveha.aliaksei.citadel.VisibleForTesting;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import kaniaveha.aliaksei.citadel.service.ByteCodeEngineeringService;
import kaniaveha.aliaksei.citadel.service.ClassesSourceService;
import kaniaveha.aliaksei.citadel.service.GraphService;
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
