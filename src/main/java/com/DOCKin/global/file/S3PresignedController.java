package com.DOCKin.global.file;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3-presigned-url")
public class S3PresignedController {
    private final S3PresignedService s3PresignedService;

    @GetMapping
    public S3PresignedResponse getPresignedUrl(FileDownloadRequest request){
        return s3PresignedService.getPresignedUrl(request.objectKey());
    }
}
