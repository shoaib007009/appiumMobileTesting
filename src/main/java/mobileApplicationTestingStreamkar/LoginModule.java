package mobileApplicationTestingStreamkar;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class LoginModule {

	
	//Android driver & Web driver wait objects.
		AndroidDriver<AndroidElement> driver = null;
	//Web driver wait object.
		WebDriverWait wait = null;
	//Excel file path object.
		String excelFilePath = null;
	//File input stream object.
		FileInputStream inputStream = null;
	//Excel workbook object.
		Workbook workbook = null;
	//Desired capabilities object.
		DesiredCapabilities capabilities = null;
		

	//This method will run before the start of each test.
	@Before
	public void setUp() throws Exception {

		//File path to excel sheet which contains username and passwords of sites.
				excelFilePath = "E:\\files\\appiumtest.xls"; //------------------------> Excel File Path Needs To Be Updated.
		
		//Input Stream for excel file.
				inputStream = new FileInputStream(excelFilePath);
		
		//Creating Workbook.
				workbook = new HSSFWorkbook(inputStream);
				
		//File path to APK
				File androidApk = new File("E:\\files\\base.apk"); //------------------------> Android APK  File Path Needs To Be Updated.
				System.out.println("Info: File path of android apk:\n" + androidApk.getAbsolutePath());
				System.out.println("Info: File path of excel file:\n" + excelFilePath);

		//Setting Capabilities For Android Platform.
				capabilities = new DesiredCapabilities();
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");//------------------------> Change to "Android Emulator" if emulator available..
				capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "120000");
				capabilities.setCapability(MobileCapabilityType.APP, androidApk.getAbsolutePath());
				
		
	}


	  //Streamkar login test.
	  @Test public void streamkarLogintest() throws InterruptedException, IOException {
		  
		  //Reading Sheet From Workbook.
			Sheet streamkar = workbook.getSheet("streamkar");

		  for (Row row : streamkar) { //Start of for each loop.
			  
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
					System.out.println("Streamkar User Name:   " + currentUsername);
					System.out.println("Streamkar Password:    " + currentPassword);
					System.out.println("Streamkar Nick Name:   " + currentNickName);
					
					
							// Condition = Username: empty + Password: empty + Expected Result: Test Failed.
							if (currentUsername.equals("") && currentPassword.equals("") && expectedResult.equals("Test Failed")) {
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Blank"  );
								System.out.println("Password: Blank"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: No username and  password is provided so the user should not be able to login to the application.");
							
								
					//Appium code
								
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  	      //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //More Options Link
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/textView9"))).click();
								  
								  //Username Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).sendKeys(currentUsername);
								  
								  //Password Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  //driver.navigate().back();
								  
								  //Sign in Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSignIn"))).click(); 
				
								  //Password field data
								  String passwordFieldText = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).getText();
								  
								  //Test Case Checking.
								  assertEquals("", passwordFieldText);
								  								 
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp(); 
								  
								  
							// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Blank"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
								
								
					//Appium code
								
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  	      //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //More Options Link
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/textView9"))).click();
								  
								  //Username Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).sendKeys(currentUsername);
								  
								  //Password Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Sign in Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSignIn"))).click(); 
								  
								  //Password field data
								  String usernameFieldText = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).getText();
								  
								  //Test Case Checking.
								  assertEquals("", usernameFieldText);
								  
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								  
								  
								  
								
							// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Blank"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
								
								
					//Appium code
								
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  	      //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //More Options Link
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/textView9"))).click();
								  
								  //Username Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).sendKeys(currentUsername);
								  
								  //Password Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Sign in Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSignIn"))).click(); 
								  
								  //Password field data
								  String passwordFieldText = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).getText();
								  
								  //Test Case Checking.
								  assertEquals("", passwordFieldText);
								  
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								   
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								    
								
							// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: The username and/or password are not correct so the user should not be able to login to the application.");
								
					//Appium code
								
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  	      //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //More Options Link
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/textView9"))).click();
								  
								  //Username Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).sendKeys(currentUsername);
								  
								  //Password Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Sign in Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSignIn"))).click(); 
															
								  //Password field data
								  String usernameFieldText = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).getText();	
								  
								  //Test Case Checking.
								  assertEquals(currentUsername, usernameFieldText);
								  
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								    
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								
							
							// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Passed");
								System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
								
								
					//Appium code
								
							      //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  	     //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								 //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //More Options Link
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/textView9"))).click();
								  
								  //Username Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_account"))).sendKeys(currentUsername);
								  
								  //Password Field
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/login_password"))).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Sign in Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSignIn"))).click(); 
								  Thread.sleep(3000L);
								  
								  //Pop up For New Version 
								  try { WebElement popUpForNewVersion = driver.findElement(By.id("android:id/button2"));
								  System.out.println("Info: Update Pop-up window found and closed.");
								  popUpForNewVersion.click(); }
								  
								  catch (NoSuchElementException e) {
								  System.out.println("Application Update Pop-up window not found.");
								  
								  }
								  
								  //Clicking In The Middle Of The Screen
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/room_list_item1_image"))).click();
								  
								  //Clicking On Profile Icon
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/home_bProfile"))).click();
								  
								  //Getting Username 
								  String userName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/me_tv_login_name_new"))).getText();
								 
								  //Testing case checking.
								  assertEquals(currentNickName, userName);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								 
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Passed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();	
								  	  
								
							}
							
							else {
								
								System.out.println("There was an exception. Please check your " + "\"Test Case Nature\"" + " in excel sheet.");
								System.out.println("Info: Writing result to excel sheet.");
								row.createCell(4).setCellValue("Check Test case");
							} // End of second if condition.
				
					
					
				} else {

							System.out.println("Streamkar: Header Row Skipped ...");
					
				} //End of first if condition.
			  
			
		      } //End of for each loop.
		  
		  
		  		inputStream.close();
		  		FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
				workbook.write(outputStream);
				System.out.println("Info: Results updated to excel sheet!!!");
				workbook.close();
				outputStream.close();	  
		  
	  }//Streamkar login test completion		  
		  
	  	
	
	  //Facebook login test.
	@Test 
	  public void streamkarFacebookLogintest() throws InterruptedException, IOException {
		
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
							
							  //Appium driver instantiated.
					  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
					  		  
					  		  //Timeout for explicit wait.
					  		  wait = new WebDriverWait(driver, 120);

							  //Skip button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
							  
							  //Facebook button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/buttton3"))).click();
							  
							  
							  //Wait for presence of facebook input fields
							  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Clicking the Login button 
							  driver.findElement(By.className("android.widget.Button")).click();
							  
							  //Ensure user is logged in
							  Thread.sleep(4000L);
							  
							  //Swipe to go upside.
							  driver.swipe(538, 1300, 538, 1700, 1000);
							  
							  //To ensure the page content is available.
							  Thread.sleep(5000L);
							 
							  //Information obtained about login fields text.
							  List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  String usernameHintText = loginFieldsText.get(0).getText();
							  
							  //Test Case Checking.
							  assertEquals("Email address or phone number", usernameHintText);
							  
							  //Test completion notification.
							  System.out.println("Info: Test Completed");
							  
							  //Updating excel cell with result
							  System.out.println("Info: Updating excel sheet with result.");
							  row.createCell(4).setCellValue("Test Failed");
							  
							  System.out.println("***********************************************************************************************");
							  driver.closeApp(); 
							  
						// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
						} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
							
							System.out.println("Test case Evaluation:");
							System.out.println("Username: Blank"  );
							System.out.println("Password: Provided"  );
							System.out.println("Test case expected result: Test Failed");
							System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
							
							
							//Appium code
							
							  //Appium driver instantiated.
					  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
					  		  
					  		  //Timeout for explicit wait.
					  		  wait = new WebDriverWait(driver, 120);
					  		  
							  //Skip button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
							  
							  //Facebook button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/buttton3"))).click();
							  
							  //Wait for presence of facebook input fields
							  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Loop through all input fields 
							  List<AndroidElement> loginFields = driver.findElements(By.className("android.widget.EditText"));
							  
							  //Send facebook password to input field
							  loginFields.get(1).sendKeys(currentPassword);
							  
							  //Tapping Back Button 
							  driver.navigate().back(); 
							  
							  //Clicking the Login button 
							  driver.findElement(By.className("android.widget.Button")).click();
							  
							  //Wait for keyboard to appear
							  Thread.sleep(1000L);
							  
							  //To ensure the page is properly swiped.
							  Thread.sleep(2000L);
							  
							  //Swipe to go upside.
							  driver.swipe(538, 1300, 538, 1700, 1000);
							  
							  //To ensure the page content is available.
							  Thread.sleep(4000L);
							 
							  //Information obtained about login fields text.
							  List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  String usernameHintText = loginFieldsText.get(0).getText();
							  
							  //Test Case Checking.
							  assertEquals("Email address or phone number", usernameHintText);
							  
							  //Test completion notification.
							  System.out.println("Info: Test Completed");
							  
							  //Updating excel cell with result
							  System.out.println("Info: Updating excel sheet with result.");
							  row.createCell(4).setCellValue("Test Failed");
							  
							  System.out.println("***********************************************************************************************");
							  driver.closeApp();
							  
							  
							  
							
						// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
						} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
							
							System.out.println("Test case Evaluation:");
							System.out.println("Username: Provided"  );
							System.out.println("Password: Blank"  );
							System.out.println("Test case expected result: Test Failed");
							System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
							
							//Appium code
							
							  //Appium driver instantiated.
					  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
					  		  
					  		  //Timeout for explicit wait.
					  		  wait = new WebDriverWait(driver, 120);
					  		  
							  //Skip button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
							  
							  //Facebook button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/buttton3"))).click();
							  
							  //Wait for presence of facebook input fields
							  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Send facebook id to input field driver.
							  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Email address or phone number\")").sendKeys(currentUsername);
							  
							  //Tapping Back Button 
							  driver.navigate().back();
							  
							  //Clicking the Login button 
							  driver.findElement(By.className("android.widget.Button")).click();
							  
							  //To ensure the page content is available.
							  Thread.sleep(4000L);
							 
							  //Information obtained about login fields text.
							  List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  String passwordHintText = loginFieldsText.get(1).getText();
							  
							  //Test Case Checking.
							  assertEquals("•••••••••••••••••", passwordHintText);
							  
							  //Test completion notification.
							  System.out.println("Info: Test Completed");
							  
							  //Updating excel cell with result
							  System.out.println("Info: Updating excel sheet with result.");
							  row.createCell(4).setCellValue("Test Failed");
							   
							  System.out.println("***********************************************************************************************");
							  driver.closeApp();
							    
							
						// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
						} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
							
							System.out.println("Test case Evaluation:");
							System.out.println("Username: Provided"  );
							System.out.println("Password: Provided"  );
							System.out.println("Test case expected result: Test Failed");
							System.out.println("Reason: The username and/or password is not correct so the user should not be able to login to the application.");
							
			 	 //Appium code
							  //Appium driver instantiated.
					  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
					  		  
					  		  //Timeout for explicit wait.
					  		  wait = new WebDriverWait(driver, 120);
					  		  
							  //Skip button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
							  
							  //Facebook button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/buttton3"))).click();
							  
							  //Wait for presence of facebook input fields
							  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Send facebook id to input field driver.
							  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Email address or phone number\")").sendKeys(currentUsername);
							  
							  //Tapping Back Button 
							  driver.navigate().back();
							  
							  //Loop through all input fields 
							  List<AndroidElement> loginFields = driver.findElements(By.className("android.widget.EditText"));
							  
							  //Send facebook password to input field
							  loginFields.get(1).sendKeys(currentPassword);
							  
							  //Tapping Back Button 
							  driver.navigate().back(); 
							  
							  //Clicking the Login button 
							  driver.findElement(By.className("android.widget.Button")).click();
							  
							  //To ensure the page content is available.
							  Thread.sleep(2000L);
							 
							  //Information obtained about login fields text.
							  List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  String passwordHintText = loginFieldsText.get(1).getText();
							  
							  //Test Case Checking.
							  assertEquals("•••••••••••••••••", passwordHintText);
							  
							  //Test completion notification.
							  System.out.println("Info: Test Completed");
							  
							  //Updating excel cell with result
							  System.out.println("Info: Updating excel sheet with result.");
							  row.createCell(4).setCellValue("Test Failed");
							    
							  System.out.println("***********************************************************************************************");
							  driver.closeApp();
							
						
						// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
						} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
							
							System.out.println("Test case Evaluation:");
							System.out.println("Username: Provided"  );
							System.out.println("Password: Provided"  );
							System.out.println("Test case expected result: Test Passed");
							System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
							
							
							//Appium code
							
							  //Appium driver instantiated.
					  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
					  		  
					  		  //Timeout for explicit wait.
					  		  wait = new WebDriverWait(driver, 120);
					  		  
							  //Skip button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
							  
							  //Facebook button
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/buttton3"))).click();
							  
							  //Wait for presence of facebook input fields
							  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Send facebook id to input field driver.
							  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Email address or phone number\")").sendKeys(currentUsername);
							  
							  //Tapping Back Button 
							  driver.navigate().back();
							  
							  //Loop through all input fields 
							  List<WebElement> loginFieldsText = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.widget.EditText")));
							  
							  //Send facebook password to input field
							  loginFieldsText.get(1).sendKeys(currentPassword);
							  
							  //Tapping Back Button 
							  driver.navigate().back(); 
							  
							  //Clicking the Login button 
							  driver.findElement(By.className("android.widget.Button")).click();
							  
							  //Wait for facebook button to appear as either "Continue" or You have already authorized streamkar 
							  Thread.sleep(6000L);
							  
							  //Find window either with "Continue" button or with "You have already authorized streamkar" text 
							  try { 
								  List<AndroidElement>buttonElement = driver.findElements(By.className("android.widget.Button"));
								  System.out.println("Info: Element found and will be clicked.");
								  buttonElement.get(1).click(); } catch (Exception e) {
								  System.out.println("Element Not Found ... "); 
								  }
							  
							  //To ensure the user is logged in 
							  Thread.sleep(5000L);
							  
							  //Clicking In The Middle Of The Screen
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/room_list_item1_image"))).click();
							  
							  //Clicking On Profile Icon
							  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/home_bProfile"))).click();
								  
							  //Getting Username 
							  String userName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/me_tv_login_name_new"))).getText();
								  
							  //Testing case checking.
							  assertEquals(currentNickName, userName);
						
							  //Test completion notification.
							  System.out.println("Info: Test Completed");
							  
							  //Updating excel cell with result
							  System.out.println("Info: Updating excel sheet with result.");
							  row.createCell(4).setCellValue("Test Passed");
							  
							  System.out.println("***********************************************************************************************");
							  driver.closeApp();	
							  	  
							
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
	  
	  
	  		inputStream.close();
	  		FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
			workbook.write(outputStream);
			System.out.println("Info: Results updated to excel sheet!!!");
			workbook.close();
			outputStream.close();
	  
	}
	  
	 

	
	 
	  //Instagram login test. 
	  @Test public void streamkarInstagramLogintest() throws InterruptedException, IOException {
	  
		  //Reading Sheet From Workbook.
			Sheet instagram = workbook.getSheet("instagram");

		  for (Row row : instagram) { //Start of for each loop.
			  
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
					System.out.println("Instagram User Name:   " + currentUsername);
					System.out.println("Instagram Password:    " + currentPassword);
					System.out.println("Instagram Nick Name:   " + currentNickName);
					
					
							// Condition = Username: empty + Password: empty + Expected Result: Test Failed.
							if (currentUsername.equals("") && currentPassword.equals("") && expectedResult.equals("Test Failed")) {
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Blank"  );
								System.out.println("Password: Blank"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: No username and  password is provided so the user should not be able to login to the application.");
							
								
					//Appium code
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Instagram Button 
								  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Log in with Instagram\")").click(); 
								  Thread.sleep(3000L);
								  
								  //List of elements 
								  List<AndroidElement> userInformation = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Instagram Username 
								  userInformation.get(0).sendKeys(currentUsername);
								  
								  //Instagram Username 
								  userInformation.get(1).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Login Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.Button"))).click();
								
								  //To ensure the user is logged in 
								  Thread.sleep(3000L);
								
								  //List of elements 
								  List<AndroidElement> inputFields = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Username field data
								  String passwordInformation = inputFields.get(01).getText();
								  
								  System.out.println(passwordInformation);
								  
								  //Testing case checking.
								  assertEquals("Password:", passwordInformation);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								
								  
								  
							// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Blank"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
								
								
					//Appium code
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Instagram Button 
								  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Log in with Instagram\")").click(); 
								  Thread.sleep(3000L);
								  
								  //List of elements 
								  List<AndroidElement> userInformation = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Instagram Username 
								  userInformation.get(0).sendKeys(currentUsername);
								  
								  //Instagram Username 
								  userInformation.get(1).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Login Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.Button"))).click();
								
								  //To ensure the user is logged in 
								  Thread.sleep(3000L);
								
								  //List of elements 
								  List<AndroidElement> inputFields = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Username field data
								  String passwordInformation = inputFields.get(01).getText();
								  
								  System.out.println(passwordInformation);
								  
								  //Testing case checking.
								  assertEquals("Password:", passwordInformation);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								  
								  
								
							// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Blank"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
								
					//Appium code
								//Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Instagram Button 
								  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Log in with Instagram\")").click(); 
								  Thread.sleep(3000L);
								  
								  //List of elements 
								  List<AndroidElement> userInformation = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Instagram Username 
								  userInformation.get(0).sendKeys(currentUsername);
								  
								  //Instagram Username 
								  userInformation.get(1).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Login Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.Button"))).click();
								
								  //To ensure the user is logged in 
								  Thread.sleep(3000L);
								
								  //List of elements 
								  List<AndroidElement> inputFields = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Username field data
								  String passwordInformation = inputFields.get(01).getText();
								  
								  System.out.println(passwordInformation);
								  
								  //Testing case checking.
								  assertEquals("Password:", passwordInformation);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
								
							// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: The username and/or password is not correct so the user should not be able to login to the application.");
								
					//Appium code
								//Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Instagram Button 
								  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Log in with Instagram\")").click(); 
								  Thread.sleep(3000L);
								  
								  //List of elements 
								  List<AndroidElement> userInformation = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Instagram Username 
								  userInformation.get(0).sendKeys(currentUsername);
								  
								  //Instagram Username 
								  userInformation.get(1).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Login Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.Button"))).click();
								
								  //To ensure the user is logged in 
								  Thread.sleep(3000L);
								
								  //List of elements 
								  List<AndroidElement> inputFields = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Username field data
								  String usernameInformation = inputFields.get(0).getText();
								  
								  //Testing case checking.
								  assertEquals(currentUsername, usernameInformation);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Failed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();
							 
							
							// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Passed");
								System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
								
								
					//Appium code
								
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								
								  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Instagram Button 
								  driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Log in with Instagram\")").click(); Thread.sleep(3000L);
								  
								  //List of elements 
								  List<AndroidElement> userInformation = driver.findElements(By.className("android.widget.EditText"));
								  
								  //Instagram Username 
								  userInformation.get(0).sendKeys(currentUsername);
								  
								  //Instagram Username 
								  userInformation.get(1).sendKeys(currentPassword);
								  
								  //Tapping Back Button 
								  driver.navigate().back();
								  
								  //Login Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.Button"))).click();
								
								  //To ensure the user is logged in 
								  Thread.sleep(3000L);
								  
								  //Authorize Button 
								  
								  try {
								  
								  List<AndroidElement> authorizeButton =
								  driver.findElements(By.className("android.widget.Button"));
								  authorizeButton.get(1).click();
								  System.out.println("Info: Authorized button clicked successfully.");
								  
								  } catch (Exception e) {
								  System.out.println("Info: Authorize button not found."); 
								  
								  }
								  
								  Thread.sleep(6000L);
								  
								  //Pop up For New Version 
								  try { 
									  
									  WebElement popUpForNewVersion = driver.findElement(By.id("android:id/button2"));
								  
									  System.out.println("Info: Update Pop-up window found and closed.");
								 
									  popUpForNewVersion.click(); 
									  } catch (NoSuchElementException e) {
								   
									  System.out.println("Application Update Pop-up window not found."); 
									  
									  }
								  
								  
								  //Clicking In The Middle Of The Screen
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/room_list_item1_image"))).click();
								  
								  //Clicking On Profile Icon
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/home_bProfile"))).click();
								  
								  //Getting Username 
								  String userName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/me_tv_login_name_new"))).getText();
								  
								  //Testing case checking.
								  assertEquals(currentNickName, userName);
							
								  //Test completion notification.
								  System.out.println("Info: Test Completed");
								  
								  //Updating excel cell with result
								  System.out.println("Info: Updating excel sheet with result.");
								  row.createCell(4).setCellValue("Test Passed");
								  
								  System.out.println("***********************************************************************************************");
								  driver.closeApp();	  	  
								
							}
							
							else {
								
								System.out.println("There was an exception. Please check your " + "\"Test Case Nature\"" + " in excel sheet.");
								System.out.println("Info: Writing result to excel sheet.");
								row.createCell(4).setCellValue("Check Test case");
							} // End of second if condition.
				
					
					
				} else {

							System.out.println("Instagram: Header Row Skipped ...");
					
				} //End of first if condition.
			  
			
		      } //End of for each loop.
		  
		  
		  		inputStream.close();
		  		FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
				workbook.write(outputStream);
				System.out.println("Info: Results updated to excel sheet!!!");
				workbook.close();
				outputStream.close();
	
	  }//End of instagram login test.
	 

	
	 /*
	  @Test public void streamkarTwitterLogintest() throws InterruptedException
	  {
	  
	  //Skip Button
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.thankyo.hwgame:id/bSkip"))).click();
	  
	  //Twitter Button
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.className(
	  "android.widget.Button"))).click();
	  
	  //Twitter Username
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.twitter.android:id/login_identifier"))).sendKeys(twitterId);
	  
	  //Twitter Password
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.twitter.android:id/login_password"))).sendKeys(twitterPassword);
	  
	  //Login wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.twitter.android:id/login_login"))).click();
	  
	  //To ensure the user is logged in Thread.sleep(5000L);
	  
	  //List of elements
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.twitter.android:id/ok_button"))).click();
	  
	  
	  //Authorize Button try {
	  
	  driver.findElement(By.id("com.twitter.android:id/ok_button")).click();
	  System.out.println("Info: Authorized button clicked successfully.");
	  
	  } catch (Exception e) {
	  System.out.println("Info: Authorize button not found."); }
	  
	  
	  Thread.sleep(6000L);
	  
	  //Pop-up For New Version try { WebElement popUpForNewVersion =
	  driver.findElement(By.id("android:id/button2"));
	  System.out.println("Info: Update Pop-up window found and closed.");
	  popUpForNewVersion.click(); } catch (NoSuchElementException e) {
	  System.out.println("Application Update Pop-up window not found."); }
	  
	  
	  //Clicking In The Middle Of The Screen
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.thankyo.hwgame:id/room_list_item1_image"))).click();
	  
	  //Clicking On Profile Icon
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.thankyo.hwgame:id/home_bProfile"))).click();
	  
	  //Getting Username String userName =
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.id(
	  "com.thankyo.hwgame:id/me_tv_login_name_new"))).getText();
	  
	  //Printing Username On Console System.out.println("Signed-In Username: "
	  + userName);
	  
	  //Testing whether the username from the mobile application matches the
	  name given by the tester.
	  System.out.println("Info: Going to perform test operation.");
	  assertEquals("Tester", userName);
	  
	  //Test Completed
	  System.out.println("Status: Streamkar Instagram Login Test Completed");
	  
	  
	  }
	  */
	
	  
	  @Test public void streamkarGoogleLogintest() throws InterruptedException, IOException {
	  
		  
		  
		  
		  //Reading Sheet From Workbook.
			Sheet google = workbook.getSheet("google");

		  for (Row row : google) { //Start of for each loop.
			  
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
								
								 
							// Condition = Username: empty + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.equals("") && currentPassword.length() > 0 && expectedResult.equals("Test Failed")) {
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Blank"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only password is provided so the user should not be able to login to the application.");
								
								
								//Appium code
								
								
								  
								  
								
							// Condition = Username: provided + Password: empty + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.equals("") && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Blank"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: Only username is  provided so the user should not be able to login to the application.");
								
								//Appium code
								
								 
								    
								
							// Condition = Username: provided + Password: provided + Expected Result: Test Failed.	
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Failed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Failed");
								System.out.println("Reason: The username and/or password is not correct so the user should not be able to login to the application.");
								
								//Appium code
								
								
							
							// Condition = Username: provided + Password: provided + Expected Result: Test Passed + Logout: True.		
							} else if (currentUsername.length() > 0 && currentPassword.length() > 0 && expectedResult.equals("Test Passed")){
								
								System.out.println("Test case Evaluation:");
								System.out.println("Username: Provided"  );
								System.out.println("Password: Provided"  );
								System.out.println("Test case expected result: Test Passed");
								System.out.println("Reason: Username and  password provided is correct so the user should be able to login to the application.");
								
								
					 //Appium code
								  //Appium driver instantiated.
						  		  driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
						  		  
						  		  //Timeout for explicit wait.
						  		  wait = new WebDriverWait(driver, 120);
								  
						  		  //Skip Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/bSkip"))).click();
								  
								  //Google Button
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.thankyo.hwgame:id/button2"))).click();
								  
								  //	Username
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.EditText"))).sendKeys(currentUsername);
								  Thread.sleep(2000L);
								  
								  //Next button
								  driver.pressKeyCode(AndroidKeyCode.ENTER);
								  Thread.sleep(2000L);
								   
								  //Password
								  wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.EditText"))).sendKeys(currentPassword);
								  Thread.sleep(2000L);
								
								  //Next button
								  driver.pressKeyCode(AndroidKeyCode.ENTER);
								  Thread.sleep(7000L);
								  
								  //Accept Button
								  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("android.view.View")));
								  driver.findElementByAndroidUIAutomator("new UiSelector().description(\"ACCEPT\")").click(); 
								  
								  
								  //Google Services
								  Thread.sleep(7000L);
								  List<AndroidElement> allElements = driver.findElements(By.className("android.widget.Button"));
								  allElements.get(0).click();
								  
								  
								  	  
								
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
		  
		  
		  		inputStream.close();
		  		FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
				workbook.write(outputStream);
				System.out.println("Info: Results updated to excel sheet!!!");
				workbook.close();
				outputStream.close();
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  

	  
	 
	 

	  
	
	  
	  //Test Completed
	  System.out.println("Status: Streamkar Google Login Test Completed");
	  
	  
	  }
	  
	  
	 

	// This method will run after the test is completed.
	  
	  
	@After
	public void tearDown() throws Exception {

		System.out.println("Info: All tests completed: ");
		
	    

	}
	
	

}
