package alip.pages;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.relevantcodes.extentreports.LogStatus;

import reports.Reports;
import utils.AccountingEntries;
import utils.BasePage1;

public class TransactionHistoryPage6 extends BasePage1 {
	
	static Actions actions = new Actions(driver);
	
	// Filter Transaction status to de-select all and select processed checkbox from ALIP website
	By transStatusFilter = By.xpath("//*[@id='filterBy']");
	By selectAllCheckBox = By.xpath("//*[@id='statusContainer']/div/label[1]/input");
	By processedCheckBox = By.xpath("//*[@id='statusContainer']/div/label[5]/input");
	
	// Filter transaction type to remove calendar processing date and calendar anniversary from ALIP website
	By transTypeFilterMultiSelect = By.xpath("//*[@id='transType']");
	By calendarAnniversaryCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[7]/input");
	By calendarProcessingDateCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[8]/input"); 
	By dollarCostAverageCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[14]/input");
	By inforceRiderChangeCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[25]");
	By rateRenewalCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[37]/input");
	By riderTerminationCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[44]");
	By riderIssueCheckBox = By.xpath("//*[@id='transTypeContainer']/div/label[43]");
	
	// Filter time range to Year To Date
	By timeRangeMultiSelection = By.xpath("//*[@id='range']");
	By yearToDateOption4 = By.xpath("//*[@id='range']/option[4]");
	
	//Apply button
	By applyFilterButton = By.xpath("//*[@id='filterButton']/a");

	// transaction history (page) variables
	List<WebElement> totalTransRows;
	WebElement chevronIcon;
	WebElement transViewLedger;
	String processedDate;
	String transNum;
	By transHistoryTBodyXPath = By.xpath("//*[@id='accountTxnHistoryResults']/table/tbody"); 
	
	//accounting entries (page) variables
	ArrayList<AccountingEntries> accountingEntriesArr = new ArrayList<AccountingEntries>();
	boolean acctgEntryTablePresent;

	/**
	 * Filter to only show processed transactions for the time range year to date, exclude transaction types:
	 * Calendar Anniversary, Calendar Processing Date, Dollar Cost Average, Inforce Rider Change, Rate Renewal, Rider Issue, Rider Termination)
	 * because these transaction types don't have accounting entries to validate
	 */
	// Filter Transaction History Page
	public TransactionHistoryPage6() {
				// filter Transaction Status
				click(transStatusFilter);
				click(selectAllCheckBox);
				click(processedCheckBox);
				
				// filter Transaction Type
				click(transTypeFilterMultiSelect);
				click(calendarAnniversaryCheckBox);
				click(calendarProcessingDateCheckBox);
				click(dollarCostAverageCheckBox);
				click(inforceRiderChangeCheckBox);
				click(rateRenewalCheckBox);
				click(riderIssueCheckBox);
				click(riderTerminationCheckBox);
				
				// filter Time Range
				click(timeRangeMultiSelection);
				click(yearToDateOption4);
				
				// click on Apply button
				click(applyFilterButton);
				System.out.println("Filtered Transaction History Page Successed");
				Reports.logAMessage(LogStatus.INFO, "Filtered Transaction History Page Successed");
	}

	/**
	 * Determine whether transaction table are shown on the screen or not
	 * 
	 * @return true - if transaction number(s) are shown
	 */
	//By transHistoryTBodyXPath = By.xpath("//*[@id='accountTxnHistoryResults']/table/tbody"); 
	boolean transHistoryPresent = false;
	public boolean TransHistoryShownOnScreen() {
		boolean transHistoryAreShown = true;
		try {
			driver.findElement(transHistoryTBodyXPath);
		} catch (Exception e) {
			transHistoryAreShown = false;
		}
		return transHistoryAreShown;
	}
	
	/***
	 * Get the total number of transaction rows from the screen
	 * @return List<WebElement> - the total transaction rows
	 */
	//By transTbody = By.xpath("//*[starts-with(@id,'ProcessedTransactions')]");
	public List<WebElement> getTotalTransactionsOnScreen() {
		WebElement tBody = driver.findElement(transHistoryTBodyXPath);
		totalTransRows = tBody.findElements(By.tagName("tr"));
		return totalTransRows;
	}
	
	/***
	 * Process one transaction on the screen.
	 * @param accountingEntriesFromDB
	 * @param row
	 */
	int r;
	public void processTransactionOnScreen(ArrayList<Integer> transNumDB, ArrayList<AccountingEntries> accountingEntriesFromDB, List<WebElement> transationsfromScreen, int row, String dateToCompareTo) {
		
 		if(!TransHistoryShownOnScreen()) {
			Reports.logAMessage(LogStatus.ERROR, "There aren't any transactions on the screen to validate");
			System.out.println("There aren't any transactions on the screen to validate");
		} else {		
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			processedDate = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+row+"_ProcessDate')]")).getText().trim();
			transNum = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+row+"_Number')]")).getText().trim();
			chevronIcon = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+row+"_Actions')]"));
			for (r = 1; r < getTotalTransactionsOnScreen().size(); r++) {
				if (!processedDate.equals(dateToCompareTo)) {
					System.out.println("Row "+ r + " with " + transNum + " has processed date as " + processedDate + " and doesn't match cycle date " + dateToCompareTo);
					processedDate = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_ProcessDate')]")).getText().trim();
					transNum = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_Number')]")).getText().trim();
					chevronIcon = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_Actions')]"));
				} else if (processedDate.equals(dateToCompareTo)) {
					break;
				}
			}
			if (processedDate.equals(dateToCompareTo)) {
			System.out.println("Trans number to validate from screen: " + transNum);
			System.out.println("Row "+ r + " with " + transNum + " has processed date as " + processedDate + " and match cycle date " + dateToCompareTo);
			// int parsedIntTransNum = Integer.parseInt(transNum); //dbg
			
			// after clicked on chevron icon, wait 5 seconds for website to generate
			// viewLedgerPopupMenuList ul element
			chevronIcon.click(); 
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			transViewLedger = driver.findElement(By.xpath("//*[text()='View Ledger']"));
			transViewLedger.click();
			validateAccountingEntries(accountingEntriesFromDB);
			//clickBackButton();
			//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			}
		}
	}
	
//	public void filterTransToMatchCycleDate(String dateToCompareTo,  int row) {
//		for (int r = 1; r < getTotalTransactionsOnScreen().size(); r++) {
//			if (!processedDate.equals(dateToCompareTo)) {
//				System.out.println("Row "+ r + " with " + transNum + " has processed date as " + processedDate + " and doesn't match cycle date " + dateToCompareTo);
//				processedDate = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_ProcessDate')]")).getText().trim();
//				transNum = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_Number')]")).getText().trim();
//				chevronIcon = driver.findElement(By.xpath("//*[starts-with(@id,'ProcessedTransactions_"+(row+r)+"_Actions')]"));
//			} else if (processedDate.equals(dateToCompareTo)) {
//				break;
//			}
//		}
//	}
	
	/**
	 * Get accounting entries/general ledgers from screen and store into array list to compare data against database
	 * @return arraylist of accounting entries/general ledgers
	 */
	public ArrayList<AccountingEntries> getAccountingEntriesFromScreen() {
		List<WebElement> totalAEntriesRows;
		WebElement accountingEntriestBody = driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody"));
		WebElement spanTag;
		String spanTagStr;
		//WebElement aEntriesBackButton = driver.findElement(By.xpath("//*[@id='txnViewLedgerBckButton']/a"));
		
		if (!AccountingEntriesAreShownOnScreen()) {
			System.out.println("No accounting entries to validate");
			Reports.logAMessage(LogStatus.ERROR, "There isn't any accounting entry on screen to validate");
		} else {
			totalAEntriesRows = accountingEntriestBody.findElements(By.tagName("tr"));
			spanTag = driver.findElement(By.tagName("span"));
			
			// dynamic span tag injection
			 if(accountingEntriestBody.findElements(By.tagName("td")).contains(spanTag)) {
			         spanTagStr = "/span";
			    } else {
			    	spanTagStr = "";
			    }
			
			AccountingEntries accountingEntries;
			for (int entryRow = 1; entryRow <= totalAEntriesRows.size(); entryRow++) {
				accountingEntries = new AccountingEntries();
				
				accountingEntries.setAccountDesc(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+entryRow+"]/td[1]/span")).getText().trim());
				accountingEntries.setAccountType(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+entryRow+"]/td[2]"+spanTagStr+"")).getText().trim());
				accountingEntries.setAccountNum(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+entryRow+"]/td[3]"+spanTagStr+"")).getText().trim());
				accountingEntries.setDebitAmt(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+entryRow+"]/td[4]"+spanTagStr+"")).getText().trim());
				accountingEntries.setCreditAmt(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+entryRow+"]/td[5]"+spanTagStr+"")).getText().trim());
				accountingEntriesArr.add(accountingEntries);
			}
		}
		return accountingEntriesArr;
	}
	
	public void validateAccountingEntries(ArrayList<AccountingEntries> accountingEntriesFromDB) {
		String accountDesc = "Account Description";
		String accountDescFromDB;
		String accountDescFromScreen;
		String accountType = "Account Type";
		String accountTypeFromDB;
		String accountTypeFromScreen;
		String accountNum = "Account Number";
		String accountNumFromDB;
		String accountNumFromScreen;
		String debitAmt = "Debit Amount";
		String debitAmtFromDB;
		String debitAmtFromScreen;
		String creditAmt = "Credit Amount";
		String creditAmtFromDB;
		String creditAmtFromScreen;
		
		ArrayList<AccountingEntries> accountingEntriesFromScreen = getAccountingEntriesFromScreen();
		
		// Verify that the number of rows from the DB matches the number of rows from the screen
		try {
			assertEquals(accountingEntriesFromDB.size(), accountingEntriesFromScreen.size());
			System.out.println("number of rows from the database = " + 
					accountingEntriesFromDB.size() + " and number of rows from the screen = " + 
					accountingEntriesFromScreen.size());
			Reports.logAMessage(LogStatus.INFO, "number of rows from the database = " + 
					accountingEntriesFromDB.size() + " and number of rows from the screen = " + 
					accountingEntriesFromScreen.size());
		} catch (Error e) {
			Reports.logAMessage(LogStatus.ERROR, "***** MISMATCH in number of rows for accounting entries\r\n number of rows from the database = " 
					+ accountingEntriesFromDB.size() + " & number of rows from the screen = " + accountingEntriesFromScreen.size());
			System.out.println("===========================================================================================\r"
					+ "***** MISMATCH in number of rows for accounting entries\r\n number of rows from the database = " 
					+ accountingEntriesFromDB.size() + " & number of rows from the screen = " 
					+ accountingEntriesFromScreen.size()
					+ "\r===========================================================================================\r");
		}
		
		// Check accounting entries on the screen against what's in the database
		for (int dbRow = 0, screenRow = 0; dbRow < accountingEntriesFromDB.size() && screenRow < accountingEntriesFromScreen.size(); dbRow++, screenRow++) {
			if (screenRow == -1 || dbRow == -1) { // -1 means row not found
				System.out.println("No accounting entries for this transaction to validate");
				Reports.logAMessage(LogStatus.ERROR, "No accounting entries for this transaction to validate");
			} else {
				AccountingEntries aERowfromDB = accountingEntriesFromDB.get(dbRow);
				AccountingEntries aERowfromScreen = accountingEntriesFromScreen.get(screenRow);
				//===============================ACCOUNT DESCRIPTION===============================//
				accountDescFromDB = aERowfromDB.getAccountDesc().trim();
				accountDescFromScreen = aERowfromScreen.getAccountDesc().trim();
				try {
					if (accountDescFromDB.equals("NULL")) {
						accountDescFromDB = "Total";
					}
					assertEquals(accountDescFromDB, accountDescFromScreen);
				} catch (Error e) {
					printError(accountDesc, accountDescFromDB, accountDescFromScreen);
				}
				//===============================ACCOUNT TYPE: G====================================//
				accountTypeFromDB = aERowfromDB.getAccountType().trim();
				accountTypeFromScreen = aERowfromScreen.getAccountType().trim();
				try {
					if (accountTypeFromDB.equals("NULL")) {
						accountTypeFromDB = "";
					}
					assertEquals(accountTypeFromDB, accountTypeFromScreen);
				} catch (Error e) {
					printError(accountType, accountTypeFromDB, accountTypeFromScreen);
				}
				//========================ACCOUNT NUMBER: COMPOSITE ACCOUNT CODES=====================//
				accountNumFromDB = aERowfromDB.getAccountNum().trim();
				accountNumFromScreen = aERowfromScreen.getAccountNum().trim();
				try {
					if (accountNumFromDB.equals("NULL")) {
						accountNumFromDB = "";
					}
					assertEquals(accountNumFromDB, accountNumFromScreen);
				} catch (Error e) {
					printError(accountNum, accountNumFromDB, accountNumFromScreen);
				}
				//===============================DEBIT AMOUNT====================================//
				debitAmtFromDB = aERowfromDB.getDebitAmt().trim();
				debitAmtFromScreen = aERowfromScreen.getDebitAmt().trim();
				try { 
					if (debitAmtFromDB.equals("$0.00")) {
						debitAmtFromDB = "";
					}
					assertEquals(debitAmtFromDB, debitAmtFromScreen);
				} catch (Error e) {
					printError(debitAmt, debitAmtFromDB, debitAmtFromScreen);
				}
				//===============================CREDIT AMOUNT====================================//
				creditAmtFromDB = aERowfromDB.getCreditAmt().trim();
				creditAmtFromScreen = aERowfromScreen.getCreditAmt().trim();
				try {
					if (creditAmtFromDB.equals("$0.00")) {
						creditAmtFromDB = "";
					} 
					assertEquals(creditAmtFromDB, creditAmtFromScreen);
				} catch (Error e) {
					printError(creditAmt, creditAmtFromDB, creditAmtFromScreen);
				}
				
				System.out.println("***MATCHED***"
						+ "\r\n D-Base: " + aERowfromDB.getAccountDesc() 
						+ " | " + aERowfromDB.getAccountType()
						+ " | " + aERowfromDB.getAccountNum()
						+ " | " + aERowfromDB.getDebitAmt()
						+ " | " + aERowfromDB.getCreditAmt()
						+ "\r\n Screen: " + aERowfromScreen.getAccountDesc()
						+ " | " + aERowfromScreen.getAccountType()
						+ " | " + aERowfromScreen.getAccountNum()
						+ " | " + aERowfromScreen.getDebitAmt()
						+ " | " + aERowfromScreen.getCreditAmt()
						+ "\r-------------------------------------------------------------------------------------------"
						);
			}
				}
				accountingEntriesFromScreen.clear();
				accountingEntriesFromDB.clear();
	}

	/***
	 * Click on back button.
	 */
	//By txnBackButton = By.xpath("//*[@id='txnViewLedgerBckButton']/a");
	public void clickBackButton() {
        WebElement backButton = driver.findElement(By.xpath("//*[text()='Back']"));
        backButton.click();  	
	}
	
	// ===========================================================================================================//
	
	/***
	 * Determine if accounting entries table is available.
	 * @return boolean indicating whether there's an accounting entries table present
	 */
	public boolean isAccountingEntryTableAvailable() {
	acctgEntryTablePresent = isElementPresent(By.xpath("//*[@id='LedgerTable']/tbody"));
		return acctgEntryTablePresent;
	}

	/***
	 * Determine whether accounting entries are shown on the screen or not.
	 * 
	 * @return true - if accounting entries are shown
	 */
	public boolean AccountingEntriesAreShownOnScreen() {
		boolean acctgEntriesAreShown = true;			// Assume accounting entries are shown
		
		try {
			driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody"));
		} catch (Exception e) {
			acctgEntriesAreShown = false;
		}
		return acctgEntriesAreShown;
	}
}
