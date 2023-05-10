package cdwpom;

import java.io.IOException;

import org.apache.xmlbeans.impl.tool.XSTCTester.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class AccountPayablePage extends BaseExecutionClass
	{
		WebDriverHelper wdHelper;
		ReusableUtility reusableUtility;
		AccountPayablePageComponents accountPayablePageComponents;
		
		
		/*
		 * constructor for initialization
		 */
		public AccountPayablePage(TestCase currentTestCase) throws IOException
		{
			super(currentTestCase);
			wdHelper = new WebDriverHelper(driver, report, dataHelper, currentTestCase.getBrowser());
			reusableUtility = new ReusableUtility(driver, report, wdHelper, dataHelper);
			accountPayablePageComponents = new AccountPayablePageComponents(report,dataHelper,wdHelper,driver, reusableUtility);
		}

		public AccountPayablePage(RemoteWebDriver driver, Report report, WebDriverHelper wdHelper, DataHelper dataHelper,
				ReusableUtility reusableUtility ) throws IOException
		{
			this.wdHelper = wdHelper;
			this.report = report;
			this.dataHelper = dataHelper;
			this.driver = driver;
			this.reusableUtility = reusableUtility;
		}
			
		/*
		 * Method to navigate to Account Paayble view page and verify Credit/debit payment option's element functinality.
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayable_CreaditDebitCard1() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			wdHelper.enableRadioButton(AccountPayablePOM.creditCardPamentRadioBtn);
			wdHelper.switchToNewWindow();
			wdHelper.clickElement(AccountPayablePOM.acceptBtn);
			if(wdHelper.isElementClickable(AccountPayablePOM.cardNumberTxtFld)&
		    	wdHelper.isElementClickable(AccountPayablePOM.nameTxtFld)&
					wdHelper.isElementClickable(AccountPayablePOM.cardMonthTxtFld)&
					wdHelper.isElementClickable(AccountPayablePOM.cardYearTxtFld)&
					wdHelper.isElementClickable(AccountPayablePOM.cardnickNameTxtFld)&
					wdHelper.isElementClickable(AccountPayablePOM.enterANewCardDrpDwn))
			{
		report.addReportStep("Verify all the elements functionality in credit/debit card payment window", "Verified all the elements functionality in credit/debit card payment window", StepResult.PASS);
			}
			else
			{
		report.addReportStep("Verify all the elements functionality in credit/debit card payment window", "Not Veri all the elements functionality in credit/debit card payment window", StepResult.FAIL);
			}
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Account Paayble view page and verify Credit/debit payment option's elements error message
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayable_CreaditDebitCard2() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			wdHelper.enableRadioButton(AccountPayablePOM.creditCardPamentRadioBtn);
			wdHelper.switchToNewWindow();
			wdHelper.clickElement(AccountPayablePOM.acceptBtn);
			wdHelper.sendKeys(AccountPayablePOM.cardNumberTxtFld, "34512");
			wdHelper.clickElement(AccountPayablePOM.cardYearTxtFld);
			String errorMessage = wdHelper.getText(AccountPayablePOM.errorMessageTxt);
			if(errorMessage.equalsIgnoreCase("Enter a valid credit card number"))
				{
			report.addReportStep("Verify if the Card Number field is highlighted and error message is displayed", "error message is verified", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Card Number field is highlighted and error message is displayed", "error message is not verified", StepResult.FAIL);
				}
			accountPayablePageComponents.navigateToAccountPayablePage();
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_MscCreditBalance1() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.mscCreditBalanceValueTxt))
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is not visible", StepResult.FAIL);
				}
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Accounts payable Page and verify the Msc Credit Balance and ShowAll, HideAll Buttons
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_MscCreditBalance2() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			//verifing apply credit balance and balance value
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.mscCreditBalanceValueTxt))
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is not visible", StepResult.FAIL);
				}
			//verifying ShowAll button 
			wdHelper.clickElement(AccountPayablePOM.showAllBtn);
			if(wdHelper.isElementPresent(AccountPayablePOM.paymentPageCreditNumberTxt))
				{
			report.addReportStep("Verify is ShowAll button is visible and invoice is expanded", "ShowAll button is visible and invoice is expanded", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify is ShowAll button is visible and invoice is expanded", "ShowAll button is not visible and invoice is not expanded", StepResult.FAIL);
				}
			//verifying HideAll Button
			wdHelper.clickElement(AccountPayablePOM.hideAllBtn);
			if(wdHelper.isElementNotPresent(AccountPayablePOM.paymentPageCreditNumberTxt))
			{
		report.addReportStep("Verify is HideAll button is visible and invoice is expanded", "HideAll button is visible and invoice is collapsed", StepResult.PASS);
			}
			else
			{
		report.addReportStep("Verify is HideAll button is visible and invoice is expanded", "HideAll button is visible and invoice is collapsed", StepResult.FAIL);
			}
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_MscCreditBalance3() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			//verifing apply credit balance and balance value
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.mscCreditBalanceValueTxt))
				{
			report.addReportStep("Verify if the 'Apply MSC balance' text and balance value text is visible", "'Apply MSC balance' text and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the 'Apply MSC balance' text and balance value text is visible", "'Apply MSC balance' text and balance value text is not visible", StepResult.FAIL);
				}
			//verifying ShowAll button and verify if the user is able to view the headers "Credit No.", "Credit Date", "Purchase Order No.", "Credit Amount"
			wdHelper.clickElement(AccountPayablePOM.showAllBtn);
			if(wdHelper.isElementPresent(AccountPayablePOM.paymentPageCreditNumberTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.paymentPageCreditDateTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.paymentPageCreditAmountTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.paymentPageCreditOrderNumberTxt))
				{
			report.addReportStep("verify if the user is able to view the headers Credit No., Credit Date, Purchase Order No., Credit Amount", "Credit No., Credit Date, Purchase Order No., Credit Amount are verified", StepResult.PASS);
				}
				else
				{
			report.addReportStep("verify if the user is able to view the headers Credit No., Credit Date, Purchase Order No., Credit Amount\"", "Credit No., Credit Date, Purchase Order No., Credit Amount are not verified", StepResult.FAIL);
				}
			driver.quit();	
		}

		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_MscCreditBalance4() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.mscCreditBalanceValueTxt))
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is not visible", StepResult.FAIL);
				}
			wdHelper.clickElement(AccountPayablePOM.showAllBtn);
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt))
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is not visible", StepResult.FAIL);
				}
			if(wdHelper.isElementPresent(AccountPayablePOM.applyMscCreditBalanceTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.mscCreditBalanceValueTxt))
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is visible", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Apply MSC balance and balance value text is visible", "Apply MSC balance and balance value text is not visible", StepResult.FAIL);
				}
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_PastDue1() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			if(wdHelper.isElementPresent(By.xpath("//p[contains(text(),'Below is a list of all open transactions on this account (Allow 24 hours for updates to reflect). You can pay open invoices online. Select the invoices you wish to pay and proceed.')]")))
			{
				report.addReportStep("'Below is a list of all open transactions on this account (Allow 24 hours for updates to reflect). You can pay open invoices online. Select the invoices you wish to pay and proceed.' text should visible under the Account Payabletext",
						"The text is present bellow the 'Account payable' text", StepResult.PASS);	
			}
			else
			{
				report.addReportStep("'Below is a list of all open transactions on this account (Allow 24 hours for updates to reflect). You can pay open invoices online. Select the invoices you wish to pay and proceed.' text should visible under the Account Payabletext",
						"The text is not present bellow the 'Account payable' text",StepResult.FAIL);		
			}
			if(wdHelper.isElementPresent(AccountPayablePOM.currentDueTxt)&&
					wdHelper.isElementPresent(AccountPayablePOM.PastDueTxt))
			{
				report.addReportStep("Verify if the user is able to view the 'Past Due' and 'Current Due' balance",
						             "current due and past due balance are verified", StepResult.PASS);
			}
			else {
				report.addReportStep("Verify if the user is able to view the 'Past Due' and 'Current Due' balance",
						             "current due and past due balance are not verified", StepResult.FAIL);
			}
			driver.quit();	
		}

		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_PastDue2() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			int invoiceDropdown = wdHelper.getElementsCount(null);
			if(invoiceDropdown == 2)
			{
				report.addReportStep("Verify if the user is able to view the 'Past Due' and 'Current Due' options in invoice dropdown",
						             "current due and past due options are verified", StepResult.PASS);
			}
			else {
				report.addReportStep("Verify if the user is able to view the 'Past Due' and 'Current Due' options in invoice dropdown",
						             "current due and past due options are not verified", StepResult.FAIL);
			}
			wdHelper.selectByVisibleText(AccountPayablePOM.inVoiceFilterDrpdwnr, "Past Due");
			driver.quit();	
		}

		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_PastDue3() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			wdHelper.selectByVisibleText(AccountPayablePOM.inVoiceFilterDrpdwnr, "Past Due");
			if(wdHelper.isElementPresent(AccountPayablePOM.invoiceNumberTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.invoiceDateTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.purchaseOrderNoTxt)&
					wdHelper.isElementPresent(AccountPayablePOM.invoiceAmountTxt))
			{
				report.addReportStep("Verify if the fields Invoice No., Invoice Date, Purchase Order No.,Invoice Amount fields are present",
						             "All the fields are verified", StepResult.PASS);
			}
			else {
				report.addReportStep("Verify if the fields Invoice No., Invoice Date, Purchase Order No.,Invoice Amount fields are present",
						             "'The fields are not verifie", StepResult.FAIL);
			}
			driver.quit();	
		}

		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_PastDue4() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			wdHelper.selectByVisibleText(AccountPayablePOM.inVoiceFilterDrpdwnr, "Past Due");
			if(wdHelper.isElementPresent(AccountPayablePOM.clockImg))
			{
				report.addReportStep("Verify if the user is able to view the 'Past Due' notification with clock symbol",
						             "'Past Due' notification with clock symbol is verified", StepResult.PASS);
			}
			else {
				report.addReportStep("Verify if the user is able to view the 'Past Due' notification with clock symbol",
						             "'Past Due' notification with clock symbol is not present", StepResult.FAIL);
			}

			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCeCom_Regression_AccountPayabl_CreditCardConvenienceFee1() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			accountPayablePageComponents.navigateToPaymentMethodPAge();
			wdHelper.enableRadioButton(AccountPayablePOM.creditCardPamentRadioBtn);
			wdHelper.switchToNewWindow();
			if(wdHelper.isElementPresent(By.xpath("//h2[contains(text(), 'Additional Card Payment Fee')]"), 20)&
					wdHelper.isElementPresent(By.xpath("//p[contains(text(), 'Dear Valued Customer,')]"), 20))
				{
			report.addReportStep("Verify if the Additional card Payment fee message,s header and body is displayed", "the Additional card Payment fee message,s header and body is displayed", StepResult.PASS);
				}
				else
				{
			report.addReportStep("Verify if the Additional card Payment fee message,s header and body is displayed", "the Additional card Payment fee message,s header and body is not displayed", StepResult.FAIL);
				}
			if(wdHelper.isElementClickable(AccountPayablePOM.acceptBtn)&
					wdHelper.isElementClickable(AccountPayablePOM.cancelBtn)&
					wdHelper.isElementClickable(AccountPayablePOM.closeBtn))
			{
		report.addReportStep("Verify if the accept, cancel and close(X) buttons are clickable", "Accept, cancel and close(X) buttons are clickable", StepResult.PASS);
			}
			else
			{
		report.addReportStep("Verify if the accept, cancel and close(X) buttons are clickable", "Accept, cancel and close(X) buttons are not clickable", StepResult.FAIL);
			}
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_CreditCardConvenienceFee2() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_CreditCardConvenienceFee3() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			driver.quit();	
		}
		
		/*
		 * Method to navigate to Order History Home Page and verify the AllOrders Tab in header Tabs
		 *TestCaseNo. QA_AO_001
		 * @author Arpitha
		 */
		public void eCom_Regression_AccountPayabl_CreditCardConvenienceFee4() throws Exception
		{
			reusableUtility.launchApplication();
			reusableUtility.login();
			accountPayablePageComponents.navigateToAccountPayablePage();
			driver.quit();	
		}


}
