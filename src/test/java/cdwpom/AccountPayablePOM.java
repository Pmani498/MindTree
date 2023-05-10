package cdwpom;

import org.openqa.selenium.By;

public class AccountPayablePOM {
	public static By accountImg = By.xpath("//div[@id='main-account-logged-in']");
	public static By accountSettingsLnk = By.xpath("//span[contains(text(),'Account Settings')]");

	//Account Payable home page header
	public static By currentDueTxt = By.xpath("//p[contains(text(), 'Current Due')]");
	public static By PastDueTxt = By.xpath("//p[contains(text(), 'Past Due')]");
	public static By availableMscCreditTxt = By.xpath("//p[contains(text(), 'Available MSC Credit')]");
	public static By viewCreditLnk = By.xpath("//a[contains(text(), 'View Credits ')]");
	public static By inVoiceFilterDrpdwnr = By.xpath("//div[@class='MuiInputBase-root MuiOutlinedInput-root invoice-filter MuiInputBase-fullWidth']");
	public static By currentDueBtn = By.xpath("//span[contains(text(), 'Current Due')]");
	public static By PastDueBtn = By.xpath("//span[contains(text(), 'Past Due')]");
	public static By viewAllBtn = By.xpath("//span[contains(text(), 'All ')]");
	public static By clockImg = By.xpath("//i[@class ='fas fa-clock orange-color ml-1']");
	public static By selectPaymentBtn = By.xpath("//div[contains(text(),'Select Payment')]");
	public static By invoiceTableBodyTbl = By.xpath("//tbody[@class='MuiTableBody-root css-1xnox0e']");
	public static By invoiceCheckBox = By.xpath("//td[@class='MuiTableCell-root']");
	
	//in invoice Table
	public static By invoiceNumberTxt = By.xpath("//p[contains(text(),'Invoice No.')]");
	public static By invoiceDateTxt = By.xpath("//p[contains(text(),'Invoice Date')]");
	public static By purchaseOrderNoTxt = By.xpath("//p[contains(text(),'Purchase Order No.')]");
	public static By invoiceAmountTxt = By.xpath("//p[contains(text(),'Invoice Amount')]");

	//payment method page 
	public static By bankAccountPamentRadioBtn = By.xpath("//input[@value='Bank Account Payment' and @ type='radio']");
	
	//mSC Creddit Balance related
	public static By applyMscCreditBalanceTxt  = By.xpath("//h4[contains(text(),'Apply MSC Credit Balance')]");
	public static By mscCreditBalanceValueTxt = By.xpath("//p[contains(text(),'available')]");
	public static By showAllBtn = By.xpath("//p[contains(text(),'show all')]");
	public static By hideAllBtn = By.xpath("//p[contains(text(),'hide all')]");
	public static By paymentPageCreditNumberTxt = By.xpath("//p[contains(text(),'Credit No.')]");
	public static By paymentPageCreditDateTxt = By.xpath("//p[contains(text(),'Credit Date')]");
	public static By paymentPageCreditOrderNumberTxt = By.xpath("//p[contains(text(),'Purchase Order No.')]");
	public static By paymentPageCreditAmountTxt = By.xpath("//p//p[contains(text(),'Credit Amount')]");
	
	//credit card details window
	public static By creditCardPaymentRadioBtn = By.xpath("//input[@value='Credit / Debit Card Payment' and @ type='radio']");
	public static By acceptBtn = By.xpath("//button[contains(text(), 'Accept')]");
	public static By cancelBtn = By.xpath("//button[contains(text(), 'Cancel')]");
	public static By closeBtn = By.xpath("//button[@type='button' and @aria-label='close']");
	public static By enterANewCardDrpDwn = By.xpath("//div[contains(text(), 'Enter a new Card')]");
	public static By nameTxtFld = By.xpath("//input[@id='cardName']");
	public static By cardNumberTxtFld = By.xpath("//input[@id='cardNumber']");
	public static By cardMonthTxtFld = By.xpath("//input[@name='cardMonth']");
	public static By cardYearTxtFld = By.xpath("//input[@name='cardYear']");
	public static By cardnickNameTxtFld = By.xpath("//input[@name='cardNickName']");
	public static By saveCardCheckBox = By.xpath("//input[@name='isSaveCard']");
	public static By errorMessageTxt = By.xpath("//p[contains(text(), 'Enter a valid credit card number')]");
	public static By validate=By.xpath("//input[text()='enter debit card numer']");
	
}
