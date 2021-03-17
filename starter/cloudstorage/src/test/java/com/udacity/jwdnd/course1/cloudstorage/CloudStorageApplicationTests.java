package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.PageObjects.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.PageObjects.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.awt.*;
import java.awt.event.KeyEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignupPage signupPage;
	private HomePage homePage;

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

}
