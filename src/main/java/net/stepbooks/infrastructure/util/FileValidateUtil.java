package net.stepbooks.infrastructure.util;

import lombok.experimental.UtilityClass;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class FileValidateUtil {

    private static final int BYTE_NUMBER = 1024;

    public static void validateFileType(MultipartFile file,
                                        String[] allowedFileTypes,
                                        String[] allowedFiles,
                                        Integer maxFileSize,
                                        String fileUnit) throws IOException {

        // Check if the file is empty
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file is empty.");
        }

        if (Objects.requireNonNull(file.getOriginalFilename()).contains("..")) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file name contains invalid characters.");
        }

        // Check if the file size is too large
        if (checkFileSize(file.getSize(), maxFileSize, fileUnit)) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file size is too large.");
        }

        // Check if the file extension
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (fileType != null) {
            if (!Arrays.asList(allowedFiles).contains(fileType.toLowerCase())) {
                throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file type is not allowed.");
            }
        }

        // Check if the file type is allowed by using request content type
//        String fileType = file.getContentType();
//        if (!Arrays.asList(allowedFileTypes).contains(fileType)) {
//            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file type is not allowed.");
//        }

        // Check if the file type is allowed by using file mime type
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getBytes());
        if (!Arrays.asList(allowedFileTypes).contains(mimeType)) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file type is not allowed.");
        }

        // Check if the file contains any malicious code
//        byte[] bytes = file.getBytes();
//        if (isMalicious(bytes)) {
//            throw new BusinessException(ErrorCode.CONSTRAINT_VALIDATION_ERROR, "The file contains malicious code.");
//        }

    }

//    private static boolean isMalicious(byte[] bytes) {
//        try {
//            // Check if the file contains any malicious code
//            String fileContent = new String(bytes, StandardCharsets.UTF_8);
//            Pattern pattern = Pattern.compile("(?i)<(\\s*)(script|meta|link|style)(.*?)>");
//            Matcher matcher = pattern.matcher(fileContent);
//            return matcher.find();
//        } catch (Exception e) {
//            return true;
//        }
//    }

    private static boolean checkFileSize(Long fileLen, Integer fileSize, String fileUnit) {
        double fileSizeCom = switch (fileUnit.toUpperCase()) {
            case "B" -> (double) fileLen;
            case "K" -> (double) fileLen / BYTE_NUMBER;
            case "M" -> (double) fileLen / (BYTE_NUMBER * BYTE_NUMBER);
            case "G" -> (double) fileLen / (BYTE_NUMBER * BYTE_NUMBER * BYTE_NUMBER);
            default ->
                    throw new IllegalArgumentException("Invalid file unit type: " + fileUnit
                            + ", and the valid value will be: B, K, M, G");
        };
        return fileSizeCom > fileSize;
    }
}
