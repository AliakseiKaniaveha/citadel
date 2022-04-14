package citadel.graph;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Size;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import citadel.toolbox.Toolbox;
import citadel.toolbox.VisibleForTesting;
import citadel.classes.ClassDefinition;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

/** Graphs related actions. */
@Service
public class GraphService {

  @Autowired private Renderer renderer;

  public Graph buildGraph(Collection<ClassDefinition> classDefinitionList) {
    MutableGraph graph = mutGraph().setDirected(true);

    classDefinitionList.forEach(
        classDefinition -> {
          String clasName = classDefinition.clasName();
          MutableNode classNode = createNode(clasName);
          graph.add(classNode);
          classDefinition.dependencies().stream().map(this::createNode).forEach(classNode::addLink);
        });

    return graph.toImmutable();
  }

  private MutableNode createNode(String name) {
    MutableNode node = mutNode(name).add(Style.FILLED, Size.mode(Size.Mode.FIXED).size(3, 3));
    node.add(calcColor(name));
    return node;
  }

  // | R | | G | | B | | B tone  |
  // part1.part2.part3.part4.part5.ClassName
  private Color calcColor(String className) {
    String[] fullClassName = className.split("\\.");
    String[] packageName = Arrays.copyOfRange(fullClassName, 0, fullClassName.length - 1);
    Iterator<String> iterator = Arrays.stream(packageName).iterator();
    int r = (iterator.hasNext() ? iterator.next().chars().sum() : 0) % 255;
    int g = (iterator.hasNext() ? iterator.next().chars().sum() : 0) % 255;
    int b = (iterator.hasNext() ? iterator.next().chars().sum() : 0) % 255;
    int transparency_0_to_255 = isOwnClass(className) ? 255 : 50;

    return Color.rgba(hex(r) + hex(g) + hex(b) + hex(transparency_0_to_255));
  }

  private boolean isOwnClass(String className) {
    return className.startsWith(this.getClass().getName().split("\\.")[0]);
  }

  private static String hex(int value) {
    final String s = Integer.toHexString(value & 0xff);
    return s.length() == 1 ? "0" + s : s;
  }

  public String render(Graph graph, String engineStr, String formatStr) {
    Engine engine = Engine.valueOf(engineStr.toUpperCase());
    Format format = Format.valueOf(formatStr.toUpperCase());
    String rendered = renderer.render(graph, engine, format);
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
    public String render(Graph graph, Engine engine, Format format) {
      return Graphviz.fromGraph(graph).engine(engine).render(format).toString();
    }
  }
}
