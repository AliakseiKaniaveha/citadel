package kaniaveha.aliaksei.citadel;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class IndexPageSpec {

  @Test
  void logoDisplayed() {
    // when: index page loaded
    open("/");

    // then: logo image rendered
    assert $(By.id("logo")).isImage() : "Logo is not properly displayed";
  }

  @Test
  void linksToReadmeShouldBeOpenedInNewTab() {
    // given: reference to the README defined
    String referenceToReadme = "https://github.com/AliakseiKaniaveha/citadel/blob/master/README.md";

    // when: index page loaded
    open("/");

    // then: it should have links to readme
    ElementsCollection linksToReadme = $$(By.cssSelector("[href*=\"" + referenceToReadme + "\"]"));
    linksToReadme.shouldHave(CollectionCondition.size(5));

    // and: each link has to be opened in new browser tab
    linksToReadme
        .asFixedIterable()
        .forEach(
            selenideElement -> selenideElement.shouldHave(Condition.attribute("target", "_blank")));
  }
}
