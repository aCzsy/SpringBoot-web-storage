package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;
    private WebDriver webDriver;

    public HomePage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(this.webDriver, this);
    }

    private void waitForElement(WebElement webElement){
        new WebDriverWait(webDriver,10)
                .until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * NOTES SECTION
     */
    /**
     * NOTES WEB ELEMENTS
     */
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "open-note-modal")
    private WebElement openNoteModal;

    @FindBy(id = "note-title-display")
    private WebElement noteTitleDisplay;

    @FindBy(id = "note-description-display")
    private WebElement noteDescrDisplay;

    @FindBy(id = "note-id")
    private WebElement noteId;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "save-note-button")
    private WebElement saveNoteButton;

    @FindBy(id = "close-note-modal-button")
    private WebElement closeNoteModalButton;

    @FindBy(id = "editNote-id")
    private WebElement editNoteId;

    @FindBy(id = "editNote-title")
    private WebElement editNoteTitle;

    @FindBy(id = "editNote-description")
    private WebElement editNoteDescription;

    @FindBy(id = "note-edit-button")
    private WebElement clickToEditButton;

    @FindBy(id = "edit-note-button")
    private WebElement saveEditNoteButton;

    @FindBy(id = "note-delete-button")
    private WebElement noteDeleteButton;

    @FindBy(id = "close-note-modal-button-edit")
    private WebElement closeNoteModalButtonEdit;

    @FindBy(id = "userTable")
    private WebElement userTable;

    @FindBy(id = "noteEditModal")
    private WebElement noteEditModal;

    @FindBy(id = "notesList")
    private WebElement notesList;

    /**
     * CREDENTIALS WEB ELEMENTS
     */

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id = "open-credentials-modal")
    private WebElement openCredentialsModal;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-table-url")
    private WebElement credentialUrlDisplayed;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "credential-submit")
    private WebElement submitCredential;

    @FindBy(id = "credential-table-password")
    private WebElement credentialPasswordDisplayed;

    @FindBy(id = "credentialEdit-url")
    private WebElement credentialEditUrl;

    @FindBy(id = "credentialEdit-username")
    private WebElement credentialEditUsername;

    @FindBy(id = "credentialEdit-password")
    private WebElement credentialEditPassword;

    @FindBy(id = "open-credentials-edit-modal")
    private WebElement clickCredentialEdit;

    @FindBy(id = "save-edit-credential")
    private WebElement saveCredentialChanges;

    @FindBy(id = "delete-credential")
    private WebElement deleteCredential;

    @FindBy(id = "credential-edit-close")
    private WebElement clickCloseEditCredential;

    @FindBy(id = "credential-list")
    private List<WebElement> listOfCredentials;

    /**
     * NOTES SECTION
     * @return
     */

    public String getNoteTitle(){
        return noteTitleDisplay.getText();
    }

    public String getNoteDescription(){
        return noteDescription.getAttribute("value");
    }

    public void clickLogoutButton(){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",logoutButton);
    }

    public void openNotesTab(){
        waitForElement(notesTab);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();",notesTab);
    }

    public void openNoteModal(){
        waitForElement(openNoteModal);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();",openNoteModal);
    }

    public void clickSaveNoteButton(){
        waitForElement(saveNoteButton);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();",saveNoteButton);
    }

    public void clickToEditButton(){
        waitForElement(clickToEditButton);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",clickToEditButton);
    }

    public void clickSaveNoteEditButton(){
        waitForElement(saveEditNoteButton);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",saveEditNoteButton);
    }

    public void clickNoteDeleteButton(){
        waitForElement(noteDeleteButton);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",noteDeleteButton);
    }

    public void createNote(String note_title, String note_descr){
        openNotesTab();
        openNoteModal();
        waitForElement(noteTitle);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + note_title + "';",noteTitle);
        waitForElement(noteDescription);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + note_descr + "';",noteDescription);
        clickSaveNoteButton();
        openNotesTab();
        waitForElement(noteDescrDisplay);
    }

    public void editNote(String note_title, String note_descr){
        createNote(note_title,note_descr);
        clickToEditButton();
        waitForElement(noteEditModal);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='New title';",editNoteTitle);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='New description';",editNoteDescription);
        clickSaveNoteEditButton();
        openNotesTab();
        waitForElement(noteDescrDisplay);
    }

    public void deleteNote(String note_title, String note_descr){
        createNote(note_title,note_descr);
        clickNoteDeleteButton();
        openNotesTab();
    }

    /**
     * CREDENTIALS SECTION
     */
    public String getCredentialUrl(){
        return credentialUrlDisplayed.getText();
    }

    public String getDecryptedCredentialPassword(){
        return credentialEditPassword.getAttribute("value");
    }

    public void openCredentialsTab(){
        waitForElement(navCredentialsTab);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",navCredentialsTab);
    }

    public void openCredentialsModal(){
        waitForElement(openCredentialsModal);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",openCredentialsModal);
    }

    public void addNewCredentials(String url, String username, String password){
        openCredentialsTab();
        openCredentialsModal();
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + url + "';",credentialUrl);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + username + "';", credentialUsername);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + password + "';", credentialPassword);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitCredential);
        openCredentialsTab();
        waitForElement(credentialPasswordDisplayed);
    }

    public void iterateOverMap(Map<String,String> map){
        map.entrySet().forEach(System.out::println);
    }

    public void deleteCredentials(){
        openCredentialsTab();
        waitForElement(deleteCredential);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", deleteCredential);
    }

    public void viewCredentialPassword() throws InterruptedException {
        Thread.sleep(1000);
        openCredentialsTab();
        waitForElement(credentialUrlDisplayed);
        waitForElement(clickCredentialEdit);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",clickCredentialEdit);
        waitForElement(credentialEditPassword);
    }

    public void saveCredentialEdit(){
        waitForElement(saveCredentialChanges);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",saveCredentialChanges);
    }

    public void clickCloseEditCredential(){
        openCredentialsTab();
        waitForElement(clickCloseEditCredential);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",clickCloseEditCredential);
    }

    public void editCredential(String url, String username, String password){
        waitForElement(credentialEditUrl);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + url + "';", credentialEditUrl);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + username + "';", credentialEditUsername);
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='" + password + "';", credentialEditPassword);
        saveCredentialEdit();
    }
}
