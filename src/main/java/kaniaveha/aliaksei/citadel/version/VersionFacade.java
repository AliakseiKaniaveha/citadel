package kaniaveha.aliaksei.citadel.version;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class VersionFacade {

  /**
   * @return application version in form major.minor.build, like 0.0.1.
   */
  public String getVersion() {
    try {
      return new BufferedReader(
              new InputStreamReader(
                  ClassLoader.getSystemClassLoader().getResourceAsStream("version")))
          .readLine();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
