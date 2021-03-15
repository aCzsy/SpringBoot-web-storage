package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {
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
        PageFactory.initElements(webDriver, this);
    }

    public void signup(String firstName, String lastName, String userName, String password){
        signupFirstName.sendKeys(firstName);
        signupLastName.sendKeys(lastName);
        signupUsername.sendKeys(userName);
        signupPassword.sendKeys(password);
        signupButton.click();
    }
}
