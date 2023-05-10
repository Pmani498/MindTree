package cdwpom;
package com.jaf.reusablecomponents;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jaf.UI.DAO.CommonDataColumn;
import com.jaf.enums.EnumClass.Browser;
import com.jaf.enums.EnumClass.StepResult;
import com.jaf.helper.DataHelper;
import com.jaf.helper.HelperClass;
import com.jaf.logger.THDLogger;
import com.jaf.reports.Report;

public class WebDriverHelper {

	Report report;
	DataHelper dataHelper;

	int noWaitTime = 1;
	int waitTime = 30;

	public boolean continueOnClickFail = false;
	public Browser currentBrowser;
	public RemoteWebDriver driver;
	public static boolean isForseeHandled = false;

	public WebDriverHelper(RemoteWebDriver driver, Report report, DataHelper dataHelper, Browser currentBrowser) {

		this.driver = driver;
		this.report = report;
		this.dataHelper = dataHelper;
		this.currentBrowser = currentBrowser;

		if (currentBrowser == Browser.IE) {

			noWaitTime = 3;
			waitTime = 50;
		}
	}

	/**
	 * Method to check if element is present
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(By elementBy) {

		try {
			return isElementPresent(elementBy, 5);
		} catch (Exception e) {
			report.addReportStep("Checking is the element present", "Element not found by " + elementBy,
					StepResult.FAIL);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Method to check if web element is present
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(WebElement webElement) throws Exception {

		try {
			return isElementPresent(webElement, 5);
		} catch (Exception e) {
			report.addReportStep("Checking is the element present", "Element not found by " + webElement,
					StepResult.FAIL);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Method to check if element is present after Dialog box is handled
	 * 
	 * @param elementBy
	 * @return
	 */
	private boolean elementPresentAfterDialogHandle(By elementBy) {

		boolean elementPresent = false;

		try {

			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

			if (driver.findElement(elementBy).isDisplayed()) {

				elementPresent = true;
			}

		} catch (NoSuchElementException nsee) {

			elementPresent = false;

		} catch (Exception e) {

			elementPresent = false;
		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to wait for specified seconds
	 * 
	 * @param seconds
	 */
	public void waitTime(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			System.out.println("Run time execption occured: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Method to check if element is present after waiting for specific time
	 * 
	 * @param elementBy
	 * @return
	 */
	public boolean isElementPresentAfterWait(final By elementBy) {

		boolean elementPresent = false;

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(120))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));

			if (driver.findElement(elementBy).isDisplayed()) {

				elementPresent = true;
			}

		} catch (NoSuchElementException nsee) {

			System.out.println("No Such Element present exception occured");

		} catch (Exception e) {

			System.out.println("Generic Exception occured");
		}

		return elementPresent;
	}

	public void navigateTo(String url) {
		driver.navigate().to(url);
	}

	/**
	 * Method to check if element is present without wait
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean noWaitElementPresent(By elementBy) throws Exception {

		return isElementPresent(elementBy, 1);
	}

	/**
	 * Method to check if element is not present without wait
	 * 
	 * @param elementBy
	 * @return
	 */
	public boolean isElementNotPresent(By elementBy) {

		boolean elementNotPresent = true;
		boolean check = false;

		try {

			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				check = driver.findElement(elementBy).isDisplayed();
			} catch (Exception nsee) {
				THDLogger.getInstance().info(
						"Element not found, NoSuchElementException caught... isElementNotpresent() method verified "
								+ nsee.getLocalizedMessage());
			}
			if (check) {
				elementNotPresent = false;
			}

		} catch (Exception se) {
			THDLogger.getInstance()
					.severe("Element not found. Problem occurred in isElementNotPresent() method " + se.getMessage());
		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
		if (elementNotPresent)
			THDLogger.getInstance().info("element is not present : " + elementBy);
		else
			THDLogger.getInstance()
					.info("element is present : " + elementBy + " User checked for isELEMENTNOTPRESENT condition");
		return elementNotPresent;
	}

	public boolean isElementNotPresent(WebElement element) {

		boolean elementNotPresent = true;
		boolean check = false;

		try {

			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				check = element.isDisplayed();
			} catch (Exception nsee) {
				THDLogger.getInstance().info(
						"Element not found, NoSuchElementException caught... isElementNotpresent() method verified "
								+ nsee.getLocalizedMessage());
			}
			if (check) {
				elementNotPresent = false;
			}

		} catch (Exception se) {
			THDLogger.getInstance()
					.severe("Element not found. Problem occurred in isElementNotPresent() method " + se.getMessage());
		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
		if (elementNotPresent)
			THDLogger.getInstance().info("element is not present : " + element);
		else
			THDLogger.getInstance()
					.info("element is present : " + element + " User checked for isELEMENTNOTPRESENT condition");
		return elementNotPresent;
	}

	/**
	 * Method to check element is present within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 */
	public boolean isElementPresent(WebElement element, By elementBy) {

		boolean elementPresent = false;

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

		try {

			elementPresent = element.findElement(elementBy).isDisplayed();

		} catch (Exception ex) {

			System.out.println("Exception occured while finding element within element");
		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickElement(By elementBy) {

		boolean exceptionOccured = false;
		String beforeClickUrl = driver.getCurrentUrl();

		try {

			if (isElementPresent(elementBy, 5)) {

				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

				try {

					try {

						driver.findElement(elementBy).click();
						THDLogger.getInstance().info("User Clicked on :" + elementBy.toString());

						if (beforeClickUrl.equalsIgnoreCase(driver.getCurrentUrl()) && currentBrowser == Browser.IE) {

							jsClick(elementBy);
							THDLogger.getInstance().info("User Clicked on :" + elementBy.toString() + " in IE browser");
						}

						/*
						 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
						 */

					} catch (NoSuchElementException nsee) {

						exceptionOccured = true;
						THDLogger.getInstance().info(nsee.toString());
					} catch (UnhandledAlertException e) {
						handleAlert();
					}

				} catch (Exception ex) {

					exceptionOccured = true;
					THDLogger.getInstance().info(ex.toString());
				}

				if (exceptionOccured) {

					try {

						jsClick(elementBy);
						validateUrl(elementBy);

					} catch (Exception ex) {

						THDLogger.getInstance().info(ex.toString());
					}

				} else {

					validateUrl(elementBy);
				}

			} else {

				if (!continueOnClickFail) {

					throw new Exception("Custom Error - occured in clickElement() method");
				}

			}

			driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		} catch (Exception ex) {

			if (ex.getMessage().trim() == "Custom Error - occured in clickElement() method") {
				report.addCustomErrorStep("User should be able to Click on element",
						"User is NOT able to Click on :" + elementBy.toString());

				// ReusableComponents rc = new ReusableComponents(driver, report, this,
				// dataHelper);
				// rc.throwCustomException();
			}

			handleAlert();

			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			report.addReportStep("Click the web element " + elementBy.toString(),
					"Unable to click element by " + elementBy, StepResult.FAIL);

		}
	}

	/* Method to check the check box */

	public void checkBox(By checkBox) throws Exception {

		try {
			if (isCheckBoxPresent(checkBox)) {
				if (!driver.findElement(checkBox).isSelected()) {
					driver.findElement(checkBox).click();
					System.out.println("Check box checked");

					// report.addReportStep("Check the checkbox " + checkBox, "Checkbox " + checkBox
					// + " checked", StepResult.PASS);
				} else {
					// report.addReportStep("Check the checkbox " + checkBox, "Failed to check the
					// checkbox",
					// StepResult.FAIL);
				}
			}

		} catch (Exception e) {
			report.addReportStep("Check the checkbox " + checkBox, "Checkbox " + checkBox + " not found",
					StepResult.FAIL);
		}
	}

	/* Method to uncheck the check box */

	public void unCheckBox(By checkBox) throws Exception {

		try {
			if (isCheckBoxPresent(checkBox)) {
				if (driver.findElement(checkBox).isSelected()) {
					driver.findElement(checkBox).click();
					System.out.println("Check box unchecked");

					// report.addReportStep("Check the checkbox " + checkBox, "Checkbox " + checkBox
					// + " checked", StepResult.PASS);
				} else {
					report.addReportStep("Uncheck the checkbox " + checkBox, "Failed to uncheck the checkbox",
							StepResult.FAIL);
				}
			}

		} catch (Exception e) {
			report.addReportStep("Uncheck the checkbox " + checkBox, "Checkbox " + checkBox + " not found",
					StepResult.FAIL);
		}
	}

	/* Method to check the check box */

	public void checkBox(WebElement checkBox) throws Exception {

		try {
			if (isCheckBoxPresent(checkBox)) {
				if (!(checkBox.isSelected())) {
					checkBox.click();
					System.out.println("Check box checked");

					// report.addReportStep("Check the checkbox " + checkBox, "Checkbox " + checkBox
					// + " checked", StepResult.PASS);
				} else {
					report.addReportStep("Check the checkbox " + checkBox, "Failed to check the checkbox",
							StepResult.FAIL);
				}
			}

		} catch (Exception e) {
			report.addReportStep("Check the checkbox " + checkBox, "Checkbox " + checkBox + " not found",
					StepResult.FAIL);
		}
	}

	public void presentRadioButton(By radioButton) throws Exception {

		if (!driver.findElement(radioButton).isDisplayed()) {
			driver.findElement(radioButton).click();
			System.out.println("Radio button displayed");
		}
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickNoUrlValidation(By elementBy) throws Exception {

		boolean exceptionOccured = false;

		if (isElementPresent(elementBy)) {

			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			try {

				try {

					driver.findElement(elementBy).click();

					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (NoSuchElementException nsee) {

					exceptionOccured = true;
					THDLogger.getInstance().info(nsee.toString());

				}

			} catch (Exception ex) {

				exceptionOccured = true;
				THDLogger.getInstance().info(ex.toString());

			}

			if (exceptionOccured) {

				try {

					jsClick(elementBy);

				} catch (Exception ex) {

					THDLogger.getInstance().info(ex.toString());
				}
			}

		} else {

			if (!continueOnClickFail) {

				report.addCustomErrorStep("Terminating test case as click element was not found",
						"Click Element: " + elementBy.toString() + " was not found");
				throw new Exception("Custom Error");
			}

		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to click element
	 * 
	 * @param element
	 * @throws Exception
	 */
	public void clickElement(WebElement element) throws Exception {

		boolean exceptionOccured = false;

		if (isElementPresent(element, 5)) {

			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			try {
				try {

					element.click();

					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (NoSuchElementException nsee) {

					exceptionOccured = true;
					THDLogger.getInstance().info(nsee.toString());
				}

			} catch (Exception ex) {

				exceptionOccured = true;
				THDLogger.getInstance().info(ex.toString());
			}

			if (exceptionOccured) {

				try {

					jsClick(element);

				} catch (Exception ex) {

					THDLogger.getInstance().info(ex.toString());
					report.addReportStep("Click the web element " + element.toString(),
							"Unable to click element by " + element, StepResult.FAIL);
				}
			}

		} else {

			if (!continueOnClickFail) {

				report.addCustomErrorStep("Terminating test case as click element was not found",
						"Click Element: " + element.toString() + " was not found");
				throw new Exception("Custom Error");
			}

		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to click using Actions
	 * 
	 * @param elementBy
	 */
	public void clickUsingActions(By elementBy) {

		try {

			if (isElementPresent(elementBy)) {

				Actions actions = new Actions(driver);
				actions.click(driver.findElement(elementBy));
			}

		} catch (Exception ex) {

			System.out.println("Exceptin occured whil clicking using Actions");
		}
	}

	/**
	 * Method to double click using actions
	 * 
	 * @param elementBy
	 */
	public void doubleClickUsingAction(By elementBy) throws Exception {

		try {

			if (isElementPresent(elementBy)) {

				/*
				 * Actions actions = new Actions(driver);
				 * actions.doubleClick(driver.findElement(elementBy));
				 * THDLogger.getInstance().info(
				 * "User is unable to double click on : "+elementBy);
				 */

				Actions action = new Actions(driver);
				action.moveToElement(driver.findElement(elementBy)).doubleClick().build().perform();
			}

		} catch (Exception ex) {
			report.addCustomErrorStep("User should be able to double click",
					"User is unable to double click on : " + elementBy);
			THDLogger.getInstance().severe("Unable to doubleClickUsingAction for element :" + elementBy);
			throw new Exception(
					"Exception has occurred in doubleClickUsingAction .Please check logs for more details.");
		}
	}

	/**
	 * Method to check if alert box is present
	 * 
	 * @return
	 */
	public boolean isAlertPresent() {

		try {

			driver.switchTo().alert();

			return true;

		} catch (NoAlertPresentException Ex) {

			// For FF throwing exception
			return false;
		}
	}

	/**
	 * Method to click element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 */
	public void clickElement(WebElement element, By elementBy) {

		if (isElementPresent(element, elementBy)) {

			element.findElement(elementBy).click();
		}
	}

	/**
	 * Method to mouse over element using javascript
	 * 
	 * @param element
	 */
	public void mouseOver(WebElement element) {

		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			((RemoteWebDriver) executor).executeScript(code, element);
			THDLogger.getInstance().info("Mouse hover over the element : " + element);
		} catch (Exception ex) {
			THDLogger.getInstance().severe("Exception occurred in wdhelper.mouseOver() method");
			report.addCustomErrorStep("User shouldbe able to mouse hover over :" + element,
					"Unable to perform mouse hover action");
		}

	}

	/**
	 * Method to mouse over element using javascript
	 * 
	 * @param elementBy
	 * @throws InterruptedException
	 */
	public void mouseOver(By elementBy) throws Exception {

		WebElement weElement = driver.findElement(elementBy);

		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";
		try {
			if (isElementPresent(elementBy, 5)) {

				JavascriptExecutor executor = (JavascriptExecutor) driver;
				((RemoteWebDriver) executor).executeScript(code, weElement);
				THDLogger.getInstance().info("Mouse hover over the element : " + elementBy);
			}
		} catch (Exception ex) {
			THDLogger.getInstance().severe("Exception occurred in wdhelper.mouseOver() method");
			report.addCustomErrorStep("User shouldbe able to mouse hover over :" + elementBy,
					"Unable to perform mouse hover action");
		}
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param elementBy
	 * @throws InterruptedException
	 */
	public void jsClick(By elementBy) throws Exception {

		WebElement weElement = driver.findElement(elementBy);

		JavascriptExecutor executor = driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", weElement);
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param weElement
	 * @throws InterruptedException
	 */
	public void jsClick(WebElement weElement) throws Exception {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", weElement);
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param documentId
	 */
	public void jsClick(String documentId) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("document.getElementById('hlViewStoreFinder').click();");

	}

	/**
	 * Method to validate url
	 * 
	 * @param elementBy
	 */
	public void validateUrl(By elementBy) {

		String envUrl = dataHelper.getCommonData(CommonDataColumn.EnvironmentUrl);

		if (!envUrl.contains("mscdirect.com")) {

			String afterClickUrl = driver.getCurrentUrl();
			envUrl = envUrl.substring(envUrl.indexOf("//") + 2, envUrl.indexOf(".com"));

			if (afterClickUrl.contains("quality.mscdirect.com") && !afterClickUrl.contains(envUrl)) {

				report.addReportStep("Click on element - " + elementBy.toString(),
						"User is redirected to environment - " + afterClickUrl, StepResult.FAIL);
			}
		}
	}

	/**
	 * Method to focus element
	 * 
	 * @param elementBy
	 */
	public void focusElement(By elementBy) {

		try {

			jsFocus(driver.findElement(elementBy));

		} catch (Exception ex) {

			System.out.println("focusElement" + ex.toString()
					+ " Exception occured while clicking element using driver focus method");
			THDLogger.getInstance().info(ex.toString());
		}
	}

	/**
	 * Method to focus element using javascript
	 * 
	 * @param element
	 */
	private void jsFocus(WebElement element) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", element);
	}

	/**
	 * Method to clear text element
	 * 
	 * @param elementBy
	 */
	public void clearElement(By elementBy) {

		driver.findElement(elementBy).sendKeys(Keys.HOME);
		driver.findElement(elementBy).sendKeys("");
		driver.findElement(elementBy).clear();
	}

	/**
	 * Method to navigate back
	 */
	public void navigateBack() {

		driver.navigate().back();
	}

	/**
	 * @param elementBy
	 * @param value     Method to return the dropDown
	 * @return
	 */
	public Select selectDropDown(By elementBy) {

		Select dropDown = null;

		try {
			dropDown = new Select(driver.findElement(elementBy));
		} catch (Exception e) {
			report.addReportStep("Select the dropdown", "Dropdown not found. It's locator " + elementBy,
					StepResult.FAIL);
		}
		return dropDown;
	}

	/**
	 * Method to select value for dropdown list by Visible Text
	 * 
	 * @param elementBy
	 * @param label
	 * @return
	 */
	public Select selectByVisibleText(By elementBy, String label) {

		Select dropDown = null;

		try {
			dropDown = new Select(driver.findElement(elementBy));
			if (!dropDown.getAllSelectedOptions().get(0).equals(label))
				dropDown.selectByVisibleText(label);
		} catch (Exception e) {
			report.addReportStep("Select the dropdown", "Dropdown not found. It's locator " + elementBy,
					StepResult.FAIL);
		}
		return dropDown;
	}

	/**
	 * @param elementBy
	 * @param value     Select By Value.
	 * @return
	 */
	public Select selectByValue(By elementBy, String value) {

		Select dropDown = null;

		try {
			dropDown = new Select(driver.findElement(elementBy));
			if (!dropDown.getAllSelectedOptions().get(0).getAttribute("value").equals(value))
				dropDown.selectByValue(value);
		} catch (Exception e) {
			report.addReportStep("Select the dropdown", "Dropdown not found. It's locator " + elementBy,
					StepResult.FAIL);
		}
		return dropDown;
	}

	/**
	 * @param elementBy
	 * @param value     Select By Value.
	 * @return
	 */
	public Select selectByValue(WebElement element, String value) {

		Select dropDown = null;

		try {
			dropDown = new Select(element);
			if (!dropDown.getAllSelectedOptions().get(0).getAttribute("value").equals(value))
				dropDown.selectByValue(value);
		} catch (Exception e) {
			report.addReportStep("Select the dropdown", "Dropdown not found. It's locator " + element, StepResult.FAIL);
		}
		return dropDown;
	}

	/**
	 * Method to select index value from drop-down list
	 * 
	 * @param elementBy
	 * @param index
	 * @return
	 */
	public Select selectindex(By elementBy, int index) {
		Select dropDown = null;
		try {
			dropDown = new Select(driver.findElement(elementBy));
			dropDown.selectByIndex(index);
		} catch (Exception e) {
			report.addReportStep("Selection at DropDown: " + elementBy.toString() + " failed due to: " + e.toString(),
					"Dropdown Value should be selected.", StepResult.FAIL);
		}
		return dropDown;

	}

	/**
	 * Method to select index value from drop-down list
	 * 
	 * @param elementBy
	 * @param index
	 * @return
	 */
	public Select selectindex(WebElement elementBy, int index) {
		Select dropDown = null;
		try {
			dropDown = new Select(elementBy);
			dropDown.selectByIndex(index);
		} catch (Exception e) {
			report.addReportStep("Selection at DropDown: " + elementBy.toString() + " failed due to: " + e.toString(),
					"Dropdown Value should be selected.", StepResult.FAIL);
		}
		return dropDown;

	}

	/**
	 * Method to send keys to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public void sendKeys(By elementBy, String typeValue) {

		try {
			if (isElementPresent(elementBy)) {

				clearElement(elementBy);

				if (!getText(elementBy).isEmpty()) {

					Thread.sleep(100);
					clearElement(elementBy);
				}

				driver.findElement(elementBy).sendKeys(typeValue);
			}
		} catch (Exception e) {
			THDLogger.getInstance().info("Element: " + elementBy.toString() + " not found" + elementBy);
			report.addReportStep("Sending string keys for Web element: " + elementBy, "Web element not found: ",
					StepResult.FAIL);
		} /*
			 * else report.addReportStep("User Entered: " + typeValue + " in the text box",
			 * "User should Enter: " + typeValue + " in the textbox", StepResult.FAIL);
			 */
	}

	/**
	 * Method to send characters to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public void sendChars(By elementBy, String typeChar) throws Exception {

		if (isElementPresent(elementBy)) {

			driver.findElement(elementBy).sendKeys(typeChar);
		}
	}

	/**
	 * Method to get text of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public String getText(By elementBy) {
		String val = null;
		try {
			if (noWaitElementPresent(elementBy)) {

				val = driver.findElement(elementBy).getText().trim();
				THDLogger.getInstance().info("reading text from : " + elementBy + " read value is : " + val);

			} else {
				THDLogger.getInstance().info("Element doesnt exists : " + elementBy + " NOT able to fetch text value");
			}
		} catch (Exception e) {
			THDLogger.getInstance().info("Element doesnt exists : " + elementBy + " NOT able to fetch text value");
		}
		return val;
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public Double getDouble(By elementBy) throws Exception {

		Double retValue = 0.0;

		try {

			retValue = Double.valueOf(getText(elementBy));

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param str
	 * @return
	 * @throws InterruptedException
	 */
	public Double getDouble(String str) throws Exception {

		Double retValue = 0.0;

		try {

			retValue = Double.valueOf(str);

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public int getInteger(By elementBy) throws Exception {

		int retValue = 0;

		try {

			retValue = Integer.valueOf(getText(elementBy));

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get text of an element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 */
	public String getText(WebElement element, By elementBy) {

		if (isElementPresent(element, elementBy)) {

			return element.findElement(elementBy).getText().trim();

		} else {

			return "";
		}
	}

	/**
	 * Method to get text of an element within parent element
	 * 
	 * @param element
	 * @return
	 * @throws InterruptedException
	 */
	public String getText(WebElement element) throws Exception {

		if (isElementPresent(element, 5)) {

			return element.getText().trim();

		} else {

			return "";

		}
	}

	/**
	 * Method to handle alert box
	 */
	public void handleAlert() {
		try {

			Alert alert = driver.switchTo().alert();
			alert.accept();

		} catch (Exception ex) {

			System.out.println("Execption occured during alert handle");

		}
	}

	/**
	 * Method to handle alert box
	 */
	public String getAlertText() {
		String alertText = "";
		try {

			Alert alert = driver.switchTo().alert();
			alertText = alert.getText();
			THDLogger.getInstance().info("Text from Alert: " + alertText);

		} catch (Exception ex) {

			System.out.println("Execption occured during alert handle");

		}
		return alertText;
	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void waitForElementPresent(final By elementBy)  {

		try {
			System.out.println("Waiting for element.. " + elementBy);
			boolean isElementPresent = false;
			Thread.sleep(10);
			isElementPresent = isElementPresent(elementBy, 10);// Reduced to 5 seconds
			/*if(!isElementPresent) {
				report.addReportStep("Waiting for element " + elementBy, "Element not found", StepResult.FAIL);
				driver.close();
			}*/
			// THDLogger.getInstance().info("waiting till 5s for element to display.");
		} catch (Exception e) {
			report.addReportStep("Waiting for element " + elementBy, "Element not found", StepResult.FAIL);
			driver.close();
		}
	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @param waitTimeSecs
	 * @throws InterruptedException
	 */
	public void waitForElementPresent(final By elementBy, int waitTimeSecs) throws Exception {

		isElementPresent(elementBy, waitTimeSecs);
		THDLogger.getInstance().info("Waiting till : " + waitTimeSecs + " for " + elementBy + " to display");
	}

	/**
	 * Method to check if element is Present within some seconds
	 * 
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isElementPresent(By elementBy, int waitForSeconds) throws Exception {

		boolean elementPresent = true;
		int count = 0;

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		while (elementPresent) {
			try {
				if (driver.findElement(elementBy).isDisplayed()) {
					THDLogger.getInstance().info(elementBy.toString() + " : element is displayed");
					break;
				} else {
					if (count == waitForSeconds) {
						elementPresent = false;
						break;
					}

					Thread.sleep(1000);
					count++;
				}
			} catch (Exception ex) {
				if (count == waitForSeconds) {
					THDLogger.getInstance().info("Not able to find element " + elementBy + " after waiting for "
							+ waitForSeconds + "seconds");
					 report.addCustomErrorStep(elementBy.toString(), "Not able to find element "+elementBy.toString()+" after waiting for "+waitForSeconds+ " seconds");
					elementPresent = false;
					report.addReportStep("Is element " + elementBy + " found", "Element not found", StepResult.WARNING);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return elementPresent;
	}

	/**
	 * Method to check if element is Present with in an element for some seconds
	 * 
	 * @param element
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(WebElement element, By elementBy, int waitForSeconds) throws Exception {

		boolean elementPresent = true;
		int count = 0;

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		while (elementPresent) {

			try {

				if (element.findElement(elementBy).isDisplayed()) {

					break;

				} else {

					if (count == waitForSeconds) {

						elementPresent = false;
						break;

					}

					Thread.sleep(1000);
					count++;

				}

			} catch (Exception ex) {

				if (count == waitForSeconds) {

					elementPresent = false;
					break;

				}

				Thread.sleep(1000);
				count++;
			}
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to check web element is present
	 * 
	 * @param element
	 * @param waitForSeconds
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(WebElement element, int waitForSeconds) throws Exception {

		boolean elementPresent = true;
		int count = 0;

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		while (elementPresent) {

			try {

				if (element.isDisplayed()) {

					break;

				} else {

					if (count == waitForSeconds) {

						elementPresent = false;
						break;
					}

					Thread.sleep(1000);
					count++;
				}

			} catch (Exception ex) {

				if (count == waitForSeconds) {

					elementPresent = false;
					break;
				}

				Thread.sleep(1000);
				count++;
			}
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to wait for page to get loaded
	 */
	public void waitForPageToLoad() {
		try {
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};

			WebDriverWait wait = new WebDriverWait(driver, 40);

			wait.until(expectation);

			THDLogger.getInstance().info("Dynamically waiting for page load until page is in steady state");

		} catch (Exception ex) {
			THDLogger.getInstance().severe("A problem occurred in waitForPageLoad() method");
			//report.addCustomErrorStep("wait for page load", "an exception occurred");
			/*
			 * throw new Exception(
			 * "An Exception occurred in waitforpageload() method . Please check  the logs for more details."
			 * );
			 */
		}
	}

	/**
	 * Component to move actual mouse pointer to top left corner of page.
	 * 
	 * @throws AWTException
	 */
	public void moveActualMouse() throws AWTException {

		Robot robot = new Robot();
		robot.mouseMove(0, 0);
		robot = null;
	}

	/**
	 * Component to move actual mouse pointer to top left corner of page
	 * 
	 * @param x
	 * @param y
	 * @throws AWTException
	 */
	public void moveActualMouse(int x, int y) throws AWTException {

		Robot robot = new Robot();
		robot.mouseMove(x, y);
		robot = null;
	}

	/**
	 * Component to click on close button of overlay and wait to until the overlay
	 * closes itself.
	 * 
	 * @param elementToCheck
	 * @param closeButton
	 * @throws Exception
	 */
	public void clickAndWaitUntilDisappear(By elementToCheck, By closeButton) throws Exception {

		if (isElementPresent(elementToCheck, 0)) {

			clickElement(closeButton);

			while (true) {

				if (isElementPresent(elementToCheck, 1)) {

				} else {

					break;
				}
			}
		}
	}

	/**
	 * Component to wait until the overlay closes itself
	 * 
	 * @param elementToCheck
	 * @throws Exception
	 */
	public void waitUntilDisappear(By elementToCheck) throws Exception {

		int i = 0;

		while (true) {

			if (isElementPresent(elementToCheck, 1)) {

			} else {

				break;
			}

			if (i == 10) {

				break;
			}

			i++;
		}
	}

	/**
	 * Component to paste text to a control
	 * 
	 * @param pasteBy
	 * @throws Exception
	 */
	public void pasteText(By pasteBy) throws Exception {

		if (isElementPresent(pasteBy)) {

			clearElement(pasteBy);
			driver.findElement(pasteBy).sendKeys(Keys.CONTROL + "v");
		}
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param elementBy
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(By elementBy, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(elementBy)) {

			attributeValue = driver.findElement(elementBy).getAttribute(attribute);
			THDLogger.getInstance()
					.info("reading attribute from : " + elementBy + " attribute value is : " + attributeValue);
		} else {
			THDLogger.getInstance().info("Element doesnt exists : " + elementBy + " Unable to read attribute "
					+ attribute + " of the element ");
		}
		return attributeValue;
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param element
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(WebElement element, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(element, 2)) {

			attributeValue = element.getAttribute(attribute);
		}

		return attributeValue;
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param element
	 * @param elementBy
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(WebElement element, By elementBy, String attribute) throws Exception {

		String attributeValue = "";

		try {
			attributeValue = element.findElement(elementBy).getAttribute(attribute);

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}

		return attributeValue;
	}

	/**
	 * Component to get CSS value for a particular control
	 * 
	 * @param elementBy
	 * @param cssAttribute
	 * @return
	 * @throws Exception
	 */
	public String getCssValue(By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(elementBy)) {

			cssValue = driver.findElement(elementBy).getCssValue(cssAttribute);
		}

		return cssValue;
	}

	/**
	 * Component to get CSS value for a particular control
	 * 
	 * @param element
	 * @param elementBy
	 * @param cssAttribute
	 * @return
	 * @throws Exception
	 */
	public String getCssValue(WebElement element, By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(element, elementBy)) {

			cssValue = element.findElement(elementBy).getCssValue(cssAttribute);
		}

		return cssValue;
	}

	/**
	 * Method to getLocation for a particular element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public Point getLocation(By elementBy) throws Exception {

		Point locations = new Point(0, 0);

		if (isElementPresent(elementBy, 5)) {

			locations = driver.findElement(elementBy).getLocation();
		}

		return locations;
	}

	/**
	 * Method to refresh web page
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {

		driver.navigate().refresh();
		waitForPageToLoad();
	}

	/**
	 * Method to get no of webElements count
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getElementsCount(By elementBy) throws Exception {

		int noOfElements = 0;

		if (noWaitElementPresent(elementBy)) {

			noOfElements = driver.findElements(elementBy).size();
		}

		return noOfElements;
	}

	/**
	 * Method to get webElements
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> getElements(By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(elementBy, 2)) {

			lstWebElements = driver.findElements(elementBy);
		}

		return lstWebElements;
	}

	/**
	 * Method to get webElements
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> getElements(WebElement webElement, By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(webElement, elementBy, 2)) {

			lstWebElements = webElement.findElements(elementBy);
		}

		return lstWebElements;
	}

	/**
	 * Method to get element count
	 * 
	 * @param webElement
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getElementsCount(WebElement webElement, By elementBy) throws Exception {

		int noOfElements = 0;

		if (isElementPresent(webElement, elementBy, 0)) {

			noOfElements = webElement.findElements(elementBy).size();
		}

		return noOfElements;
	}

	/**
	 * Method to open new tab
	 * 
	 * @param url
	 */
	public void openTab(String url) {

		String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
		script = String.format(script, url);

		Object element = ((JavascriptExecutor) driver).executeScript(script);

		if (element instanceof WebElement) {

			WebElement anchor = (WebElement) element;
			anchor.click();
			((JavascriptExecutor) driver).executeScript("var a=arguments[0];a.parentNode.removeChild(a);", anchor);

			Set<String> handles = driver.getWindowHandles();
			String current = driver.getWindowHandle();
			handles.remove(current);

			String newTab = handles.iterator().next();

			driver.switchTo().window(newTab);

			report.addReportStep("Open new tab", "New tab opened successfully", StepResult.PASS);

		} else {

			report.addReportStep("Open new tab", "New tab was not opened", StepResult.FAIL);
		}
	}

	public void closeBrowser() {
		SessionId session = driver.getSessionId();
		if (session != null)
			report.addReportStep("Execution Session ID: " + session.toString(), "Seesion is captured.",
					StepResult.PASS);
		if (driver.getWindowHandles().size() > 1) {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				driver.close();
			}
		} else {
			driver.quit();
		}
	}

	public void get(String url) {
		driver.get(url);
	}

	/**
	 * Method to close Window of a Browser
	 */
	public void closeWindow() {
		driver.close();
	}

	/**
	 * Method to trigger javascript on web element
	 * 
	 * @param script
	 * @param element
	 */
	public void trigger(String script, WebElement element) {

		((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Executes a script
	 * 
	 * @note Really should only be used when the web driver is sucking at exposing
	 *       functionality natively
	 * @param script The script to execute
	 */
	public Object trigger(String script) {

		return ((JavascriptExecutor) driver).executeScript(script);
	}

	/**
	 * Method to get width in pixels of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getWidth(By elementBy) throws Exception {

		int width = 0;

		if (noWaitElementPresent(elementBy)) {

			width = driver.findElement(elementBy).getSize().getWidth();
		}

		return width;
	}

	/**
	 * Method to get height in pixels of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getHeight(By elementBy) throws Exception {

		int height = 0;

		if (noWaitElementPresent(elementBy)) {

			height = driver.findElement(elementBy).getSize().getHeight();
		}

		return height;
	}

	/**
	 * Method to clear browser history
	 * 
	 * @throws Exception
	 */
	public void clearHistory() throws Exception {

		driver.manage().deleteAllCookies();
		refresh();

		report.addReportStep("Clear the history or the browser and refresh the page",
				"History is cleared and page is refreshed", StepResult.PASS);
	}

	/**
	 * Method to set display style of element to block
	 * 
	 * @param elementBy
	 */
	public void blockElement(By elementBy) {

		WebElement weElement = driver.findElement(elementBy);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].style.display = 'block';", weElement);
	}

	/**
	 * Method to check if element is selected
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isSelected(By elementBy) throws Exception {

		return driver.findElement(elementBy).isSelected();
	}

	/**
	 * This method checks the ascending order of list.
	 * 
	 */
	public boolean isAscending(List<Integer> sortOrder) {

		boolean ascending = true;

		for (int i = 1; i < sortOrder.size() && (ascending); i++) {
			ascending = ascending && sortOrder.get(i) >= sortOrder.get(i - 1);
		}

		return ascending;
	}

	/**
	 * This method checks the descending order of list.
	 * 
	 */
	public boolean isDescending(List<Integer> sortOrder) {

		boolean descending = true;

		for (int i = 1; i < sortOrder.size() && (descending); i++) {
			descending = descending && sortOrder.get(i) <= sortOrder.get(i - 1);
		}

		return descending;
	}

	/**
	 * Converts the accented characters to deaccented Ex: D�cor to Decor
	 * 
	 */
	public String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	/**
	 * Function that returns a string within quotes. Intended to be used in reports.
	 * 
	 */
	public String inQuotes(String str) {

		if (str.contains("$")) {
			str = str.replaceAll("\\$", "");
		}
		if (str != null)
			return "\"" + str + "\"";
		else
			return "";
	}

	/**
	 * Method to move to element
	 * 
	 * @param elementBy
	 */
	public void moveToElement(By elementBy) {

		Actions act = new Actions(driver);
		WebElement mainMenu = driver.findElement(elementBy);
		act.moveToElement(mainMenu).build().perform();
	}

	// code to handle foresee
	/*public void handleForesee() {
		try {
			if (driver.findElement(ForeseePOM.yesFeedbackButton).isDisplayed()) {
				// if(driver.findElement(CommonElements.noThanksButton).isDisplayed())
				// {
				if (isElementPresent(ForeseePOM.noThanksButton)) {
					driver.findElement(ForeseePOM.noThanksButton).click();
					System.out.println("First foresee");
				} else {
					driver.findElement(By.xpath("//*[@id='acsMainInvite']/a")).click();
					System.out.println("Second foresee");
				}
				if (isElementNotPresent(By.id("ForeseePOM.yesFeedbackButton")))
					report.addReportStep("Verify if foresee is handled", "Foresee should be handled", StepResult.PASS);
				else
					report.addReportStep("Verify if foresee is handled", "Foresee should be handled", StepResult.FAIL);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "Foresee did not pop up");
		}
	}*/

	/**
	 * @param webElement
	 * @return
	 * 
	 *         Method to return list of web Elements
	 */
	public List<WebElement> getListOfElements(By webElement) {
		List<WebElement> webElements = null;
		try {

			webElements = driver.findElements(webElement);
			/*report.addReportStep("List of elements for : " + webElement.toString() + " is generated. ",
					"List of webelemts for : " + webElement.toString() + " should be generated. ", StepResult.PASS);*/
		} catch (Exception e) {
			/*report.addReportStep("List of elements for : " + webElement.toString() + " is generated. ",
					"List of webelemts for : " + webElement.toString() + " should be generated. ", StepResult.FAIL);*/
		}
		return webElements;
	}

	/**
	 * @param webElement
	 * @param text       Method to Wait for an text to be present on the page.
	 */
	public void waitForTextToBePresent(By webElement, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(webElement, text));
			report.addReportStep("Text: " + text + " is Visible ", "Text: " + text + " should be Visible ",
					StepResult.PASS);
		} catch (Exception e) {
			report.addReportStep("Text is not visble for element: " + webElement.toString(),
					"Text should be visble for element: " + webElement.toString(), StepResult.FAIL);
		}

	}

	/**
	 * @return Method to return Page URL
	 */
	public String getCurrentUrl() {

		String url = null;
		try {
			url = driver.getCurrentUrl();
			// report.addReportStep("Current Url: " + url, "Expected Url:" + url,
			// StepResult.PASS);
		} catch (Exception e) {
			// report.addReportStep("Current Url: " + url, "Expected Url:" + url,
			// StepResult.FAIL);
		}

		return url;
	}

	/**
	 * @return Method to Return Page Title
	 */
	public String getTitle() {
		String title = null;
		try {
			title = driver.getTitle();
			THDLogger.getInstance().info("Page Title: " + title);
			// report.addReportStep("Current Title: " + title, "Current Title: " + title,
			// StepResult.PASS);
		} catch (Exception e) {
			// report.addReportStep("Current Title: " + title, "Current Title: " + title,
			// StepResult.FAIL);
		}

		return title;
	}

	/**
	 * @param element Method to return webElement
	 * @return
	 */
	public WebElement getWebElement(By element) {
		WebElement webElement = null;
		try {
			webElement = driver.findElement(element);
		} catch (Exception e) {
			report.addReportStep("Web element is not found on the page: " + webElement,
					"Web ELement should be present in the page", StepResult.FAIL);
		}
		return webElement;

	}

	/**
	 * @return Method to get PageSource.
	 */
	public String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * Method to switch to newly opened tab
	 * 
	 * @return
	 */
	public boolean switchToNewWindow() {
		boolean check = false;
		try {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
			}
			check = true;
		} catch (Exception e) {
			THDLogger.getInstance().info("No New Window is Open....");
		}
		return check;
	}

	/**
	 * Open new tab with url
	 */
	public void openNewTab(String url) throws Exception {
		((JavascriptExecutor) driver).executeScript("window.open()");
		switchToNewWindow();
		driver.get(url);
	}

	/**
	 * Method to switch to newly opened tab
	 * 
	 * @return
	 */
	public ArrayList<String> getWindowHandles() throws Exception {
		return new ArrayList<String>(driver.getWindowHandles());

	}

	/**
	 * Method to switch to parent window
	 * 
	 * @param parentWindow
	 * @throws Exception
	 */
	public void switchToParentWindow() throws Exception {
		ArrayList<String> tabs = getWindowHandles();
		driver.switchTo().window(tabs.get(0));
	}

	/**
	 * @param elementPresentOnPage
	 * 
	 *                             Method to open expected window.
	 */
	public void verifyWindowOpened(By elementPresentOnPage, String type) {
		try {
			switchToParentWindow();
			ArrayList<String> size = getWindowHandles();
			int tabNumber = 0;
			boolean check = false;
			while (tabNumber < size.size()) {
				driver.switchTo().window(size.get(tabNumber));
				if (type.equalsIgnoreCase("outlook")) {
					System.out.println(getTitle());
					if (getTitle().contains("Outlook")) {
						check = true;
						break;
					}
				} else {
					check = isElementNotPresent(elementPresentOnPage);
				}
				if (check) {
					THDLogger.getInstance().info("Switching window... to: " + tabNumber);
				}
				tabNumber++;
			}

			if (!check) {
				THDLogger.getInstance().info("Found the window...");
			}

		} catch (Exception e) {
			THDLogger.getInstance().info("Error occured while navigating to desired window: " + e.toString());
		}
	}

	/**
	 * Method to switch to any window
	 * 
	 * @param tabNumber
	 * @throws Exception
	 */
	public void switchToWindow(int tabNumber) throws Exception {
		ArrayList<String> tabs = getWindowHandles();
		driver.switchTo().window(tabs.get(tabNumber));
	}

	/**
	 * Method to scroll Down in a page.
	 */
	public void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("scroll(0,200)", "");
	}

	/**
	 * @throws Exception Method to Scroll to bottom of the page.
	 */
	public void scrollToBotttomOfPage() throws Exception { // WD Helper

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * Method to scroll on the page give +ve values to scroll Down. give -ve values
	 * to Scroll Up.
	 * 
	 * @param value
	 */
	public void scrollBy(int value) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0," + value + ")");
	}

	/**
	 * Method scroll down the web page by the visibility of the element.
	 */
	public void scrollByVisibilityOfElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void getUrl(String url) {
		try {

			driver.get(url);
		} catch (Exception e) {

			report.addReportStep("URL: " + url, "check the url manually.", StepResult.FAIL);
		}
	}

	public void quitBrowser() {
		SessionId session = driver.getSessionId();
		if (session != null)
			report.addReportStep("Execution Session ID: " + session.toString(), "Seesion is captured.",
					StepResult.PASS);
		driver.quit();
	}

	public boolean isTextPresent(String txt) {
		try {
			boolean b = driver.getPageSource().contains(txt);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void actionClass(WebElement tooltipXpath) {
		Actions act = new Actions(driver);
		act.moveToElement(tooltipXpath).perform();
	}

	public void enableRadioButton(By radioButton) {

		try {

			if (driver.findElement(radioButton).isEnabled()) {
				driver.findElement(radioButton).click();
			}
		} catch (Exception e) {
			// report.addReportStep("Enable the radio button " + radioButton, "Unable to
			// locate the radio button", StepResult.FAIL);
		}
	}

	public boolean isRadioButtonPresent(By radioButton) {

		try {
			if (driver.findElement(radioButton).isEnabled()) {
				return true;
			}
		} catch (Exception e) {
			// report.addReportStep("Is radio button present", "Radio button " + radioButton
			// + " not present", StepResult.FAIL);
		}
		return false;
	}

	public boolean isCheckBoxPresent(By checkBox) {

		try {
			if (driver.findElement(checkBox).isEnabled()) {
				return true;
			}
		} catch (Exception e) {
			report.addReportStep("Is check box present", "Check box" + checkBox + " not present", StepResult.FAIL);
		}
		return false;
	}

	public boolean isCheckBoxPresent(WebElement checkBox) {

		try {
			if (checkBox.isEnabled()) {
				return true;
			}
		} catch (Exception e) {
			report.addReportStep("Is check box present", "Check box" + checkBox + " not present", StepResult.FAIL);
		}
		return false;
	}

	public void scrollIntoView(WebElement e) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true)", e);
	}

	public void enableRadioButton(WebElement radioButton) {
		try {

			if (radioButton.isEnabled()) {
				radioButton.click();
			}
		} catch (Exception e) {
			report.addReportStep("Enable the radio button " + radioButton, "Unable to locate the radio button",
					StepResult.FAIL);
		}
	}
	
	public boolean isRadioButtonSelected(WebElement radioButton) {
		try {
			if (radioButton.isSelected()) {
				return true;
			}
		} catch (Exception e) {
			report.addReportStep("Check if radio button " + radioButton + " is selected ", "Unable to locate the radio button",
					StepResult.FAIL);
		}
		return false;
	}
	
	public boolean isRadioButtonSelected(By radioButton) {
		try {
			if (driver.findElement(radioButton).isSelected()) {
				return true;
			}
		} catch (Exception e) {
			report.addReportStep("Check if radio button " + radioButton + " is selected ", "Unable to locate the radio button",
					StepResult.FAIL);
		}
		return false;
	}

	/**
	 * @param ele Method to move an element to right of application.
	 */
	public void dragAndDropToRight(By ele) {
		Actions act = new Actions(driver);
		act.dragAndDropBy(getWebElement(ele), 1519, 0);
		act.build().perform();
	}

	/**
	 * @param ele Method to move an element to left of application.
	 */
	public void dragAndDropToLeft(By ele) {
		Actions act = new Actions(driver);
		act.dragAndDropBy(getWebElement(ele), 0, 1414);
		act.build().perform();
		THDLogger.getInstance().info(
				"User draged from element: " + ele.toString() + " to x-position: " + 0 + " to y-position: " + 1414);
	}

	/**
	 * @param ele Method to move an element to desired position of application.
	 */
	public void dragAndDrop(By ele, int xPosition, int yPosition) {
		Actions act = new Actions(driver);
		act.dragAndDropBy(getWebElement(ele), xPosition, yPosition);
		act.build().perform();
		THDLogger.getInstance().info("User draged from element: " + ele.toString() + " to x-position: " + xPosition
				+ " to y-position: " + yPosition);
	}

	/**
	 * @param ele Method to move an element to desired position of application.
	 */
	public void dragAndDrop(By fromElemet, By toElemet) {
		Actions act = new Actions(driver);
		act.dragAndDrop(getWebElement(fromElemet), getWebElement(toElemet));
		act.build().perform();
		THDLogger.getInstance()
				.info("User draged from element: " + fromElemet.toString() + " to element: " + toElemet.toString());
	}

	/**
	 * Method to verify Element Clickable or not.
	 * 
	 * @return
	 */
	public boolean isElementClickable(By ele) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		boolean check = false;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			driver.findElement(ele).click();
			check = true;
			THDLogger.getInstance().info("User is able to click on element.");
		} catch (Exception e) {
			report.addReportStep("Element " + ele + " is not clickable due to: " + e.toString(),
					"Element should be clickable.", StepResult.FAIL);
		}
		return check;
	}

	/**
	 * Method to verify Element Clickable or not.
	 * 
	 * @return
	 */
	public boolean isElementClickable(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		boolean check = false;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			check = true;
			(ele).click();
			THDLogger.getInstance().info("User is able to click on element.");
		} catch (Exception e) {
			report.addReportStep("Element " + ele + " is not clickable due to: " + e.toString(),
					"Element should be clickable.", StepResult.FAIL);
		}
		return check;
	}

	/**
	 * Method to verify Element Not Clickable.
	 * 
	 * @return
	 */
	public boolean isElementNotClickable(By ele) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		boolean check = false;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			driver.findElement(ele).click();
			check = false;
		} catch (Exception e) {
			check = true;
			THDLogger.getInstance().info("User is unable to click on element.");
			report.addReportStep("Element " + ele + " is not clickable.", "Element should be clickable.",
					StepResult.PASS);
		}
		return check;
	}

	/**
	 * Method to verify Element Not Clickable.
	 * 
	 * @return
	 */
	public boolean isElementNotClickable(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		boolean check = false;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			(ele).click();
			check = false;
		} catch (Exception e) {
			check = true;
			THDLogger.getInstance().info("User is unable to click on element.");
			report.addReportStep("Element " + ele + " is not clickable.", "Element should be clickable.",
					StepResult.PASS);
		}
		return check;
	}

	/**
	 * @param element
	 * @return Method to wait for element to be visible
	 */
	public boolean waitForElementVisible(By element) {
		WebDriverWait wait = new WebDriverWait(driver, 3000);
		boolean isVisible = false;
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			isVisible = true;
		} catch (Exception e) {
			isVisible = false;
			THDLogger.getInstance().info("Element is not Visible even after 30 secs.... due to: " + e.toString());
		}
		return isVisible;
	}

	/**
	 * @param element
	 * @return Method to wait for element to be visible
	 */
	public boolean waitForElementInvisibility(By element) {
		WebDriverWait wait = new WebDriverWait(driver, 3000);
		boolean isVisible = false;
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
			isVisible = true;
		} catch (Exception e) {
			isVisible = false;
			THDLogger.getInstance().info("Element is Visible even after 30 secs.... due to: " + e.toString());
		}
		return isVisible;
	}
	/* Method to Uncheck the check box */

	public void uncheckBox(By checkBox) throws Exception {

		try {
			if (isCheckBoxPresent(checkBox)) {
				if (driver.findElement(checkBox).isSelected()) {
					driver.findElement(checkBox).click();
					System.out.println("Check box Unchecked");

					report.addReportStep("UnCheck the checkbox " + checkBox, "UnCheckbox " + checkBox + " checked",
							StepResult.PASS);
				} else {
					report.addReportStep("UnCheck the checkbox " + checkBox, "Check box is already uncheck",
							StepResult.WARNING);
				}
			}

		} catch (Exception e) {
			report.addReportStep("UnCheck the checkbox " + checkBox, "Checkbox " + checkBox + " not found",
					StepResult.FAIL);
		}
	}

	/**
	 * Method to set the window size
	 * 
	 * @param height
	 * @param width
	 */
	public void resizeWindow(int width, int height) {

		Dimension dimension = new Dimension(width, height);
		// Resize current window to the set dimension
		driver.manage().window().setSize(dimension);

	}

	/**
	 * Method to maximize window
	 */
	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public String getCurrentDate() {
		Date date = new Date();
		String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return modifiedDate;
	}

	public String getNmberOfDaysBetweenDates(String beforeDate, String afterDate) {

		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
		String dateBeforeString = beforeDate;
		String dateAfterString = afterDate;
		float daysBetween = 0;

		try {
			Date dateBefore = myFormat.parse(dateBeforeString);
			Date dateAfter = myFormat.parse(dateAfterString);
			long difference = dateAfter.getTime() - dateBefore.getTime();
			daysBetween = (difference / (1000 * 60 * 60 * 24));
			/*
			 * You can also convert the milliseconds to days using this method float
			 * daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
			 */
			System.out.println("Number of Days between dates: " + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween + "";
	}

	/**
	 * @return Method to get the scroll position on the page.
	 */
	public Long getScrollPosition() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Long value = (Long) executor.executeScript("return window.pageYOffset;");
		return value;
	}

	/**
	 * Method to set the size to iphone view.
	 */
	public void setIphoneWindow() {
		driver.manage().window().setSize(new Dimension(414, 736));
	}

	/**
	 * Method to set the size to Ipad view.
	 */
	public void setIpadScreen() {
		driver.manage().window().setSize(new Dimension(786, 1024));
	}

	/**
	 * Method to set the size to Ipad Pro view.
	 */
	public void setIpadProScreen() {
		driver.manage().window().setSize(new Dimension(1024, 1366));
	}

	/**
	 * Method to set the size to Galaxy S5 view.
	 */
	public void setGalaxyS5Window() {
		driver.manage().window().setSize(new Dimension(360, 640));
	}

	public void moveToElementAndClick(By elementBy) {

		Actions act = new Actions(driver);
		WebElement mainMenu = driver.findElement(elementBy);
		act.moveToElement(mainMenu).perform();
		act.moveToElement(mainMenu).click().perform();
	}

	public void moveToElementAndClick(WebElement elementBy) {

		Actions act = new Actions(driver);
		// WebElement mainMenu = driver.findElement(elementBy);
		act.moveToElement(elementBy).perform();
		act.moveToElement(elementBy).click().perform();
	}
	
	/**
	 * Author shiragar
	 * Method check the web element is enabled or disabled 
	 * @param elementBy
	 * @return
	 */
	public boolean isElementEnabled(By elementBy) {
		return driver.findElement(elementBy).isEnabled();
	}
	
	/**
	 * Author shriagar
	 * @param dropDownElement
	 * @return
	 * Method return all the option webelement of dropdown
	 */
	public List<WebElement> getOptions(By dropDownElement){
		
		WebElement dropdown = driver.findElement(dropDownElement); 
        Select select = new Select(dropdown); 
       java.util.List<WebElement> options = select.getOptions(); 
       
       return options;
	}
	
	/**
	 * @author shiragar
	 * @param value
	 * @param element
	 * 
	 * Method to set attribute
	 */
	public void setAttribute(String value,By element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement webelement = driver.findElement(element);
		js.executeScript("arguments[0].setAttribute('value', '"+value+"')",webelement);

	}
	
	public boolean getFlag(String flagName) {
		Properties prop = new Properties();
		FileReader reader;
		try {
			reader = new FileReader(HelperClass.baseModel.getRunConfigPath());
			prop.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			report.addReportStep("Read the flag value from prperties file", "Flag vale not found", StepResult.FAIL);
		} catch (IOException e) {
			e.printStackTrace();
			report.addReportStep("Read the flag value from prperties file", "Flag vale not found", StepResult.FAIL);
		}
		
		if(prop.getProperty(flagName).equals("true"))
			return true;
		else
			return false;
	}
	
	/*
	 * @author: Atul Singh
	 * @since: 15 June 2022
	 * @description: this method is used to enter text by characters
	 * @param: elementBy, typeValue
	 * @return:
	 */
	public void sendKeysByCharacter(By elementBy, String typeValue) {

		try {
			if (isElementPresent(elementBy)) {

				WebElement element = driver.findElement(elementBy);
				element.clear();

				for (int i = 0; i < typeValue.length(); i++) {
					char c = typeValue.charAt(i);
					String s = new StringBuilder().append(c).toString();
					element.sendKeys(s);
					waitForPageToLoad();
					Thread.sleep(500);
				}

			}
		} catch (Exception e) {
			THDLogger.getInstance().info("Element: " + elementBy.toString() + " not found" + elementBy);
			report.addReportStep("Sending string keys for Web element: " + elementBy, "Web element not found: ",
					StepResult.FAIL);
		}
	}
	
	/*
	 * @author: Atul Singh
	 * @since: 27 June 2022
	 * @description: this method is used to wait for element to disappear
	 * @param: element, time 
	 * @return:
	 */
	public void waitForElementToDisapper(WebElement element, int time) throws Exception {
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.invisibilityOf(element));
	}
}
