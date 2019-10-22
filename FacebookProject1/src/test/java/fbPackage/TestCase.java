package fbPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TestCase extends TestBase {
	
	/*
	 * Use the Runner - to execute the script.
	 * All Locators are kept on Configuration File
	 * Picking test data from the excel sheet
	 * Mark @Test(enabled = true or false) based on what you want to run at a time
	 */
	
	@Test(enabled = true)
	public void fbLoginTest() throws Exception {
		
		System.out.println("Facebook Test");
		driver.get(config.getProperty("testSiteURL"));
		driver.manage().window().maximize();
		enterText("username_CSS", excel.getCellData("fbtest", 0, 2));
		enterText("password_CSS", excel.getCellData("fbtest", 1, 2));
		Thread.sleep(4000);
		click("submitBtn_CSS");
		verifyElementIsSelected("newsFeed_CSS");
		WebElement elementY = driver.findElement(By.xpath(OR.getProperty("WhatsOnYourMind2_XPATH")));
		hoverOver(elementY);
		enterText("WhatsOnYourMind0_CSS",excel.getCellData("fbtest", 2, 2));
		click("postbtn_CSS");
		verifyWebElements(excel.getCellData("fbtest", 2, 2),OR.getProperty("verifyPostMsg"));
		
	}
	
	

}
