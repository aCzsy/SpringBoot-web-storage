package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteUploadService {
    private NoteMapper noteMapper;
    private UserMapper userMapper;
    private User currentUser;
    private Authentication authentication;


    public NoteUploadService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public void addNote(NoteFormObject noteFormObject, Authentication authentication){
        currentUser = userMapper.getUser(authentication.getName());
        Note note = new Note();
        note.setUserid(currentUser.getUserid());
        note.setNotetitle(noteFormObject.getNoteTitle());
        note.setNotedescription(noteFormObject.getNoteDescription());
        int num_notes = noteMapper.insertNote(note);
        System.out.println(num_notes + " notes added");
        System.out.println("userid on note: " + note.getUserid());
        System.out.println("noteid on note: " + note.getNoteid());
    }

    public List<Note> getAllNotes(Authentication authentication){
        currentUser = userMapper.getUser(authentication.getName());
        return noteMapper.getAllNotes(currentUser.getUserid());
    }

    public Note getNote(String notetitle){
        return noteMapper.getNote(notetitle);
    }

    public Note getNoteById(Integer noteid){
        return noteMapper.getNoteById(noteid);
    }

    public void deleteNote(Integer userid, Integer noteid){
        Integer userById = userMapper.getUserById(userid).getUserid();
        Integer noteById = noteMapper.getNoteById(noteid).getNoteid();
        if(userById == noteMapper.getNoteById(noteid).getUserid()){
            System.out.println("userById : " + userById);
            noteMapper.deteleNote(noteById);
        } else{
            throw new RuntimeException("There was an error while processing this request...");
        }
    }

    public void updateNote(String newTitle, String description, String title){
        Note note = noteMapper.getNote(title);
        Integer noteId = note.getNoteid();
        int numRows = noteMapper.updateNote(newTitle,description,noteId);
        System.out.println(numRows + " rows updated");
        System.out.println("Successfully updated note with title: " + title + " and noteid : " + noteId);
        System.out.println("New title: " + newTitle + "\nNew description : " + description);
    }

    public void updateNoteById(String newTitle, String description, Integer noteid){
        noteMapper.updateNote(newTitle, description, noteid);
    }

}
