package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private UserMapper userMapper;
    private CredentialsMapper credentialsMapper;
    private User currentUser;

    public CredentialService(UserMapper userMapper, CredentialsMapper credentialsMapper, User currentUser) {
        this.userMapper = userMapper;
        this.credentialsMapper = credentialsMapper;
        this.currentUser = currentUser;
    }

    public List<Credential> getAllCredentials(Authentication authentication){
        currentUser = userMapper.getUser(authentication.getName());
        return credentialsMapper.getAllCredentials(currentUser.getUserid());
    }
}
