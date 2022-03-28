package kaniaveha.aliaksei.citadel;

import kaniaveha.aliaksei.citadel.facade.ClassesGraphFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppRestController {

  @Autowired private ClassesGraphFacade classesGraphFacade;

  @GetMapping("/classes-graph/visualisation")
  public String getClassesGraphVisualisation() {
    return classesGraphFacade.visualiseOwnClasses("SVG");
  }
}
