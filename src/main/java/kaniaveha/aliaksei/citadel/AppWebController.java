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

  /** just for testing, will be removed soon */
  @GetMapping("/anotherPage")
  public String anotherPage(
      @RequestParam(name = "name", required = false, defaultValue = "World") String name,
      Model model) {
    model.addAttribute("name", name);
    return "anotherPage";
  }
}
