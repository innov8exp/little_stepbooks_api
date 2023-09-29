package com.stepbook.domain.file.service.impl;

import com.stepbook.domain.file.service.FileService;
import com.stepbook.infrastructure.exception.BusinessException;
import com.stepbook.infrastructure.exception.ErrorCode;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileServiceImpl implements FileService {

    @Value("${aws.region}")
    private String region;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.pre-signed-url-expire-time}")
    private long expireTime;
    @Value("${aws.cdn}")
    private String cdnUrl;

    public String upload(MultipartFile file, String filename) {
        AmazonS3 s3Client = this.getS3Client();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, inputStream, objectMetadata);
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            return uploadResult.getKey();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String upload(File file, String filename) {
        AmazonS3 s3Client = this.getS3Client();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, file);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        Upload upload = transferManager.upload(putObjectRequest);
        try {
            UploadResult uploadResult = upload.waitForUploadResult();
            return uploadResult.getKey();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            transferManager.shutdownNow();
        }
        return null;
    }

    @Override
    public void batchUpload(String path, List<File> fileList) {
        AmazonS3 s3Client = this.getS3Client();
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        MultipleFileUpload multipleFileUpload = transferManager
                .uploadFileList(bucketName, path, new File("."), fileList);
        try {
            multipleFileUpload.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.UPLOAD_FILE_FAILED);
        } finally {
            transferManager.shutdownNow();
        }
    }

    @Override
    public void uploadContent(String key, String content) {
        AmazonS3 s3Client = this.getS3Client();
        s3Client.putObject(bucketName, key, content);
    }

    @Override
    public void uploadContents(List<String> keys, List<String> contents) {
        AmazonS3 s3Client = this.getS3Client();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String content = contents.get(i);
            s3Client.putObject(bucketName, key, content);
        }
    }

    public InputStreamResource download(String key) {
        S3ObjectInputStream inputStream = this.getS3Client().getObject(bucketName, key).getObjectContent();
        return new InputStreamResource(inputStream);
    }

    public void delete(String key) {
        this.getS3Client().deleteObject(bucketName, key);
    }

    @Override
    public void deleteKeys(List<String> keys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        List<DeleteObjectsRequest.KeyVersion> keyVersions = keys.stream()
                .map(DeleteObjectsRequest.KeyVersion::new).collect(Collectors.toList());
        deleteObjectsRequest.withKeys(keyVersions);
        getS3Client().deleteObjects(deleteObjectsRequest);
    }

    public String getPreSignedUrl(String key) {
        try {
            AmazonS3 s3Client = this.getS3Client();
            // Set the pre-signed URL to expire after two hour.
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += expireTime;
            expiration.setTime(expTimeMillis);
            // Generate the pre-signed URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }

}
