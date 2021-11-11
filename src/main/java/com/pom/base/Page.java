package com.pom.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.JavascriptExecutor;
import com.aventstack.extentreports.ExtentReports;
import com.pom.utilities.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Page {
	
	
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = LogManager.getLogger("devpinoyLogger");
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\com\\pom\\excel\\MyApp.xlsx");
	public static WebDriverWait wait;
	public static ExtentReports extent;
	public static String browser;
	public static Actions action ;
	
	public static JavascriptExecutor jse;
	
	
	
	

	@BeforeSuite
	public void setUp() throws IOException {

		if (driver == null) {

			fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\com\\pom\\properties\\Config.properties");
			config.load(fis);
			log.debug("Initializing Config file");
			fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\com\\pom\\properties\\OR.properties");
			OR.load(fis);
			log.debug("Initializing OR file");
		}
		
		//Jenkins setup
		if(System.getenv("browser")!=null && System.getenv("browser")!=null) {
			browser = System.getenv("browser");
		}else {
			browser = config.getProperty("browser");
		}
		
		config.setProperty("browser", browser);

		if (config.getProperty("browser").equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			log.debug("FireFox Browser launched");
		} else if (config.getProperty("browser").equals("chrome")) {
			
			Map<String,Object> prefs = new HashMap<String,Object>();
			prefs.put("profile.default_content_setting_values.notifications", 2);
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobars");
			
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			log.debug("chrome Browser launched");
		} else if (config.getProperty("browser").equals("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			log.debug("Edge Browser launched");
		}

		driver.get(config.getProperty("testsiteUrl"));
		log.debug("Navigated to : " + config.getProperty("testsiteUrl").toString());
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),
				TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
		wait.pollingEvery(Duration.ofSeconds(3));
		action = new Actions(driver);
		jse = (JavascriptExecutor)driver;
		
	}

	public void click(String locator) {
		if (locator.endsWith("_CSS")) {
			driver.findElement(ByCssSelector.cssSelector(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(ByXPath.xpath(OR.getProperty(locator))).click();
		}
		log.debug("Currently at webpage : "+driver.getTitle());

	}
	static WebElement dropdown;
	
	public void select(String locator , String value) {
		if (locator.endsWith("_CSS")) {
		dropdown = driver.findElement(ByCssSelector.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown =	driver.findElement(ByXPath.xpath(OR.getProperty(locator)));
		}

		log.debug("Currently at webpage : "+driver.getTitle());
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		Reporter.log("Selecting from dropdown :" +locator +"Value selected is : "+value);
	}

	public void type(String locator, String value) {
		if (locator.endsWith("_CSS")) {
			driver.findElement(ByCssSelector.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(ByXPath.xpath(OR.getProperty(locator))).sendKeys(value);
		}
		log.debug("Currently at webpage : "+driver.getTitle());
	}

	
	public WebElement locator(String locator) {
		
		WebElement ele = driver.findElement(ByXPath.xpath(OR.getProperty(locator)));
		return ele;
	}
	
	public boolean isElementPresent(By by) {

		try {

			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	
	public static void verifyEquals(String expected,String actual) {
		
		try {

			Assert.assertEquals(actual, expected);
		}catch(Throwable t) {
			Reporter.log("<br>"+"Verification failure"+t.getMessage()+"<br>");
			
		}
		log.debug("Currently at webpage : "+driver.getTitle());
		
	}
	
	
	@AfterSuite
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}

		log.debug("Test Execution Completed!");
	}
}
