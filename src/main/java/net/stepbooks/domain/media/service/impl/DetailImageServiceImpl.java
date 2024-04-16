package net.stepbooks.domain.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.DetailImage;
import net.stepbooks.domain.media.mapper.DetailImageMapper;
import net.stepbooks.domain.media.service.DetailImageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailImageServiceImpl extends ServiceImpl<DetailImageMapper, DetailImage> implements DetailImageService {


}
