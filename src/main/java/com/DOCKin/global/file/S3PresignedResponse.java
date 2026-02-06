package com.DOCKin.global.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class S3PresignedResponse {
    String presignedUrl;
}
