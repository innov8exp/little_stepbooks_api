package net.stepbooks.domain.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.mapper.MediaMapper;
import net.stepbooks.domain.media.service.MediaService;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {
}
