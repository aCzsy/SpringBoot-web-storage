package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteUploadService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private NoteUploadService noteUploadService;
    private UserService userService;
    private NoteMapper noteMapper;

    public HomeController(NoteUploadService noteUploadService, UserService userService) {
        this.noteUploadService = noteUploadService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Model model, Authentication authentication){
        model.addAttribute("notes",noteUploadService.getAllNotes(authentication));
        return "home";
    }

    @PostMapping("/home")
    public String writeNote(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Model model, Authentication authentication){
        noteUploadService.addNote(noteFormObject, authentication);
        model.addAttribute("notes",noteUploadService.getAllNotes(authentication));
        return "home";
    }

    @GetMapping("/delete")
    public String deleteCurrentNote(@RequestParam(value = "id") Integer id, @RequestParam(value = "noteid") Integer noteid, Model model, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Authentication authentication){
        User user = userService.getUserById(id);
        Note note = noteUploadService.getNoteById(noteid);
        noteUploadService.deleteNote(user.getUserid(), note.getNoteid());
        model.addAttribute("notes",noteUploadService.getAllNotes(authentication));
        System.out.println("Executing deleteCurrentNote()");
        return "home";
    }

    @PostMapping("/update")
    public String updateNote(@RequestParam("noteId") Integer noteId, Model model, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,Authentication authentication){
        System.out.println("noteId: " + noteId);
        noteUploadService.updateNoteById(noteFormObject.getNoteTitle(), noteFormObject.getNoteDescription(), noteId);
        model.addAttribute("notes",noteUploadService.getAllNotes(authentication));
        return "home";
    }


}
