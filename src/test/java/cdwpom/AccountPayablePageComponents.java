package cdwpom;

package com.jaf.reusablecomponents;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.jaf.enums.EnumClass.StepResult;
import com.jaf.helper.DataHelper;
import com.jaf.pageobjects.AccountPayablePOM;
import com.jaf.reports.Report;

public class AccountPayablePageComponents
{
	Report report;
	DataHelper dataHelper;
	WebDriverHelper wdHelper;
	RemoteWebDriver driver;
	ReusableUtility reusableUtility;
	
	public AccountPayablePageComponents(Report report, DataHelper dataHelper, WebDriverHelper wdHelper,
			RemoteWebDriver driver, ReusableUtility reusableUtility) throws IOException 
	{
		this.wdHelper = wdHelper;
		this.report=report;
		this.dataHelper=dataHelper;
		this.driver=driver;
		this.reusableUtility = new ReusableUtility(driver, report, wdHelper, dataHelper, 0);
	}


	/*
	 * Re-Usable method
	 * Method to navigate to Account Payable Home PAge
	 * @author Arpitha
	 */
	public void navigateToAccountPayablePage() throws Exception  
	{
       // wdHelper.moveToElement(AccountPayablePOM.accountImg);
	   // wdHelper.clickElement(AccountPayablePOM.accountSettingsLnk);
	    driver.get("https://qualityc.mscdirect.com/ui/myaccount/accountspayable");
	    wdHelper.waitForPageToLoad();
	    report.addReportStep("account payable home page should be display", "account payable home page is displayed", StepResult.PASS);
	    wdHelper.waitForElementPresent(By.xpath("//p[contains(text(), 'Current Due')]"));
	}
	
	/*
	 * Re-Usable method
	 * Method to navigate to Payment method page 
	 * @author Arpitha
	 */
	public void navigateToPaymentMethodPage() throws Exception  
	{
        wdHelper.moveToElement(AccountPayablePOM.invoiceTableBodyTbl);
	    wdHelper.checkBox(AccountPayablePOM.invoiceCheckBox);
	    wdHelper.clickElement(AccountPayablePOM.selectPaymentBtn);
	    wdHelper.waitForPageToLoad();
	    report.addReportStep("Payment method page should be display", "Payment method page is displayed", StepResult.PASS);
	}
	
	/*
	 * Re-Usable method
	 * Method to verify to credit/debit card payment radio button 
	 * @author Arpitha
	 */
	public void verifyNavigateToCreditDebitCardPament() throws Exception  
	{
		if(wdHelper.isElementPresent(AccountPayablePOM.creditCardPamentRadioBtn))
		{
			report.addReportStep("Verify credit/debit card payment radio button is present", "credit/debit card payment radio button is verified", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify credit/debit card payment radio button is present", "credit/debit card payment radio button is not verified", StepResult.FAIL);
				}
		wdHelper.enableRadioButton(AccountPayablePOM.creditCardPamentRadioBtn);
		wdHelper.switchToNewWindow();
		wdHelper.clickElement(AccountPayablePOM.acceptBtn);
	}

}
