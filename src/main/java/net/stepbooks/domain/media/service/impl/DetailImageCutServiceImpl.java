package net.stepbooks.domain.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.DetailImageCut;
import net.stepbooks.domain.media.mapper.DetailImageCutMapper;
import net.stepbooks.domain.media.service.DetailImageCutService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailImageCutServiceImpl extends ServiceImpl<DetailImageCutMapper, DetailImageCut>
        implements DetailImageCutService {


}
