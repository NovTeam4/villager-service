package com.example.villagerservice.post.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    void fileUpload(List<MultipartFile> multipartFile, List<String> filePath);
}
