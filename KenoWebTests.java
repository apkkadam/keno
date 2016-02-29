import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;


public class KenoWebTests {

	private WebDriver driver;
	private String baseURL;
	
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		
		// Instead of https, if http is given, it will automatically redirect to https  
		baseURL = "https://keno.com.au?jurisdiction=ACT";
		driver.get(baseURL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void testScenarios() {
		checkGameGuideNameAndSize();	
		findYourLocalPub("Keno");
	}
	
	public void checkGameGuideNameAndSize() {		
		// Navigate to the "How to play" section
		driver.findElement(By.xpath("//p//a[@href='/how-to-play']")).click();
		
		// Scrolling the web page is not required for this test. Can be skipped by commenting it out.
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,400)", "");
		
		// Find the name of the pdf
		WebElement name = driver.findElement(By.xpath("//div[@class='details']//div"));
		String pdfName = name.getText();
		
		// Find the size of the pdf
		WebElement size = driver.findElement(By.xpath("//div[@class='details']//small"));
		String pdfSize = size.getText();

		// Assert that both pdf name and pdf size matches the expectation
		Assert.assertEquals(pdfName, "Game Guide.pdf");
		Assert.assertEquals(pdfSize, "3.3MB");
	}
	
	public void findYourLocalPub(String name) {		
		// Navigate to the "Find your local" section
		driver.findElement(By.xpath("//p//a[@ui-sref='venue-finder']")).click();
		
		// Get rid of the share location prompt
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
		
		// Click the button "Pub" and type in the name to search for
		driver.findElement(By.xpath("//button[contains(.,'Pub')]")).click();
		driver.findElement(By.id("venue-search")).sendKeys(name);
		
		// Locate and assert the search results returned with the expected results
		WebElement searchResultOne = driver.findElement(By.xpath("//*[contains(text(),'Statesman Hotel')]"));
		String venueOne = searchResultOne.getText();
		
		WebElement searchResultTwo = driver.findElement(By.xpath("//*[contains(text(),'Fyshwick Tavern')]"));
		String venueTwo = searchResultTwo.getText();
		
		Assert.assertEquals(venueOne, "Statesman Hotel");
		Assert.assertEquals(venueTwo, "Fyshwick Tavern");
	}
	
	@After
	public void tearDown() throws Exception {
		// Added the sleep so that we can see the search results for 2 seconds
		Thread.sleep(2000);
		driver.close();
	}
}
