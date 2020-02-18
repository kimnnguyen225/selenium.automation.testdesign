package alip.pages;

import org.openqa.selenium.By;

import alip.pages.SearchContractPage4;
import utils.BasePage1;

/**
 * For anything related to the ALIP website home page that's shown after login.
 * @author Kim Nguyen
 */
public class HomePage3 extends BasePage1 {
	
	private By searchContractNumber = By.id("a_contracts");
	private By homeMenu = By.xpath("//*[@id='a_home']");
	
	/**
	 * Click the "Contracts" link from menu
	 * @return SearchContractPage4
	 */
	public SearchContractPage4 clickContracts() {
		driver.findElement(searchContractNumber).click();
		return new SearchContractPage4();
	}
	
	public HomePage3 clickHome() {
		driver.findElement(homeMenu).click();
		return new HomePage3();
	}

}
