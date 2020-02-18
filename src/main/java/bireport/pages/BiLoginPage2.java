package bireport.pages;

import org.openqa.selenium.By;

import bireport.pages.BiHomePage3;
import utils.BasePage1;

/**
 * Login page to BI launch pad (site is used to download BI Report)
 * @author Kim Nguyen
 *
 */

public class BiLoginPage2 extends BasePage1 {

	private By system = By.xpath("//*[@id='_id0:logon:CMS']");
	private By username = By.xpath("//*[@id='_id0:logon:USERNAME']");
	private By password = By.xpath("//*[@id='_id0:logon:PASSWORD']");
	private By logOnBttn = By.xpath("//*[@id='_id0:logon:logonButton']");
	
	/***
	 * Log into the web site with the specified credentials.
	 * 
	 * @param username
	 * @param pw
	 * @return HomePage
	 */
	public BiHomePage3 procedureLogin(String system, String username, String pw) {
		refreshPage();
		this.setSystem(system);
		this.setLoginId(username);
		this.setPassword(pw);
		this.clickLogin();
		return new BiHomePage3();
	}
	
	/***
	 * Specify the system
	 * @param sys
	 */
	public void setSystem(String sys) {
//		if (!driver.findElement(system).getAttribute("value").toString().equals(sys)) {
			driver.findElement(system).clear();
			driver.findElement(system).sendKeys(sys);
//		}
	}
	
	/***
	 * Specify the login ID
	 * @param un
	 */
	public void setLoginId(String un) {
//		if (!driver.findElement(username).getAttribute("value").toString().equals(un)) {
			driver.findElement(username).sendKeys(un);
//		}
	}

	/***
	 * Specify the password
	 * @param pw
	 */
	public void setPassword(String pw) {
		driver.findElement(password).sendKeys(pw);
	}

	/***
	 * Click the Login button.
	 */
	public void clickLogin() {
		driver.findElement(logOnBttn).click();
	}
}
