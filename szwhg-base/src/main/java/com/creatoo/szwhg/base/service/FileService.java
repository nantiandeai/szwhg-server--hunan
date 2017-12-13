package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.FileType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

/**
 * Created by yunyan on 2017/9/2.
 */
@Service
public class FileService {
    @Value("${upload.dir}")
    private String rootDir;


    public String saveFile(FileType type,String filename,InputStream input) {
        try {
            String id = UUID.randomUUID().toString();
            String relativeDir=type+"/"+id.substring(0,1)+"/"+id.substring(1,2)+"/"+id.substring(2,3)+"/"+id.substring(3,4);
            String realDir=rootDir+"/"+relativeDir;
            int index=StringUtils.lastIndexOf(filename,".");
            String ext=filename.substring(index+1);
            File targetDir = new File(realDir);
            if (!targetDir.exists()) FileUtils.forceMkdir(targetDir);
            File newfile = new File(targetDir, id+"."+ext);
            IOUtils.copy(input, new FileOutputStream(newfile));
            return relativeDir+"/"+id+"."+ext;
        } catch (IOException e) {
            throw new BsException("");
        }
    }

    public InputStream getFileStream(String id){
        try {
            return new FileInputStream(rootDir +id);
        } catch (FileNotFoundException e) {
            throw new BsException("");
        }
    }

    public byte[] getFileBytes(String id){
        try {
            return IOUtils.toByteArray(new FileInputStream(rootDir +id));
        } catch (IOException e) {
            throw new BsException("");
        }
    }
}
