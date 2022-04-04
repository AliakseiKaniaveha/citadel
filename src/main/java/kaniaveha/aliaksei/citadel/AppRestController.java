package kaniaveha.aliaksei.citadel;

import kaniaveha.aliaksei.citadel.facade.ClassesGraphFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {

  @Autowired private ClassesGraphFacade classesGraphFacade;

  @GetMapping("/classes-graph/visualisation")
  public String getClassesGraphVisualisation(
      @RequestParam(required = false, defaultValue = "DOT") String engine,
      @RequestParam(required = false, defaultValue = "SVG") String format) {
    return classesGraphFacade.visualiseOwnClasses(engine, format);
  }
}
