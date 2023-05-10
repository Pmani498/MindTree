package cdwpom;


	import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlbeans.impl.tool.XSTCTester.TestCase;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.DataProvider;

import com.jaf.enums.EnumClass.OverAllResult;
import com.jaf.enums.EnumClass.RunType;
import com.jaf.enums.EnumClass.StepResult;
import com.jaf.helper.DataHelper;
import com.jaf.helper.HelperClass;
import com.jaf.logger.THDLogger;
import com.jaf.reports.Report;
import com.jaf.setup.TestSetUp;
import com.mysql.cj.jdbc.Driver;
import com.test.Program;

	public class BaseExecutionClass {

		public Import report;
		public DataProvider dataHelper;
		public RemoteWebDriver driver;
		private TestCase testCase;

		/**
		 * 
		 * Method to get report instance
		 * 
		 * @return Report
		 */
		public Report getReport() {

			return report;
		}

		/**
		 * Method to set report field
		 * 
		 * @param report
		 */
		public void setReport(Report report) {

			this.report = report;
		}

		/**
		 * Method to return datalper instance
		 * 
		 * @return DataHelper
		 */
		public DataHelper getDataHelper() {

			return dataHelper;
		}

		/**
		 * Method to set dataHelper field
		 * 
		 * @param dataHelper
		 */
		public void setDataHelper(DataHelper dataHelper) {

			this.dataHelper = dataHelper;
		}

		/**
		 * Method to get selenium driver instance
		 * 
		 * @return RemoteWebdriver
		 */
		public RemoteWebDriver getDriver() {

			return driver;
		}

		/**
		 * Method to set driver field
		 * 
		 * @param driver
		 */
		public void setDriver(RemoteWebDriver driver) {

			this.driver = driver;
		}

		/**
		 * Method to get current test case instance
		 * 
		 * @return TestCase
		 */
		public TestCase getTestCase() {

			return testCase;
		}

		/**
		 * Method to set test case field
		 * 
		 * @param testCase
		 */
		public void setTestCase(TestCase testCase) {

			this.testCase = testCase;
		}

		/**
		 * empty constructor
		 */
		public BaseExecutionClass() {

		}

		/**
		 * constructor overload
		 * 
		 * @param currentTestCase
		 * @throws IOException
		 * @throws Exception
		 */
		public BaseExecutionClass(TestCase currentTestCase) throws IOException {

			this.testCase = currentTestCase;

			this.dataHelper = TestSetUp.getDataHelper(currentTestCase);

			Driver driverClass = new Driver();
			// this.driver = driverClass.getWebDriver(currentTestCase);

			// Added code for re-execution of test cases in same browser

			if (Program.prop.get("Debugging").equals("No")) {
				this.driver = driverClass.getWebDriver(currentTestCase);

				if (this.driver != null) {
					// Method to store the driver session ID
					HttpCommandExecutor executor = (HttpCommandExecutor) driver.getCommandExecutor();
					URL url = executor.getAddressOfRemoteServer();

					System.out.println("URL: " + url);
					SessionId session_id = driver.getSessionId();
					System.out.println("Sesssion ID: " + session_id);

					HashMap<String, String> sessionDetails = new HashMap<String, String>();
					sessionDetails.put("Session ID", session_id.toString());
					sessionDetails.put("URL", url.toString());

					driverClass.setDriverSession(sessionDetails);
				}
			} else if (Program.prop.get("Debugging").equals("Yes")) {

				// Method to get the driver session ID from Json file.
				ArrayList<String> driverDetails = driverClass.getDriverSession();
				THDLogger.getInstance().info(".............Execution Started in debuggin Mode.......");
				URL prevUrl = new URL(driverDetails.get(0));
				this.driver = driverClass.reInvokeDriver(new SessionId(driverDetails.get(1)), prevUrl);
			}

			this.report = new Report(currentTestCase, this.driver);
			report.driver = this.driver;

		}

		/**
		 * Method to generate footer for reports
		 */
		public void generateFooter() {

			report.generateFooter();
		}

		/**
		 * Method to generate error step in report
		 */
		public void addErrorStep() {

			report.addErrorStep();
		}

		/**
		 * Method to generate error step in report
		 */
		public void browserClosedStep() {

			report.browserClosedStep();
		}

		/**
		 * Method to end test case
		 */
		public void endTestCase() {
			if (Program.prop.get("Debugging").equals("No")) {
				if (HelperClass.baseModel.getRunOnSauceLabs() && testCase.getSauceLabFlag()) {

					if (HelperClass.baseModel.getExecutionRunType() == RunType.SauceLabs) {

						boolean isTestPass = false;
						if (testCase.getOverAllResult().equals(OverAllResult.PASS)) {
							isTestPass = true;
						}
						SessionId session = driver.getSessionId();
						if (session != null)
							report.addReportStep("Execution Session ID: " + session.toString(), "Seesion is captured.",
									StepResult.PASS);
						((JavascriptExecutor) getDriver())
								.executeScript("sauce:job-result=" + (isTestPass ? "passed" : "failed"));
						if (getDriver() != null) {
							if (getDriver().getWindowHandles().size() > 1) {
								for (String handle : getDriver().getWindowHandles()) {
									getDriver().switchTo().window(handle);
									getDriver().quit();
								}
							} else {
								getDriver().quit();
							}
						}
						// getDriver().quit();
						// ((JavascriptExecutor)getDriver()).executeScript("sauce:job-result = " +
						// testResult);
					}
				}

				try {

					Driver.quitDriver(driver, testCase.getSauceLabFlag());

					if (HelperClass.baseModel.getExecutionRunType() == RunType.GridParallel) {

						testCase.getGridNode().isAvailable = true;
					}

				} catch (Exception ex) {

					System.out.println(ex.getMessage());
				}
			}
		}

		/**
		 * Method to set current iteration count
		 * 
		 * @param currentIteration
		 */
		public void setToCurrentIteration(int currentIteration) {

			dataHelper.setToCurrentIteration(currentIteration);
			report.setCurrentIteration(currentIteration);
		}

		/**
		 * Method to set total iteration count
		 * 
		 * @param totalIterationCount
		 */
		public void setTotalIterationCount(int totalIterationCount) {

			report.setTotalIterationCount(totalIterationCount);
		}

		/**
		 * Method to generate header for reports
		 */
		public void generateHeader() {

			report.generateHeader();
		}

		/**
		 * Method to get driver instance
		 */
		public void getCurrentDriver() {

			Driver driverClass = new Driver();
			driver = driverClass.getWebDriver(testCase);
		}

		/**
		 * Method to close browser
		 */
		public void quitDriver() {

			try {

				Driver.quitDriver(driver, testCase.getSauceLabFlag());

			} catch (Exception ex) {

				System.out.println(ex.getMessage());
			}
		}
	}
