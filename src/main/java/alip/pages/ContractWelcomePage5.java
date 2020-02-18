package alip.pages;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import alip.pages.TransactionHistoryPage6;
import reports.Reports;
import utils.BasePage1;

/**
 * This page is shared across other testing pages once a policy number entered and contract information populated
 * This page only displays contract information details in a text block box to the top left of the policy
 * 
 * @author Kim Nguyen
 */

public class ContractWelcomePage5 extends BasePage1 {
	
	// Contract information to validate
	By contractNumber = By.xpath("//*[@id='ContractInfobox']/table/tbody/tr[1]/td[2]");
	By productName = By.xpath("//*[@id='ContractInfobox']/table/tbody/tr[2]/td[2]");
	// By policyStatus = By.xpath("//*[@id='ContractInfobox']/table/tbody/tr[4]/td[2]");
	// By insuredName = By.xpath("//*[@id='ContractInfobox']/table/tbody/tr[5]/td[2]");
	// By ownerName = By.xpath("//*[@id='ContractInfobox']/table/tbody/tr[6]/td[2]");
	
	//Transactions subMenu
	By TransactionSubMenu = By.xpath("//*[@id='subMenu']/ul/li[6]/a/span");
	
	/**
	 * Get Contract info details of the policy
	 * @return contract information details - Object[]
	 */
	public Object[] procedureGetContractInformationDetailsFromScreen() {
		Object[] details = new Object[2];
		
		details[0] = getContractNumberFromScreen();
		details[1] = getProductNameFromScreen();
		
		return details;
	}
	
	/**
	 * Get policy number
	 * @return String - Policy Number
	 */
	public String getContractNumberFromScreen () {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		return driver.findElement(contractNumber).getAttribute("textContent");
	}
	
	/**
	 * Get the product name
	 * @return String - Product Name
	 */
	String productNameToNumber;
	public String getProductNameFromScreen() {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		productNameToNumber = driver.findElement(productName).getText().trim();
		if (productNameToNumber.equals("Indexed UL")) {
			productNameToNumber = "004";
		} else if (productNameToNumber.equals("Virtus IUL II")) {
			productNameToNumber = "005";
		}
		return productNameToNumber;
	}
	
	/***
	 * Check whether the policy is displayed or not.
	 * @return true - if policy is shown
	 */
	public boolean policyIsShown() {
		boolean policyshown = false;
		
		try {
			if (driver.findElement(contractNumber).isDisplayed())
				policyshown = true;			
		} catch (Exception e) {
			// Do nothing, already set to false
		}
		
		return policyshown;
	}
	
	/***
	 * Compare the contract information box's details shown on the screen against what's shown in the database.
	 * @param expectedContractInformationBoxDetails
	 * @param actualContractInformationBoxDetails
	 */
	public void validateContractInformationBoxDetails(Object[] expectedContractInformationBoxDetails, Object[] actualContractInformationBoxDetails) {
		boolean errorsExist = false;
		
		Reports.logAMessage(LogStatus.INFO, "Validating Policy Number and Product Name");
		System.out.println("Validating Policy Number and Product Name");
		
		if(!arrayIsEmpty(expectedContractInformationBoxDetails)) {
			for (int j = 0; j <2; j++) {
				try {
					assertEquals(expectedContractInformationBoxDetails[j], actualContractInformationBoxDetails[j]);
				} catch (Error e) {
					errorsExist = true;
					System.out.println("***** MISMATCH on ALIP screen contract information box's details" 
										+ "\r\n expected: " + expectedContractInformationBoxDetails[j]
										+ "\r\n actual: " + actualContractInformationBoxDetails[j]);
					Reports.logAMessage(LogStatus.ERROR, "***** MISMATCH on ALIP screen contract information box's details" 
							+ "\r\n expected: " + expectedContractInformationBoxDetails[j]
							+ "\r\n actual: " + actualContractInformationBoxDetails[j]);
				}
			}
		} else {
			errorsExist = true;
			Reports.logAMessage(LogStatus.ERROR, "No contract information details was returned from the database, "
												+ "so screen values can't be compared against database values");	
			System.out.println("No contract information details was returned from the database," 
								+ "	so screen values can't be compared against database values");
		} 
		// ================================================================================================================================= //
		if (errorsExist) {
			Reports.logAMessage(LogStatus.FAIL, "Validation for policy's contract information details failed");
			System.out.println("Validation for policy's contract information details failed");
		}
		else {
			System.out.println("Validation for Policy Number and Product Name passed");
			Reports.logAMessage(LogStatus.PASS, "Validation for Policy Number and Product Name passed");			
		}
	}
	
	/**
	 * Click on Transactions subMenu
	 * @return Transaction History Page
	 */
	WebDriverWait wait = new WebDriverWait(driver, 10); 
	public TransactionHistoryPage6 clickTransactionSubMenu() {
		driver.findElement(TransactionSubMenu).click();
		return new TransactionHistoryPage6();
	}

}
