package StepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class Definition {
 
    public static WebDriver driver;
 
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Lenovo\\Desktop\\cc\\chromedriver.exe");
 
        driver = new ChromeDriver();
 
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
 
    @Given("User is on zooplus page")
    public void userOnHomePage() {
 
        driver.get("https://www.zooplus.com/checkout/cartEmpty.htm");
    }
    
    @Then("User accept the cookies in popup")
    public void acceptCookies() {
    	try {
    		click(driver.findElement(By.xpath("//div[@class='ot-sdk-container']//button[text()='Agree and continue']")));
    	}catch(Exception e) {
    		System.out.println("Cookies pop up did not show up");
    	}
    }
    
    @Then("Add {int} item in the recommendations to cart")
    public void addRec2Cart(int count) throws InterruptedException {
    	int counts = 0;
    	while(counts < count)
    	{
    		List<WebElement> btns = driver.findElements(By.xpath("//div[@class='lSSlideWrapper usingCss']/ul/li/div//button/*[local-name()='svg']"));
        	for(WebElement ele : btns)
        	{
        		try {
        		click(ele);
        		counts++;
        		break;
        		}catch(Exception e) {
        			
        		}
        	}
    	}
    }
 
    @When("Check the url contains {string}")
    public void entersUsername(String urlText) throws InterruptedException {
    	System.out.println(driver.getCurrentUrl());
    	assertThat(driver.getCurrentUrl(), containsString(urlText));
 
    }
 
    @When("User enters password as {string}")
    public void entersPassword(String passWord) throws InterruptedException {
 
        System.out.println("Password Entered");
        driver.findElement(By.name("txtPassword")).sendKeys(passWord);
        driver.findElement(By.id("btnLogin")).submit();
    }
    
    @When("{string} the {string} Priced Item")
    public void incrementDecrement(String incDec , String lowHigh) throws InterruptedException {
    	Thread.sleep(2000);
    	List<Float> prices =  priceSortPrint();
    	Float priceChoose;
    	if(lowHigh.toLowerCase().equals("lowest"))
    		priceChoose= prices.get(prices.size()-1);
    	else
    		priceChoose= prices.get(0);
    	String btnName = "";
    	if(incDec.toLowerCase().equals("increase"))
    		btnName = "increaseQuantityBtn";
    	else if(incDec.toLowerCase().equals("delete"))
    		btnName = "removeArticleBtn";
    	else
    		btnName = "reduceQuantityBtn";
    	WebElement incDecBtn = driver.findElement(By.xpath("//span[contains(text(),'"+priceChoose.toString()+"')]/ancestor::div[@role='row']//form//button[@data-zta='"+btnName+"']"));
    	click(incDecBtn);
       
    }
 
    @Then("Sort the prices in descending order")
    public List<Float> priceSortPrint() throws InterruptedException {
    	List<Float> prices = new LinkedList<Float>();
        List<WebElement> cartRows = driver.findElements(By.xpath("//div[@class='cart__table two__column']/div[@role='row']/div[@class='price']//span[@data-zta='articlePrice']"));
        for(WebElement cartrow : cartRows)
        {
        	prices.add(Float.parseFloat(cartrow.getText().trim().split("€")[1]));
        }
        System.out.println("Prices before sort \n"+prices.toString());
        Collections.sort(prices,Collections.reverseOrder());
        System.out.println("Prices after sort \n"+prices);
        return prices;
    }
    
    @Then("Check SubTotal and Total are correct")
    public void totalCheck() throws InterruptedException  {
    	List<Float> prices =  priceSortPrint();
    	Float subTotal = Float.parseFloat("0");
    	Float Total = Float.parseFloat("0");
    	for(Float cost : prices)
    		subTotal+=cost;
    	String shipping = driver.findElement(By.xpath("//span[@data-zta='shippingCostValueOverview']")).getText().trim();
    	Float ShippingCost;
    	if(!shipping.equals("Free"))
    		ShippingCost = Float.parseFloat(shipping.split("€")[1]);
    	else
    		ShippingCost = (float) 0;
    	Total = subTotal + ShippingCost;
    	System.out.println("SubTotal : \t"+subTotal+"\n Total : \t"+ Total);
    	assert(driver.findElement(By.xpath("//span[@data-zta='overviewSubTotalValue']")).getText().trim().split("€")[1].equals(subTotal.toString()));
    	assert(driver.findElement(By.xpath("//span[@data-zta='total__price__value']")).getText().trim().split("€")[1].equals(Total.toString()));
    }
    
    @Then("Change the shipping country to {string} and postcode as {string}")
    public void changeShipping(String country, String postcode) throws InterruptedException  {
    	click(driver.findElement(By.xpath("//span[@data-zta='shippingCountryName']")));
    	click(driver.findElement(By.xpath("//*[@id=\"tippy-1\"]/div/div[1]/div/div[2]/form/div/button")));
    	click(driver.findElement(By.xpath("//*[@data-label='"+country+"']")));
    	driver.findElement(By.xpath("//*[@id=\"tippy-1\"]//form/input")).sendKeys(postcode);
    	click(driver.findElement(By.xpath("//*[@id=\"tippy-1\"]//form/button")));
    	Float ShippingCost = Float.parseFloat(driver.findElement(By.xpath("//span[@data-zta='shippingCostValueOverview']")).getText().trim().split("€")[1]);
    	System.out.println("Shipping Cost : \t"+ShippingCost);
    }
 
    public void click(WebElement element) throws InterruptedException {
    	element.click();
    	Thread.sleep(2000);
    }
    
    @After
    public void teardown() {
 
        driver.quit();
    }
 
}