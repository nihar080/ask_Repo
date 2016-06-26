package com.SeleniumFramework.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
//import org.openqa.selenium.WebDriverBackedSelenium;
//import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.SeleniumFramework.commons.util.ExcelFileUtil;

@RunWith(BlockJUnit4ClassRunner.class)

public class DriverClassTest extends FunctionalLibrary {
	public String envFile, screenShotRep, detailedRep;
	public String tcStatus, moduleName, cCellData, dCellData, eCellData, chromedriver;
	public String objectName, objName, xpathProperty, reportzip, rvg;
	public int screenshotflag, screenshotCount, callactionFlag, reportFlag;
	public static String line2 = "";
	FunctionalLibrary seleniumHandler;

	ExcelFileUtil excelFileUtil = ExcelFileUtil.getInstance();

	TestExecutor testExecutor = new TestExecutor();

	@Test
	public void newTest() throws Exception {
		if (excelFileUtil.driverInstance.equalsIgnoreCase("WebDriver")) {
			seleniumHandler = new FunctionalLibrary();
		}

		rvg = "";
		String JTESTEXL = "SeleniumFramework\\Test_Excel\\Tester.xls";
		fileOut = new FileOutputStream(JTESTEXL);
		workbook = new HSSFWorkbook();
		worksheet = workbook.createSheet("TestResult");

		importData();

		Date now = new Date();
		zipdate = DateFormat.getDateTimeInstance().format(now).toString();
		zipdate = zipdate.replaceAll(":", "_");
		File zipfolder = new File("SeleniumFramework\\TestExecutionZip_Reports");
		if (!zipfolder.exists()) {
			zipfolder.mkdir();
		}

		reportzip = "SeleniumFramework\\TestExecutionZip_Reports\\" + excelFileUtil.result_backup_name + "_" + zipdate
				+ ".zip";
		excelFileUtil.zipDir(reportzip, excelFileUtil.htmlRep, zipdate);

		System.out.println("Total Testcases Executed: " + totalTCount);
		System.out.println("Failed Test Cases: " + failedTCount);
		File deldr = new File("SeleniumFramework\\Test_Reports\\Test_Reports_" + zipdate);
		excelFileUtil.deleteDir(deldr);

		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();

		JasperReportExecut();
	}

	/**
	 * importData method gets the location/Path of Test Suite, Test Modules,
	 * Element Collection files
	 * 
	 * @param testUtility
	 *            testUtility contains the path of provider excelsheet where
	 *            paths of all above required files/folders are stored
	 */
	private void importData() {
		try {
			// Checks for platform to start their respective services

			if (excelFileUtil.platform.equalsIgnoreCase("Chrome")) {
				executeForChrome();
			} else if (excelFileUtil.platform.equalsIgnoreCase("FireFox")) {
				executeForFireFox();
			} else if (excelFileUtil.platform.equalsIgnoreCase("IExplorer")) {
				executeForIEExplorer();
			} else if (excelFileUtil.platform.equalsIgnoreCase("All")) {
				executeForIEExplorer();
				Thread.sleep(3000);
				executeForFireFox();
				Thread.sleep(3000);
				executeForChrome();

			} else if (excelFileUtil.platform.equalsIgnoreCase("Sauce")) {// Use this if you want to run on sauceLab
				executeForSauce();
			} else {
				LOG_VAR = 0;
			}
			BufferedWriter out_result = new BufferedWriter(new FileWriter(reportDate, true));
			out_result.newLine();
			out_result.write("</table>");
			out_result.close();
			BufferedWriter out_detailedResult = new BufferedWriter(new FileWriter(reportLog, true));
			out_detailedResult.newLine();
			out_detailedResult.write("</table>");
			out_detailedResult.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception from ImportData Function: " + e.getMessage());
		}

	}

	private void executeForChrome() throws Exception {

		if (excelFileUtil.environment.equalsIgnoreCase("Desktop_Web")) {
			String ss;
			ss = "SeleniumFramework\\lib\\chromedriver.exe";
			System.out.println("SS: " + ss);
			System.setProperty("webdriver.chrome.driver", ss);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, ss);
			ChromeDriverService service = ChromeDriverService.createDefaultService();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-extensions");
			driver = new ChromeDriver(service, options);

			selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(driver, "http://www.google.com");
			Thread.sleep(3000);
			System.out.println("Platform for URL: " + selenium.getEval("navigator.userAgent"));
			String rv = selenium.getEval("navigator.userAgent");
			try {
				if (rvg.equalsIgnoreCase("")) {
					rvg = "Chrome" + rv.split("Chrome/")[1].split(" ")[0];
				} else {
					rvg = rvg + "_Chrome" + rv.split("Chrome/")[1].split(" ")[0];
				}

				rv = "Chrm_" + rv.split("Chrome/")[1].split(" ")[0].split("\\.")[0];

			} catch (Exception e) {
				rv = "Chrm";
				rvg = "Chrm";
			}
			excelFileUtil.tmpBrowserVer = rv;
			PREVIOUS_TEST_CASE = "Before Test Execution";

			testExecutor.testSuite(moduleName, reportLib, seleniumHandler, rv);

			LOG_VAR = 1;
			TEST_STEP_COUNT = 1;
			failedStep = " ";
			service.stop();
			driver.close();
			driver.quit();

		}

	}

	private void executeForFireFox() throws Exception {
		excelFileUtil.platform = "Firefox";
		tmpPlatform = excelFileUtil.platform;

		FirefoxProfile profile = new FirefoxProfile();

		// reportLib.driver.close();

		if (excelFileUtil.environment.equalsIgnoreCase("Desktop_Web")) {
			driver = new FirefoxDriver(profile);
			selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(driver, "http://www.google.com");
			PREVIOUS_TEST_CASE = "Before Test Execution";
			Thread.sleep(5000);
			System.out.println("TesInfo: Platform for URL: " + selenium.getEval("navigator.userAgent"));
			String rv = selenium.getEval("navigator.userAgent");
			try {
				if (rvg.equalsIgnoreCase("")) {
					rvg = "FF" + rv.split("Firefox/")[1];
				} else {
					rvg = rvg + "_FF" + rv.split("Firefox/")[1];
				}

				rv = "FF_" + rv.split("Firefox/")[1];

			} catch (Exception e) {
				rv = "FF";
				rvg = "FF";
			}
			oldTab = driver.getWindowHandle();

			excelFileUtil.tmpBrowserVer = rv;
			testExecutor.testSuite(moduleName, reportLib, seleniumHandler, rv); // Calls
																				// testSuite
																				// method
																				// with
																				// new
																				// user
																				// agent
																				// each
																				// time
			PREVIOUS_TEST_CASE = "Before Test Execution";
			LOG_VAR = 1;
			TEST_STEP_COUNT = 1;
			failedStep = " ";
			testcaseCounter = 0;
			driver.close();
			driver.quit();
		}

	}

	private void executeForIEExplorer() throws Exception {
		excelFileUtil.platform = "IExplorer";
		tmpPlatform = excelFileUtil.platform;
		String ss = new File("SeleniumFramework\\lib\\IEDriverServer.exe").getCanonicalPath();
		System.setProperty("webdriver.ie.driver", ss);
		DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
		capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		driver = new InternetExplorerDriver(capab);
		selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(driver, "http://www.google.com");
		String rv = selenium.getEval("navigator.userAgent");

		try {
			if (rvg.equalsIgnoreCase("")) {
				rvg = "IE" + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];
			} else {
				rvg = rvg + "_IE" + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];
			}

			rv = "IE_" + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];

		} catch (Exception e) {
			rv = "IE";
			rvg = "IE";
		}
		excelFileUtil.tmpBrowserVer = rv;
		PREVIOUS_TEST_CASE = "Before Test Execution";
		Thread.sleep(5000);

		testExecutor.testSuite(moduleName, reportLib, seleniumHandler, rv); // Calls
																			// testSuite
																			// method
																			// with
																			// new
																			// user
																			// agent
																			// each
																			// time

		PREVIOUS_TEST_CASE = "Before Test Execution";

		LOG_VAR = 1;
		TEST_STEP_COUNT = 1;
		failedStep = " ";
		testcaseCounter = 0;

		driver.close();
		driver.quit();
	}

	/**
	 * @throws Exception
	 */
	private void executeForSauce() throws Exception {
		// System.out.println(System.getenv("SELENIUM_BROWSER"));
		// System.out.println(System.getenv("SELENIUM_VERSION"));
		// System.out.println(System.getenv("SELENIUM_PLATFORM"));
		// System.out.println(System.getenv("SAUCE_ONDEMAND_BROWSERS"));
		JSONParser parser = new JSONParser();
		JSONArray ja = (JSONArray) parser.parse(System.getenv("SAUCE_ONDEMAND_BROWSERS"));
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		JSONObject js = null;
		for (Object object : ja) {
			js = (JSONObject) object;
			desiredCapabilities.setBrowserName(js.get("browser").toString());
			desiredCapabilities.setVersion(js.get("browser-version").toString());
			desiredCapabilities.setCapability(CapabilityType.PLATFORM, js.get("platform").toString());
			desiredCapabilities.setCapability("build",
					System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));
			executeSauceOneByOne(desiredCapabilities);
		}
	}

	/**
	 * @param desiredCapabilities
	 * @throws Exception
	 * 
	 */
	private void executeSauceOneByOne(DesiredCapabilities desiredCapabilities) throws Exception {
		driver = new RemoteWebDriver(new URL("http://" + System.getenv("SAUCE_USERNAME") + ":"
				+ System.getenv("SAUCE_ACCESS_KEY") + "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
		selenium = new com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium(driver, "http://www.google.com");
		String rv = selenium.getEval("navigator.userAgent");

		try {
			if (rvg.equalsIgnoreCase("")) {
				rvg = desiredCapabilities.getBrowserName() + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];
			} else {
				rvg = rvg + "_Sauce" + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];
			}

			rv = "Sauce_" + rv.replaceAll("\\s+", "").split("MSIE")[1].split(";")[0];

		} catch (Exception e) {
			rv = "Sauce";
			rvg = "Sauce";
		}
		excelFileUtil.tmpBrowserVer = rv;
		PREVIOUS_TEST_CASE = "Before Test Execution";
		Thread.sleep(5000);

		testExecutor.testSuite(moduleName, reportLib, seleniumHandler, rv); // Calls
																			// testSuite
																			// method
																			// with
																			// new
																			// user
																			// agent
																			// each
																			// time

		PREVIOUS_TEST_CASE = "Before Test Execution";

		LOG_VAR = 1;
		TEST_STEP_COUNT = 1;
		failedStep = " ";
		testcaseCounter = 0;

		driver.close();
		driver.quit();
	}

}