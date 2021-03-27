package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;
    private FileService fileService;
    private String successNote;


    @Autowired
    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                           @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                           Model model, Authentication authentication){
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @PostMapping("/add-note-result")
    public String writeNote(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                            @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                            Model model, Authentication authentication){
        noteService.addNote(noteFormObject, authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "Note has been successfully added.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @GetMapping("/delete-note-result")
    public String deleteCurrentNote(@RequestParam(value = "id") Integer id, @RequestParam(value = "noteid") Integer noteid, Model model,
                                    @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                    @ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Authentication authentication){
        User user = userService.getUserById(id);
        Note note = noteService.getNoteById(noteid);
        noteService.deleteNote(user.getUserid(), note.getNoteid());
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        System.out.println("Executing deleteCurrentNote()");
        successNote = "Note has been successfully deleted.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @PostMapping("/update-note-result")
    public String updateNote(@RequestParam("noteId") Integer noteId, Model model, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,Authentication authentication){
        System.out.println("noteId: " + noteId);
        noteService.updateNoteById(noteFormObject.getNoteTitle(), noteFormObject.getNoteDescription(), noteId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "Your note has been successfully updated.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @PostMapping("/add-credential-result")
    public String addCredential(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                                @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                Model model, Authentication authentication){
        credentialService.addCredential(credentialFormObject,authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "Credential has been successfully added.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @GetMapping("/delete-credential-result")
    public String deleteCredential(@RequestParam(value = "id") Integer id, @RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication){

        User user = userService.getUserById(id);
        Credential credential = credentialService.getCredential(credentialId);
        credentialService.deleteCredential(user.getUserid(),credential.getCredentialid());

        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "Credential has been deleted successfully.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @PostMapping("/update-credential-result")
    public String updateCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication){
        credentialService.updateCredential(credentialFormObject.getCredentialUrl(),credentialFormObject.getCredentialUsername(), credentialFormObject.getCredentialPassword(),credentialId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "Credential has been successfully updated.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @PostMapping("/add-file-result")
    public String uploadFile(@ModelAttribute("fileFormObject") FileFormObject fileFormObject, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             Model model, Authentication authentication) throws IOException, SQLException {
        String uploadError = null;
        successNote = "File has been successfully uploaded.";
        if(fileService.fileAlreadyExists(fileFormObject.getFileUpload().getOriginalFilename())){
            uploadError = "File with this name already exists.";
            model.addAttribute("uploadError", uploadError);
        } else if(fileFormObject.getFileUpload().getSize() == 0){
            uploadError = "Please select file to upload.";
            model.addAttribute("uploadError", uploadError);
        } else{
            model.addAttribute("successNote", successNote);
            fileService.addFile(fileFormObject, authentication);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "result";
    }

    @GetMapping("/delete-file-result")
    public String deleteFile(@RequestParam(value = "id") Integer id, @RequestParam(value = "fileid") Integer fileId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             Model model, Authentication authentication){

        User user = userService.getUserById(id);
        File file = fileService.getFileById(fileId);
        fileService.deleteFile(user.getUserid(), file.getFileId());

        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        successNote = "File has been deleted successfully.";
        model.addAttribute("successNote",successNote);
        return "result";
    }

    @GetMapping("/download-file")
    public ResponseEntity downloadFile(@RequestParam(value = "fileId") Integer fileId){
        File file = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

}
