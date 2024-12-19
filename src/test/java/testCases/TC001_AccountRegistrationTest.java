package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass{
	
	@Test(groups= {"Regression","Master"})
	public void verify_account_registration() {
		
		try {
		logger.info("*********Starting TC001_AccountRegistrationTest***********");
		
		HomePage hp=new HomePage(driver);
		logger.info("********Clicked on Account*********");
		hp.clickMyAccount();
		logger.info("********Clicked on Register*********");
		hp.clickRegister();
		
		AccountRegistrationPage regPage=new AccountRegistrationPage(driver);
		
		regPage.setFirstName(randomString().toUpperCase());
		regPage.setLastName(randomString().toUpperCase());
		regPage.setEmail(randomString()+"@gmail.com");
		regPage.setTelephone(randomNumber());
		
		String password=randomAlphaNumeric();
		
		regPage.setPassword(password);
		regPage.setConfirmPassword(password);
		
		regPage.setPrivacyPolicy();
		regPage.clickContinue();
		
		logger.info("********Validating expected message*********");
		String confmsg=regPage.getConfirmationMsg();
		if(confmsg.equals("Your Account Has Been Created!")) {
			Assert.assertTrue(true);
		}else {
			logger.error("Test failed...");
			logger.debug("Debug logs...");
			Assert.assertTrue(false);
		}
		
		}
		catch(Exception e) {
			Assert.fail();
		}
		
		logger.info("*********Finished TC001_AccountRegistrationTest***********");
	}

}
