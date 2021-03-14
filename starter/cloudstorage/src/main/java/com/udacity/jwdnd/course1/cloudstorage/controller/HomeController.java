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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;
    private FileService fileService;


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

    @PostMapping("/home")
    public String writeNote(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                            @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                            Model model, Authentication authentication){
        noteService.addNote(noteFormObject, authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @GetMapping("/deleteNote")
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
        return "home";
    }

    @PostMapping("/updateNote")
    public String updateNote(@RequestParam("id") Integer noteId, Model model, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,Authentication authentication){
        System.out.println("noteId: " + noteId);
        noteService.updateNoteById(noteFormObject.getNoteTitle(), noteFormObject.getNoteDescription(), noteId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                                @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                Model model, Authentication authentication){
        credentialService.addCredential(credentialFormObject,authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @GetMapping("/delete-credential")
    public String deleteCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication){
        credentialService.deleteCredential(credentialId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @PostMapping("/update-credential")
    public String updateCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication){
        credentialService.updateCredential(credentialFormObject.getCredentialUrl(),credentialFormObject.getCredentialUsername(), credentialFormObject.getCredentialPassword(),credentialId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @PostMapping("/add-file")
    public String uploadFile(@ModelAttribute("fileFormObject") FileFormObject fileFormObject, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             Model model, Authentication authentication) throws IOException, SQLException {
        String uploadError = null;
        String successUpload = "File has been successfully uploaded";
        if(fileService.fileAlreadyExists(fileFormObject.getFileUpload().getOriginalFilename())){
            uploadError = "File with this name already exists";
            model.addAttribute("uploadError", uploadError);
        } else{
            model.addAttribute("successUpload", successUpload);
            fileService.addFile(fileFormObject, authentication);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @GetMapping("/delete-file")
    public String deleteFile(@RequestParam(value = "id") Integer fileId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             Model model, Authentication authentication){
        fileService.deleteFile(fileId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @GetMapping("/download-file")
    public ResponseEntity downloadFile(@RequestParam(value = "fileId") Integer fileId, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject, HttpServletResponse httpServletResponse,
                                                 @ModelAttribute("fileFormObject") FileFormObject fileFormObject, @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject){
        File file = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }
}
