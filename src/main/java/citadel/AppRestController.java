package citadel;

import citadel.graph.ClassesGraphFacade;
import citadel.toolbox.VersionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {

  @Autowired private VersionFacade versionFacade;
  @Autowired private ClassesGraphFacade classesGraphFacade;

  @GetMapping("/rest/v1/version")
  public String getVersion() {
    return versionFacade.getVersion();
  }

  @GetMapping("/rest/v1/classes-graph/visualisation")
  public String getClassesGraphVisualisation(
      @RequestParam(required = false, defaultValue = "DOT") String engine,
      @RequestParam(required = false, defaultValue = "SVG") String format) {
    return classesGraphFacade.visualiseOwnClasses(engine, format);
  }
}
