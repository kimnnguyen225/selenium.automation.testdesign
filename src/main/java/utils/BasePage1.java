package utils;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.relevantcodes.extentreports.LogStatus;
import reports.Reports;

/***
 * This is a base class that other pages will inherit/extend from.
 * @author Kim Nguyen
 */
public class BasePage1 {
	
	protected static WebDriver driver;
	private WebDriverWait wait;
	
	/***
	 * Set the web driver
	 * @param driver
	 */
	@SuppressWarnings("static-access")
	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}

	/***
	 * Get the actual title of the page that's shown (on tab)
	 * @return String - the title of the page
	 */
	public String getPageTitle() {
		return driver.getTitle();
	}
	
	/**
	 * Click on element
	 * @param element
	 */
	public void click(By element) {
		driver.findElement(element).click();
	}
	
	/**
	 * print error to the console and to the report
	 * @param desc
	 */
	public void printError(String desc, String accountDescDB, String accountDescScreen) {
		System.out.println("*****MISMATCH with "+desc+ ": "
				+ "\r\n Expected account desc from DB: " + accountDescDB
				+ "\r\n Actual account desc on Screen: " + accountDescScreen);
		Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH-1***** in accounting entries: "
				+ accountDescScreen 
				+ "\r\n Expected: " + accountDescDB
				+ "\r\n Actual: " + accountDescScreen);
	}
	
	/***
	 * Refresh the page.
	 */
	public void refreshPage() {
		driver.navigate().refresh();
	}
	
	/***
	 * Check if the specified element is present
	 * @param element - the locator for the element
	 * @return boolean - indicating whether the specified element is shown or not
	 */
	protected boolean isElementPresent(By element) {
		        
        if (driver.findElements(element).isEmpty()) {
        	return false;
        } else {
        	return true;
        }
    }
	
	/***
	 * Check whether the URL has an error in it or not
	 * @return boolean - whether the url has an error in it or not
	 */
	public boolean urlHasAnError() {
		String url = driver.getCurrentUrl();
		boolean error = false;
		
		if (url.contains("ERROR"))
			error = true;
		return error;
	}
	
	/***
	 * Check if the specified array is empty or not
	 * @param arr
	 * @return true if empty
	 */
	protected boolean arrayIsEmpty(Object arr[]) {
        boolean empty = true;
        
        for (Object obj : arr) {
        	if (obj != null) {
        		empty = false;
        		break;
        	}
        }
       	return empty;
    }
	
	/***
	 * Wait the specified amount of time for the expected element to display
	 * @param locator - for example, pass locator in this format: By.id("username")
	 * @param timeInSeconds
	 */
	protected void waitForElementToDisplay(By locator, int timeInSeconds) {
		wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/***
	 * Wait for the expected page to display
	 * @param expectedTitle
	 * @param timeInSecs
	 */
	public void WaitForExpectedPageToDisplay(String expectedTitle, int timeInSecs) {
		String actualTitle = getPageTitle();
		int count = 0;
		
		while (!actualTitle.equals(expectedTitle) && count < timeInSecs) {
			actualTitle = driver.getTitle();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			count++;
		}
	}

	/***
	 * Verify that the page shown is the expected page
	 * @param expectedTitle
	 */
	protected void VerifyExpectedPageIsShown(String expectedTitle) {
		String actualTitle = getPageTitle();
		
		if (!expectedTitle.equals(actualTitle))
			Reports.logAMessage(LogStatus.FAIL, "The actual page title '" + actualTitle + "' is not equal to the expected page title '" + expectedTitle + "'");
		
		assertEquals(expectedTitle, actualTitle);
	}
	
	/***
	 * Verify that the URL doesn't have an error.
	 */
	protected void VerifyUrlDoesntHaveAnError() {
		if (urlHasAnError())
			Reports.logAMessage(LogStatus.ERROR, "BasePage: URL has an error");
	}

}
