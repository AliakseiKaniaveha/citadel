package kaniaveha.aliaksei.citadel.service;

import kaniaveha.aliaksei.citadel.VisibleForTesting;
import net.lingala.zip4j.ZipFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Service
public class ClassesSourceService {

  public File getMyOwnJar() {
    try {
      return new File(
          getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    } catch (URISyntaxException e) {
      throw new IllegalStateException();
    }
  }

  /**
   * @return own classes of this application - <strong>without</strong> any libraries
   */
  public Collection<byte[]> getClasses(File pathToJar) {
    return getClassFiles(pathToJar).stream().map(this::getBytes).toList();
  }

  @VisibleForTesting
  Collection<Path> getClassFiles(File jar) {
    try {
      String unzipDest = Files.createTempDirectory("myOwnClasses").toString();
      new ZipFile(jar).extractAll(unzipDest);
      return Files.walk(Paths.get(unzipDest)).filter(path -> hasExtension(path, ".class")).toList();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private byte[] getBytes(Path path) {
    try {
      return Files.readAllBytes(path);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private boolean hasExtension(Path myOwnClassesLocation, String suffix) {
    return myOwnClassesLocation.toString().endsWith(suffix);
  }
}
