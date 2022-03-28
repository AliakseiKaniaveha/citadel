package kaniaveha.aliaksei.citadel;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
}
