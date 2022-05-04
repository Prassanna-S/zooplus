package runner;
 
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
 
@CucumberOptions(tags = "", features = "src/test/resources/features/login.feature", glue = "StepDefinition")
 
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {
 
}