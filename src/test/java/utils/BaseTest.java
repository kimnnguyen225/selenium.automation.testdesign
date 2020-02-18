package utils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.relevantcodes.extentreports.LogStatus;

import reports.Reports;

public class BaseTest {
	protected static String currentTest;
	protected static String website;
	
	protected static WebDriver webdriver = null;
	protected static BasePage1 basePage;
	
	protected static String expectedPage = null;
	private static String APP_URL = "";
	private static final String ALIPT4 = "http://tst-alpt4.corp.onfs.com/FrontOffice/en/Login.jsp"; // ALIPTest4
	private static final String BI_REPORT = "http://qa-boe/BOE/portal/1507250825/InfoView/logon.faces"; //user: testqa pass: Testqa123
																										// System: QA-BOESVR.CORP.ONFS.COM:7400
	
	// Flags
		protected Boolean debug = false;
		
		@Rule
	    public final TestName testName = new TestName();
	    
		/***
		 * Get the browser to use in testing and open the specified browser.
		 */
		@BeforeClass
		public static void setup() {
			// Get the browser to use in testing
			System.setProperty("testBrowser", "chrome");
			
			// Get the browser to use in testing
			String browserToUse = System.getProperty("testBrowser");
			
			// Open the specified browser
			openBrowser(browserToUse);
		}
		
		/***
		 * Launch the application, maximize the browser, delete cookies, and
		 * instantiate/set up the base page.
		 */
	    @Before 
	    public void launchApplication() throws Exception {
			currentTest = testName.getMethodName();
		    
			// Start the report
			Reports.startReportAndOverwrite(currentTest, "G:\\Accounting_Automation_Results\\");

			// Start the test log
			Reports.startLogger(currentTest);
		    
			// Set the URL to use
			setAppUrl();

			// Navigate to the URL
			webdriver.get(APP_URL); //ALIPT4 region
			
			// Maximize the browser
			webdriver.manage().window().maximize();

			// Delete all cookies
			webdriver.manage().deleteAllCookies();

			basePage = new BasePage1();
			basePage.setWebDriver(webdriver);
	    }
	    
	    /***
	     * Get the website to be used in testing.
	     * @return String
	     */
	    public static String getWebsite() {
	    	return website;
	    }
	    
		public static void setAppUrl() {			
			if (currentTest.contains("Accounting")) {
				APP_URL = ALIPT4;
				
				if (APP_URL.toLowerCase().contains("t2")) {
					website = "ALIPTEST2";
				} else if (APP_URL.toLowerCase().contains("t4")) {
					website = "ALIPTEST4";
				}
				else {
					Reports.logAMessage(LogStatus.ERROR, "BaseTest: The APP_URL doesn't indicate which environment to run on");
				}
			}
			else if (currentTest.contains("BiReport")) {
				APP_URL = BI_REPORT;
			}
			else
			{
				Reports.logAMessage(LogStatus.ERROR, "BaseTest: Test script name doesn't contain an indicator of which website to use, please add one of these to the test script name: Accounting, BiReport");
			}
		}

		/***
		 * Opens and initiates the specified browser
		 * @param browserType
		 */
		public static void openBrowser(String browserType) {
			if ("chrome".equals(browserType.toLowerCase())) {
				setChromeDriverProperty();
				ChromeOptions options = setChromeOptions();
				webdriver = new ChromeDriver(options);
			} else {
				System.out.println("Browser type " + browserType + " hasn't been handled yet.");
				Reports.logAMessage(LogStatus.ERROR, "Browser type " + browserType + " hasn't been handled yet.");
			}
		}
		
		/***
		 * Close the browser.
		 */
		@AfterClass
		public static void closeBrowser() {
			// End the report
			Reports.endReport();
			
			if (webdriver != null) {
			webdriver.quit();
			webdriver = null;
			}
		}

		/***
		 * Get the web driver
		 * @return WebDriver
		 */
		public static WebDriver getWebDriver() {
			return webdriver;
		}
		
		/***
		 * Set path to Chrome driver.
		 */
		public static void setChromeDriverProperty() {
			System.setProperty("webdriver.chrome.driver", "C:\\automation_new_code\\onfs.alip.accounting\\BrowserDriver/chromedriver.exe");
		}
		
		/***
		 * Initialize the Chrome driver.
		 * If doesn't want Selenium to take focus/physical mouse, then uncomment --headless argument
		 */
		public static ChromeOptions setChromeOptions() {
			ChromeOptions cOptions = new ChromeOptions();
			cOptions.addArguments("disable-infobars");
			//cOptions.addArguments("--headless", "window-size=1920,1080");
			cOptions.addArguments("--disable-notifications");
			return cOptions;
		}
}
