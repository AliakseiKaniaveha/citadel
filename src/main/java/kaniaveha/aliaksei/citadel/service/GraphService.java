package kaniaveha.aliaksei.citadel.service;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.node;

/** Classes graph related actions. */
@Service
public class GraphService {
  public Graph buildGraph(Collection<ClassDefinition> classDefinitionList) {
    MutableGraph graph = mutGraph().setDirected(true);

    classDefinitionList.forEach(
        classDependencies -> {
          Node classNode = node(classDependencies.clasName());
          classDependencies
              .dependencies()
              .forEach(referencedClass -> graph.add(classNode.link(node(referencedClass))));
        });

    return graph.toImmutable();
  }

  public File render(Graph graph, String formatStr) {
    try {
      Format format = Format.valueOf(formatStr);
      return Graphviz.fromGraph(graph)
          .render(format)
          .toFile(File.createTempFile("graph", format.fileExtension));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
