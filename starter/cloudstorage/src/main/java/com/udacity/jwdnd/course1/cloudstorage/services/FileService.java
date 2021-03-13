package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileFormObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileService {
    private UserMapper userMapper;
    private FileMapper fileMapper;

    public FileService(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    public void addFile(FileFormObject fileFormObject, Authentication authentication) throws IOException, SQLException {
        File file = new File();
        file.setFilename(fileFormObject.getFileUpload().getOriginalFilename());
        file.setContenttype(fileFormObject.getFileUpload().getContentType());
        file.setFilesize(String.valueOf(fileFormObject.getFileUpload().getSize()));
        file.setUserid(userMapper.getUser(authentication.getName()).getUserid());
        file.setFiledata(fileFormObject.getFileUpload().getBytes());
        fileMapper.insertFile(file);
    }

    public boolean fileAlreadyExists(String filename){
        return fileMapper.getFileByName(filename) != null;
    }

    public File getFile(String fileName){
        return fileMapper.getFileByName(fileName);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFile(fileId);
    }

    public List<File> getAllFiles(Authentication authentication){
        return fileMapper.getAllFiles(userMapper.getUser(authentication.getName()).getUserid());
    }

    public void deleteFile(Integer fileId){
        fileMapper.deleteFile(fileId);
    }
}
