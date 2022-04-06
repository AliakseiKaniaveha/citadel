package kaniaveha.aliaksei.citadel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;

public class ClassesGraphSpec {

  @BeforeAll
  static void beforeAll() {
    Configuration.baseUrl = "http://localhost:8080";
  }

  @Test
  void classesGraphVisualized() {
    // when landing page loaded
    open("/");

    // then link to graph displayed
    SelenideElement linkToGraph = $(By.id("navigate-to-classes-graph"));

    // and it navigates to classes graph
    linkToGraph.click();
    switchTo().window("Citadel: Classes Graph");

    // and classes graph is a SVG
    SelenideElement svgTag = $(By.id("svg"));
    svgTag.shouldHave(cssClass("svg"));

    SelenideElement gTag = svgTag.find(By.id("graph0"));
    gTag.shouldHave(cssClass("graph"));
    assert gTag.getTagName().equals("g");
  }
}
