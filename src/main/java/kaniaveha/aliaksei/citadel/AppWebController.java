package kaniaveha.aliaksei.citadel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppWebController {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("attribute", "value");
    return "index";
  }

  @GetMapping("/graph")
  public String graph(
      @RequestParam(name = "graphContent", required = false, defaultValue = "graph stub")
          String name,
      Model model) {
    model.addAttribute("graphContent", name);
    return "graph";
  }
}
