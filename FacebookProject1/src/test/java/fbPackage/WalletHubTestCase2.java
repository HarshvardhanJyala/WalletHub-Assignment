package fbPackage;

import java.io.IOException;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class WalletHubTestCase2 extends TestBase {

	@Test(enabled = true)
	public void walletHubReview() throws InterruptedException, IOException {

		System.out.println(" Wallet Hub Test");
		driver.get(config.getProperty("walletSiteURL"));
		driver.manage().window().maximize();
		waitUntilPresent("loginBtn_XPATH");
		click("loginBtn_XPATH");
		waitUntilPresent("user_XPATH");
		enterText("user_XPATH", excel.getCellData("fbtest", 3, 2));
		enterText("pwd_XPATH", excel.getCellData("fbtest", 4, 2));
		// click("uncheckToggleBtn_XPATH");
		Thread.sleep(4000);
		click("loginSubmitBtn_XPATH");
		driver.navigate().to(config.getProperty("walletSiteURL"));
		hitTheReviewBtn();
		System.out.println("----");
		hoverAndHightlightCheck("ratingBoxes_CSS");
		click("drpDwnSelect_XPATH");
		click("drpDnHealthInsSelect_XPATH");
		click("writeReview_XPATH");
		enterText2("writeReview_XPATH");
		click("submitBtn_XPATH");

	}

	/*
	 * for Below @Test Mark (enabled = false) while running first-time because after
	 * submission Application sends a verification email to your registered email
	 * id which is a manual step You need to then come to application again to
	 * verify your review has been posted.
	 */
	
	@Test(enabled = false)
	public void postedReviewValidation() throws IOException, InterruptedException {

		driver.get(config.getProperty("walletSiteURL"));
		driver.manage().window().maximize();

		boolean vValidateUsr = validateUsr("validateUsr_XPATH");
		while (vValidateUsr == false) {
			System.out.println("Inside while loop vValidateUsr...");
			waitUntilPresent("loginBtn_XPATH");
			click("loginBtn_XPATH");
			waitUntilPresent("user_XPATH");
			enterText("user_XPATH", excel.getCellData("fbtest", 3, 2));
			enterText("pwd_XPATH", excel.getCellData("fbtest", 4, 2));
			Thread.sleep(4000);
			click("loginSubmitBtn_XPATH");
			vValidateUsr = true;

		}

		/*
		 * ##### This message appears after post but there are discrepancies in the application so DOESNOT WORK most of time ### 
		 * 
		 * String postConfirmatinText=getStringText("reviewConfirmation_XPATH"); 
		 * verifyEquals("Awesome!",postConfirmatinText);
		 */

		driver.navigate().to(config.getProperty("walletSiteURL"));
		String reviewText = driver.findElement(By.xpath(OR.getProperty("yourReview_XPATH"))).getText();
		verifyEquals("Your Review", reviewText);

	}

}
