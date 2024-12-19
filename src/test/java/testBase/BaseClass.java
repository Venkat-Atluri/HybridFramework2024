package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {
	
	public static WebDriver driver;
    public Logger logger;
    public Properties p;
    public ChromeOptions chromeOptions;
    public FirefoxOptions firefoxOptions;
    public EdgeOptions edgeOptions;
	
	@SuppressWarnings("deprecation")
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setUp(String os, String br) throws IOException {
		
		//Loading config.properties file
		FileReader file=new FileReader("./src//test//resources//config.properties");
		p=new Properties();
		p.load(file);
		
		logger=LogManager.getLogger(this.getClass()); //gets log4j2.xml file into 'logger' variable
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote")) {
			
			MutableCapabilities capabilities=new MutableCapabilities();
			
			//os
			if(os.equalsIgnoreCase("windows")) {
				capabilities.setCapability("platformName", "Windows 10");
			}else if(os.equalsIgnoreCase("mac")) {
				capabilities.setCapability("platformName", "Mac OS");
			}else if(os.equalsIgnoreCase("linux")) {
				capabilities.setCapability("platformName", "Linux");
			}else {
				System.out.println("No matching os...");
				return;
			}
			
			//browser
			switch(br.toLowerCase()) {
			case "chrome": capabilities.setCapability("browserName", "chrome"); break;
			case "firefox": capabilities.setCapability("browserName", "firefox"); break;
			case "edge": capabilities.setCapability("browserName", "MicrosoftEdge"); break; //sometimes fails to load because of browser start-up failure
			default: System.out.println("No matching browser..."); return;
			}
			
			URI gridURI=URI.create("http://localhost:4444/wd/hub");
			
			driver=new RemoteWebDriver(gridURI.toURL(), capabilities);

		}
		
		if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
			
			switch(br.toLowerCase()) {
			case "chrome": driver=new ChromeDriver(); break;
			case "edge": driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default: System.out.println("invalid browser name..."); return;
			}	
		}
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL2"));
		driver.manage().window().maximize();
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void tearDown() {
		driver.quit();
	}
	
	@SuppressWarnings("deprecation")
	public String randomString() {
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	@SuppressWarnings("deprecation")
	public String randomNumber() {
		String generatedString=RandomStringUtils.randomNumeric(10);
		return generatedString;
	}
	
	@SuppressWarnings("deprecation")
	public String randomAlphaNumeric() {
		String generatedString=RandomStringUtils.randomAlphabetic(3);
		String generatedNumber=RandomStringUtils.randomNumeric(3);
		return (generatedString+"@"+generatedNumber);
	}
	
	public String captureScreen(String tname) throws IOException {
		
		String timeStamp=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot ts=(TakesScreenshot)driver;
		File sourceFile=ts.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\"+tname+"_"+timeStamp+".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
		
		return targetFilePath;
	}

}
