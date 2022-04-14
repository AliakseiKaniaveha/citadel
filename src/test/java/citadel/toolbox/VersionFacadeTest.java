package citadel.toolbox;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;

class VersionFacadeTest {

  public static final String MAJOR_MINOR_BUILD_PATTERN = "^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)?$";

  private final VersionFacade testee = new VersionFacade();

  @Test
  void returnsAppVersion() {
    assertThat(testee.getVersion(), matchesPattern(MAJOR_MINOR_BUILD_PATTERN));
  }

  @Test
  void versionIsInMajorMinorBuildForm() {
    assertThat("0.0.1", matchesPattern(MAJOR_MINOR_BUILD_PATTERN));
    assertThat("10.11.12", matchesPattern(MAJOR_MINOR_BUILD_PATTERN));
    assertThat("0.0.0.1", not(matchesPattern(MAJOR_MINOR_BUILD_PATTERN)));
    assertThat("0.0.x", not(matchesPattern(MAJOR_MINOR_BUILD_PATTERN)));
  }
}
