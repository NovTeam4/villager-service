package com.example.villagerservice.post.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.villagerservice.post.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    @Override
    public void fileUpload(List<MultipartFile> multipartFile, List<String> filePath) {
        int num = 0;
        try {
            for (MultipartFile file : multipartFile) {
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentLength(file.getInputStream().available());
                String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1)
                        .toLowerCase();
                String contentTypeTail = "jpeg";
                if (fileExtension.equals("gif")) contentTypeTail = "gif";
                else if (fileExtension.equals("png")) contentTypeTail = "png";
                objMeta.setContentType("image/"+contentTypeTail);

                try (InputStream inputStream = file.getInputStream()) {
                    amazonS3Client.putObject(new PutObjectRequest(bucket, filePath.get(num++), inputStream, objMeta)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch (IOException e) {
                    throw new FileUploadException();
                }
            }
        } catch (Exception e) {
            log.error("error : ", e);
        }
    }
}
