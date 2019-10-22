package fbPackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestBase {

	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\fbTestData.xlsx");
	public static String browser;
	public static WebElement dropdown;
	public static WebDriverWait wait;
	public static Actions action;

	@BeforeSuite
	public void setUp() throws InterruptedException {

		if (driver == null) {

			try {
				fis = new FileInputStream(System.getProperty("user.dir")
						+ "\\src\\test\\resources\\configurationFile\\Config.properties");
				config.load(fis);

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(System.getProperty("user.dir")
						+ "\\src\\test\\resources\\configurationFile\\ObjectRepo.properties");
				OR.load(fis);

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {

				browser = System.getenv("browser");
			} else {
				browser = config.getProperty("browser");
			}
			config.setProperty("browser", browser);

			if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\chromedriver.exe");

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-notifications");
				driver = new ChromeDriver(options);
			} else {
				System.out.println("execution will happen in chrome browser only, Kinldy run in chrome only..");
			}
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),
					TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, 30);
		}
	}

	@AfterSuite
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	/*
	 * ******************************** UTILITIES FUNCTIONS
	 * ************************************************ BELOW ::>
	 */

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void click(String locator) {
		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
			System.out.println("Clicked the Element successfully : " + locator);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
			System.out.println("Clicked the Element successfully : " + locator);
		}
	}

	public void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				System.out.println("Clicking on element with using java script click");

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				System.out.println("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to click on element " + e.getStackTrace());
		}
	}

	public static void hoverOver(WebElement locator) {
		action = new Actions(driver);
		try {
			action.moveToElement(locator).click().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enterText(String locator, String value) {
		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		}
	}

	public void select(String locator, String value) {
		if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
		}
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
	}

	public static String getStringText(String locator) {
		try {
			String strText = driver.findElement(By.xpath(OR.getProperty(locator))).getText();
			System.out.println("GetText is : " + strText);
			return strText;
		} catch (Exception e) {
			e.getMessage();
			return "";
		}
	}

	public static String getTextFromElement(String locator) {
		try {
			WebElement gText = getWebElement(locator);
			String strgText = gText.getText();
			return strgText;
		} catch (Exception e) {
			System.out.println("Make sure you have chosen only xpath for this element");
			return "";
		}
	}

	public static WebElement getWebElement(String elementXpath) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXpath)));
		WebElement oElement = driver.findElement(By.xpath(elementXpath));
		if (oElement == null) {
			System.out.println("Not able to convert this element to WebElement..");
		}
		return oElement;
	}

	public static void verifyWebElements(String expected, String actual) throws IOException {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(actual)));
		List<WebElement> Act = driver.findElements(By.xpath(actual));
		System.out.println(" Total Feed counts are : " + Act.size());
		System.out.println(" Text Value of (Expected :" + expected);
		for (WebElement obj : Act) {
			String gText = obj.getText();
			System.out.println("Text value is(ActualValue) : " + gText);
			if (gText.equalsIgnoreCase(expected)) {
				System.out.println(" Post message validated successfully");
			} else {
				System.out.println(" UNABLE to Validate Post Message");
			}
		}
	}

	public static void verifyEquals(String expected, String actual) throws IOException {
		try {
			System.out.println("ExpectedMsg : " + expected);
			System.out.println("ActualMsg : " + actual);
			Assert.assertEquals(actual, expected);
			System.out.println("Post Msg Verification Passed");
		} catch (Throwable t) {
			t.getStackTrace();
			System.out.println("Post msg Verification Failed");
		}
	}

	public static boolean handleAlert() {
		boolean presentFlag = false;
		try {
			// Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			presentFlag = true;
			System.out.println("Alert text is : " + alert.getText());
			alert.accept();
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
			System.out.println("Alert is NOT present!!");
		}
		return presentFlag;
	}

	public static boolean verifyElementIsSelected(String locator) {
		boolean presentFlag = false;
		try {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).isSelected();
			presentFlag = true;
		} catch (NoSuchElementException e) {
			System.out.println("News Feed tab is not selected by default..");
			presentFlag = false;
		}
		return presentFlag;
	}

	public static void hitTheReviewBtn() {
		try {
			List<WebElement> allhref = driver.findElements(By.tagName("a"));
			for (WebElement each : allhref) {
				String s = each.getText() + "::" + each.getAttribute("href");
				if (s.contains("reviews")) {
					System.out.println(" HREF Links for review " + s);
					each.click();
					break;
				}
			}
		} catch (ElementNotInteractableException e) {
			e.getMessage();
			System.out.println("Problem in hitTheReviewBtn..");
		}
	}

	public static void enterText2(String locator) {
		try {
			String str1 = " This Review is automatically genereted via script....Wallet Hub is now Rated to FOUR STAR ...!!!!!!!..............................";
			String str2 = " WAIT A SECOND .. you heard is right..This Review is automatically genereted via script....Wallet Hub is now Rated to FOUR STAR ...";
			String str = str1.concat(str2);
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void hoverAndHightlightCheck(String locator) {
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(OR.getProperty(locator))));
			WebElement element = driver.findElement(By.cssSelector(OR.getProperty(locator)));
			System.out.println(element);
			Thread.sleep(5000);
			List<WebElement> ratingboxes = element.findElements(By.tagName("svg"));
			for (int i = 0; i < ratingboxes.size(); i++) {
				System.out.println("Number: " + i + " " + ratingboxes.get(i));
				if (i == 3) {
					System.out.println("fourth: " + i + " " + ratingboxes.get(i));
					// Mouse hover, then check hightlighted and then click
					Actions action = new Actions(driver);
					action.moveToElement(ratingboxes.get(i)).click();
					System.out.println("what happened here :: jvaScript executer");
					((JavascriptExecutor) driver).executeScript("return window.getSelection().toString();");
					String color = ratingboxes.get(i).getCssValue("color");
					System.out.println(color);
					String backcolor = ratingboxes.get(i).getCssValue("background-color");
					System.out.println(backcolor);
					if (!color.equals(backcolor)) {
						System.out.println("Text is highlighted!");
					} else {
						System.out.println("Text is not highlighted!");
					}
					action.perform();
					String backcolor2 = ratingboxes.get(i).getCssValue("background-color");
					System.out.println(backcolor2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void waitUntilPresent(String locator) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(locator))));
			System.out.println("Locator found : " + locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateUsr(String locator) {
		boolean presentFlag = false;
		try {
			String vUser = getStringText(locator);
			if (vUser.startsWith("Har")) {
				System.out.println("Auto-Logged-in, No need to login again");
				presentFlag = true;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return presentFlag;
	}

}
