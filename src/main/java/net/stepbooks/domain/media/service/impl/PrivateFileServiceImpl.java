package net.stepbooks.domain.media.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.infrastructure.enums.AccessPermission;
import net.stepbooks.infrastructure.enums.MediaType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.UploadDto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateFileServiceImpl implements FileService {

    private final MediaService mediaService;

    @Value("${aws.region}")
    private String region;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.pre-signed-url-expire-hour}")
    private int expireHour;
    @Value("${aws.cdn}")
    private String cdnHost;

    public Media upload(MultipartFile file, String filename, UploadDto uploadDto) {
        String path = uploadDto.getDomain().getPath();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String objectName = UUID.randomUUID() + "." + extension;
        AmazonS3 s3Client = this.getS3Client();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        String objectKey;
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path + objectName,
                    inputStream, objectMetadata);
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            objectKey = uploadResult.getKey();
        } catch (IOException | InterruptedException e) {
            throw new BusinessException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        Media media = Media.builder().objectName(objectName).fileName(filename)
                .fileSize(file.getSize()).objectType(MediaType.IMAGE).objectKey(objectKey)
                .accessPermission(AccessPermission.PRIVATE)
                .assetDomain(uploadDto.getDomain())
                .bucketName(bucketName).storePath(path).build();
        mediaService.save(media);
        media.setObjectUrl(getUrl(objectKey));
        return media;
    }

    @Override
    public Media upload(File file, String filename, UploadDto uploadDto) {
        String path = uploadDto.getDomain().getPath();
        String extension = FilenameUtils.getExtension(file.getName());
        String objectName = UUID.randomUUID() + "." + extension;
        AmazonS3 s3Client = this.getS3Client();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path + objectName, file);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        Upload upload = transferManager.upload(putObjectRequest);
        String objectKey;
        try {
            UploadResult uploadResult = upload.waitForUploadResult();
            objectKey = uploadResult.getKey();
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.UPLOAD_FILE_FAILED);
        } finally {
            transferManager.shutdownNow();
        }
        Media media = Media.builder().objectName(objectName).fileName(filename)
                .fileSize(file.length()).objectType(MediaType.IMAGE).objectKey(objectKey)
                .accessPermission(AccessPermission.PRIVATE)
                .assetDomain(uploadDto.getDomain())
                .bucketName(bucketName).storePath(path).build();
        mediaService.save(media);
        media.setObjectUrl(getUrl(objectKey));
        return media;
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
        mediaService.remove(Wrappers.<Media>lambdaQuery().eq(Media::getObjectKey, key));
    }

    @Override
    public void deleteKeys(List<String> keys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        List<DeleteObjectsRequest.KeyVersion> keyVersions = keys.stream()
                .map(DeleteObjectsRequest.KeyVersion::new).collect(Collectors.toList());
        deleteObjectsRequest.withKeys(keyVersions);
        getS3Client().deleteObjects(deleteObjectsRequest);
        mediaService.remove(Wrappers.<Media>lambdaQuery().in(Media::getObjectKey, keys));
    }

    public String getUrl(String key) {
        try {
            AmazonS3 s3Client = this.getS3Client();
            // Set the pre-signed URL to expire after two hour.
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, expireHour);
            // Generate the pre-signed URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(calendar.getTime());
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            //https://stepbook.stage.s3.cn-north-1.amazonaws.com.cn
//            https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage
//            return url.toString();
            log.debug("原始地址：{}", url.toString());
            String query = url.getQuery();
//            String newUrlString = cdnHost + "/" + bucketName + "/" + key + "?" + query;
//            log.debug("path: {}", newUrlString);
            return url.toString();
        } catch (SdkClientException e) {
            throw new BusinessException(ErrorCode.S3_FAILED);
        }
    }

    private AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }

}
