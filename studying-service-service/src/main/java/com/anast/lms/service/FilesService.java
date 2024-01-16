package com.anast.lms.service;

import com.anast.lms.model.CustomMultipartFile;
import com.anast.lms.model.course.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FilesService {

    @Value("${upload.path}")
    private String dirPath;

    public byte[] getFileDataBytes(ModuleResource resource) {
        File file = new File(dirPath + "\\" + resource.getFileName());

        try (FileInputStream inputStream = new FileInputStream(file)){
            return inputStream.readAllBytes();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return new byte[0];
        }
    }

    public void uploadFileData(CustomMultipartFile file) {
        String filePath = dirPath + "\\" + file.getOriginalFilename();
        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
