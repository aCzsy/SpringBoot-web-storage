package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.PageObjects.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptDecryptService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignupPage signupPage;
	private HomePage homePage;

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private EncryptionService encryptionService;

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
	public void testCreateNote(){
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
		homePage.createNote(noteTitle,noteDescription);
		Assertions.assertEquals(noteTitle,homePage.getNoteTitle());
		Assertions.assertEquals(noteDescription,homePage.getNoteDescription());
	}

	@Test
	public void testEditNote(){
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
		homePage.editNote(noteTitle,noteDescription);
		Assertions.assertEquals("New title", homePage.getNoteTitle());
		Assertions.assertEquals("New description", homePage.getNoteDescription());
	}

	@Test
	public void testDeleteNote(){
		String noteTitle = "My note";
		String noteDescription = "This note is for testing";

		testSignupLogin();
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
		List<String> credentialsRawPasswords = listOfCredentials
				.stream()
				.map(Credential::getPassword)
				.collect(Collectors.toList());

		testSignupLogin();

		listOfCredentials
				.forEach(x -> homePage.addNewCredentials(x.getUrl(), x.getUsername(), x.getPassword()));

		List<Credential> returnedListOfCredentials = credentialService.getAllCredentialsWithoutId();

		Map<String,String> credentialElements = returnedListOfCredentials
				.stream()
				.collect(Collectors.toMap(Credential::getPassword, Credential::getKey));

		List<String> decrytptedPasswords = credentialElements
				.entrySet()
				.stream()
				.map(element -> encryptionService.decryptValue(element.getKey(),element.getValue()))
				.collect(Collectors.toList());

		Assertions.assertFalse(returnedListOfCredentials.isEmpty());
		Assertions.assertEquals(3, returnedListOfCredentials.size());
		Assertions.assertTrue(credentialsRawPasswords.containsAll(decrytptedPasswords) && decrytptedPasswords.containsAll(credentialsRawPasswords));
	}

	@Test
	public void testDeleteCredentials(){
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

		listOfCredentials
				.forEach(x -> homePage.addNewCredentials(x.getUrl(), x.getUsername(), x.getPassword()));

		listOfCredentials
				.forEach(x -> homePage.deleteCredentials());

		List<Credential> returnedListOfCredentials = credentialService.getAllCredentialsWithoutId();

		Assertions.assertTrue(returnedListOfCredentials.isEmpty());
	}

}
