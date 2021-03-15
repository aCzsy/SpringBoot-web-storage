package com.udacity.jwdnd.course1.cloudstorage.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    public HomePage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

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

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "close-note-modal-button-edit")
    private WebElement closeNoteModalButtonEdit;

    public void clickLogoutButton(){
        logoutButton.submit();
    }

    public void createNote(String note_title, String note_descr){
        noteTitle.sendKeys(note_title);
        noteDescription.sendKeys(note_descr);
        saveNoteButton.click();
    }
}
