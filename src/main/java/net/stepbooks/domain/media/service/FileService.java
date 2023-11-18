package net.stepbooks.domain.media.service;

import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.interfaces.admin.dto.UploadDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {

    Media upload(MultipartFile file, String filename, UploadDto uploadDto);

    Media upload(File file, String filename, UploadDto uploadDto);

    void uploadContent(String key, String content);

    void uploadContents(List<String> keys, List<String> contents);

    InputStreamResource download(String key);

    void delete(String key);

    void deleteKeys(List<String> keys);

    String getUrl(String key);

}
