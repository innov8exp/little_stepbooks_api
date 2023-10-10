package net.stepbooks.domain.common.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {

    String upload(MultipartFile file, String filename);

    String upload(File file, String filename);

    void batchUpload(String path, List<File> fileList);

    void uploadContent(String key, String content);

    void uploadContents(List<String> keys, List<String> contents);

    InputStreamResource download(String key);

    void delete(String key);

    void deleteKeys(List<String> keys);

    String getPreSignedUrl(String key);

}
