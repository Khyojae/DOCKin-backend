package com.DOCKin.global.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j

public class S3Processor {

    private final AmazonS3 amazonS3;

    public InputStream getObjectBytes(String bucketName, String objectKey){
        return amazonS3.getObject(bucketName,objectKey)
                .getObjectContent()
                .getDelegateStream();
    }

    public String uploadFile(String bucketName, String objectKey, MultipartFile file){
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucketName,objectKey,file.getInputStream(),metadata);
            return amazonS3.getUrl(bucketName,objectKey).toString();
        } catch (Exception e){
            throw new RuntimeException("S3 업로드 실패",e);
        }
    }
}
