package com.DOCKin.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${AWS_ACCESS_KEY}")
    private String accesskey;

    @Value("${AWS_SECRET_KEY}")
    private String secretkey;

    @Value("${AWS_REGION}")
    private String region;

    @Bean
    public AmazonS3 AmazonS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accesskey,secretkey)))
                .withRegion(region).build();
    }
}
