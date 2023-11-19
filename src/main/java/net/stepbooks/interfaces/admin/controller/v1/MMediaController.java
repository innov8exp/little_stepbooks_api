package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.domain.media.service.MediaOpsService;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.infrastructure.enums.AccessPermission;
import net.stepbooks.infrastructure.enums.AssetDomain;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.FileValidateUtil;
import net.stepbooks.interfaces.admin.dto.UploadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Media", description = "媒体资源相关接口")
@RestController
@RequestMapping("/admin/v1/medias")
@RequiredArgsConstructor
public class MMediaController {

    @Value("${once-upload-file-count}")
    private Integer onceUploadFileCount;
    @Value("${file-unit}")
    private String fileUnit;
    @Value("${max-file-size}")
    private Integer maxFileSize;
    @Value("${allowed-file-types}")
    private String[] allowedFileTypes;
    @Value("${allowed-files}")
    private String[] allowedFiles;

    private final MediaService mediaService;
    private final MediaOpsService mediaOpsService;
    private final FileService publicFileServiceImpl;
    private final FileService privateFileServiceImpl;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件")
    public ResponseEntity<Media> uploadFile(@RequestParam("file") @NotNull MultipartFile file,
                                            @RequestParam AccessPermission permission,
                                            @RequestParam(required = false) AssetDomain domain) {
        if (ObjectUtils.isEmpty(domain)) {
            domain = AssetDomain.DEFAULT;
        }
        UploadDto uploadDto = new UploadDto(permission, domain);
        try {
            FileValidateUtil.validateFileType(file, allowedFileTypes, allowedFiles, maxFileSize, fileUnit);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        String filename = file.getOriginalFilename();
        if (AccessPermission.PRIVATE.equals(uploadDto.getPermission())) {
            Media media = privateFileServiceImpl.upload(file, filename, uploadDto);
            String url = privateFileServiceImpl.getUrl(media.getObjectKey());
            media.setObjectUrl(url);
            return ResponseEntity.ok(media);
        }
        Media media = publicFileServiceImpl.upload(file, filename, uploadDto);

        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        Media media = mediaService.getById(id);
        privateFileServiceImpl.delete(media.getObjectKey());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件信息")
    public ResponseEntity<Media> getMedia(@PathVariable String id) {
        Media media = mediaService.getById(id);
        String url;
        if (AccessPermission.PRIVATE.equals(media.getAccessPermission())) {
            url = privateFileServiceImpl.getUrl(media.getObjectKey());
        } else {
            url = publicFileServiceImpl.getUrl(media.getObjectKey());
        }
        media.setObjectUrl(url);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Media>> getMedias(@RequestParam String[] ids) {
        List<Media> medias = mediaOpsService.getByIds(ids);
        return ResponseEntity.ok(medias);
    }

}
