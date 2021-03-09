package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                           @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                           Model model, Authentication authentication){
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        return "home";
    }

    @PostMapping("/home")
    public String writeNote(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                            @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                            Model model, Authentication authentication){
        noteService.addNote(noteFormObject, authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        return "home";
    }

    @GetMapping("/deleteNote")
    public String deleteCurrentNote(@RequestParam(value = "id") Integer id, @RequestParam(value = "noteid") Integer noteid, Model model,
                                    @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                    @ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Authentication authentication){
        User user = userService.getUserById(id);
        Note note = noteService.getNoteById(noteid);
        noteService.deleteNote(user.getUserid(), note.getNoteid());
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        System.out.println("Executing deleteCurrentNote()");
        return "home";
    }

    @PostMapping("/updateNote")
    public String updateNote(@RequestParam("noteId") Integer noteId, Model model,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,Authentication authentication){
        System.out.println("noteId: " + noteId);
        noteService.updateNoteById(noteFormObject.getNoteTitle(), noteFormObject.getNoteDescription(), noteId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        return "home";
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                Model model, Authentication authentication){
        credentialService.addCredential(credentialFormObject,authentication);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        return "home";
    }

    @GetMapping("/delete-credential")
    public String deleteCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication){
        credentialService.deleteCredential(credentialId);
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        return "home";
    }

}
