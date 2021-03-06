package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.PageObjects.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteFormObject;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptDecryptService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //REMOVES DATA BEFORE EACH TEST
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //H2 USED IN IN-MEMORY MODE
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignupPage signupPage;
	private HomePage homePage;
	private WebElement credentialsTab;
	private WebElement editBtn;
	private WebElement table;

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private EncryptionService encryptionService;
	@Autowired
	private EncryptDecryptService encryptDecryptService;

	private void waitForElement(WebElement webElement){
		new WebDriverWait(driver,40)
				.until(ExpectedConditions.visibilityOf(webElement));
	}

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testSignupLogin(){
		String username = "User";
		String password = "root";

		driver.get("http://localhost:" + port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup("John", "Doe",username,password);
		driver.get("http://localhost:" + port + "/login");

		loginPage = new LoginPage(driver);
		loginPage.login(username,password);

		homePage = new HomePage(driver);
		Assertions.assertEquals("Home",driver.getTitle());
	}

	@Test
	public void testUnauthorizedAccess(){
		driver.get("http://localhost:" + port + "/login");
		signupPage = new SignupPage(driver);
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + port + "/signup");
		loginPage = new LoginPage(driver);
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + port + "/home");
		homePage = new HomePage(driver);
		Assertions.assertNotEquals("Home",driver.getTitle());
	}

	@Test
	public void testSignupLoginLogout() throws InterruptedException {
		String username = "User";
		String password = "root";

		driver.get("http://localhost:" + port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup("John", "Doe",username,password);
		driver.get("http://localhost:" + port + "/login");

		loginPage = new LoginPage(driver);
		loginPage.login(username,password);

		homePage = new HomePage(driver);
		Assertions.assertEquals("Home",driver.getTitle());

		homePage.clickLogoutButton();
		Assertions.assertNotEquals("Home",driver.getTitle());
		driver.get("http://localhost:" + port + "/home");
		Assertions.assertEquals("Login",driver.getTitle());
	}

	@Test
	public void testCreateNote() throws InterruptedException {
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
		//Creating a note and verifying that the note details are displayed
		homePage.createNote(noteTitle,noteDescription);
		Assertions.assertEquals(noteTitle,homePage.getNoteTitle());
		Assertions.assertEquals(noteDescription,homePage.getNoteDescriptionDisplayed());
	}

	@Test
	public void testEditNote(){
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
		//Editing a note and verifying that new details are updated
		homePage.editNote(noteTitle,noteDescription);
		Assertions.assertEquals("New title", homePage.getNoteTitle());
		Assertions.assertEquals("New description", homePage.getNoteDescriptionDisplayed());
	}

	@Test
	public void testDeleteNote(){
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
		//Deleting note and verifying that nothing is displayed
		homePage.deleteNote(noteTitle,noteDescription);
		Assertions.assertThrows(NoSuchElementException.class,()-> {
			homePage.getNoteTitle();
		});

	}

	/**
	 * CREDENTIALS SECTION
	 */

	@Test
	public void testCredentialsAddAndVerifiedEncryption(){
		//Creating a set of new credentials
		Credential credential1 = new Credential();
		credential1.setUrl("http://facebook.com");
		credential1.setUsername("User");
		credential1.setPassword("root");
		Credential credential2 = new Credential();
		credential2.setUrl("http://google.com");
		credential2.setUsername("User2");
		credential2.setPassword("root2");
		Credential credential3 = new Credential();
		credential3.setUrl("http://dropbox.com");
		credential3.setUsername("User3");
		credential3.setPassword("root3");

		List<Credential> listOfCredentials = new ArrayList<>();
		listOfCredentials.add(credential1);
		listOfCredentials.add(credential2);
		listOfCredentials.add(credential3);
//		List<String> credentialsRawPasswords = listOfCredentials
//				.stream()
//				.map(Credential::getPassword)
//				.collect(Collectors.toList());

		testSignupLogin();

		//Adding each credential
		listOfCredentials
				.forEach(x -> homePage.addNewCredentials(x.getUrl(), x.getUsername(), x.getPassword()));

		//Getting all credentials from db
		List<Credential> returnedListOfCredentials = credentialService.getAllCredentialsWithoutId();

		//Number of rows in a table
		int numOfRows = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")).size();

		//Looping through each row and verifying that every password displayed is encrypted
		for(int i = 0; i < numOfRows;i++){
			table = driver.findElement(By.id("credentialTable"));
			waitForElement(table);
			WebElement credentialPassword = table.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i+1) + "]/td[3]"));
			Assertions.assertEquals(credentialPassword.getText(),returnedListOfCredentials.get(i).getPassword());
		}

//		Map<String,String> credentialElements = returnedListOfCredentials
//				.stream()
//				.collect(Collectors.toMap(Credential::getPassword, Credential::getKey));
//
//		List<String> decrytptedPasswords = credentialElements
//				.entrySet()
//				.stream()
//				.map(element -> encryptionService.decryptValue(element.getKey(),element.getValue()))
//				.collect(Collectors.toList());

//		Assertions.assertFalse(returnedListOfCredentials.isEmpty());
//		Assertions.assertEquals(3, returnedListOfCredentials.size());
//		Assertions.assertTrue(credentialsRawPasswords.containsAll(decrytptedPasswords) && decrytptedPasswords.containsAll(credentialsRawPasswords));
	}

	@Test
	public void testDeleteCredentials(){
		//Creating a set of new credentials
		Credential credential1 = new Credential();
		credential1.setUrl("http://facebook.com");
		credential1.setUsername("User");
		credential1.setPassword("root");
		Credential credential2 = new Credential();
		credential2.setUrl("http://google.com");
		credential2.setUsername("User2");
		credential2.setPassword("root2");
		Credential credential3 = new Credential();
		credential3.setUrl("http://dropbox.com");
		credential3.setUsername("User3");
		credential3.setPassword("root3");

		List<Credential> listOfCredentials = new ArrayList<>();
		listOfCredentials.add(credential1);
		listOfCredentials.add(credential2);
		listOfCredentials.add(credential3);

		testSignupLogin();

		//Adding each credential
		listOfCredentials
				.forEach(x -> homePage.addNewCredentials(x.getUrl(), x.getUsername(), x.getPassword()));

		//Deleting each credential
		listOfCredentials
				.forEach(x -> homePage.deleteCredentials());

		//Getting all credentials from db
		List<Credential> returnedListOfCredentials = credentialService.getAllCredentialsWithoutId();

		//Asserting that no credentials were returned, the list is empty and there is nothing displayed
		Assertions.assertTrue(returnedListOfCredentials.isEmpty());
		Assertions.assertThrows(NoSuchElementException.class,() -> {
			homePage.getCredentialUrl();
		});
	}

	@Test
	public void testEditPassword() {
		//Creating set of new credentials
		Credential credential1 = new Credential();
		credential1.setUrl("http://facebook.com");
		credential1.setUsername("User");
		credential1.setPassword("root");
		Credential credential2 = new Credential();
		credential2.setUrl("http://google.com");
		credential2.setUsername("User2");
		credential2.setPassword("root2");
		Credential credential3 = new Credential();
		credential3.setUrl("http://dropbox.com");
		credential3.setUsername("User3");
		credential3.setPassword("root3");

		List<Credential> listOfCredentials = new ArrayList<>();
		listOfCredentials.add(credential1);
		listOfCredentials.add(credential2);
		listOfCredentials.add(credential3);

		testSignupLogin();
		//Adding each credential
		listOfCredentials
				.forEach(x -> homePage.addNewCredentials(x.getUrl(), x.getUsername(), x.getPassword()));

		//Getting passwords of all credentials from db, decrypting them and storing them in a list
		List<String> decryptedCredentialPasswords = credentialService.getAllCredentialsWithoutId()
				.stream()
				.map(element -> encryptionService.decryptValue(element.getPassword(),element.getKey()))
				.collect(Collectors.toList());

		List<CredentialFormObject> editedCredentials = new ArrayList<>();

		//Creating set of credentials which will replace existing ones
		CredentialFormObject credentialFormObject1 = new CredentialFormObject();
		credentialFormObject1.setCredentialUrl("http://microsoft.com");
		credentialFormObject1.setCredentialUsername("New user");
		credentialFormObject1.setCredentialPassword("newroot");

		CredentialFormObject credentialFormObject2 = new CredentialFormObject();
		credentialFormObject2.setCredentialUrl("http://johndoe.com");
		credentialFormObject2.setCredentialUsername("New user2");
		credentialFormObject2.setCredentialPassword("newroot2");

		CredentialFormObject credentialFormObject3 = new CredentialFormObject();
		credentialFormObject3.setCredentialUrl("http://testing.com");
		credentialFormObject3.setCredentialUsername("New user3");
		credentialFormObject3.setCredentialPassword("newroot3");

		editedCredentials.add(credentialFormObject1);
		editedCredentials.add(credentialFormObject2);
		editedCredentials.add(credentialFormObject3);

		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		waitForElement(credentialsTab);
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",credentialsTab);

		//Number of rows in a table
		int numOfRows = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")).size();

		//Looping through each row, clicking edit button,
		// verifying that pw in editmodal is decrypted,
		// editing credential,saving it, verifying that each credential is displayed
		//and their passwords are encrypted
		for(int i = 0;i < numOfRows;i++){
			editBtn = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i+1) + "]/td[1]/button"));
			waitForElement(editBtn);
			editBtn.click();
			Assertions.assertEquals(homePage.getDecryptedCredentialPassword(),decryptedCredentialPasswords.get(i));
			homePage.editCredential(editedCredentials.get(i).getCredentialUrl(), editedCredentials.get(i).getCredentialUsername(), editedCredentials.get(i).getCredentialPassword());
			table = driver.findElement(By.id("credentialTable"));
			waitForElement(table);
			WebElement credentialUrl = table.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div[1]/table/tbody/tr[" + (i+1) +"]/th"));
			waitForElement(credentialUrl);
			Assertions.assertEquals(credentialUrl.getText(),editedCredentials.get(i).getCredentialUrl());
		}

	}

}
