package com.javaMavenTest.blobFile;

import org.springframework.web.multipart.MultipartFile;

public class blobFileVo {
    private MultipartFile imgFile;
 
    public MultipartFile getImgFile() {
        return imgFile;
    }
 
    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }
}


