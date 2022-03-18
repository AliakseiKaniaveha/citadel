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
    //when landing page loaded
    open("/");

    //then link to another page displayed
    SelenideElement linkToAnotherPage = $(By.linkText("another page"));

    //and it navigates to another page
    linkToAnotherPage.click();
    SelenideElement anotherPageContent = $(By.tagName("body"));
    anotherPageContent.shouldHave(text("Hello, world!"));
  }
}
