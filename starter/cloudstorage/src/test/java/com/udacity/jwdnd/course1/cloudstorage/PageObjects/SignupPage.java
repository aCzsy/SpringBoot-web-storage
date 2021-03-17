package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignupPage {
    private WebDriver webDriver;

    @FindBy(id = "inputFirstName")
    private WebElement signupFirstName;

    @FindBy(id = "inputLastName")
    private WebElement signupLastName;

    @FindBy(id = "inputUsername")
    private WebElement signupUsername;

    @FindBy(id = "inputPassword")
    private WebElement signupPassword;

    @FindBy(id = "submit-signup-button")
    private WebElement signupButton;

    public SignupPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForElement(WebElement webElement){
        new WebDriverWait(webDriver,10)
                .until(ExpectedConditions.visibilityOf(webElement));
    }

    public void signup(String firstName, String lastName, String userName, String password){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + firstName + "';", signupFirstName);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + lastName + "';", signupLastName);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + userName + "';", signupUsername);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + password + "';",signupPassword);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", signupButton);
    }
}
