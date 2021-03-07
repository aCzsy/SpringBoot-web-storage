package com.udacity.jwdnd.course1.cloudstorage.model;

import java.sql.Blob;

public class File {
    private Integer fileId;
    private String filename;
    private String contenttype;
    private String filesize;
    private User user;
    private Blob filedata;
}
