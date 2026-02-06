package com.DOCKin.global.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3PresignedService {
    private final S3Processor s3Processor;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public S3PresignedResponse getPresignedUrl(String objectKey){
        return new S3PresignedResponse(s3Processor.getPresignedUrl(bucketName,objectKey));
    }
}
