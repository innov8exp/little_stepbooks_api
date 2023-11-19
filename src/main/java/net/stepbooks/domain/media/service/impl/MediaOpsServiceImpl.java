package net.stepbooks.domain.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.domain.media.service.MediaOpsService;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.infrastructure.enums.AccessPermission;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaOpsServiceImpl implements MediaOpsService {

    private final MediaService mediaService;
    private final FileService privateFileServiceImpl;
    private final FileService publicFileServiceImpl;

    @Override
    public List<Media> getByIds(String[] ids) {
        List<Media> medias = mediaService.list(Wrappers.<Media>lambdaQuery().in(Media::getId, Arrays.asList(ids)));
        return medias.stream().peek(media -> {
            if (AccessPermission.PRIVATE.equals(media.getAccessPermission())) {
                media.setObjectUrl(privateFileServiceImpl.getUrl(media.getObjectKey()));
            }
        }).collect(Collectors.toList());
    }
}
