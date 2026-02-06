package com.DOCKin.global.file;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

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

    public String getPresignedUrl(String bucketName, String objectKey){
        Date date= new Date();
        long time = date.getTime() + 1000*60*5;
        date.setTime(time);

        URL presignedUrl = amazonS3.generatePresignedUrl(bucketName,
                objectKey,
                date);

        return presignedUrl.toString();
    }
}
