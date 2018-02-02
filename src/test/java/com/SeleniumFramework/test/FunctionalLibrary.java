package com.SeleniumFramework.test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.SeleniumFramework.commons.util.ConnectionHelper;
import com.SeleniumFramework.commons.util.ExcelFileUtil;
import com.SeleniumFramework.commons.util.ResponseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.document.DocumentDescriptor;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonHandle;
import com.thoughtworks.selenium.Selenium;

import junit.framework.Assert;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class FunctionalLibrary extends ReportLibrary {

	private static String host;
	private static Integer port;
	private static String username;
	private static String password;
	private static String database;

	// ADDED SOME FIELDS FOR API//
	public String APIurl = "";
	public List<String> APIheader = new ArrayList<String>();
//	public String APIheader = "";
	public String APImethod = "";
	public String APIpayload = "";
	String APIactualResponse = "";

	// private static DatabaseClient client;

	final String fulfillmentCollectionName = "FulfillmentRequestTracking";
	final String DispatchCollectionName = "DispatchRecord";
	private DocumentDescriptor desc;
	private DatabaseClient client = getClient();
	private JacksonHandle jacksonHandle = new JacksonHandle();
	private JSONDocumentManager documentManager = client.newJSONDocumentManager();
	private DocumentMetadataHandle fulfillmentMetadata = new DocumentMetadataHandle()
			.withCollections(fulfillmentCollectionName);
	private DocumentMetadataHandle dispatchRecordMetadata = new DocumentMetadataHandle()
			.withCollections(DispatchCollectionName);

	public static String PREVIOUS_TEST_CASE = "Before Test Execution";
	protected static int LOG_VAR = 1;
	protected String testFlag, environment;
	protected ReportLibrary reportLib = new ReportLibrary();
	protected static boolean LOOP_FLAG = false;

	protected HSSFWorkbook scriptWorkbook;
	protected HSSFSheet readScriptSheet, readLoopSheet, readtestcasesheet, readactionsheet;
	protected ChromeDriverService chromeService;
	protected DesiredCapabilities capabilities;

	public static String url, tcStartTime, uaMain;
	public static String fieldName, fieldValue, inputSheet, testModulePath;
	public static String screenName, fieldElementType, fieldName2;
	protected int currTestRowPtr, startRow, endRow;
	protected String TC_ID = null;
	protected String TC_DESC = null;
	public static int TEST_STEP_COUNT = 1, tempCounter = 1, totalTCount = 0, testcaseCounter = 1, TCCounter = 1;
	public static int failFlag;
	ExcelFileUtil excelFileUtil = ExcelFileUtil.getInstance();

	protected static String fValue_tmp = "";
	public static Selenium Sel_driver, sel;
	public static String BrwsrBsln;
	public static POIFSFileSystem poifs3;
	// public static WebDriverBackedSelenium driver_bkd=null;
	public static int synctime, Longsynctime;
	public static HashMap<String, String> dataholder = new HashMap<String, String>(); // HashMap
																						// for
																						// DataHold
	public static Robot robo;
	public static String ACTUALPARA = "";// Used in
											// Geteleproperty,Asserteleproperty
											// Keywords
	public static String DVARIABLE = "";
	public static WebDriverWait wait;
	public static String BURL;

	/**
	 * Method keyword: This method has different keywords definition in nested
	 * if statement
	 * 
	 * @param fieldName
	 * @param objName:
	 *            contains object name on which action is to be performed
	 * @param feType:
	 *            contains field element type by which the object will be found
	 *            by selenium.
	 * @param fValue:
	 *            contains value to be put/verified into/with field.
	 * @param action:
	 *            contains the keyword with the help of which action is being
	 *            performed.
	 * @throws InterruptedException
	 * @throws IOException
	 */

	public enum KeywordActions {
		OpenURL, Input, Click, CheckByIndex, SetCheckBox, ClearAndType, Clear, WaitTime, // General
																							// Keywords
		AddAPIurl, AddAPImethod, AddAPIheader, GetAPIResponse, CheckAPIResponse, // API
																					// Keywords
																					// added
																					// by
																					// Naveen
		VerifyElementExists, VerifyElementByValue, VerifyElementProperty, VerifyURL, // Verification
																						// Keywords
		VerifyTextPresent, VerifyLink, VerifyMultiLinks, VerifyFalseEleExist, VerifyAlertText, VerifyPageSource,

		SelectIndxValTxt, // Select operations

		HoldelementValue, SendelementValue, Geteleproperty, Asserteleproperty, // Data
																				// Hold
																				// and
																				// Verification
																				// Keywords

		ClosewindowByTitle, CloseBrowser, SwitchToWindow, VerifywindowTitle, clickLinkUsingDisplayName,// Window
																				// and
																				// Browser
																				// Related
																				// Keywords

		Actionclick, MouseHoverclick, MouseHoverJs, // MouseHover Keywords
													// (Action class)

		TypeRandomNbr, HandleAlert, KeyEvent, GetPreviousDate,

		NavigateBack, ClosePDF, JscriptExecutor, VerifyTableRowCount, MouseHover, CloseReminder, JsClick, UncheckAllSelectbyIndex, OptionalClick, CloseAddEditAlerts, SwitchToframe, // Other
																																														// Keywords

		CloudSSOLogin, ValidateResponse, ValidateResponseExcel, ValidateB2CPrior, AngJsClick, AngJsInput, AngJsVerifyElemExists, AngJsVerifyLink, AngJsVerifyTextPresent, AngJsSelectIndxValTxt, AngJSVerifyTextInput, MarklogicDBConnc, SecurityCheck, JsScroll;

	}

	public void keyword(String objName, String feType, String fValue, String action, String fieldName)
			throws InterruptedException, IOException {

		try {

			wait = new WebDriverWait(driver, 120);
			synctime = 1000;Longsynctime = 10000;
			KeywordActions Action = KeywordActions.valueOf(action);

			switch (Action) {

			case AddAPIurl:
				APIurl = fValue;
                URL url = new URL(
//                		FunctionalLibrary.url
                		"http://dispatcher-tmptst1.ose.optum.com/uhcm/beneficiary.html"
                		);
                String host = url.getHost();
                URL callingURL = new URL(fValue);
                String myCalledURL = callingURL.toString().replace(callingURL.getHost(), url.getHost());
                callingURL = new URL(myCalledURL);
//              System.out.println("Current Host: "+callingURL.getHost());
                
                System.out.println("Calling URL: "+ callingURL.toString());


				break;
			case AddAPImethod:
				APImethod = fValue;
				break;
			case AddAPIheader:
//				APIheader = fValue;
				APIheader.add(fValue);
				break;
			case GetAPIResponse:
				getAPIresponse(fValue);
				break;

			case CheckAPIResponse:
				checkAPIresponse(fValue);
				break;

			case OpenURL:
				if (System.getenv("SELENIUM_HOST") != null) {
					System.out.println("######### SELENIUM_HOST: "+System.getenv("SELENIUM_HOST"));
					FunctionalLibrary.url = FunctionalLibrary.url.replace(new URL(FunctionalLibrary.url).getHost(),
							(new URL(System.getenv("SELENIUM_HOST"))).getHost());
//					System.out.println("######### Calling URL: "+FunctionalLibrary.url);
				}
				fValue_tmp = FunctionalLibrary.url;
				funcOpenUrl(feType, objName, fValue);
				Thread.sleep(synctime);
				break;
			case Click:

				funcClick(feType, objName, fValue);
				// Thread.sleep(Longsynctime);Thread.sleep(3000);
//				Thread.sleep(Longsynctime);
				System.out.println("Click Performed !!!");
				break;
			case Input:
				funcInput(feType, objName, fValue);
				Thread.sleep(synctime);
				break;

			case ClearAndType:
				funClearthnType(feType, objName, fValue);
				break;

			case CheckByIndex:
				funcCheckByIndex(feType, objName, fValue);
				Thread.sleep(synctime);
				break;

			case UncheckAllSelectbyIndex:
				funcUncheckAll(feType, objName, fValue);
				Thread.sleep(synctime);
				break;

			case VerifyElementExists:
				funcVerify(feType, objName);
				Thread.sleep(synctime);
				break;

			case VerifywindowTitle:
				funcVerifyWin(feType, objName, fValue);
				break;

			case ClosewindowByTitle:
				funcCloseWin(feType, objName, fValue);
				break;

			case SetCheckBox:
				funcSetcheck(feType, objName, fValue);
				break;

			case VerifyElementByValue:
				funVerifyfieldValue(feType, objName, fValue);
				break;

			case VerifyURL:
				funcVerifyURL(fValue);
				break;

			case SelectIndxValTxt:
				funcSelectData(feType, objName, fValue);
				break;

			case VerifyTextPresent:
				funTextpresent(feType, objName, fValue);
				break;

			case VerifyAlertText:
				funAlertText(feType, objName, fValue);
				break;

			case HoldelementValue:
				funHoldvalue(feType, objName, fValue);
				break;

			case clickLinkUsingDisplayName:
				clickLinkByName(fValue);
				break;
			case SendelementValue:
				funSendValue(feType, objName, fValue);
				break;

			case VerifyElementProperty: // Data Holder
				funElementProp(feType, objName, fValue);
				break;

			case VerifyLink:
				funLink(feType, objName, fValue);
				break;

			case VerifyMultiLinks:
				funMultiLinks(feType, objName, fValue);
				break;

			case TypeRandomNbr:
				funGenrtNumbr(feType, objName, fValue);
				break;

			case HandleAlert:
				funHandleAlert(feType, fValue);
				break;

			case CloseAddEditAlerts:
				funCloseAddEditAlerts(feType, objName, fValue);
				break;

			case Clear:
				funClear(feType, objName, fValue);
				Thread.sleep(synctime);
				break;

			case VerifyFalseEleExist:
				funVerifyFals(feType, objName, fValue);
				Thread.sleep(synctime);
				break;

			case KeyEvent:
				robo = new Robot();
				funKeyEvents(feType, objName, fValue);
				break;

			case Actionclick:
				actionsClick(feType, objName, fValue);
				break;

			case WaitTime:
				Thread.sleep(synctime);
				Thread.sleep(Longsynctime);

				break;

			case CloseBrowser:
				funBrowserclose();
				break;

			case Geteleproperty:
				funGetprop(feType, objName, fValue);
				break;

			case Asserteleproperty:
				funChkprop(feType, objName, fValue);
				break;

			case NavigateBack:
				driver.navigate().back();
				Thread.sleep(synctime);
				driver.navigate().refresh();
				break;

			case SwitchToWindow:
				funSwitchWin(feType, objName, fValue);
				break;

			case SwitchToframe:
				funSwitchframe(feType, objName, fValue);
				break;

			case ClosePDF:
				funClosePDF();
				break;

			case MouseHoverclick:
				funmouseHoverClick(feType, objName, fValue);
				break;

			case MouseHover:
				funmouseHover(feType, objName, fValue);
				break;

			case MouseHoverJs:
				funMouseHoverJs(feType, objName, fValue);
				break;

			case VerifyTableRowCount:
				funTableRowCount(feType, objName, fValue);
				break;

			case JscriptExecutor:
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("" + fValue + "");
				break;

			case JsScroll:
				JavascriptExecutor js1 = (JavascriptExecutor) driver;
				js1.executeScript("scroll(0,250)", "");
				break;

			case JsClick:
				JSfuncClick(feType, objName);
				Thread.sleep(Longsynctime);
				break;

			case VerifyPageSource:
				funverifyPageSource(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case GetPreviousDate:
				fungetPreviousDate(feType, objName, fValue);
				Thread.sleep(Longsynctime);

			case OptionalClick:
				funOptionalClick(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case ValidateResponse:
				funValidateResponse(inputSheet, feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case ValidateResponseExcel:
				funValidateResponseRxcel(inputSheet, fieldName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case CloudSSOLogin:
				funCloudSSOLogin(inputSheet, feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case ValidateB2CPrior:
				funB2CPrior(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case AngJsClick:
				funcAngJSClick(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case AngJsInput:
				funcAngJSInput(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyElemExists:
				funcAngJSVerfiyElemExists(feType, objName);
				Thread.sleep(Longsynctime);
				break;

			case AngJsSelectIndxValTxt:
				funcAngJSSelectData(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyLink:
				funAngJsVerifyLink(feType, objName, fValue);
				Thread.sleep(Longsynctime);
				break;

			case AngJsVerifyTextPresent:
				funAngJsVerifyTextpresent(feType, objName, fValue);
				break;
			case AngJSVerifyTextInput:
				funcAngVerifyTextInput(feType, objName, fValue);
				break;
			case MarklogicDBConnc:
				funMarklogicDBConnc();
				break;
			case SecurityCheck:
				funSecurityAnsw(feType, objName, fValue);
				break;

			}
		} catch (Exception e) {
			LOG_VAR = 0;
			// testFlag="n"
			System.out.println("########## EXCEPTION OCCURED ############");
			System.out.println(e.toString());
			String newline = System.getProperty("line.separator");
			String Trace = "Exception thrown from Keyword method : " + newline + e.getMessage();
			sendLog(Trace, PREVIOUS_TEST_CASE, TEST_STEP_COUNT);
		}
	}

	private void funMarklogicDBConnc() {
		// TODO Auto-generated method stub

		getClient();
		JacksonHandle jacksonHandle = null;
		// FulfillmentDAOImpl daoImpl = new FulfillmentDAOImpl();
		// jacksonHandle = daoImpl.readFulfillmentRecordById("test332");
		jacksonHandle = readFulfillmentRecordById("ISLTEST13");

		JsonNode node = jacksonHandle.get();
		System.out.println("Root Node" + node);
		// System.out.println(jacksonHandle.get().get("requestHeader"));

		// objMap.

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			// System.out.println("field Key :" + field.getKey());
			// System.out.println("field Value :" + field.getValue());

			if (field.getKey() == "requestHistory") {

				JsonNode innerNode = field.getValue();
				System.out.println("Inner key for Request history" + innerNode);
				findArrNodeValue(innerNode);

			}

			if (field.getKey() == "fulfillmentRequest") {

				JsonNode innerNode = field.getValue();

				System.out.println("Inner key for Request header " + innerNode);
				findNodeValue(innerNode);

			}

		}
		// System.out.println(node.fields());

		// System.out.println(jacksonHandle.get().get("fulfillmentRequest").get("requestHeader"));

	}

	public DatabaseClient getClient() {
		try {

			// client = DatabaseClientFactory.newClient(host, port, database,
			// username, password, Authentication.BASIC);

			client = DatabaseClientFactory.newClient("dbsrt0998.uhc.com", 8000, "FSL_SYS", "fslmlnpd@ms", "2Jvr34DH",
					Authentication.BASIC);

			// LOGGER.debug("MarkLogic connection is created");
			System.out.println();

		} catch (Exception e) {
			// LOGGER.error("Error occured during connection establishment."+e);
		}
		return client;
	}

	private void funSecurityAnsw(String feType, String objName, String fValue) throws InterruptedException {

		try {
			WebElement Online = driver.findElement(By.id("authQuestionWrapper"));

			if (Online.isDisplayed()) {
				WebElement SecurityQuestion = driver.findElement(By.xpath("//label[@id='authQuestiontextLabelId']"));
				String QuestionName = SecurityQuestion.getText();

				String Answers = fValue;
				HashMap<String, String> hm = new HashMap<String, String>();

				String[] Values = Answers.split(";");
				for (int i = 0; i < Values.length; i++) {
					String[] QuestionAnswer = Values[i].split("#");
					hm.put(QuestionAnswer[0], QuestionAnswer[1]);
				}
				Set<String> keys = hm.keySet();
				for (String key : keys) {
					System.out.println("Value of " + key + " is: " + hm.get(key));
				}

				if (QuestionName.contains("friend")) {
					fValue = hm.get("friend");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("favorite")) {
					fValue = hm.get("favorite");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("color")) {
					fValue = hm.get("color");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("school")) {
					fValue = hm.get("school");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("phone")) {
					fValue = hm.get("phone");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("sport")) {
					fValue = hm.get("sport");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else if (QuestionName.contains("mother")) {
					fValue = hm.get("mother");
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys(fValue);
				} else {
					driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys("answer1");
				}

				driver.findElement(By.id("authQuestionSubmitButton")).click();

				Thread.sleep(synctime);
				Thread.sleep(synctime);
			}

		} catch (Exception e) {
			System.out.println("TestInfo >> Online Security Page Not displayed");
		}

		Thread.sleep(synctime);
		Thread.sleep(synctime);

		try {
			Thread.sleep(50000);
			WebElement LinkLogo = driver.findElement(By.xpath("//h3[contains(text(),'Welcome')]"));
			System.out.println("TestInfo >> Home page is Displayed !!");

		} catch (Exception e) {
			System.out.println("TestInfo >> Hompe page Not Displayed !!");
		}

		/*
		 * try{ driver.switchTo().defaultContent(); for (String Handle :
		 * driver.getWindowHandles()) {
		 * System.out.println(driver.switchTo().window(Handle).getTitle());
		 * String Title = driver.switchTo().window(Handle).getTitle();
		 * if(Title.equals("Provider Portal")) {
		 * Thread.sleep(synctime);Thread.sleep(synctime);
		 * driver.switchTo().window(Handle); System.out.println(
		 * "TestInfo : Successfully Switched to Provider Portal Window!");
		 * break;
		 * 
		 * } }
		 * 
		 * } catch (Exception e) { System.out.println(
		 * "TestErro : Page not shown or Provider Portal is very slow to load !!"
		 * ); }
		 */

	}

	private void funB2CPrior(String feType, String objName, String fValue) throws InterruptedException {
		Thread.sleep(Longsynctime);
		Thread.sleep(Longsynctime);
		driver.findElement(By.xpath("//div[contains(text(),'Continue')]")).click();
		Thread.sleep(Longsynctime);
		funSwitchWin(feType, objName, "United Healthcare Online");
		driver.close();
	}

	private void funCloudSSOLogin(String inputSheet2, String feType, String objName, String fValue)
			throws FileNotFoundException, InterruptedException, IOException {

		funBrowserclose();
		driver.get("https://cloud-test.optum.com/cloudappspot/home.do");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(synctime);
		Thread.sleep(synctime);
		driver.findElement(By.id("login-sendz")).click();
		Thread.sleep(synctime);
		Thread.sleep(synctime);

		try {
			WebElement signinElement = driver.findElement(By.xpath("//h1[contains(text(),'Sign In')]"));

			if (signinElement.isDisplayed()) {
				if (fValue.contains("EBC")) {
					// driver.findElement(By.id("EMAIL")).sendKeys("testinternal1");
					// driver.findElement(By.id("PASSWORD")).sendKeys("password1");
					driver.findElement(By.id("EMAIL")).sendKeys("testinternal1");
					driver.findElement(By.id("PASSWORD")).sendKeys("password1");
				} else {

					// driver.findElement(By.id("EMAIL")).sendKeys("purnima1");
					// driver.findElement(By.id("PASSWORD")).sendKeys("Password!1");

					driver.findElement(By.id("EMAIL")).sendKeys("testoptum1");
					driver.findElement(By.id("PASSWORD")).sendKeys("password1");

				}

				driver.findElement(By.id("submitButton")).click();

				Thread.sleep(synctime);
				Thread.sleep(10000);

				try {
					WebElement OnlineSecurity = driver.findElement(By.xpath("//*[contains(text(),'Online Security')]"));

					if (OnlineSecurity.isDisplayed()) {
						driver.findElement(By.id("challengeQuestionList[0].userAnswer")).sendKeys("answer1");
						driver.findElement(By.id("authQuestionSubmitButton")).click();
						Thread.sleep(synctime);
						Thread.sleep(synctime);
					}
				} catch (Exception e) {
					System.out.println("TestInfo : Security screen not shown!!");
					Thread.sleep(synctime);
					Thread.sleep(50000);
				}

				try {

					Thread.sleep(synctime);
					Thread.sleep(synctime);
					for (String Handle : driver.getWindowHandles())// 2
					{
						System.out.println(driver.switchTo().window(Handle).getTitle());
						String Title = driver.switchTo().window(Handle).getTitle();
						if (Title.equals("Optum Cloud Dashboard")) {
							driver.switchTo().window(Handle);
							System.out.println("TestInfo : Successfully Switched to Window!");
							break;

						}
					}

				} catch (Exception e) {
					System.out.println("TestErroe : Page not shown or URL is very slow to load !!");
				}

				try {
					WebElement frame = driver.findElement(By.xpath("//iframe[contains(@id,'homeFrame')]"));
					WebDriverWait wait = new WebDriverWait(driver, 60);
					wait.until(ExpectedConditions.visibilityOf(frame));
				} catch (Exception e) {
					System.out.println("No Iframe found");
				}
				driver.switchTo().frame("homeFrame");

				if (fValue.contains("EBC")) {
					driver.findElement(By.xpath("//a[text()='Apps']")).click();
					Thread.sleep(synctime);
					Thread.sleep(synctime);
					driver.findElement(By.xpath("//a[contains(@href,'.sr')]//img")).click();
					Thread.sleep(synctime);
					Thread.sleep(synctime);

				} else {
					driver.findElement(By.xpath("//a[contains(@href,'.cr')]//img")).click();
					Thread.sleep(synctime);
					Thread.sleep(synctime);

				}

				try {
					driver.switchTo().defaultContent();
					for (String Handle : driver.getWindowHandles())// 2
					{
						System.out.println(driver.switchTo().window(Handle).getTitle());
						String Title = driver.switchTo().window(Handle).getTitle();
						if (Title.equals("Provider Portal")) {
							Thread.sleep(synctime);
							Thread.sleep(synctime);
							driver.switchTo().window(Handle);
							System.out.println("TestInfo : Successfully Switched to Window!");
							break;

						}
					}

				} catch (Exception e) {
					System.out.println("TestErro : Page not shown or URL is very slow to load !!");
				}
			}

		} catch (Exception e)

		{
			System.out.println("Test Error : Exception Occured");

			Thread.sleep(synctime);
			Thread.sleep(50000);

			for (String Handle : driver.getWindowHandles())// 2
			{
				System.out.println(driver.switchTo().window(Handle).getTitle());
				String Title = driver.switchTo().window(Handle).getTitle();
				if (Title.equals("Optum Cloud Dashboard")) {
					driver.switchTo().window(Handle);
					System.out.println("TestInfo : Successfully Switched to Window!");
					break;

				} else if ((Title.equals("Optum Cloud Dashboard"))) {
					driver.switchTo().window(Handle).close();
					System.out.println("TestInfo : Closing the Parent window !!");
				}
			}

			driver.switchTo().frame("homeFrame");

			driver.findElement(By.xpath("//div[@id='app-dashbrd-page'][1]//li[1]//img")).click();
			Thread.sleep(synctime);
			Thread.sleep(synctime);

			for (String Handle : driver.getWindowHandles())// 2
			{
				System.out.println(driver.switchTo().window(Handle).getTitle());
				String Title = driver.switchTo().window(Handle).getTitle();
				if (Title.equals("ProviderRegistrationPortal")) {
					driver.switchTo().window(Handle);
					System.out.println("TestInfo : Successfully Switched to Window!");
					break;

				}

			}
		}

	}

	private void funValidateResponse(String sheet, String feType, String objName, String fValue)
			throws IOException, InterruptedException {

		String Outputparam = fValue;

		String[] Value = sheet.split("#");

		String Outputparam_Sheet = Value[0];
		String ResponseID = Value[1];

		String Outputfile = "WebServicesAutomation"+File.separator+"Output.xls";
		// fileOut = new FileOutputStream(Outputfile);
		// workbook = new HSSFWorkbook();
		// worksheet = workbook.getSheet("ServiceName");

		FileInputStream afis = new FileInputStream(Outputfile);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet(Outputparam_Sheet);
		String ExpectedValue = "";
		WebElement element;
		element = funcFindElement(feType, objName);

		String TagName = element.getTagName();

		if (TagName.equalsIgnoreCase("input") || TagName.equalsIgnoreCase("select")) {

			ExpectedValue = element.getAttribute("value");
		} else {
			ExpectedValue = element.getAttribute("innertext");
			if (ExpectedValue == null) {
				ExpectedValue = element.getText();
			}

		}

		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(ResponseID)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition,
							FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(Outputparam)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = excelFileUtil.getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);

						if (OutputparamValue.contains(".")) {
							String ValueA = OutputparamValue.substring(0, 4);

							OutputparamValue = ValueA;
						} else if (Outputparam.contains("ProgramValue")) {
							if (OutputparamValue.equals("5")) {
								OutputparamValue = "Declined to Respond";
							} else if (OutputparamValue.equals("6")) {
								OutputparamValue = "Response Not required";
							} else if (OutputparamValue.equals("7")) {
								OutputparamValue = "Does not Apply";
							} else if (OutputparamValue.equals("8")) {
								OutputparamValue = "Unable to calculate score";
							} else if (OutputparamValue.equals("9")) {
								OutputparamValue = "N/A";
							}

						}
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";

			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

				// break;
			} else {
				break;

			}
		}

		if (ExpectedValue.contains(OutputparamValue)) {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Found and Matching ");

		} else {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Not Found ");

		}

	}

	private void funValidateResponseRxcel(String Outsheet, String Testsheet, String fValue)
			throws IOException, InterruptedException {

		String Outputparam = fValue;

		String[] Value = Outsheet.split("#");

		String Outputparam_Sheet = Value[0];
		String ResponseID = Value[1];

		String Outputfile = "WebServicesAutomation"+File.separator+"Output.xls";

		String[] TestSheetValue = Testsheet.split("#");

		String TestRow = TestSheetValue[0];
		String TestColumn = TestSheetValue[1];

		String ExpectedValue = funGetDatafromExcel(TestRow, TestColumn);

		FileInputStream afis = new FileInputStream(Outputfile);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet(Outputparam_Sheet);

		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(ResponseID)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition,
							FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(Outputparam)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = excelFileUtil.getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";

			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

				// break;
			} else {
				break;

			}
		}

		if (ExpectedValue.contains(OutputparamValue)) {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Found and Matching ");

		} else {
			System.out.println("Expected Parameter Value : " + ExpectedValue);
			System.out.println("TestInfo : " + OutputparamValue + " " + "Parameter Not Found ");
			failFlag = 0;
			LOG_VAR = 0;
		}

	}

	public String funGetDatafromExcel(String TestDatasheet_Name, String TestDataheader)
			throws IOException, InterruptedException {

		// String Outputparam = fValue;

		String module = ReportLibrary.strModuleName;
		String TestDatafile_path = "SeleniumFramework"+File.separator+"Test_Modules"+File.separator+"" + module + ".xls";

		System.out.println("Complete Path" + TestDatafile_path);

		FileInputStream afis = new FileInputStream(TestDatafile_path);
		POIFSFileSystem apoifs = new POIFSFileSystem(afis);
		HSSFWorkbook aworkbook = new HSSFWorkbook(apoifs);
		HSSFSheet areadsheet = aworkbook.getSheet("Test_ALPHA");
		String ExpectedValue = "";

		int FIELD_NAME_CLMN_CNTR = 0;
		int FIELD_NAME_CLMN_NO = 0;
		int rowposition = 0;
		boolean Status = false;
		String getFieldNameColumnHeader;
		String OutputparamValue = " ";
		String Rowstatus = "true";

		while (rowposition < 500) {
			getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition, FIELD_NAME_CLMN_CNTR);

			if (getFieldNameColumnHeader.equalsIgnoreCase(TestDatasheet_Name)) {
				Rowstatus = "true";

				while (FIELD_NAME_CLMN_CNTR < 500 && Rowstatus != "false") {
					getFieldNameColumnHeader = excelFileUtil.getCellValue(areadsheet, rowposition,
							FIELD_NAME_CLMN_CNTR);

					if (getFieldNameColumnHeader.equalsIgnoreCase(TestDataheader)) {
						FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;

						OutputparamValue = excelFileUtil.getCellValue(areadsheet, rowposition + 1, FIELD_NAME_CLMN_NO);
						Status = true;
						break;
					}

					else {
						FIELD_NAME_CLMN_CNTR = FIELD_NAME_CLMN_CNTR + 1;
					}

				}
			} else {
				Rowstatus = "false";

			}

			if (!Status) {
				rowposition = rowposition + 1;
				FIELD_NAME_CLMN_CNTR = 0;

			} else {
				break;

			}

		}
		return OutputparamValue;

	}

	private void funcOpenUrl(String feType, String objName, String fValue)
			throws InterruptedException, FileNotFoundException, IOException {

		funBrowserclose();
		driver.get(fValue_tmp);
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(synctime);
		Thread.sleep(synctime);

	}

	private static void funCloseAddEditAlerts(String feType, String objName, String fValue) {

		try {
			List<WebElement> Close = driver.findElements(By.xpath(objName));
			System.out.println("TestInfo:Alert Count" + Close.size());
			for (WebElement CloseButton : Close) {
				try {
					CloseButton.click();
				} catch (Exception e) {
					System.out.println("TestInfo : Looping to close the Alert");
				}
			}

		} catch (Exception e) {
			System.out.println("TestError : Alert not Shown on the Page");
		}

	}

	private static void funOptionalClick(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;

		try {
			element = funcFindElement(feType, objName);

		} catch (Exception e) {
			element = null;
		}

		if (element != null) {
			System.out.println("TestInfo : Peforming Click Operation");
			element.click();
			element.sendKeys(Keys.ENTER);
		} else {
			System.out.println("TestInfo : Element not Shown on the Page");

		}

	}

	private static void funverifyPageSource(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = driver.findElement(By.id("PWGadgetBIfr"));
		driver.switchTo().frame(element);
		List<WebElement> Buttons = driver.findElements(By.xpath("//img[contains(text(),'Select')]"));
		System.out.println("Size" + Buttons.size());
		for (WebElement Name : Buttons) {

			System.out.println(Name.getText());
		}
		String SourceCode = driver.getPageSource();
		System.out.println(SourceCode);
		if (SourceCode.contains(fValue)) {

			System.out.println("TestInfo: String Present in Source code");
			failFlag = 1;
		} else {

			System.out.println("TestError: String Present in Source code");
			failFlag = 0;
		}

	}

	private static void fungetPreviousDate(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -18);
		java.util.Date result = cal.getTime();
		System.out.println(dateFormat.format(result));
		String DateValue = dateFormat.format(result);
		element.sendKeys(DateValue);
	}

	private static void funcDrugDetails(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Drug_Details_Text = element.getText();

		String Line[] = Drug_Details_Text.split("[\\r\\n]+");

		for (String Drug_Text : Line)

		{
			if ((Drug_Text.contains("Last")) || (Drug_Text.contains("Remaining")) || (Drug_Text.contains("Expires"))
					|| (Drug_Text.contains("Pharmacy"))) {
				System.out.println("TestInfo: Text Present on the Screen" + Drug_Text);
				failFlag = 1;

			} else {
				System.out.println("TestError:Unable to find the Text ");
				failFlag = 0;
			}

		}

	}

	private static void JSfuncClick(String fetype, String objName) throws InterruptedException, IOException {
		Thread.sleep(Longsynctime);
		Thread.sleep(Longsynctime);
		WebElement element;
		element = funcFindElement(fetype, objName);
		Thread.sleep(Longsynctime);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
		Thread.sleep(Longsynctime);
	}

	private void funcAngJSClick(String fetype, String objName, String fValue) throws InterruptedException, IOException {
		WebElement element;
		element = funcFindElement(fetype, objName);
		// boolean pageFlags[] = checkIfAngularJsPageLoad();
		if (checkIfAngularJsPageLoad()) {
			element.click();
			Thread.sleep(Longsynctime);
			Thread.sleep(Longsynctime);
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform click operation");
		}
	}

	private static void funAngJsVerifyLink(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			if (element.isDisplayed()) {
				System.out.println("TestInfo: Link is Displayed on Screen");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError: Link is Not Displayed on Screen");
				failFlag = 0;
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to peform verifylink operation");
		}
	}

	private static void funAngJsVerifyTextpresent(String feType, String objName, String fValue)
			throws InterruptedException {
		String validator_gbl;
		@SuppressWarnings("deprecation")
		boolean Flag = selenium.isTextPresent(fValue);
		validate = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			if (Flag) {
				System.out.println("TestInfo : Text present on Screen");
				validator_gbl = validate.getText();
				System.out.println(validator_gbl);
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError : Text not present on Screen");
				validator_gbl = validate.getText();
				failFlag = 0;
				// LOG_VAR= 0;
				System.out.println(validator_gbl);
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to peform verifyTextPresent operation");
		}

	}

	private static void funcAngJSInput(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			element.clear();
			element.sendKeys(fValue);
			Thread.sleep(Longsynctime);
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform input operation");
		}

	}

	private static void funcAngVerifyTextInput(String feType, String objName, String fValue)
			throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (checkIfAngularJsPageLoad()) {
			String temp = element.getAttribute("value");

			if (temp.equals(fValue)) {
				System.out.println("Correct Text is displayed in textbox");
				System.out.println(temp);
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				System.out.println("TestError : Text not present in textbox");

				failFlag = 0;
				LOG_VAR = 0;
				System.out.println(temp);
			}
			Thread.sleep(Longsynctime);
		} else {
			throw new NoSuchElementException("Element- " + objName + " is not loaded still to perform input operation");
		}

	}

	private static boolean funcAngJSVerfiyElemExists(String fetype, String objName)
			throws IOException, InterruptedException {
		WebElement element;
		element = funcFindElement(fetype, objName);
		if (checkIfAngularJsPageLoad()) {
			if (!(element.equals(null)) || (element.isEnabled() && element.isDisplayed())) {
				System.out.println("TestInfo : Element Exists on Screen");
				failFlag = 1;
				LOG_VAR = 1;
				return true;
			} else {
				failFlag = 0;
				LOG_VAR = 0;
				System.out.println("TestError : Element not Exists on Screen");
				return false;
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to perform verifyelementexists operation");
		}

	}

	/* Method to select based on Value,Index and Text */
	private static void funcAngJSSelectData(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Select select = new Select(element);
		String[] Value = fValue.split("#");
		if (checkIfAngularJsPageLoad()) {
			if (Value[0].equalsIgnoreCase("Index")) {
				int Num = Integer.parseInt(Value[1]);
				select.selectByIndex(Num);
			} else if (Value[0].equalsIgnoreCase("Value")) {
				select.selectByValue(Value[1]);
			} else {
				select.selectByVisibleText(fValue);
			}
		} else {
			throw new NoSuchElementException(
					"Element- " + objName + " is not loaded still to perform selectdata operation");
		}
	}

	public static boolean checkIfAngularJsPageLoad() throws InterruptedException {
		// long sleepTime = 10000;
		int noOfIterations = 10;
		int iterationCount = 0;
		// boolean Pageloadflag[];
		boolean PageLoad = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (iterationCount < noOfIterations) {
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				System.out.println("Page Is loaded.");
				System.out.println("iteration count is -" + iterationCount);
				// return true;
				// Pageloadflag[0 ]= true;
				PageLoad = true;
				break;
			} else { // Pageloadflag[0 ]= false;
				Thread.sleep(1000);
				iterationCount++;
			}
		}
		// below code if we get hook(element), which gets enabled/disabled post
		// internal service request status for individual components refresh
		/*
		 * if(hook){ Pageloadflag[1]= true; }else{ Pageloadflag[1]= false; }
		 * return Pageloadflag;
		 */
		return PageLoad;

	}

	private static void funOpenEnrollURL(String FieldValue, String FieldName)
			throws FileNotFoundException, IOException, InterruptedException {
		String[] Value = FieldValue.split("#");
		String ClientCode = getSettingsFromOpenEnroll(Value[0]);
		String PlanCode = getSettingsFromOpenEnroll(Value[1]);
		String OURL = getSettingsFromOpenEnroll(FieldName);
		String SURL = OURL.replace("CVARIABLE", ClientCode);
		String FURL = SURL.replace("PVARIABLE", PlanCode);
		System.out.println("TestInfo: OpenEnroll URL :" + " " + FURL);
		driver.get(FURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(Longsynctime);
		Thread.sleep(synctime);

	}

	private static void funTableRowCount(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> element = driver.findElements(By.xpath(objName));
		int ACount = element.size();
		System.out.println("Actual Count :" + ACount);
		int ECount = Integer.parseInt(fValue);
		if (ACount == ECount) {
			System.out.println("TestInfo:" + ACount + " " + "Rows Present on Page");
			failFlag = 1;
		} else {
			failFlag = 0;
			System.out.println("TestError:" + ECount + " " + "Rows Present on Page");
		}

	}

	private static void funClosePDF() throws InterruptedException, IOException {
		/*
		 * if(BrowserName.equalsIgnoreCase("Internet Explorer")) {
		 * Thread.sleep(synctime); Runtime.getRuntime().exec(
		 * "C:\\CBT_Selenium_Framework\\lib\\HelperFiles\\HandleAPDF.exe");
		 * }else if (BrowserName.equalsIgnoreCase("Firefox")||BrowserName.
		 * equalsIgnoreCase("Chrome")) { for(String win
		 * :driver.getWindowHandles()) {
		 * System.out.println(driver.switchTo().window(win).getTitle()); String
		 * title = driver.switchTo().window(win).getTitle(); Thread.sleep(5000);
		 * if (title.contains(".pdf")) { driver.close(); for(String win1
		 * :driver.getWindowHandles()) { Thread.sleep(5000);
		 * System.out.println(driver.switchTo().window(win1).getTitle());
		 * driver.switchTo().window(win1); Thread.sleep(5000); } } } }
		 */

	}

	private void funBrowserclose() throws InterruptedException, FileNotFoundException, IOException {
		if (excelFileUtil.platform.contains("Explorer")) {

			try {
				Runtime.getRuntime().exec("taskkill /F /IM WINWORD.exe");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread.sleep(Longsynctime);
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(capabilities);
			// selenium = new
			// WebDriverBackedSelenium(driver,getSettingsFromConfig("URL"));
			Thread.sleep(synctime);
			failFlag = 1;
		} else if (excelFileUtil.platform.equalsIgnoreCase("Firefox")) {
			driver.close();
			Thread.sleep(Longsynctime);
			FirefoxProfile profile = new FirefoxProfile();
			driver = new FirefoxDriver(profile);
			failFlag = 1;
		} else if (excelFileUtil.platform.equalsIgnoreCase("Chrome")) {
			driver.close();
			String ss;
			ss = "lib"+File.separator+"chromedriver.exe";
			System.out.println("SS: " + ss);
			System.setProperty("webdriver.chrome.driver", ss);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, ss);
			ChromeDriverService service = ChromeDriverService.createDefaultService();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-extensions");
			driver = new ChromeDriver(service, options);
			Thread.sleep(Longsynctime);
			failFlag = 1;

		}
		{

		}

	}

	private static void funSwitchWin(String feType, String objName, String fValue) throws InterruptedException {
		// driver.switchTo().defaultContent();
		Thread.sleep(synctime);
		try {
			for (String Handle : driver.getWindowHandles())// 2
			{
				System.out.println(driver.switchTo().window(Handle).getTitle());
				String Title = driver.switchTo().window(Handle).getTitle();
				if (Title.equals(fValue)) {
					driver.switchTo().window(Handle);
					System.out.println("TestInfo : Successfully Switched to Window!");
					break;

				}
			}

		} catch (Exception e) {
			// TODO: handle exception

			/*
			 * List<WebElement> ele = driver.findElements(By.xpath("abc"));
			 * ele.size()
			 */
		}
	}

	// funSwitchframe

	private static void funSwitchframe(String feType, String objName, String fValue) throws InterruptedException {

		Thread.sleep(synctime);
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(synctime);
			driver.switchTo().frame(fValue);
			Thread.sleep(synctime);

		} catch (Exception e) {
			// TODO: handle exception

			/*
			 * List<WebElement> ele = driver.findElements(By.xpath("abc"));
			 * ele.size()
			 */
		}
	}

	
	/**
	 * @param feType = xpath/id/name/link/linkText/CSS
	 * @param objName = String
	 * @param fValue = Attribure of element
	 * @description: This will store attribute innertext/Value or text to the global variable 
	 * @throws InterruptedException
	 */
	private static void funGetprop(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Attribute = fValue.trim();

		if (Attribute.equalsIgnoreCase("innertext")) {
			String Storevalue = element.getAttribute(Attribute);
			if (Storevalue == null) {

				Storevalue = element.getText().trim();
				System.out.println("TestInfo: ACTUALPARA Value " + Storevalue);
				DVARIABLE = Storevalue;
			}
			System.out.println("TestInfo: ACTUALPARA Value " + Storevalue);
			DVARIABLE = Storevalue;
			dataholder.put(ACTUALPARA, Storevalue);
		} else if (Attribute.equalsIgnoreCase("value")) {
			String Storevalue = element.getAttribute(Attribute);

			if (Storevalue == null) {
				Storevalue = element.getText().trim();
			}
			dataholder.put(ACTUALPARA, Storevalue);

		}

	}

	private static void funChkprop(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Attribute = fValue.trim();
		String ExpectedPropValue = "";

		try {

			if (Attribute != null) {

				if (element.getTagName().equalsIgnoreCase("input")) {
					ExpectedPropValue = element.getAttribute("value").trim();

				} else {
					ExpectedPropValue = element.getAttribute("innertext");
					if (ExpectedPropValue == null) {
						ExpectedPropValue = element.getText().trim();
						System.out.println("TestInfo: Expected Value " + ExpectedPropValue);
					}
					// String ExpectedPropValue = element.getText().trim();
					System.out.println("TestInfo: Expected Value " + ExpectedPropValue);
				}

				if (!(inputSheet.isEmpty())) {

					if (ExpectedPropValue.equalsIgnoreCase(fValue)) {
						System.out.println("TestInfo: Property Values are Matching ");
						failFlag = 1;

					} else

					{
						System.out.println("TestError: Property Values are Not Matching ");
						System.out.println("Expected Value" + ExpectedPropValue);
						System.out.println("Actual Value" + fValue);
						failFlag = 0;
						LOG_VAR = 0;
					}

				}

			} else {
				String ActualPropValue = dataholder.get(ACTUALPARA);
				if (ExpectedPropValue.equalsIgnoreCase(ActualPropValue)) {
					System.out.println("TestInfo: Property Values are Matching ");
					failFlag = 1;

				} else

				{
					System.out.println("TestError: Property Values are Not Matching ");
					System.out.println("Expected Value" + ExpectedPropValue);
					System.out.println("Actual Value" + ActualPropValue);
					failFlag = 0;
					LOG_VAR = 0;
				}

			}

		} catch (Exception e) {
			failFlag = 0;
			System.out.println("TestError: Error in Property Match !!");
			LOG_VAR = 0;
		}

	}

	private static void funKeyEvents(String feType, String objName, String fValue) throws InterruptedException {
		if (fValue.equalsIgnoreCase("CTRL+END")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_END);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_END);
		} else if (fValue.equalsIgnoreCase("CTRL+HOME")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_HOME);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_HOME);
		} else if (fValue.equalsIgnoreCase("ENTER")) {
			robo.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_ENTER);

		}

		else if (fValue.equalsIgnoreCase("TAB")) {
			robo.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_TAB);

		}

		else if (fValue.equalsIgnoreCase("CTRLDELETE")) {
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_A);
			Thread.sleep(synctime);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyRelease(KeyEvent.VK_A);
			robo.keyPress(KeyEvent.VK_DELETE);
			robo.keyRelease(KeyEvent.VK_DELETE);

		}

		else if (fValue.equalsIgnoreCase("ARROWDOWN")) {
			// WebElement element;
			// element = funcFindElement(feType,objName);
			// element.sendKeys(Keys.ARROW_DOWN);
			// Thread.sleep(synctime);
			robo.keyPress(KeyEvent.VK_KP_DOWN);
			robo.keyRelease(KeyEvent.VK_KP_DOWN);
			Thread.sleep(synctime);
			// robo.keyPress(KeyEvent.VK_ENTER);
			// robo.keyRelease(KeyEvent.VK_ENTER);

		}
	}

	public static void actionsClick(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		wait.until(ExpectedConditions.visibilityOf(element));// Explicit Wait
																// for Element
		Actions builder = new Actions(driver);
		builder.moveToElement(element).build().perform();
		element.click();
	}

	public static void clickLinkByName(String favlue){
		List<WebElement> elements=driver.findElements(By.xpath(".//table[@id='tblltrlist']//tr/td/span/a"));
		for(WebElement element:elements){
			String expval= element.getText().trim();
			System.out.println(expval);
			if(expval.equals(favlue.trim())){
				element.click();
				break;
			}
			else{
				
			}
		}
		
	}
	
	public static void funmouseHoverClick(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
		element.click();

	}

	public static void funmouseHover(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
		// element.click();

	}

	private static void funMouseHoverJs(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String script = "if(document.createEvent) " + "{ var evObj = document.createEvent('MouseEvents'); "
				+ "evObj.initEvent('mouseover', true, false); " + "arguments[0].dispatchEvent(evObj); } "
				+ "else if(document.createEventObject) " + "{ arguments[0].fireEvent('onmouseover'); " + "}";

		String script1 = "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
				+ "arguments[0].dispatchEvent(evObj);";

		jsExecutor.executeScript(script, element);

		element.click();

	}

	private static void funVerifyFals(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		try {
			element = driver.findElement(By.xpath(objName));

		} catch (Exception e) {
			element = null;
		}

		if (element != null) {
			System.out.println("TestError : Element Exists on Screen");
			failFlag = 0;
		} else {
			System.out.println("TestInfo : Element not Exists on Screen");
			failFlag = 1;
		}

	}

	private static void funClear(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		element.clear();
	}

	private static void funHandleAlert(String feType, String fValue) throws InterruptedException {
		if (fValue.equalsIgnoreCase("OK")) {
			driver.switchTo().alert().accept();
			Thread.sleep(synctime);
		} else {
			driver.switchTo().alert().dismiss();

		}
	}

	private static void funAlertText(String feType, String objName, String fValue)

	{
		String AAlert_Text = fValue;
		String EAlert_Text = driver.switchTo().alert().getText();
		System.out.println("AlertInfo: Alert Text is " + " " + EAlert_Text);

		if (AAlert_Text.equals(EAlert_Text)) {
			System.out.println("TestInfo:Alert Text is Matching!");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError:Alert Text Not Matching!");
			failFlag = 0;
			// LOG_VAR= 0;
		}
	}

	private static void funGenrtNumbr(String feType, String objName, String fValue)
			throws InterruptedException, AWTException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String[] Number = fValue.split(",");
		int min = Integer.parseInt(Number[0]);
		int max = Integer.parseInt(Number[1]);
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		System.out.println(randomNum);
		String value = Integer.toString(randomNum);
		System.out.println(value);

		Robot robo = new Robot();
		robo.keyPress(KeyEvent.VK_CONTROL);
		robo.keyPress(KeyEvent.VK_END);
		Thread.sleep(synctime);
		element.sendKeys(value);

		robo.keyRelease(KeyEvent.VK_CONTROL);
		robo.keyRelease(KeyEvent.VK_END);
	}

	private static void funMultiLinks(String feType, String objName, String fValue) {
		List<WebElement> Links = driver.findElements(By.linkText(objName));
		for (WebElement ele : Links) {
			System.out.println(ele.getText());
		}
		int Act_LinksCount = Links.size();
		int Exp_LinksCount = Integer.parseInt(fValue);
		System.out.println("Actual Links:" + Act_LinksCount + "Expected Links: " + Exp_LinksCount);
		if (Act_LinksCount == Exp_LinksCount) {
			System.out.println("TestInfo: Links count is Matching.");
			failFlag = 1;
		} else {
			System.out.println("TestError: Links Count Not Matching!!");
			failFlag = 0;
		}

	}

	private static void funLink(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (element.isDisplayed()) {
			System.out.println("TestInfo: Link is Displayed on Screen");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError: Link is Not Displayed on Screen");
			failFlag = 0;
		}
	}

	private static void funElementProp(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String ActualVal = dataholder.get(fValue);
		String ExpectedVal = element.getAttribute("value");
		if (ActualVal.equals(ExpectedVal)) {
			System.out.println("TestInfo : Field Value is Matching !");
			failFlag = 1;
			LOG_VAR = 1;

		} else {
			failFlag = 0;
			System.out.println("TestError : Field Value Not Matching !");
		}

	}

	private static void funSendValue(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String SendValue = dataholder.get(fValue);
		element.sendKeys(SendValue);
		System.out.println("TestInfo : Send Value is: " + " " + SendValue);
	}

	private static void funHoldvalue(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Holdvalue = element.getAttribute("value");
		System.out.println("TestInfo : Hold Value is: " + " " + Holdvalue);
		dataholder.put(fValue, Holdvalue); // Store in HashTable in Key/Value
											// format.
	}

	private static void funTextpresent(String feType, String objName, String fValue) throws InterruptedException {
		String validator_gbl;
		boolean Flag = selenium.isTextPresent(fValue);
		validate = funcFindElement(feType, objName);
		if (Flag) {
			System.out.println("TestInfo : Text present on Screen");
			validator_gbl = validate.getText();
			System.out.println(validator_gbl);
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError : Text not present on Screen");
			validator_gbl = validate.getText();
			failFlag = 0;
			// LOG_VAR= 0;
			System.out.println(validator_gbl);
		}

	}

	/** Function to click the check box based on Index **/
	private static void funcCheckByIndex(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> eles = driver.findElements(By.xpath(objName));
		int Index = Integer.parseInt(fValue);
		int counter = 0;
		for (WebElement ele : eles) {
			if (counter == Index) {
				ele.click();
				break;
			}
			counter++;
		}
	}

	/** Function to click the check box based on Index **/
	private static void funcUncheckAll(String feType, String objName, String fValue) throws InterruptedException {
		List<WebElement> eles = driver.findElements(By.xpath(objName));
		for (WebElement ele : eles) {
			ele.click();
		}

		int Index = Integer.parseInt(fValue);
		int counter = 0;
		for (WebElement ele : eles) {
			if (counter == Index) {
				ele.click();
				break;
			}
			counter++;
		}
	}

	private static void funcVerifyWin(String feType, String objName, String fValue) {
		for (String windowHandle : driver.getWindowHandles()) {
			String Actual_Title = driver.switchTo().window(windowHandle).getTitle();
			if (Actual_Title.equalsIgnoreCase(fValue)) {
				System.out.println("TestInfo : Window Exists.");
				failFlag = 1;
				LOG_VAR = 1;
			} else {
				failFlag = 0;
				// LOG_VAR=0;
				System.out.println("TestError : Window not Exists!");

			}
		}
	}

	/** Function to Close Window By Title **/

	private static void funcCloseWin(String feType, String objName, String fValue) {
		Boolean Winflag = false;
		for (String windowHandle : driver.getWindowHandles()) {
			String Actual_Title = driver.switchTo().window(windowHandle).getTitle();
			System.out.println(Actual_Title);
			if (Actual_Title.equalsIgnoreCase(fValue)) {
				driver.switchTo().window(windowHandle).close();
				System.out.println("TestInfo : Window was Closed.");
				Winflag = true;
				failFlag = 1;
				LOG_VAR = 1;
				break;
			}
		}
		if (!Winflag) {
			failFlag = 0;
			System.out.println("TestError: Unable to Close Window or not Exists");
		}
	}

	private static void funcSetcheck(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		if (fValue.equalsIgnoreCase("check")) {

			if (!(element.isSelected())) {
				element.click();
			}
		} else if (fValue.equalsIgnoreCase("uncheck")) {
			if (element.isSelected()) {
				element.click();
			}
		}
	}

	private static void funVerifyfieldValue(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		String Actual_Value = element.getAttribute("value");
		if (Actual_Value.equals(fValue)) {
			System.out.println("TestInfo: Filed value is Matching");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			failFlag = 0;
			System.out.println("TestError: Filed value  not Matching!");

		}

	}

	private static void funClearthnType(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		element.clear();
		element.sendKeys(fValue);
		Thread.sleep(Longsynctime);

	}

	private static void funcVerifyURL(String fValue) {
		String URL = "";
		URL = driver.getCurrentUrl();
		if (fValue.equals(URL)) {
			System.out.println("TestInfo:URL is Matching");
			failFlag = 1;
			LOG_VAR = 1;
		} else {
			System.out.println("TestError:URL is not Matching");
			// LOG_VAR= 0;
			failFlag = 0;
			// testFlag="n";
		}
	}

	/* Method to select based on Value,Index and Text */
	private static void funcSelectData(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		Select select = new Select(element);
		String[] Value = fValue.split("#");

		if (Value[0].equalsIgnoreCase("Index")) {
			int Num = Integer.parseInt(Value[1]);
			select.selectByIndex(Num);
		} else if (Value[0].equalsIgnoreCase("Value")) {
			select.selectByValue(Value[1]);
		} else {
			select.selectByVisibleText(fValue);
		}
	}

	private static void funcInput(String feType, String objName, String fValue) throws InterruptedException {
		WebElement element;
		element = funcFindElement(feType, objName);
		element.sendKeys(fValue);
	}

	private void funcClick(String fetype, String objName, String fValue) throws InterruptedException, IOException {
//		WebElement element;
//		element = ;
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(funcFindElement(fetype, objName))).click();
//		element.click();
//		Thread.sleep(Longsynctime);
//		Thread.sleep(Longsynctime);

	}

	private static boolean funcVerify(String fetype, String objName) throws IOException, InterruptedException {
		{
			WebElement element;
			element = funcFindElement(fetype, objName);
			if (!(element.equals(null)) || (element.isEnabled() && element.isDisplayed())) {
				System.out.println("TestInfo : Element Exists on Screen");
				failFlag = 1;
				LOG_VAR = 1;
				return true;
			} else {
				failFlag = 0;
				LOG_VAR = 0;
				System.out.println("TestError : Element not Exists on Screen");
				return false;
			}
		}
	}

	public static String getValueFromAppConfig(String fvalue) {
		try {

			// FileInputStream file = new FileInputStream(new
			// File(DriverClass.APPCONFIG));
			FileInputStream file = new FileInputStream(new File(""));
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						Boolean Value = cell.getBooleanCellValue();
						if (Value.equals(fvalue)) {
							cell = cellIterator.next();
							fvalue = cell.getStringCellValue();
						}
						break;

					case Cell.CELL_TYPE_STRING:
						String SValue = cell.getStringCellValue();
						if (SValue.contains(fvalue)) {
							cell = cellIterator.next();
							fvalue = cell.getStringCellValue();
							break;
						}
						break;
					}
				}

			}
			file.close();
			FileOutputStream out = new FileOutputStream(new File("SeleniumFramework"+File.separator+"Test_Templates"+File.separator+"OPTUMIDDATA.xls"));
			workbook.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fvalue;
	}

	public static String getSettingsFromExxConfig(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("SeleniumFramework"+File.separator+"Test_Templates"+File.separator+"AppConfig.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	public static String getSettingsFromTemplate(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("SeleniumFramework"+File.separator+"Test_Templates"+File.separator+"Registration_Data.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	protected static WebElement funcFindElement(String elmToIdentify, String obj) {
		if (elmToIdentify.equals("") && obj.equals("")) {
			System.out.println("No such Object is found: " + fieldName + " on screen: " + screenName);
			LOG_VAR = 0;
			// testFlag="n";
			String Trace = "No such Object is found: " + fieldName + " on screen: " + screenName;
			sendLog(Trace, PREVIOUS_TEST_CASE, TEST_STEP_COUNT);
		} else {
			By by = null;

					if (elmToIdentify.equalsIgnoreCase("LinkText")) {
						by = By.linkText(obj);
					} else if (elmToIdentify.equalsIgnoreCase("Class")) {
						by = By.className(obj);
					} else if (elmToIdentify.equalsIgnoreCase("CSS")) {
						by = By.cssSelector(obj);
						by = By.id(obj);
					} else if (elmToIdentify.equalsIgnoreCase("Name")) {
						by = By.name(obj);
					} else if (elmToIdentify.equalsIgnoreCase("PartialLinkText")) {
						by = By.partialLinkText(obj);
					} else if (elmToIdentify.equalsIgnoreCase("TagName")) {
						by = By.tagName(obj);
					} else if (elmToIdentify.equalsIgnoreCase("xpath")) {
						by = By.xpath(obj);
					} else if (elmToIdentify.equalsIgnoreCase("dynamicXpath")) {
						obj = obj.replace("VARIABLE", DVARIABLE);
						by = By.xpath(obj);
					}
					return (new WebDriverWait(driver, 15)).until((ExpectedConditions.presenceOfElementLocated(by)));
		}
		throw new NoSuchElementException("Timeout waiting for Object: " + obj + ", whose FieldName is: " + fieldName
				+ " on Screen: " + screenName);
	}

	public static String getSettingsFromOpenEnroll(String strKey) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("SeleniumFramework"+File.separator+"Test_Templates"+File.separator+"OpenEnroll.properties"));
		String strData = prop.getProperty(strKey);
		strData = strData.trim();
		return strData;
	}

	/**
	 * Method getStartTime: this method is called when a new test cases is about
	 * to be executed. It returns String in form of hh:mm:ss
	 * 
	 * @return hh:mm:ss
	 */
	public String getStartTime() {
		String hh, mm, ss;
		Calendar cal = new GregorianCalendar();
		String ist;
		int HOURS24 = cal.get(Calendar.HOUR_OF_DAY); // 0..23
		int MIN = cal.get(Calendar.MINUTE); // 0..59
		int SEC = cal.get(Calendar.SECOND); // 0..59
		hh = Integer.toString(HOURS24);
		mm = Integer.toString(MIN);
		ss = Integer.toString(SEC);
		ist = hh + ":" + mm + ":" + ss;
		return ist;
	}

	/**
	 * Method getNumberofIterations : this method gets the number of iterations
	 * for what times looped test steps to be iterated.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getNumberofIterations() throws IOException {// TODO: what is the
															// input to this
															// method, is
															// readScriptSheet
		String dataSheetName;
		int LOOPITERATION = 0;
		int TEMP_SR = startRow;
		int TEMP_ER = endRow;
		int loopRowCounter;
		int dataListPointer;
		String counterString, delimiter;
		HSSFSheet newSheet;

		while (TEMP_SR <= TEMP_ER) {
			dataSheetName = excelFileUtil.getCellValue(readScriptSheet, TEMP_SR, 5);
			if (!(dataSheetName.isEmpty())) {
				System.out.println("datasheetName: " + dataSheetName);
				loopRowCounter = 1;
				dataListPointer = 0;
				counterString = null;
				delimiter = null;
				// getting the number of iterations
				boolean inputFlag = true;
				// Opens the datasheet
				// newSheet=scriptWorkbook.getSheet(dataSheetName);
				newSheet = scriptWorkbook.getSheet("Test_Data");
				delimiter = excelFileUtil.getCellValue(newSheet, dataListPointer, 0);
				while (!delimiter.equalsIgnoreCase("End")) {
					if (delimiter.equalsIgnoreCase(dataSheetName)) {
						break;
					} else {
						dataListPointer = dataListPointer + 1;
						delimiter = excelFileUtil.getCellValue(newSheet, dataListPointer, 0);
					}

				}
				// In while loop below, it checks for the number of data to be
				// taken.If flag is 'y' the increases the counter.
				dataListPointer = dataListPointer + 1;
				while (inputFlag) {
					counterString = excelFileUtil.getCellValue(newSheet, dataListPointer, 0);// newsheet.getRow(LoopRowCounter).getCell(0).getStringCellValue().trim();
					System.out.println("counterString: " + counterString);
					try {
						if (!counterString.equalsIgnoreCase("END_LIST")) {
							loopRowCounter = loopRowCounter + 1;
							dataListPointer++;
						} else {
							inputFlag = false;
							System.out.println("Input Data Count ends");
						}
					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
						e.printStackTrace();
					}
				}

				if (LOOPITERATION < loopRowCounter) {
					LOOPITERATION = loopRowCounter - 1;
				}
			}
			TEMP_SR = TEMP_SR + 1;
		}
		System.out.println("LOOPITERATION : " + LOOPITERATION);
		return LOOPITERATION; // returns the number of data.
	}

	public void executeTest(String testName) throws Exception {
		int TESTROWCOUNTER = 1;
		boolean TestStepFlag = true;
		String testStepName, testExecutionFlag, loopString;
		String startRowString, endRowString;
		String testStepsToLoop;
		while (TestStepFlag) {
			testStepName = excelFileUtil.getCellValue(readScriptSheet, TESTROWCOUNTER, 2);
			testExecutionFlag = excelFileUtil.getCellValue(readScriptSheet, TESTROWCOUNTER, 0);// readtestcasesheet.getRow(TestRowCounter).getCell(1).getStringCellValue().trim();

			if (testExecutionFlag.equalsIgnoreCase("End")) {
				TestStepFlag = false;
			} // else if(testName.equals(CURRENTTESTCASE))
			else if (testName.equals(testStepName)) {
				currTestRowPtr = TESTROWCOUNTER;
				PREVIOUS_TEST_CASE = testName;
				TC_ID = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 1);
				TC_DESC = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 6);
				totalTCount = totalTCount + 1;
				// }
				loopString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 3);// readtestcasesheet.getRow(TestRowCounter).getCell(2).getStringCellValue().trim();
				// here it gets start row and end row for parameterization
				if (loopString.equalsIgnoreCase("Loop")) {
					startRowString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 4);// readtestcasesheet.getRow(TestRowCounter).getCell(3).getStringCellValue().toString().trim();
					endRowString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 5);// readtestcasesheet.getRow(TestRowCounter).getCell(4).getStringCellValue().toString().trim();
					if ((startRowString.isEmpty()) && (endRowString.isEmpty())) {
						int TEMPVAR = currTestRowPtr;
						startRow = currTestRowPtr;
						boolean TEMPFLAG = true;
						while (TEMPFLAG) {
							testStepsToLoop = excelFileUtil.getCellValue(readScriptSheet, TEMPVAR, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();
							// System.out.println("TestStepsToLoop: " +
							// TestStepsToLoop);
							if (testStepsToLoop.equals(PREVIOUS_TEST_CASE)) {
								TEMPVAR = TEMPVAR + 1;
							} else {
								TEMPFLAG = false;
							}
						}
						endRow = TEMPVAR - 1;
						System.out.println("Start Row: " + startRow);
						System.out.println("End Row: " + endRow);
					} else {
						startRow = Integer.parseInt(startRowString) - 1;
						endRow = Integer.parseInt(endRowString) - 1;
					}
					LOOP_FLAG = true;
				} else {
					LOOP_FLAG = false;
				}
				break;// TODO:why this break-lenina
			}
			TESTROWCOUNTER = TESTROWCOUNTER + 1;
		}
		if (testFlag.equalsIgnoreCase("y")) {
			tcStartTime = getStartTime();
		}
	}

	public int[] getTotalStepsAndStepPointer(String testName) throws IOException { // TODO:
																					// good
																					// to
																					// move
																					// to
																					// diff
																					// class
		int[] retObj = { 0, 0, 0, 0, 1 };
		int TESTROWCOUNTER = 1, TEMPVAR = 1;
		boolean TestStepFlag = true;
		boolean TEMPFLAG = true;
		String testStepName, testExecutionFlag, loopString;
		String startRowString, endRowString;
		String testStepsToLoop;
		while (TestStepFlag) {
			testStepName = excelFileUtil.getCellValue(readScriptSheet, TESTROWCOUNTER, 2);
			testExecutionFlag = excelFileUtil.getCellValue(readScriptSheet, TESTROWCOUNTER, 0);// readtestcasesheet.getRow(TestRowCounter).getCell(1).getStringCellValue().trim();
			if (testExecutionFlag.equalsIgnoreCase("End")) {
				retObj[4] = 0;
				TestStepFlag = false;
			} // else if(testName.equals(CURRENTTESTCASE))
			else if (testName.equals(testStepName)) {
				currTestRowPtr = TESTROWCOUNTER;
				PREVIOUS_TEST_CASE = testName;
				TC_ID = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 1);
				TC_DESC = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 6);
				totalTCount = totalTCount + 1;
				loopString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 3);
				if (loopString.equalsIgnoreCase("Loop")) {
					startRowString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 4);// readtestcasesheet.getRow(TestRowCounter).getCell(3).getStringCellValue().toString().trim();
					endRowString = excelFileUtil.getCellValue(readtestcasesheet, TCCounter, 5);// readtestcasesheet.getRow(TestRowCounter).getCell(4).getStringCellValue().toString().trim();
					if ((startRowString.isEmpty()) && (endRowString.isEmpty())) {
						int TEMPLOOPVAR = currTestRowPtr;
						startRow = currTestRowPtr;
						boolean TEMPLOOPFLAG = true;
						while (TEMPLOOPFLAG) {
							testStepsToLoop = excelFileUtil.getCellValue(readScriptSheet, TEMPLOOPVAR, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();

							if (testStepsToLoop.equals(testName)) {
								TEMPLOOPVAR = TEMPLOOPVAR + 1;
							} else {
								TEMPLOOPFLAG = false;
							}
						}
						endRow = TEMPLOOPVAR - 1;
						System.out.println("Start Row: " + startRow);
						System.out.println("End Row: " + endRow);
					} else {
						startRow = Integer.parseInt(startRowString) - 1;
						endRow = Integer.parseInt(endRowString) - 1;
					}
					LOOP_FLAG = true;
				} else {
					LOOP_FLAG = false;
					int tempLoop = currTestRowPtr;
					while (TEMPFLAG) {
						testStepsToLoop = excelFileUtil.getCellValue(readScriptSheet, tempLoop, 2);// readscriptsheet.getRow(tempvar).getCell(1).getStringCellValue().trim();
						// System.out.println("TestStepsToLoop: " +
						// TestStepsToLoop);
						if (testStepsToLoop.equals(testName)) {
							TEMPVAR = TEMPVAR + 1;
						} else {
							TEMPFLAG = false;
						}
						tempLoop = tempLoop + 1;
					}
				}
				break;
			}
			TESTROWCOUNTER = TESTROWCOUNTER + 1;
		}
		retObj[0] = TEMPVAR - 1;
		retObj[1] = currTestRowPtr;
		retObj[2] = startRow;
		retObj[3] = endRow;
		System.out.println("Start Row: " + startRow);
		System.out.println("End Row: " + endRow);
		return retObj;
	}

	private static JRXlsDataSource getDataSource1() throws JRException {
		JRXlsDataSource ds;
		try {
			String[] columnNames = new String[] { "TCID", "TESTCASENAME", "RESULT", "BROWSER", "TESTSTAUS", "TESTCOUNT",
					"TPASS", "TFAIL", "TESTURL" };
			int[] columnIndexes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
			System.out.println(System.getProperty("user.dir"));
			String url = System.getProperty("user.dir") + "/SeleniumFramework/Test_Excel/Tester.xls";
			ds = new JRXlsDataSource(JRLoader.getLocationInputStream(url
			// "C:\\CM_CBT_Automation\\SeleniumWebAutomationFramework\\Selenium_Framework\\SeleniumFramework\\Test_Excel\\Tester.xls"
			// "SeleniumFramework\\Test_Excel\\Tester.xls"
			));

			ds.setColumnNames(columnNames, columnIndexes);
			ds.setUseFirstRowAsHeader(true);

			// uncomment the below line to see how sheet selection works
		} catch (IOException e) {
			throw new JRException(e);
		}

		return ds;
	}

	public static void exportReportToXHtmlFile(JasperPrint jasperPrint, String outputFile)
			throws JRException, IOException, InterruptedException {
		JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile);
		Runtime rTime = Runtime.getRuntime();
		System.out.println(System.getProperty("user.dir"));
		String url = System.getProperty("user.dir") +File.separator+"SeleniumFramework"+File.separator+"Test_Jasper_Report"+File.separator+"Report.html";
		String browser = "C:"+File.separator+"Program Files"+File.separator+"Internet Explorer"+File.separator+"iexplore.exe ";
		Process pc = rTime.exec(browser + url);
		pc.waitFor();
	}

	public static void JasperReportExecut() throws JRException, IOException, InterruptedException {
		String reportFile = "SeleniumFramework"+File.separator+"Jasper_Data"+File.separator+"Jasper.jrxml";
		JRXlsDataSource ds1 = getDataSource1();
		JasperPrint jasperPrint;
		JasperDesign jasperDesign = JRXmlLoader.load(reportFile);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds1);
		exportReportToXHtmlFile(jasperPrint, "SeleniumFramework"+File.separator+"Test_Jasper_Report"+File.separator+"Report.html");

	}

	public JacksonHandle readFulfillmentRecordById(String id) {
		String recordId = File.separator+ fulfillmentCollectionName +File.separator+ id + ".json";
		Boolean isExist = isExistFulfillmentRecord(id);
		if (isExist) {
			jacksonHandle = documentManager.read(recordId, fulfillmentMetadata, jacksonHandle);
		}
		return jacksonHandle;
	}

	public JacksonHandle readDispatchRecordById(String id) {
		String recordId = File.separator + DispatchCollectionName +File.separator+ id + ".json";
		Boolean isExist = isExistDispatchRecord(id);
		if (isExist) {
			jacksonHandle = documentManager.read(recordId, dispatchRecordMetadata, jacksonHandle);
		}
		return jacksonHandle;
	}

	public boolean isExistFulfillmentRecord(String id) {
		String recordId = File.separator + fulfillmentCollectionName + File.separator + id + ".json";
		desc = documentManager.exists(recordId);
		if (desc != null)
			return true;
		else
			return false;
	}

	public boolean isExistDispatchRecord(String id) {
		String recordId = File.separator + DispatchCollectionName +File.separator+ id + ".json";
		desc = documentManager.exists(recordId);
		if (desc != null)
			return true;
		else
			return false;
	}

	/*
	 * public static void main(String[] args) { JacksonHandle jacksonHandle=
	 * null; FulfillmentDAOImpl daoImpl = new FulfillmentDAOImpl();
	 * //jacksonHandle = daoImpl.readFulfillmentRecordById("test332");
	 * jacksonHandle = daoImpl.readFulfillmentRecordById("ISLTEST13");
	 * 
	 * JsonNode node = jacksonHandle.get(); System.out.println("Root Node" +
	 * node); //System.out.println(jacksonHandle.get().get("requestHeader"));
	 * 
	 * 
	 * // objMap.
	 * 
	 * Iterator<Map.Entry<String,JsonNode>> fieldsIterator = node.fields();
	 * while(fieldsIterator.hasNext()) { Map.Entry<String,JsonNode> field =
	 * fieldsIterator.next(); //System.out.println("field Key :" +
	 * field.getKey()); //System.out.println("field Value :" +
	 * field.getValue());
	 * 
	 * if (field.getKey() == "requestHistory") {
	 * 
	 * JsonNode innerNode = field.getValue(); System.out.println(
	 * "Inner key for Request history" + innerNode);
	 * findArrNodeValue(innerNode);
	 * 
	 * }
	 * 
	 * if (field.getKey() == "fulfillmentRequest") {
	 * 
	 * JsonNode innerNode = field.getValue();
	 * 
	 * System.out.println("Inner key for Request header " + innerNode);
	 * findNodeValue(innerNode);
	 * 
	 * }
	 * 
	 * } // System.out.println(node.fields());
	 * 
	 * //System.out.println(jacksonHandle.get().get("fulfillmentRequest").get(
	 * "requestHeader"));
	 * 
	 * 
	 * 
	 * }
	 */
	public void findNodeValue(JsonNode innerNode) {
		// TODO Auto-generated method stub

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = innerNode.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			// System.out.println("field Key :" + field.getKey());
			// System.out.println("field Value :" + field.getValue());

			if (field.getKey().equals("requestHeader")) {

				JsonNode childNode = field.getValue();
				System.out.println("Inner key for requestheader is " + childNode);
				Iterator<Map.Entry<String, JsonNode>> childFieldsIterator = childNode.fields();
				while (childFieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> childField = childFieldsIterator.next();
					System.out.println("childField field Key :" + childField.getKey());
					System.out.println(" childField field Value :" + childField.getValue());

				}

				// findNodeValue(innerNode);

			}
			// jsonNode.fields();
			// System.out.println("json Node :" + jsonNode);
		}

	}

	public void findArrNodeValue(JsonNode innerNode) {
		// TODO Auto-generated method stub

		for (JsonNode jsonNode : innerNode) {

			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				// System.out.println("field Key :" + field.getKey());
				// System.out.println("field Value :" + field.getValue());

				if (field.getKey().equals("eventType") && field.getValue().textValue().equals("Generated")) {

					JsonNode childNode = field.getValue();
					System.out.println("Inner key for event Type" + childNode);
					// findNodeValue(innerNode);

				}
				// jsonNode.fields();
				// System.out.println("json Node :" + jsonNode);
			}

		}

	}

	public static String readFile(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public void getAPIresponse(String fValue) throws JSONException, Exception {
		String jPath = "";
		APIactualResponse = "";
		for (String str : fValue.split("/")) {
			jPath = jPath + ""+File.separator+"" + str;
		}
		String payloadPath = "SeleniumFramework"+File.separator+"API" + jPath;

		HttpURLConnection httpURLConnection = null;
		JSONObject requestJsonObject = new JSONObject(readFile(payloadPath));
		HashMap<String, String> headerParameters = new HashMap<String, String>();
		
//		headerParameters.put(APIheader.split(":")[0], APIheader.split(":")[1]);
		for (String apiHeader : APIheader) {
			headerParameters.put(apiHeader.split(":")[0], apiHeader.split(":")[1]);
		}
		if (APImethod.equalsIgnoreCase("Post")) {
			httpURLConnection = (HttpURLConnection) ConnectionHelper.createPostConnection(APIurl, headerParameters);
		} else if (APImethod.equalsIgnoreCase("Get")) {
			httpURLConnection = (HttpURLConnection) ConnectionHelper.createGetConnection(APIurl, headerParameters);
		}
		APIactualResponse = ResponseHelper.postResponseObject(httpURLConnection, requestJsonObject);
		int responseCode = httpURLConnection.getResponseCode();
		System.out.println("#################### RESPONSE CODE: " + responseCode);
		if (APIactualResponse.isEmpty() || responseCode != 200) {
			System.out.println("Got no response for the API");
			throw new Exception("Got no response for the API");
		} 
//		else {
//			System.out.println("################## GOT RESPONSE: " +APIactualResponse);
//		}
		httpURLConnection.disconnect();
		APIheader.clear();
		headerParameters.clear();
	}

	public void checkAPIresponse(String fValue) throws Exception {
		String jsonPath = "";
		for (String str : fValue.split("/")) {
			jsonPath = jsonPath + ""+File.separator+"" + str;
		}
		String ExpectedJsonPath = "SeleniumFramework"+File.separator+"API" + jsonPath;
		if (APIactualResponse.isEmpty()) {
			System.out.println("No Response to compare. Seem the rest call failed");
			throw new Exception("No Response to compare. Seem the rest call failed");
		}
		JSONObject expectedJson = new JSONObject(readFile(ExpectedJsonPath));
		JSONObject actualJson = new JSONObject(APIactualResponse);

		System.out.println("#################### Actual Response ################");
		System.out.println(actualJson.toString());
		System.out.println("#################### Expected Response #################");
		System.out.println(expectedJson.toString());

		if (!expectedJson.toString().equals(actualJson.toString())) {
			System.out.println("Responses not matched");
			throw new Exception("API response didn't matched to expected");
		}
	}

}
//Test Commit
