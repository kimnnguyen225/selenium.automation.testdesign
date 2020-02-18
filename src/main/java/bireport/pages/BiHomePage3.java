package bireport.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import utils.BasePage1;

/**
 * Search for BI Report to download
 * @author Kim Nguyen
 *
 */
public class BiHomePage3 extends BasePage1 {

	private By alipTestingFolder = By.xpath("//*[@id='ListingURE_detailView_listColumn_0_0_1']"); //double click to open
	private By testInForceReportsFolder = By.xpath("//*[@id='ListingURE_detailView_listColumn_0_0_1']"); //double click to open
	private By alipDailyAccountingReportInForceTestFile = By.xpath("//*[@id='ListingURE_detailView_listColumn_7_0_1']"); // double click to open
	private By exportIcon = By.xpath("//*[@id='IconImg__dhtmlLib_309']"); // click to open Export window
	private By fileTypeDropDownMenu = By.xpath("//*[@id='fileTypeList']"); //change file type from PDF to XLSX
	private By optionExcelXLSX = By.xpath("//*[@id='fileTypeList']/option[2]");
	private By okBttnToDownloadReport = By.xpath("//*[@id='Btn_OK_BTN_idExportDlg']"); // click on OK button to download report
	// after click on OK button, wait 1 minute for the site to load BI Report to download to local machine
	
	
	/**
	 * double clicks on folder named "ALIP Testing"
	 * double clicks on folder named "TEST - In Force Reports"
	 * double clicks on file named "ALIP Daily Accounting Report - In Force - TEST"
	 * wait 10-15 seconds for the excel spreadsheet to load and to display
	 * click on export icon, new Export window popup
	 * change file type from PDF to XLSX
	 * click OK button to download BI Report for current cycle of current release
	 * wait 1 minute for site to load BI Report to be able to save to local machine
	 * 
	 * specify saved-to folder for BI Report to be saved to
	 */
	public void downloadBIReport() {
		Actions actions = new Actions(driver);
		driver.findElement(alipTestingFolder);
		actions.doubleClick((WebElement) alipTestingFolder).perform();
		driver.findElement(testInForceReportsFolder);
		actions.doubleClick((WebElement) testInForceReportsFolder).perform();
		driver.findElement(alipDailyAccountingReportInForceTestFile);
		actions.doubleClick((WebElement) alipDailyAccountingReportInForceTestFile).perform();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(exportIcon).click();
		driver.findElement(fileTypeDropDownMenu).click();
		driver.findElement(optionExcelXLSX).click();
		driver.findElement(okBttnToDownloadReport).click();
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		
		
		String path = "G:\\Release 9\\Cycle 4 -12-31-2021\\Accounting"; //Chrome appends \Default to the options path
		//ChromeOptions options = ((ChromeOptions) driver).ChromeOptions();
		//options.addArguments("user-data-dir="+path);
//		options.set_preference("browser.download.folderList", 2)
//		options.set_preference("browser.download.manager.showWhenStarting", False)
//		options.set_preference("browser.download.dir", 'PATH TO DESKTOP')
//		options.set_preference("browser.helperApps.neverAsk.saveToDisk", "application/x-gzip")
//
//		driver = webdriver.Firefox(firefox_options=options)
//		driver.get("Name of web site I'm grabbing from")
//		driver.find_element_by_xpath("//a[contains(text(), 'DEV.tgz')]").click()
	}
}
