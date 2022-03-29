package kaniaveha.aliaksei.citadel.integratedtest;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class Toolbox {

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
