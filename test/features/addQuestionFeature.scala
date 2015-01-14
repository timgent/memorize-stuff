package features

/**
 * Created by timmeh on 12/01/15.
 */
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.openqa.selenium._
import org.openqa.selenium.firefox._
import java.util.concurrent.TimeUnit
import org.scalatest.selenium
import play.api.test.Helpers

class addQuestionFeature extends FeatureSpec with GivenWhenThen {
  val port = Helpers.testServerPort
  implicit val driver = new FirefoxDriver
  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)
  scenario("A user is able to add pairs of questions and answers") {
    Given("A user is on the home page")
    driver.get(s"http://localhost:$port/")

    When("And they navigate to the add question page")
    val inputElement = driver.findElement(By.name("Create Question")).click()

    Then("A user is able to add pairs of questions and answers")
    assert(driver.findElementByLinkText("Add Question and Answer").isDisplayed())
    driver.close()
  }

}
