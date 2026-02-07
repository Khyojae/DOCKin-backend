package com.DOCKin.global.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {
    private final S3Processor s3Processor;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public String uploadImage(MultipartFile file){
        if(file==null || file.isEmpty()) return null;

        try{
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString()+extension;

            return s3Processor.uploadFile(bucketName,uniqueFileName,file);
        } catch(Exception e){
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }
}
