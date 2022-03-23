package kaniaveha.aliaksei.citadel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SmokeSpec {

  @BeforeAll
  static void beforeAll() {
    Configuration.baseUrl = "http://localhost:8080";
  }

  @Test
  void smokeTest() {
    // when landing page loaded
    open("/");

    // then link to graph displayed
    SelenideElement linkToGraph = $(By.id("navigate-to-graph"));

    // and it navigates to another page
    linkToGraph.click();
    SelenideElement graphPageContent = $(By.tagName("body"));
    graphPageContent.shouldHave(text("This is graph stub"));
  }
}
