package mobileApplicationTestingLivebaze;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class LiveBazeLoginModule {
	
	//Android driver & Web driver wait objects
			AndroidDriver<AndroidElement> driver = null;
	//Web driver wait object
			WebDriverWait wait = null;
	//Excel file path object
			String excelFilePath = null;
	//File input stream object
			FileInputStream inputStream = null;
	//Excel workbook object
			Workbook workbook = null;
	//Desired capabilities object
			DesiredCapabilities capabilities = null;
	//Output stream object
			FileOutputStream outputStream = null; 		
			
			
			//This method will run before the start of each test.
			@Before
			public void setUp() throws Exception {

				//File path to excel sheet which contains username and passwords of sites.
						excelFilePath = "E:\\files\\appiumtest.xls"; //------------------------> Excel File Path Needs To Be Updated With Respect To Your System.
				
				//Input Stream for excel file.
						inputStream = new FileInputStream(excelFilePath);
				
				//Creating Workbook.
						workbook = new HSSFWorkbook(inputStream);
						
				//File path to APK
						File androidApk = new File("E:\\files\\livebaze.apk"); //------------------------> Android APK  File Path Needs To Be Updated With Respect To Your System.
						System.out.println("Info: File path of android apk:\n" + androidApk.getAbsolutePath());
						System.out.println("Info: File path of excel file:\n" + excelFilePath);

				//Setting Capabilities For Android Platform.
						capabilities = new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
						capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");//------------------------> Change to "Android Emulator" if emulator available..
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "120000");
						capabilities.setCapability(MobileCapabilityType.APP, androidApk.getAbsolutePath());
				
			}			
			
			//Livebaze login test.
			@Test 
			public void livebazeLogintest() throws InterruptedException, IOException {
				  
				  //Reading Sheet From Workbook.
					Sheet livebaze = workbook.getSheet("livebaze");
					
				  for (Row row : livebaze) { //Start of for each loop.
					  
					  	if (row.getRowNum() >= 1) { //First if condition.
					  		
					  		System.out.println("Info: Current Row Number: " + row.getRowNum());
					  		
					  		//Cell information stored in program.
					  		Cell currentUsernameE  =  row.getCell(0); // First cell in the row.
							Cell currentPasswordE  =  row.getCell(1); // Second cell in the row.
							Cell currentNickNameE  =  row.getCell(2); // Third cell in the row.
							Cell expectedResultE   =  row.getCell(3); // Forth cell in the row.
							
							//Check whether cell values are empty.
							String currentUsername  =  currentUsernameE == null ? "" : currentUsernameE.toString(); 
							String currentPassword  =  currentPasswordE == null ? "" : currentPasswordE.toString();
							String currentNickName  =  currentNickNameE == null ? "" : currentNickNameE.toString(); 
							String expectedResult   =  expectedResultE  == null ? "" : expectedResultE.toString();  
							
							//Row information printed to console.
							System.out.println("User Information From Excel Sheet:");
							System.out.println("Livebaze User Name:   " + currentUsername);
							System.out.println("Livebaze Password:    " + currentPassword);
							System.out.println("Livebaze Nick Name:   " + currentNickName);
							
							
									// Condition = Username: empty + Password: empty + Expected Result: Test Failed.
									if (currentUsername.equals("") && currentPassword.equals("") && expectedResult.equals("Test Failed")) {
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Blank"  );
										System.out.println("Password: Blank"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: No username and  password is provided so the user should not be able to login to the application.");
										
							//Appium code
										
										  //Android Driver Start Method
										  driver = androidDriverStart();
										  
										  //Web Driver Wait
										  wait = webDriverWait(driver);
										
										  //Login Method
										  liveBazeLogin(driver, wait, currentUsername, currentPassword);
										  
								  		  //Username
								  		  String externalUsername = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_account"))).getText();
								  		  
										  //Test Case Checking
										  assertEquals("Username", externalUsername);
										  								 
										  //Test completion notification.
										  System.out.println("Info: Test Completed");
										  
										  //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Failed");
										  
										  //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook);
										  
										  
										  
									// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
									} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Blank"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
										
							//Appium code
										
										   //Android Driver Start Method
										  driver = androidDriverStart();
										  
										  //Web Driver Wait
										  wait = webDriverWait(driver);
										
										  //Login Method
										  liveBazeLogin(driver, wait, currentUsername, currentPassword);
										  
										  //Timeout for explicit wait.
										  wait = new WebDriverWait(driver, 120);
								  		  
								  		  //Username
								  		  String externalUsername = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_account"))).getText();
								  		 
										  //Test Case Checking.
										  assertEquals("Username", externalUsername);
										  
										  //Test completion notification.
										  System.out.println("Info: Test Completed");
										  
										  //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Failed");
										 
										  //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook);
										  
										  
										
									// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
									} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Blank"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
										
							//Appium code
										
										  //Android Driver Start Method
										  driver = androidDriverStart();
										  
										  //Web Driver Wait
										  wait = webDriverWait(driver);
										  
										  //Login Method
										  liveBazeLogin(driver, wait, currentUsername, currentPassword);
										  
										  //Timeout for explicit wait.
										  wait = new WebDriverWait(driver, 120);
								  		  
								  		  //Username
								  		  String externalPassword = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_password"))).getText();
		
										  //Test Case Checking.
										  assertEquals(currentPassword, externalPassword);
										  
										  //Test completion notification.
										  System.out.println("Info: Test Completed");
										  
										  //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Failed");
										 
										  //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook);
										  
										    
										
									// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
									} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: The username and/or password are not correct so the user should not be able to login to the application.");
										
							//Appium code
										
										  //Android Driver Start Method
										  driver = androidDriverStart();
										  
										  //Web Driver Wait
										  wait = webDriverWait(driver);
										  
										  //Login Method
										  liveBazeLogin(driver, wait, currentUsername, currentPassword);
										  
										  //Timeout for explicit wait.
										  wait = new WebDriverWait(driver, 120);
								  		  
								  		  //Username
								  		  String externalUsername = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_account"))).getText();
								  		  
										  //Test Case Checking
										  assertEquals(currentUsername, externalUsername);
										  
										  //Test completion notification.
										  System.out.println("Info: Test Completed");
										  
										  //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Failed");
										    
										  //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook);
										
									
									// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
									} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Passed");
										System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
										
							//Appium code
									
										  //Android Driver Start Method
										  driver = androidDriverStart();
										  
										  //Web Driver Wait
										  wait = webDriverWait(driver);
										  
										  //Login Method
										  liveBazeLogin(driver, wait, currentUsername, currentPassword);
										  
										  //Timeout for explicit wait.
										  wait = new WebDriverWait(driver, 120);
								  		  
								  		  //User nick name
								  		  String externalNickName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/me_tv_login_name_new"))).getText();
										
										  //Testing case checking
										  assertEquals(currentNickName, externalNickName);
										  
										  //Actual testcase result
										  System.out.println("Test case actual result: Test Passed");
									
										  //Test completion notification
										  System.out.println("Info: Test Completed");
										 
										  //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Passed");
										  
										  //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook);
										  	  
									}
									
									else {
										
										System.out.println("There was an exception. Please check your " + "\"Test Case Nature\"" + " in excel sheet.");
										System.out.println("Info: Writing result to excel sheet.");
										row.createCell(4).setCellValue("Check Test case");
									} //End of if, else if, else
						
							
						} else {

									System.out.println("Info: Livebaze excel sheet header row skipped ...");
							
						} //End of first if condition.
					  
					
				      } //End of for each loop.
				  		
				  		//Closing input stream
				  		inputStream.close();
				  		
				  		//Closing workbook
				  		workbook.close();
				  		
				  		//Writing to console 
						System.out.println("Info: All Tests Completed !!!"); 
				  
			  }//Livebaze login test completion	
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------			  
			  
			//Facebook login test.
			 @Test 
			public void livebazeFacebookLogintest() throws InterruptedException, IOException {
					
				  //Reading Sheet From Workbook.
					Sheet facebook = workbook.getSheet("facebook");

				  for (Row row : facebook) { //Start of for each loop.
					  
					  	if (row.getRowNum() >= 1) { //First if condition.
					  		
					  		System.out.println("Info: Current Row Number: " + row.getRowNum());
					  		
					  		//Cell information stored in program.
					  		Cell currentUsernameE  =  row.getCell(0); // First cell in the row.
							Cell currentPasswordE  =  row.getCell(1); // Second cell in the row.
							Cell currentNickNameE  =  row.getCell(2); // Third cell in the row.
							Cell expectedResultE   =  row.getCell(3); // Forth cell in the row.
							
							//Check whether cell values are empty.
							String currentUsername  =  currentUsernameE == null ? "" : currentUsernameE.toString(); 
							String currentPassword  =  currentPasswordE == null ? "" : currentPasswordE.toString();
							String currentNickName  =  currentNickNameE == null ? "" : currentNickNameE.toString(); 
							String expectedResult   =  expectedResultE  == null ? "" : expectedResultE.toString();  
							
							//Row information printed to console.
							System.out.println("User Information From Excel Sheet:");
							System.out.println("Facebook User Name:   " + currentUsername);
							System.out.println("Facebook Password:    " + currentPassword);
							System.out.println("Facebook Nick Name:   " + currentNickName);
							
							
									// Condition = Username: empty + Password: empty + Expected Result: Test Failed.
									if (currentUsername.equals("") && currentPassword.equals("") && expectedResult.equals("Test Failed")) {
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Blank"  );
										System.out.println("Password: Blank"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: No username and  password is provided so the user should not be able to login to the application.");
									
							//Appium code
										 //Android Driver Start Method
										   driver = androidDriverStart();
										  
										 //Web Driver Wait
										    wait = webDriverWait(driver);
									 
										 //Livebaze Facebook Login Method-Not Successful
										    liveBazeFacebookLoginNotSuccessful(driver, wait, currentUsername, currentPassword);
										   
										 //Information obtained about login fields text.
											List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
			                             
									     //Username hint text   
										    String usernameHintText = loginFieldsText.get(0).getText();
											  
										 //Test Case Checking.
											assertEquals("Email address or phone number", usernameHintText); 
											
										 //Test completion notification.
											System.out.println("Info: Test Completed");
												  
										 //Updating excel cell with result
											System.out.println("Info: Updating excel sheet with result.");
											row.createCell(4).setCellValue("Test Failed");
												 
										 //File Writing Method
											writeResultToExcelSheet(outputStream, workbook); 
										  
										 
										  
									// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
									} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Blank"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
											
							//Appium code
										 //Android Driver Start Method
										   driver = androidDriverStart();
										  
										 //Web Driver Wait
										    wait = webDriverWait(driver);
									 
										 //Livebaze Facebook Login Method-Not Successful
										    liveBazeFacebookLoginNotSuccessful(driver, wait, currentUsername, currentPassword);
										   
										 //Information obtained about login fields text.
											List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
			                             
									     //Username hint text   
										    String usernameHintText = loginFieldsText.get(0).getText();
											  
										 //Test Case Checking.
											assertEquals("Email address or phone number", usernameHintText); 
											
										 //Test completion notification.
											System.out.println("Info: Test Completed");
												  
										 //Updating excel cell with result
											System.out.println("Info: Updating excel sheet with result.");
											row.createCell(4).setCellValue("Test Failed");
												 
										 //File Writing Method
											writeResultToExcelSheet(outputStream, workbook); 
										  
										
									// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
									} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Blank"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
										
							//Appium code
										 //Android Driver Start Method
										   driver = androidDriverStart();
										  
										 //Web Driver Wait
										    wait = webDriverWait(driver);
									 
										 //Livebaze Facebook Login Method-Not Successful
										    liveBazeFacebookLoginNotSuccessful(driver, wait, currentUsername, currentPassword);
										   
										 //Information obtained about login fields text.
											List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
			                             
									     //Password hint text   
											String passwordHintText = loginFieldsText.get(1).getText();
											  
										 //Test Case Checking.
											assertEquals("•••••••••••••••••", passwordHintText); 
											
										 //Test completion notification.
											System.out.println("Info: Test Completed");
												  
										 //Updating excel cell with result
											System.out.println("Info: Updating excel sheet with result.");
											row.createCell(4).setCellValue("Test Failed");
												 
										 //File Writing Method
											writeResultToExcelSheet(outputStream, workbook); 
										  
										
									// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
									} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Failed");
										System.out.println("Reason: The username and/or password is not correct so the user should not be able to login to the application.");
										
							//Appium code
										 //Android Driver Start Method
										   driver = androidDriverStart();
										  
										 //Web Driver Wait
										    wait = webDriverWait(driver);
									 
										 //Livebaze Facebook Login Method-Not Successful
										    liveBazeFacebookLoginNotSuccessful(driver, wait, currentUsername, currentPassword);
										   
										 //Information obtained about login fields text.
											List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
			                             
									     //Username from input field   
											String passwordHintText = loginFieldsText.get(1).getText();
											  
										 //Test Case Checking.
											assertEquals("•••••••••••••••••", passwordHintText); 
											
										 //Test completion notification.
											System.out.println("Info: Test Completed");
												  
										 //Updating excel cell with result
											System.out.println("Info: Updating excel sheet with result.");
											row.createCell(4).setCellValue("Test Failed");
												 
										 //File Writing Method
											writeResultToExcelSheet(outputStream, workbook); 
										  
										
									
									// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
									} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
										
										System.out.println("Test case Evaluation:");
										System.out.println("Username: Provided"  );
										System.out.println("Password: Provided"  );
										System.out.println("Test case expected result: Test Passed");
										System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
										
							//Appium code
										 //Android Driver Start Method
										  driver = androidDriverStart();
										  
										 //Web Driver Wait
										  wait = webDriverWait(driver);
										  
										 //Livebaze Facebook Login Method
										  liveBazeFacebookLoginSuccessful(driver, wait, currentUsername, currentPassword);
										    
										 //Getting Username 
										  String externalNickname = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/me_tv_login_name_new"))).getText();  
										  
										 //Test Case Checking.
										  assertEquals(currentNickName, externalNickname);
											  
									     //Test completion notification.
										  System.out.println("Info: Test Completed");
											  
									     //Updating excel cell with result
										  System.out.println("Info: Updating excel sheet with result.");
										  row.createCell(4).setCellValue("Test Failed");
											 
									     //File Writing Method
										  writeResultToExcelSheet(outputStream, workbook); 

										
									}
									
									else {
										
										System.out.println("There was an exception. Please check your " + "\"Test Case Nature\"" + " in excel sheet.");
										System.out.println("Info: Writing result to excel sheet.");
										row.createCell(4).setCellValue("Check Test case");
									} // End of second if condition.
						
							
							
						} else {

									System.out.println("Facebook: Header Row Skipped ...");
							
						} //End of first if condition.
					  
					
				      } //End of for each loop.
				  
				  
				} //Livebaze facebook login test completion
				
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------				
			  
			  
			  
			  
			  	  
			  
	//-----------------------------------------------------------------------------------------------> Methods		  
	
	//Android Driver Initialization Method
	public AndroidDriver<AndroidElement> androidDriverStart() throws MalformedURLException{
		
		//Appium driver instantiated.
		  return new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities); 
	}
	
	//Web Driver Wait Method
	public WebDriverWait webDriverWait(AndroidDriver<AndroidElement> driver){
		
		return new WebDriverWait(driver, 120);
	}
	
	//Livebaze Login Method
	public void liveBazeLogin(AndroidDriver<AndroidElement> driver, WebDriverWait wait, String currentUsername, String currentPassword) throws MalformedURLException{
		
  		//Profile icon
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/footer_profile_iv"))).click();
  		//Sign in button
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/me_tv_signin"))).click();
  		//Username
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_account"))).sendKeys(currentUsername);
  		//Password
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/login_password"))).sendKeys(currentPassword);
  		//Keyboard Back
  		  driver.pressKeyCode(AndroidKeyCode.BACK);
  	    //Sing in to account
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/sign_in_btn"))).click();  
		 
	}
	 
	//Livebaze Facebook Login Method-Successful
	public void liveBazeFacebookLoginSuccessful(AndroidDriver<AndroidElement> driver, WebDriverWait wait, String currentUsername, String currentPassword) throws MalformedURLException, InterruptedException{
		
  		//Profile icon
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/footer_profile_iv"))).click();
  		//Sign in button
  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/me_tv_signin"))).click();
  		//Facebook Button
  		  List<WebElement> socialLoginButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.ImageView")));
  		  socialLoginButtons.get(2).click();
  		//Loop through all input fields 
		  List<WebElement> loginFields = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));  
  		//Username
  		  loginFields.get(0).sendKeys(currentUsername);
  		//Tapping Back Button 
  		  driver.pressKeyCode(AndroidKeyCode.BACK);
  		//Password
  		  loginFields.get(1).sendKeys(currentPassword);
  	    //Tapping Back Button 
		  driver.pressKeyCode(AndroidKeyCode.BACK);
		//Wait after the values have been entered
		  Thread.sleep(1000L);
		//Clicking the Login button 
		  driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Log In \")").click();
		//Wait for facebook button to appear as either "Continue" or You have already authorized streamkar 
		  Thread.sleep(5000L);
		//Find window either with "Continue" button or with "You have already authorized streamkar" text 
		  try { 
			  List<AndroidElement>buttonElement = driver.findElements(By.className("android.widget.Button"));
			  System.out.println("Info: Element found and will be clicked.");
			  buttonElement.get(1).click(); } catch (Exception e) {
			  System.out.println("Element Not Found ... "); 
			  }  
		//To ensure the user is logged in 
		  Thread.sleep(5000L); 
	}
	
	//Livebaze Facebook Login Method-Not Successful
	public void liveBazeFacebookLoginNotSuccessful(AndroidDriver<AndroidElement> driver, WebDriverWait wait, String currentUsername, String currentPassword) throws MalformedURLException, InterruptedException{
			
	  		//Profile icon
	  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/footer_profile_iv"))).click();
	  		//Sign in button
	  		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.melot.thankyo:id/me_tv_signin"))).click();
	  		//Facebook Button
	  		  List<WebElement> socialLoginButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.ImageView")));
	  		  socialLoginButtons.get(2).click();
	  		//Loop through all input fields 
			  List<WebElement> loginFields = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));  
	  		//Username
	  		  loginFields.get(0).sendKeys(currentUsername);
	  		//Tapping Back Button 
	  		  driver.pressKeyCode(AndroidKeyCode.BACK);
	  		//Password
	  		  loginFields.get(1).sendKeys(currentPassword);
	  	    //Tapping Back Button 
			  driver.pressKeyCode(AndroidKeyCode.BACK);
			//Wait before the login button is clicked
			  Thread.sleep(1000L);
			//Clicking the Login button 
			  driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Log In \")").click();
			//Wait for the login credentials to be processed
			  Thread.sleep(1000L);
			//Tapping the back button
			  driver.pressKeyCode(AndroidKeyCode.BACK);
			//To perform swipe correctly
			  Thread.sleep(2000L);
		    //Swipe to go upside.
			  driver.swipe(538, 1300, 538, 1700, 1000);
			
		}
	
	
	//File Writing Method
	public void writeResultToExcelSheet(FileOutputStream outputStream, Workbook workbook) throws IOException{
		 
		  //Creating output stream for excel file
		  outputStream = new FileOutputStream(new File(excelFilePath));
		  //Writing test case result to excel file
		  workbook.write(outputStream);
		  //Closing output stream
		  outputStream.close();
		  //Closing the application in the phone
		  driver.closeApp();
		  //Printing test separation line
		  System.out.println("***********************************************************************************************");
		 
	 }			

}
