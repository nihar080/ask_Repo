package com.SeleniumFramework.test;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.SeleniumFramework.commons.util.ExcelFileUtil;

public class TestExecutor extends FunctionalLibrary{

	public int reportCount, startTimeFlag,loopStartRow;
	public String action,objectName,testModulePath;
	public int TC_VAR;
	ExcelFileUtil excelFileUtil = ExcelFileUtil.getInstance();

	/**
	 * Method testSuite: Opens the Module List, identifies the total number of test modules to be executed and passes the module name to keywordDriver method whose execution flag is "Yes"
	 * @param uc 
	 * @param: none  
	 */
	public void testSuite(String moduleName, ReportLibrary reportLib, FunctionalLibrary seleniumHandler, String uc) 
	{ 
		//TODO: it iterates through list of modules and executes test cases

		try {
			// Opens Test Suite Driver Excel
		    FileInputStream TS = new FileInputStream(excelFileUtil.testSuite);
		    POIFSFileSystem poifs2 = new POIFSFileSystem(TS);
		    HSSFWorkbook TSUworkbook = new HSSFWorkbook(poifs2);
		    HSSFSheet readtsusheet = TSUworkbook.getSheet("ModuleList");

		    int MODULE_COUNT=1;
		    String flag;
		    int COUNTER =1;
			String executionFlag,moduleResultFolder;
			

			/**
			 * while loop below checks execution flag and calls keywordDriver method for each modulename whose flag is yes 
			 */
			while (COUNTER==1) 
			{
				flag = excelFileUtil.getCellValue(readtsusheet,MODULE_COUNT,0);
				if(flag.equalsIgnoreCase("End")) 
				{ 
					COUNTER=0;					
					System.out.println("All Test modules execution done");
				} else 
				{
					
					executionFlag = excelFileUtil.getCellValue(readtsusheet,MODULE_COUNT,3);
				    
				    if  (executionFlag.equalsIgnoreCase("Yes"))
				    {
				    	moduleName=excelFileUtil.getCellValue(readtsusheet,MODULE_COUNT,1);
				    	moduleResultFolder = moduleName+"_"+uc;
				    	moduleResultFolder = moduleResultFolder.replaceAll(" ","");
						String modulepath = excelFileUtil.htmlRep+"\\"+moduleResultFolder;
						scrshtPath = modulepath+"\\"+"ScreenShots";
						excelFileUtil.createFolder(modulepath);
						excelFileUtil.createFolder(scrshtPath);
						setUp(modulepath);
						
						PREVIOUS_TEST_CASE = "Before Test Execution";
						
						failedStep = " ";
					   
						
						keywordDriver(moduleName, reportLib, seleniumHandler,uc);
					
					}    
				      //Start the Next Script
				      MODULE_COUNT = MODULE_COUNT + 1;				      
				}
           }
		} catch(Exception e) {
			//LogVar= e.getMessage();
			System.out.println("Exception from TestSuite Function: " + e.getMessage());
		}
	}

	/**
	 * keywordDriver Method calls teststep method for each step enlisted in test module excel sheet, it handles looping too  
	 * @param uc 
	 * @param failedStep 
	 * @param moduleName: contains the name of test module to be executed
	 * @throws IOException 
	 */

	private void keywordDriver(String moduleName, ReportLibrary reportLib, FunctionalLibrary seleniumHandler, String uc) throws IOException 
	{// TODO: executeTestSuiteForModule()	

		try {
			strModuleName = moduleName;
			//createUpdateExcel(moduleName,uc);
			seleniumHandler.testcaseCounter=1;
			testModulePath =  excelFileUtil.testModuleContainerPath +"\\"+ strModuleName + ".xls";
			
			System.out.println("                                                ");
			System.out.println(" >>>>> TestInfo : Test Execution Started <<<<<<<");
			System.out.println("TestInfo : Execution Browser :"+excelFileUtil.platform);
		    System.out.println("TestInfo : Execution TestModule Name: "+testModulePath );
		    
		    FileInputStream TSN = new FileInputStream(testModulePath);
			POIFSFileSystem poifs3 = new POIFSFileSystem(TSN);
			seleniumHandler.scriptWorkbook = new HSSFWorkbook(poifs3);
			seleniumHandler.readScriptSheet = seleniumHandler.scriptWorkbook.getSheet("TestScript");
			seleniumHandler.readtestcasesheet = seleniumHandler.scriptWorkbook.getSheet("TestCases");

			seleniumHandler.TCCounter = 1;
			seleniumHandler.currTestRowPtr=1;
		    reportCount = 1;
		    startTimeFlag = 0;
		    seleniumHandler.TCCounter = 1;
		    /**
		     * Start Executing Test Case
		     * While loop below checks for each step in module*/
			String testName;
			boolean TestCaseFlag = true;

		    while(TestCaseFlag)
		    {//TODO: good to use true default and delete TestCaseFlag variable
		    	seleniumHandler.testFlag = excelFileUtil.getCellValue(seleniumHandler.readtestcasesheet, seleniumHandler.TCCounter,0);
				if(seleniumHandler.testFlag.equalsIgnoreCase("End")) 
				{
					TestCaseFlag = false;
				} else if(seleniumHandler.testFlag.equalsIgnoreCase("y"))
				{
					TC_VAR = 1;
					seleniumHandler.environment = excelFileUtil.getCellValue(seleniumHandler.readtestcasesheet, seleniumHandler.TCCounter,7);
					if(seleniumHandler.environment.equals(null)||seleniumHandler.environment.equals(" ")||seleniumHandler.environment.equals(""))
					{
						seleniumHandler.url = excelFileUtil.mainUrl;
					}else if(seleniumHandler.environment.equalsIgnoreCase("Production"))
					{
						seleniumHandler.url = excelFileUtil.productionUrl;
					}else if(seleniumHandler.environment.equalsIgnoreCase("Stage"))
					{
						seleniumHandler.url = excelFileUtil.stageUrl;
					}else if(seleniumHandler.environment.equalsIgnoreCase("Offline"))
					{
						seleniumHandler.url = excelFileUtil.offlineUrl;
					}
					else if(seleniumHandler.environment.equalsIgnoreCase("Live"))
					{
						seleniumHandler.url = excelFileUtil.liveUrl;
					}else if(seleniumHandler.environment.equalsIgnoreCase("test1"))
					{
						System.out.println("TestInfo : Execution Environment :"+seleniumHandler.environment);
						seleniumHandler.url = excelFileUtil.test1;
					}else if(seleniumHandler.environment.equalsIgnoreCase("test2"))
					{
						seleniumHandler.url = excelFileUtil.test2;
					}else if(seleniumHandler.environment.equalsIgnoreCase("test3"))
					{
						seleniumHandler.url = excelFileUtil.test3;
					}else
					{
						seleniumHandler.url = excelFileUtil.mainUrl;
					}
					seleniumHandler.LOG_VAR=1;
					seleniumHandler.testFlag="y";
					seleniumHandler.TEST_STEP_COUNT = 1;
					testName=excelFileUtil.getCellValue(seleniumHandler.readtestcasesheet, seleniumHandler.TCCounter,2);
					System.out.println("TestInfo : Executing Testcase :" + testName);
					int[] testSteps = seleniumHandler.getTotalStepsAndStepPointer(testName);
					seleniumHandler.currTestRowPtr=testSteps[1];
					seleniumHandler.startRow = testSteps[2];
					seleniumHandler.endRow = testSteps[3];
	    			int testMatch =  testSteps[4];
	    			seleniumHandler.tcStartTime = seleniumHandler.getStartTime();	
					//executeTest(testName);checkForNewTestCase();
	    			
				if(testMatch == 1) 
				{
					if(seleniumHandler.LOOP_FLAG) 
					{ 
						int loopCount;
			    		int ParamFlag = 1;
			    		String tempFailedStep=" ";			    		  
			    		// int LoopedTestStepCount;
			    		loopCount= seleniumHandler.getNumberofIterations();
			    		// boolean First_Iteration = false;	    		 
			    		System.out.println("LoopCount: " + loopCount);
		    			if(seleniumHandler.startRow>seleniumHandler.currTestRowPtr)
		    			{
		    				loopStartRow = seleniumHandler.currTestRowPtr;
		    			} else 
		    			{
		    				loopStartRow =seleniumHandler.startRow;
		    			}
		    		    for(tempCounter = 1;tempCounter<=loopCount;tempCounter++) 
		    		    {
		    		    	String Temp = "Iteration: " + seleniumHandler.tempCounter;
		    				f_sendTestStepResultIteration(Temp);
		    			    System.out.println("LOG_VAR : " + seleniumHandler.LOG_VAR);
			    		    if(LOG_VAR==0) 
			    		    {
			    			    LOG_VAR=1;
			    			    seleniumHandler.testFlag="y";
			    			    
			    	        }
			    		    while(loopStartRow<=seleniumHandler.endRow) 
			    		    {
			    			    if(LOG_VAR==0)
			    			    {
			    				    ParamFlag = LOG_VAR;
			    				    tempFailedStep = failedStep;
				    			    break;
				    		    }
			    			    
			    			    validate1 = "";
			    			   
			    			    testStep(seleniumHandler, reportLib);		    			 
			    			    
			    			    loopStartRow = loopStartRow+1;
			    		    }		    			 
			    		    seleniumHandler.TEST_STEP_COUNT = (seleniumHandler.startRow-seleniumHandler.currTestRowPtr)+1;  //TEST_STEP_COUNT is for reporting looped steps for each data set.
			    		    loopStartRow = seleniumHandler.startRow;
			    		    String ETemp = "End-Iter: " + seleniumHandler.tempCounter;
			    		    f_sendTestStepResultIteration(ETemp);
			    	    }
			    	    if(seleniumHandler.LOG_VAR ==1) 
			    	    {
			    		    seleniumHandler.LOG_VAR = ParamFlag;
			    		    reportLib.failedStep = tempFailedStep;
			    	    }
			    	   
			        }
					
					else 
			        {
			        	int teststepcount=testSteps[0]; //No Loop flags it jump here
			    		for(int i=1; i<=teststepcount; i++) 
			    		{
			    			 //Passing empty validate value so that existing value must not written for any exception in next validate statement.
		    			    reportLib.validate1 = "";
			    			testStep(seleniumHandler, reportLib);
				            seleniumHandler.currTestRowPtr = seleniumHandler.currTestRowPtr+1;
			    		 }	     
			    }

					   //excelFileUtil.writeStepExcel(seleniumHandler.PREVIOUS_TEST_CASE,seleniumHandler.LOG_VAR,reportLib.failedStep,"N", reportLib.QCExcelPath,seleniumHandler.testcaseCounter);
					  // excelFileUtil.writeStepExcel(seleniumHandler.PREVIOUS_TEST_CASE,TC_VAR,reportLib.failedStep,"N", reportLib.QCExcelPath,seleniumHandler.testcaseCounter);
					   seleniumHandler.testcaseCounter = seleniumHandler.testcaseCounter+1;
					   reportLib.f_sendTestCaseResult(seleniumHandler.TC_ID,reportLib.strModuleName,seleniumHandler.PREVIOUS_TEST_CASE,seleniumHandler.TC_DESC,seleniumHandler.tcStartTime,TC_VAR,reportLib.failedStep);   
					   funExcelResult(seleniumHandler.TC_ID,PREVIOUS_TEST_CASE,strModuleName,TC_VAR,excelFileUtil.platform,failedTCount,passedTCount,seleniumHandler.url);
				}
			   }
			   seleniumHandler.TCCounter = seleniumHandler.TCCounter+1;
		    }
	    } catch(Exception e) {
		    seleniumHandler.LOG_VAR= 0;
		    e.printStackTrace();
		   //failedStep = getCellValue(readScriptSheet,currTestRowPtr,1);
		   System.out.println("Exception from KeywordDriver Function: " + e.getMessage());						
	   }
    }

	/**
	 * Method testStep: Gets element and performs action over the element
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private void testStep(FunctionalLibrary seleniumHandler, ReportLibrary reportLib) throws IOException, InterruptedException {	//TODO: executeTestStep	
		try {
			int tempStartRow;
		    if(seleniumHandler.LOOP_FLAG) 
		    {
		    	tempStartRow=loopStartRow;
		    } else 
		   {
			   tempStartRow=seleniumHandler.currTestRowPtr;
		   }
	      if (seleniumHandler.testFlag.equalsIgnoreCase("y"))
	      {
	    	    	
	    	  screenName = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,3);//readscriptsheet.getRow(temp_Start_Row).getCell(2).getStringCellValue().trim();
	    	  action = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,4);//readscriptsheet.getRow(temp_Start_Row).getCell(3).getStringCellValue().trim();
	    	  inputSheet = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,5);//readscriptsheet.getRow(temp_Start_Row).getCell(4).getStringCellValue().trim();
	    	  
	    	  System.out.println("Screen Name:"+seleniumHandler.screenName + "||"+"Action :"+ action + " ||"+"TestData Sheet :"+ seleniumHandler.inputSheet );  	
	    	 
	    	if (inputSheet.trim().isEmpty())
	    	  {
	    		  int FIELD_INDEX = 0;				
				  int INDEX_COUNTER = 0;
				  boolean ROW_FLAG=true;
				  /**
				   * while loop below gets field name and field value from script sheet untill fields are empty.
				   */
				  while (INDEX_COUNTER<500 && ROW_FLAG) 
				  {
					  fieldName = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,6+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
					  fieldValue=excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,7+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();
                      
					  if((fieldName.isEmpty()) || (fieldName.equals(""))||(fieldName.equals(null)))
					  {
							if ((seleniumHandler.fieldValue.isEmpty()) || (seleniumHandler.fieldValue.equals(""))||(seleniumHandler.fieldValue.equals(null))) 
							{
								ROW_FLAG=false;
								break;
							}else 
							{
								FIELD_INDEX=FIELD_INDEX+2;
							}
					   } 
					  else 
					   {
						   FIELD_INDEX=FIELD_INDEX+2;
					   }
					  //Get Object and its Element Type from Object Repository
					  String[]  actionObject = excelFileUtil.getObject(seleniumHandler.screenName,seleniumHandler.fieldName);
                      
					  System.out.println("Field Locater Type :" + actionObject[1]+"||"+" Field Locater Value: "+actionObject[0]);
					  fieldElementType = actionObject[1];
					  objectName = actionObject[0];
					  
					  //Passes (objectName, fieldElementType,fieldValue, action) to keyword method where desired action is performed over the object.
					  System.out.println("FieldName: "+seleniumHandler.fieldName+ "|| " + "FieldValue: " + seleniumHandler.fieldValue);
					  
					 LOG_VAR = 1;
					 keyword(objectName, seleniumHandler.fieldElementType,seleniumHandler.fieldValue, action,seleniumHandler.fieldName);
					  if(LOG_VAR==1) 
					  {
						  failedStep = " ";
						  failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
                          System.out.println("TestInfo: Test Step passed !!" );
					  } else if(seleniumHandler.LOG_VAR==0) 
					  {
					  try {
						  	TC_VAR = 0;
						  	failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
						  if(reportLib.failedStep.isEmpty()||reportLib.failedStep.equals(null)||reportLib.failedStep.equals(" ")||reportLib.failedStep.equals("")) {
							  System.out.println("TestInfo: Test Step failed :"+reportLib.failedStep);
							  reportLib.failedStep=" ";
						  }
                      } catch(Exception e) {
                          reportLib.failedStep = " ";
						  System.out.println("TestError :Exception handling, Passing white space to failed Step");
					 }
				 }
					  //System.out.println("Action:" + action);	 
					  try{
					        f_sendTestStepResult(seleniumHandler.TC_ID,reportLib.strModuleName,seleniumHandler.PREVIOUS_TEST_CASE,"TestStep_"+seleniumHandler.TEST_STEP_COUNT,seleniumHandler.screenName,action,seleniumHandler.fieldName,seleniumHandler.fieldValue, seleniumHandler.LOG_VAR);
					        }catch(Exception e)
					        {
					        	e.printStackTrace();
					        }
				 
                  System.out.println("Test Case Name: " + seleniumHandler.PREVIOUS_TEST_CASE);
                  
			      INDEX_COUNTER = INDEX_COUNTER+1;
			      seleniumHandler.fieldName = "";
			      seleniumHandler.fieldValue = "";
			      if(seleniumHandler.LOG_VAR==0) 
			      {
			          
			    	  TC_VAR = 0;
			    	  TEST_STEP_COUNT=TEST_STEP_COUNT+1;
			        
			      }else 
			      {
			          TEST_STEP_COUNT=TEST_STEP_COUNT+1;
			      }
			   }
	    	}
	    else if (action.equalsIgnoreCase("ValidateResponse")) 
	    		{
	    			System.out.println("TestInfo : Getting info Webservice Validation Sheet");
	    			
	    			  int FIELD_INDEX = 0;				
					  int INDEX_COUNTER = 0;
					  boolean ROW_FLAG=true;
					  /**
					   * while loop below gets field name and field value from script sheet untill fields are empty.
					   */
					  while (INDEX_COUNTER<500 && ROW_FLAG) 
					  {
						  fieldName = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,6+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
						  fieldValue=excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,7+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();
	                      
						  if((fieldName.isEmpty()) || (fieldName.equals(""))||(fieldName.equals(null)))
						  {
								if ((seleniumHandler.fieldValue.isEmpty()) || (seleniumHandler.fieldValue.equals(""))||(seleniumHandler.fieldValue.equals(null))) 
								{
									ROW_FLAG=false;
									break;
								}else 
								{
									FIELD_INDEX=FIELD_INDEX+2;
								}
						   } 
						  else 
						   {
							   FIELD_INDEX=FIELD_INDEX+2;
						   }
						  //Get Object and its Element Type from Object Repository
						  String[]  actionObject = excelFileUtil.getObject(seleniumHandler.screenName,seleniumHandler.fieldName);
	                      
						  System.out.println("Field Locater Type :" + actionObject[1]+"||"+" Field Locater Value: "+actionObject[0]);
						  fieldElementType = actionObject[1];
						  objectName = actionObject[0];
						  
						  //Passes (objectName, fieldElementType,fieldValue, action) to keyword method where desired action is performed over the object.
						  System.out.println("FieldName: "+seleniumHandler.fieldName+ "|| " + "FieldValue: " + seleniumHandler.fieldValue);
						  
						 LOG_VAR = 1;
						 keyword(objectName, seleniumHandler.fieldElementType,seleniumHandler.fieldValue, action,seleniumHandler.fieldName);
						  if(LOG_VAR==1) 
						  {
							  failedStep = " ";
							  failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
	                          System.out.println("TestInfo: Test Step passed !!" );
						  } else if(seleniumHandler.LOG_VAR==0) 
						  {
						  try {
							  	TC_VAR = 0;
							  	failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
							  if(reportLib.failedStep.isEmpty()||reportLib.failedStep.equals(null)||reportLib.failedStep.equals(" ")||reportLib.failedStep.equals("")) {
								  System.out.println("TestInfo: Test Step failed :"+reportLib.failedStep);
								  reportLib.failedStep=" ";
							  }
	                      } catch(Exception e) {
	                          reportLib.failedStep = " ";
							  System.out.println("TestError :Exception handling, Passing white space to failed Step");
						 }
					 }
						  //System.out.println("Action:" + action);	 
						  try{
						        f_sendTestStepResult(seleniumHandler.TC_ID,reportLib.strModuleName,seleniumHandler.PREVIOUS_TEST_CASE,"TestStep_"+seleniumHandler.TEST_STEP_COUNT,seleniumHandler.screenName,action,seleniumHandler.fieldName,seleniumHandler.fieldValue, seleniumHandler.LOG_VAR);
						        }catch(Exception e)
						        {
						        	e.printStackTrace();
						        }
					 
	                  System.out.println("Test Case Name: " + seleniumHandler.PREVIOUS_TEST_CASE);
	                  
				      INDEX_COUNTER = INDEX_COUNTER+1;
				      seleniumHandler.fieldName = "";
				      seleniumHandler.fieldValue = "";
				      if(seleniumHandler.LOG_VAR==0) 
				      {
				          
				    	  TC_VAR = 0;
				    	  TEST_STEP_COUNT=TEST_STEP_COUNT+1;
				        
				      }else 
				      {
				          TEST_STEP_COUNT=TEST_STEP_COUNT+1;
				      }
				   }
	    		}
	    else if (action.equalsIgnoreCase("ValidateResponseExcel")) 
		{
			System.out.println("TestInfo : Getting info from Soap Outputfile Sheet");
			
			  int FIELD_INDEX = 0;				
			  int INDEX_COUNTER = 0;
			  boolean ROW_FLAG=true;
			  /**
			   * while loop below gets field name and field value from script sheet untill fields are empty.
			   */
			  while (INDEX_COUNTER<500 && ROW_FLAG) 
			  {
				  fieldName = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,7+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(7+FieldIndex).getStringCellValue().trim();
				  fieldValue=excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,6+FIELD_INDEX).trim();//readscriptsheet.getRow(temp_Start_Row).getCell(8+FieldIndex).getStringCellValue().trim();
                  
				  if((fieldName.isEmpty()) || (fieldName.equals(""))||(fieldName.equals(null)))
				  {
						if ((seleniumHandler.fieldValue.isEmpty()) || (seleniumHandler.fieldValue.equals(""))||(seleniumHandler.fieldValue.equals(null))) 
						{
							ROW_FLAG=false;
							break;
						}else 
						{
							FIELD_INDEX=FIELD_INDEX+2;
						}
				   } 
				  else 
				   {
					   FIELD_INDEX=FIELD_INDEX+2;
				   }
				  //Get Object and its Element Type from Object Repository
				  String[]  actionObject ;
                  
				  fieldElementType = " ";
				  objectName = "";
				  //String Manualsheet = fieldName; 
				  //Passes (objectName, fieldElementType,fieldValue, action) to keyword method where desired action is performed over the object.
				  System.out.println("FieldName: "+seleniumHandler.fieldName+ "|| " + "FieldValue: " + seleniumHandler.fieldValue);
				  
				 LOG_VAR = 1;
				 keyword(objectName, seleniumHandler.fieldElementType,seleniumHandler.fieldValue, action,seleniumHandler.fieldName);
				  if(LOG_VAR==1) 
				  {
					  failedStep = " ";
					  failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
                      System.out.println("TestInfo: Test Step passed !!" );
				  } else if(seleniumHandler.LOG_VAR==0) 
				  {
				  try {
					  	TC_VAR = 0;
					  	failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
					  if(reportLib.failedStep.isEmpty()||reportLib.failedStep.equals(null)||reportLib.failedStep.equals(" ")||reportLib.failedStep.equals("")) {
						  System.out.println("TestInfo: Test Step failed :"+reportLib.failedStep);
						  reportLib.failedStep=" ";
					  }
                  } catch(Exception e) {
                      reportLib.failedStep = " ";
					  System.out.println("TestError :Exception handling, Passing white space to failed Step");
				 }
			 }
				  //System.out.println("Action:" + action);	 
				  try{
				        f_sendTestStepResult(seleniumHandler.TC_ID,reportLib.strModuleName,seleniumHandler.PREVIOUS_TEST_CASE,"TestStep_"+seleniumHandler.TEST_STEP_COUNT,seleniumHandler.screenName,action,seleniumHandler.fieldName,seleniumHandler.fieldValue, seleniumHandler.LOG_VAR);
				        }catch(Exception e)
				        {
				        	e.printStackTrace();
				        }
			 
              System.out.println("Test Case Name: " + seleniumHandler.PREVIOUS_TEST_CASE);
              
		      INDEX_COUNTER = INDEX_COUNTER+1;
		      seleniumHandler.fieldName = "";
		      seleniumHandler.fieldValue = "";
		      if(seleniumHandler.LOG_VAR==0) 
		      {
		    	  TC_VAR = 0;
		    	  TEST_STEP_COUNT=TEST_STEP_COUNT+1; 
		      }else 
		      {
		          TEST_STEP_COUNT=TEST_STEP_COUNT+1;
		      }
		   }
		}
	    else
	    	{
	    	
	    		String Test_Data = excelFileUtil.test_data;// WB - Here need to pass service name
	    	
	    		//performing action for parameterized steps
	    	    seleniumHandler.readLoopSheet=seleniumHandler.scriptWorkbook.getSheet(Test_Data); //Input sheet not empty it jumps here
	    	    
	    	    //seleniumHandler.inputSheet;
	    	    int LOOP_INDEX = 0;
	    		int LOOP_INDEX_COUNTER = 0;
	    		//int DATALIST_HEADER = 0;	    		
	    		String loopedFieldName, loopedFieldValue, delimiter;
	    		int dataListPointer = 0;
	    		int tempCounternoloop = 1;
	    		
	    		delimiter = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet, dataListPointer,0);
	    		while(!delimiter.equalsIgnoreCase("End")) // May be this condition not required for WB
	    		{
	    			if(delimiter.equalsIgnoreCase(seleniumHandler.inputSheet))
	    			{	    				
	    				break;
	    			}else
	    			{
	    				dataListPointer = dataListPointer+1;
	    				delimiter = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet, dataListPointer,0);
	    			}
	    		}
	    		/**
	    		 * This loop will get Datasheet's name and Column name from FieldName and(or) FieldValue of the current row of Script.
	    		 * It will Split the Datasheet and Value/Name column Names.
	    		 * Opens the Datasheet starts getting FieldName and(or)FieldValue one by one. 		
	    		 */	 
	    		while(LOOP_INDEX_COUNTER<500) 
	    		{
	    		    loopedFieldName = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,6+LOOP_INDEX);//readscriptsheet.getRow(temp_Start_Row).getCell(7+loopIndex).getStringCellValue().trim();
					loopedFieldValue=excelFileUtil.getCellValue(seleniumHandler.readScriptSheet, tempStartRow,7+LOOP_INDEX);//readscriptsheet.getRow(temp_Start_Row).getCell(8+loopIndex).getStringCellValue().trim();

					if (loopedFieldName.isEmpty()) 
					{
					    if (loopedFieldValue.isEmpty())
					    {
					        break;
					     }
					    
					    else 
					     {
					         LOOP_INDEX=LOOP_INDEX+2;
							 int FIELD_VALUE_CLMN_NO1=0;
							 int FIELD_VALUE_LOOP_CNTR1 = 0;
							
							 String getFieldValueColumnHeader1;
							 while(FIELD_VALUE_LOOP_CNTR1<500) 
							 {
							     getFieldValueColumnHeader1 = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet, dataListPointer,FIELD_VALUE_LOOP_CNTR1);//readloopsheet.getRow(0).getCell(dataloopcounter1).getStringCellValue().trim();
                                 if(getFieldValueColumnHeader1.equalsIgnoreCase(loopedFieldValue))
                                 {
                                     FIELD_VALUE_CLMN_NO1 = FIELD_VALUE_LOOP_CNTR1;												
									 break;
								 }											
								  FIELD_VALUE_LOOP_CNTR1=FIELD_VALUE_LOOP_CNTR1+1;
							 }

							 seleniumHandler.fieldValue = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet,dataListPointer + seleniumHandler.tempCounter,FIELD_VALUE_CLMN_NO1);//readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber1).getStringCellValue().trim();								
							 System.out.println("FieldValue: " + seleniumHandler.fieldValue);
							 
							 tempCounternoloop = tempCounternoloop+1;
						}
					}else 
					{
					    LOOP_INDEX=LOOP_INDEX+2;
						if(!(loopedFieldValue.isEmpty())) 
						{
						    int FIELD_VALUE_CLMN_NO2=0;
							int FIELD_VALUE_LOOP_CNTR2 = 0;
							String getFieldValueColumnHeader2;								
							while(FIELD_VALUE_LOOP_CNTR2<500)
							{
							    getFieldValueColumnHeader2 = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet,dataListPointer,FIELD_VALUE_LOOP_CNTR2);//readloopsheet.getRow(0).getCell(dataloopcounter2).getStringCellValue().trim();
								if(getFieldValueColumnHeader2.equalsIgnoreCase(loopedFieldValue)) 
								{									
									FIELD_VALUE_CLMN_NO2 = FIELD_VALUE_LOOP_CNTR2;
									break;
								}
								FIELD_VALUE_LOOP_CNTR2=FIELD_VALUE_LOOP_CNTR2+1;
							}
							
							seleniumHandler.fieldValue=excelFileUtil.getCellValue(seleniumHandler.readLoopSheet, dataListPointer+seleniumHandler.tempCounter,FIELD_VALUE_CLMN_NO2);//readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber2).getStringCellValue().trim();
							System.out.println("FieldValue: " + seleniumHandler.fieldValue);
						}						
						 int FIELD_NAME_CLMN_CNTR = 0;
						 int FIELD_NAME_CLMN_NO=0;
						 String getFieldNameColumnHeader;								
						 while(FIELD_NAME_CLMN_CNTR<500) 
						 {
						     getFieldNameColumnHeader = excelFileUtil.getCellValue(seleniumHandler.readLoopSheet,dataListPointer,FIELD_NAME_CLMN_CNTR);//readloopsheet.getRow(0).getCell(dataloopcounter).getStringCellValue().trim();
							 if(getFieldNameColumnHeader.equalsIgnoreCase(loopedFieldName))
							 {
							     FIELD_NAME_CLMN_NO = FIELD_NAME_CLMN_CNTR;
								 break;
							 }
							 
							 FIELD_NAME_CLMN_CNTR=FIELD_NAME_CLMN_CNTR+1;
						 }							
						// fieldName=excelFileUtil.getCellValue(seleniumHandler.readLoopSheet, dataListPointer+seleniumHandler.tempCounter,FIELD_NAME_CLMN_NO);//readloopsheet.getRow(Temp_Counter).getCell(ColumnNumber).getStringCellValue().trim();
						 fieldName = loopedFieldName; //Naveen
						 System.out.println("FieldName: " + seleniumHandler.fieldName);	
					} 				
					//Get Object and its Element Type from Object Repository
					String[]  actionObject = excelFileUtil.getObject(seleniumHandler.screenName,seleniumHandler.fieldName);				
					fieldElementType = actionObject[1];
					objectName = actionObject[0];
					 seleniumHandler.LOG_VAR = 1;
					//Performs Action
					seleniumHandler.keyword(objectName, seleniumHandler.fieldElementType,seleniumHandler.fieldValue, action,seleniumHandler.fieldName);
			        if(seleniumHandler.LOG_VAR==1) 
			        {
			            reportLib.failedStep = " ";
						System.out.println("TestInfo: Test Step passed !!");
					}else if(seleniumHandler.LOG_VAR==0) {
						  try {
							  	TC_VAR = 0;
							  	reportLib.failedStep = excelFileUtil.getCellValue(seleniumHandler.readScriptSheet,tempStartRow,1);
							  	if(reportLib.failedStep.isEmpty()||reportLib.failedStep.equals(null)||reportLib.failedStep.equals(" ")||reportLib.failedStep.equals("")) 
							  	{
								 System.out.println("TestError :Failed Step : "+reportLib.failedStep);
								  reportLib.failedStep=" ";
							  }
	                      } catch(Exception e)
	                      {
	                          failedStep = " ";
							  System.out.println("Exception handling, Passing white space to failed Step");
						 }
					 }
						  
			        try{
						   f_sendTestStepResult(seleniumHandler.TC_ID,reportLib.strModuleName,seleniumHandler.PREVIOUS_TEST_CASE,"TestStep_"+seleniumHandler.TEST_STEP_COUNT,seleniumHandler.screenName,action,seleniumHandler.fieldName,seleniumHandler.fieldValue, seleniumHandler.LOG_VAR);
						 }catch(Exception e)
						        {
						        	e.printStackTrace();
						        }

			        seleniumHandler.fieldName = "";
			        seleniumHandler.fieldValue = "";
					if(seleniumHandler.LOG_VAR==0)
			        {
						//failedStep = getCellValue(readScriptSheet,currTestRowPtr,1);
						 TC_VAR = 0;
						break;
			        }else
			        {
			        	TEST_STEP_COUNT = TEST_STEP_COUNT+1;
			        }
			        LOOP_INDEX_COUNTER = LOOP_INDEX_COUNTER+1;
					}
	    	}        
	      }
		}catch(Exception e)
			{
			 TC_VAR = 0;
			seleniumHandler.LOG_VAR= 0;
			//failedStep = getCellValue(readScriptSheet,currTestRowPtr,1);
			System.out.println("Exception from TestStep Function: " + e.getMessage());
			String Trace = "Exception from TestStep Function: " + e.getMessage();
			reportLib.sendLog(Trace, seleniumHandler.PREVIOUS_TEST_CASE, seleniumHandler.TEST_STEP_COUNT);
			//sendLog(e.getStackTrace());
			seleniumHandler.testFlag="n";
			}
	    }

	
}
