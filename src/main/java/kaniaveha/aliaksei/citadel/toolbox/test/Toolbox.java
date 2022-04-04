package kaniaveha.aliaksei.citadel.toolbox.test;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.PortNode;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * The same as {@link kaniaveha.aliaksei.citadel.toolbox.Toolbox} but for unit/integrated-unit
 * tests, not production code.
 *
 * <p>It might be better to place it in test code, but having it here allows to avoid duplication of
 * 'tools' used in both unit and integrated unit tests.
 */
public class Toolbox {

  /** Compiles string java code into byte code. */
  public static byte[] compile(String sourceCode) {
    try {
      Matcher matcher = Pattern.compile("class .* \\{|implements|extends").matcher(sourceCode);
      matcher.find();
      String className =
          matcher
              .group()
              .replace("class", "")
              .replaceAll("\\{.*|implements.*|extends.*", "")
              .trim();

      Path tempDir = Files.createTempDirectory(null).getParent();
      Path packageDir = Paths.get(tempDir.toString(), "test", "dependencies");
      new File(packageDir.toString()).mkdirs();
      // Path sourceFile = Files.createTempFile(packageDir, className, ".java");
      Path sourceFile = Paths.get(packageDir.toString(), className + ".java");
      Files.write(sourceFile, sourceCode.getBytes());

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      int compiled = compiler.run(null, null, null, sourceFile.toFile().getPath());
      assertThat("Compilation failed!", compiled, equalTo(0));

      File classFile = new File(packageDir + "/" + className + ".class");
      return Files.readAllBytes(classFile.toPath());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Looks for a resource file under a test resources sub-folder, see example:
   *
   * <pre>{@code
   * package kaniaveha.aliaksei.citadel.facade;
   * class ClassesGraphFacadeTest {
   *    void buildsClassesGraphFromJar() {
   *     // given
   *     Path pathToJar = Routines.getTestResource("twoClasses.jar").toPath();
   *     ...
   *     }
   * }}
   * would expect file {@code src/test-integrated-unit/resources/kaniaveha.aliaksei.citadel.facade/ClassesGraphFacadeTest/twoClasses.jar}
   */
  public static File getTestResource(String resourceName) {
    Class<?> testClass = getCallerClass();
    String pathToResource = testClass.getName().replace('.', '/') + "/" + resourceName;
    URL resource = testClass.getClassLoader().getResource(pathToResource);
    assertThat("Resource " + pathToResource + " is not exist", resource, notNullValue());
    return new File(resource.getFile());
  }

  private static Class<?> getCallerClass() {
    String callerClassName =
        Arrays.stream(new Exception().getStackTrace())
            .filter(notFromThisClass())
            .findFirst()
            .orElseThrow(IllegalStateException::new)
            .getClassName();

    try {
      return Class.forName(callerClassName);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Predicate<StackTraceElement> notFromThisClass() {
    return stackTraceElement -> !stackTraceElement.getClassName().equals(Toolbox.class.getName());
  }

  public static class Grpahs {
    public static List<MutableNode> getLinkedNodes(MutableNode node) {
      return node.links().stream()
              .map(link -> link.to())
              .map(PortNode.class::cast)
              .map(PortNode::node)
              .map(MutableNode.class::cast)
              .toList();
    }

    public static MutableNode findNode(String name, Collection<MutableNode> nodes) {
      return nodes.stream()
              .filter(node -> node.name().toString().equals(name))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("No node " + name + " found within " + nodes));
    }

    public static List<String> toNames(Collection<MutableNode> nodes) {
      return nodes.stream().map(MutableNode::name).map(Label::toString).toList();
    }
  }
}
