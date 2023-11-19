package net.stepbooks.domain.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.mapper.MediaMapper;
import net.stepbooks.domain.media.service.MediaService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {


}
