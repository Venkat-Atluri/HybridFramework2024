package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
	
	WebDriver driver;
	
	//constructor for all page object classes
	public BasePage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}

}
