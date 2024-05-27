package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.mapper.UserPointsLogMapper;
import net.stepbooks.domain.points.service.UserPointsLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserPointsLogServiceImpl extends ServiceImpl<UserPointsLogMapper, UserPointsLog>
        implements UserPointsLogService {

    @Override
    public int pointsTotal(String userId, LocalDate thisYearsNewYear, LocalDate nextYearsNewYear) {


        return 0;
    }
}
