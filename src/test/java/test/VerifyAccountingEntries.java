package test;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import com.relevantcodes.extentreports.LogStatus;

import alip.pages.ContractWelcomePage5;
import alip.pages.HomePage3;
import alip.pages.LoginPage2;
import alip.pages.SearchContractPage4;
import alip.pages.TransactionHistoryPage6;
import reports.Reports;
import utils.AccountingEntries;
import utils.BaseTest;
import utils.TestData;
import utils.WebDriverUtils;

public class VerifyAccountingEntries extends BaseTest {
	//selenium is running in headless mode/browser --- disable this option in BaseTest on setChromeOptions method if needed
	
	// credential for T4 region
	 String username = "R53";
	 String password = "@Alip123";
	 
	 //date
	 String currentCycleDate;
	 String cycleDateALIPScreen;
	 String currentProcDate;
	 
	 //policies
	 ArrayList<String> policies;
	 String policyNumber = "";
	 boolean policyFailed = false;
	 boolean errorsExist = false;
	 
	 // policy info - validate policy number and product name
	 Object[] contractDetailsFromScreen;
	 Object[] contractDetailsFromDB;
	 
	 // transaction
	 ArrayList<Integer> transNumbers;
	 Integer transNumber;
	 Boolean transactionHistoryIsPresent = true;
	 
	 // accounting entries
	 ArrayList<AccountingEntries> accountingEntriesFromScreen;
	 ArrayList<AccountingEntries> accountingEntriesFromDB;
	 
	 WebDriverUtils util = new WebDriverUtils(getWebDriver());
	 
	 static TransactionHistoryPage6 transactionHistoryPage;
		
	 @Test
	 public void VerifyAccountingData() throws Exception {

		 TestData testData = new TestData(); //queries to get data from DB
		 currentCycleDate = testData.getCurrentCycleDate(); // 8 pm
		 cycleDateALIPScreen = testData.getCycleDateForALIPScreen(); // format mm/dd/yyyy
		 currentProcDate = testData.getCurrentProcDate(); // 12 am
		 policies = testData.getAllPoliciesFromDB();
		 Reports.logAMessage(LogStatus.INFO, "Database connected successfully");
		 System.out.println("Database connected successfully");
		 
		 if(!(policies.size() <= 0)) { //ArrayList of string
			 
			//Login
			System.out.println("*** Log into ALIP website ***");
			Reports.logAMessage(LogStatus.INFO, "*** Log into ALIP website ***");
			LoginPage2 loginPage = new LoginPage2();
			HomePage3 homePage = loginPage.procedureLogin(username, password);
			System.out.println("*** Logged-in Successfully ***");
			Reports.logAMessage(LogStatus.PASS, "*** Logged-in Successfully ***");
				
			for (int i = 128; i < policies.size(); i++) {
				//after debug restart from 130th policy
					policyNumber = policies.get(i);
					Reports.logAMessage(LogStatus.INFO, "=======================================");
					Reports.logAMessage(LogStatus.INFO, "Policy to validate: " + policyNumber);
					System.out.println("======================================");
					System.out.println("--------------------------------\r\n"
							         + "|                              |\r\n"
							         + "| Policy to validate: " + policyNumber+ " |\r\n"
							         + "|                              |\r\n"
							         + "--------------------------------");
					
					// Account Welcome page - click on Contracts menu link
					SearchContractPage4 searchContractPage = homePage.clickContracts();
					
					// Admin Search Contracts page - enter policy number and click Search buton
					ContractWelcomePage5 contractWelcome = searchContractPage.procedureSearchForPolicy(policyNumber);
					
					// Validate policy info (policy number and product name)
					contractDetailsFromScreen = new Object[2];
					contractDetailsFromScreen = contractWelcome.procedureGetContractInformationDetailsFromScreen();
					contractDetailsFromDB = testData.getContractInformationBoxDetailsFromDB(policyNumber);
					
					// Compare screen values against database values contract details
					contractWelcome.validateContractInformationBoxDetails(contractDetailsFromDB, contractDetailsFromScreen);
					// Take a screenshot of the contract details
					//Reports.addScreenshotToReport(getWebDriver(), "2-ContractDetails");
	
					// click on Transactions subMenu link to get to Transaction History page
					transactionHistoryPage = contractWelcome.clickTransactionSubMenu();
					
					transNumbers = testData.getTransNumbersOfEachPolicyFromDB(policyNumber);
					Reports.logAMessage(LogStatus.PASS, "Successfully retrieved " + transNumbers.size() + " transaction numbers to test");
					Reports.logAMessage(LogStatus.PASS, "Transactions:\r\n" + transNumbers);
					System.out.println("Successfully retrieved " + transNumbers.size() + " transaction numbers to test"
										+ "\r\nTransactions: \r\n" + transNumbers);
					
					List<WebElement> totalTransRowsOnScreen = transactionHistoryPage.getTotalTransactionsOnScreen();
					System.out.println(totalTransRowsOnScreen.size());
					
					// Take a screenshot of the displayed policy
					Reports.addScreenshotToReportNoTimestamp(getWebDriver(), policyNumber + "_TransactionHistoryPage", policyNumber);
					
					for (int index = 0; index < transNumbers.size(); index++) {
						transNumber = transNumbers.get(index);
						 //get a single transaction's accounting entries from DB
						Reports.logAMessage(LogStatus.INFO, "Transaction number to validate: " + transNumber);
						System.out.println("Transaction number to validate: " + transNumber);
						
					
						//transNumber = testData.getTransNumberFromDB(transNumbers);
						accountingEntriesFromDB = new ArrayList<AccountingEntries>();
						accountingEntriesFromDB = testData.getAcctgEntriesForOneTransFromDB(policyNumber, transNumber);
						//Reports.addScreenshotToReport(getWebDriver(), "3-AccountingEntries");
						
						// *********************************************************************
						//debugging to filter transaction processed date that not match with current cycle date
			//			transactionHistoryPage.filterTransToMatchCycleDate(cycleDateALIPScreen, index+1);
						// *********************************************************************
						
						// validate accounting entries from ALIP screen against what are shown in DB Staging
						transactionHistoryPage.processTransactionOnScreen(transNumbers, accountingEntriesFromDB, totalTransRowsOnScreen, index + 1, cycleDateALIPScreen);
						
						// Take a screenshot of the displayed GL
						Reports.addScreenshotToReportNoTimestamp(getWebDriver(), policyNumber + "_" + transNumber, policyNumber);	
						
						transactionHistoryPage.clickBackButton();
						System.out.println(accountingEntriesFromDB);
						accountingEntriesFromDB.clear();
						System.out.println(accountingEntriesFromDB);
						//Reports.addScreenshotToReport(getWebDriver(), "3-AccountingEntries");
						System.out.println("End");
					}
					transNumbers.clear();
				}
		}
	}
}
