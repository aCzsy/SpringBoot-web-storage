package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement loginUsername;

    @FindBy(id = "inputPassword")
    private WebElement loginPassword;

    @FindBy(id = "login-submit-button")
    private WebElement loginButton;

    @FindBy(id = "signup-link")
    private WebElement loginSignupLink;

    public LoginPage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void login(String username, String password){
        loginUsername.sendKeys(username);
        loginPassword.sendKeys(password);
        loginButton.click();
    }
}
