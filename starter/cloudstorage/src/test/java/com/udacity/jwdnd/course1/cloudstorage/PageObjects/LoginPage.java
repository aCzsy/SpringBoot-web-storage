package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver webDriver;

    @FindBy(id = "inputUsername")
    private WebElement loginUsername;

    @FindBy(id = "inputPassword")
    private WebElement loginPassword;

    @FindBy(id = "login-submit-button")
    private WebElement loginButton;

    @FindBy(id = "signup-link")
    private WebElement loginSignupLink;

    public LoginPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForElement(WebElement webElement){
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOf(webElement));
    }

    public void login(String username, String password){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + username + "';",loginUsername);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + password + "';", loginPassword);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",loginButton);
    }
}
