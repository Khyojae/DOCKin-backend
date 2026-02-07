package com.DOCKin.global.file;

import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Service
@RequiredArgsConstructor
public class SpringFileDownloadService {
    private final S3Processor s3Processor;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public Resource getFileResource(String objectKey, HttpServletResponse response){
        InputStream inputStream = s3Processor.getObjectBytes(bucketName,objectKey);

        try{
            String originFileName = URLDecoder.decode(objectKey,"UTF-8");
            response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\"",
                    originFileName));
        } catch (UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
        response.setContentType("image/png");
        return new InputStreamResource(inputStream);
    }
}
