package citadel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppWebController {

  @Autowired private AppRestController appRestController;

  @GetMapping("/")
  public String index(Model model) {
    return "index";
  }

  @GetMapping("/classesGraph")
  public String graph(
      @RequestParam(required = false, defaultValue = "DOT") String engine,
      @RequestParam(required = false, defaultValue = "SVG") String format,
      Model model) {
    model.addAttribute(
        "graphContent", appRestController.getClassesGraphVisualisation(engine, format));
    return "classesGraph";
  }
}
