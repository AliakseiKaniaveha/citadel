package kaniaveha.aliaksei.citadel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppWebController {

  @Autowired private AppRestController appRestController;

  @GetMapping("/")
  public String index(Model model) {
    return "index";
  }

  @GetMapping("/classesGraph")
  public String graph(Model model) {
    model.addAttribute("graphContent", appRestController.getClassesGraphVisualisation());
    return "classesGraph";
  }
}
