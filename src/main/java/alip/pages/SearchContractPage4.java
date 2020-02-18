package alip.pages;

import org.openqa.selenium.By;

import alip.pages.ContractWelcomePage5;
import utils.BasePage1;

/**
 * For anything related to Search Contract Number page
 * @author Kim Nguyen
 */
public class SearchContractPage4 extends BasePage1 {
	
	private By policyNumber = By.id("searchText"); //enter policy number into this text field
	private By searchButton = By.id("searchButton"); //click on search button
	
	/**
	 * Enter policy number and search
	 * @param Polnumber
	 * @return ContractWelcomePage5
	 */
	public ContractWelcomePage5 procedureSearchForPolicy(String Polnumber) {
		this.clearPolicyNumber();
		this.setPolicyNumber(Polnumber);
		this.clickSearchButton();
	
		return new ContractWelcomePage5();
	}
	
	/**
	 * Clear the policy text field/box
	 */
	public void clearPolicyNumber() {
		driver.findElement(policyNumber).clear();
	}
	
	/**
	 * Enter policy number into search contract text box field
	 * 
	 * @param number
	 */
	public void setPolicyNumber(String number) {
		driver.findElement(policyNumber).sendKeys(number);
	}
	
	/**
	 * Click the search button
	 */
	public void clickSearchButton() {
		driver.findElement(searchButton).click();
	}
}
