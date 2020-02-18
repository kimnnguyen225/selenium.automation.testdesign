package alip.pages;

import org.openqa.selenium.By;

import alip.pages.HomePage3;
import utils.BasePage1;

/**
 * Login page to ALIP website
 * @author Kim Nguyen
 */
public class LoginPage2 extends BasePage1 {
	
	private By username = By.xpath("//input[@name='j_username']");
	private By password = By.xpath("//input[@name='j_password']");
	private By logInBttn = By.xpath("//input[@name='B12']");

	/***
	 * Log into the web site with the specified credentials.
	 * 
	 * @param username
	 * @param pw
	 * @return HomePage
	 */
	public HomePage3 procedureLogin(String username, String pw) {
		this.setLoginId(username);
		this.setPassword(pw);
		this.clickLogin();
		return new HomePage3();
	}
	
	/***
	 * Specify the login ID
	 * @param un
	 */
	public void setLoginId(String un) {
		driver.findElement(username).sendKeys(un);
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
		driver.findElement(logInBttn).click();
	}
}

