package citadel.toolbox;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VersionFacade {

  /**
   * @return application version in form major.minor.build, like 0.0.1.
   */
  public String getVersion() {
    List<String> lines =
        new BufferedReader(
                new InputStreamReader(
                    ClassLoader.getSystemClassLoader().getResourceAsStream("version")))
            .lines()
            .collect(Collectors.toList());
    return Toolbox.extractSingleton(lines, "Version file must have exactly one line");
  }
}
