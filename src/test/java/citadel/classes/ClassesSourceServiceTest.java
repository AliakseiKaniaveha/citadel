package citadel.classes;

import citadel.toolbox.test.Toolbox;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ClassesSourceServiceTest {

  private final ClassesSourceService testee = new ClassesSourceService();

  @Test
  void providesClassFilesFromJar() {
    // given
    File pathToJar = Toolbox.getTestResource("citadel.jar");

    // when
    Collection<Path> actualFiles = testee.getClassFiles(pathToJar);

    // then
    List<String> actualFileNames =
        actualFiles.stream().map(Path::getFileName).map(Path::toString).toList();
    assertThat(
        "Actual collection: " + actualFileNames,
        actualFileNames,
        containsInAnyOrder(
            "App.class",
            "AppWebController.class",
            "AppRestController.class",
            "ClassesGraphFacade.class",
            "ClassBinary.class",
            "ClassDependencies.class",
            "ByteCodeEngineeringService.class",
            "ClassesSourceService.class",
            "GraphService.class",
            "VisibleForTesting.class"));
  }

  @Test
  void providesClassesFromJar() {
    // given
    File pathToJar = Toolbox.getTestResource("citadel.jar");

    // when
    Collection<byte[]> actualClasses = testee.getClasses(pathToJar);

    // then
    assertThat(actualClasses, iterableWithSize(10));
    actualClasses.forEach(
        byteCode ->
            assertThat(
                byteCode.length,
                greaterThanOrEqualTo(181))); // the tiniest class is compiled to 181 bytes
  }

  @Test
  void resolvesOwnClassesJar() {
    // given
    // in tests, it resolved to the build folder
    // but the rest is tested without any assumptions

    // when
    File myOwnJar = testee.getMyOwnJar();

    // then
    assertThat(
        "Actual path: " + myOwnJar,
        myOwnJar.toPath().endsWith("build/classes/java/main"),
        is(true));
  }
}
