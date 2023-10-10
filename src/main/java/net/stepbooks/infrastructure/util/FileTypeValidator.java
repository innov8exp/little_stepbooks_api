package net.stepbooks.infrastructure.util;

import jakarta.validation.ValidationException;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileTypeValidator {

    public static void validateFileType(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            FileMagic fileMagic = FileMagic.valueOf(bis);
            if (fileMagic.compareTo(FileMagic.OOXML) < 0) {
                throw new ValidationException("file type not support, only excel xlsx format");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValidationException("try to detect file type occur IO error: " + e.getMessage());
        }
    }
}
