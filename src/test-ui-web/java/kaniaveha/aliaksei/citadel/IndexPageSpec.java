package kaniaveha.aliaksei.citadel;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class IndexPageSpec {

  @Test
  void indexPageShowsExpectedContent() {
    // when: index page loaded
    open("/");

    // then: it has logo
    assert $(By.id("logo")).isImage() : "Logo is not properly displayed";

    // and: it has a link to github
    assert $(By.cssSelector("[href=\"https://github.com/AliakseiKaniaveha/citadel\"]"))
        .isDisplayed();

    // and: it has a link to readme
    assert $(By.cssSelector(
            "[href=\"https://github.com/AliakseiKaniaveha/citadel/blob/master/README.md\"]"))
        .isDisplayed();

    // and: it has a link to classes graph
    assert $(By.cssSelector("[href=\"/classesGraph?engine=circo\"]")).isDisplayed();
  }

  @Test
  void allLinksShouldBeOpenedInNewTab() {
    // when: index page loaded
    open("/");

    // then: it should have links to readme
    ElementsCollection links = $$(By.cssSelector("a[href]"));
    links.shouldHave(CollectionCondition.sizeGreaterThan(3));

    // and: each link has to be opened in new browser tab
    links
        .asFixedIterable()
        .forEach(
            selenideElement -> selenideElement.shouldHave(Condition.attribute("target", "_blank")));
  }
}
