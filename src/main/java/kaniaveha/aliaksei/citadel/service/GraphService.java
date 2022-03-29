package kaniaveha.aliaksei.citadel.service;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import kaniaveha.aliaksei.citadel.Toolbox;
import kaniaveha.aliaksei.citadel.VisibleForTesting;
import kaniaveha.aliaksei.citadel.model.ClassDefinition;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.node;

/** Graphs related actions. */
@Service
public class GraphService {

  @Autowired private Renderer renderer;

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

  public String render(Graph graph, String formatStr) {
    Format format = Format.valueOf(formatStr.toUpperCase());
    String rendered = renderer.render(graph, format);
    return postProcess(rendered, format);
  }

  private String postProcess(String rendered, Format format) {
    if (format.equals(Format.SVG)) {
      Element svgTag =
          Toolbox.extractSingleton(
              Jsoup.parse(rendered).body().getElementsByTag("svg"),
              "Rendered svg does not contain <svg> tag: " + rendered,
              "Rendered svg contains more than one <svg> tag: " + rendered);
      Attributes attributes = svgTag.attributes().clone();
      svgTag.clearAttributes();
      svgTag.attr("id", "svg").addClass("svg").attr("preserveAspectRatio", "xMidYMid meet");
      svgTag.attributes().addAll(attributes);
      return svgTag.toString();
    }
    return rendered;
  }

  @VisibleForTesting
  @Service
  static class Renderer {
    public String render(Graph graph, Format format) {
      return Graphviz.fromGraph(graph).render(format).toString();
    }
  }
}
