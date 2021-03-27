package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private UserMapper userMapper;
    private CredentialsMapper credentialsMapper;
    private EncryptDecryptService encryptDecryptService;
    private User currentUser;

    public CredentialService(UserMapper userMapper, CredentialsMapper credentialsMapper, EncryptDecryptService encryptDecryptService) {
        this.userMapper = userMapper;
        this.credentialsMapper = credentialsMapper;
        this.encryptDecryptService = encryptDecryptService;
    }

//    @PostConstruct
//    public void postConstruct(Authentication authentication){
//        currentUser = userMapper.getUser(authentication.getName());
//    }


    public void addCredential(CredentialFormObject credentialFormObject, Authentication authentication){
        Credential credential = new Credential();
        credential.setUrl(credentialFormObject.getCredentialUrl());
        credential.setUsername(credentialFormObject.getCredentialUsername());
        credential.setKey(encryptDecryptService.getEncodedKey());
        String encryptedPassword = encryptDecryptService.encrypt(credentialFormObject.getCredentialPassword());
        credential.setPassword(encryptedPassword);
        currentUser = userMapper.getUser(authentication.getName());
        credential.setUserid(currentUser.getUserid());
        credentialsMapper.insertCredential(credential);
    }

    public List<Credential> getAllCredentials(Authentication authentication){
        currentUser = userMapper.getUser(authentication.getName());
        return credentialsMapper.getAllCredentials(currentUser.getUserid());
    }

    public List<Credential> getAllCredentialsWithoutId(){
        return credentialsMapper.getAllCredentialsWithoutId();
    }

    public Credential getCredential(Integer credentialId){
        return credentialsMapper.getCredential(credentialId);
    }

    public void deleteCredential(Integer userid, Integer credentialid){
        Integer userById = userMapper.getUserById(userid).getUserid();
        Integer credentialById = credentialsMapper.getCredential(credentialid).getCredentialid();
        if(userById.equals(credentialsMapper.getCredential(credentialid).getUserid())){
            credentialsMapper.deteleCredential(credentialById);
        } else{
            throw new RuntimeException("There was an error while processing this request...");
        }
    }

    public void updateCredential(String newUrl, String newUsername, String newPassword, Integer credentialId){
        String password = encryptDecryptService.encrypt(newPassword);
        int updatedCredential = credentialsMapper.updateCredential(newUrl,newUsername,password,credentialId);
        System.out.println("Updated " + updatedCredential + " credentials");
    }


}
