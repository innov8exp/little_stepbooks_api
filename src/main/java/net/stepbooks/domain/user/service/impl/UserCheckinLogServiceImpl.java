package net.stepbooks.domain.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.domain.user.entity.UserCheckinLog;
import net.stepbooks.domain.user.mapper.UserCheckinLogMapper;
import net.stepbooks.domain.user.service.UserCheckinLogService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.client.dto.CheckinDto;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCheckinLogServiceImpl extends ServiceImpl<UserCheckinLogMapper, UserCheckinLog>
        implements UserCheckinLogService {

    private final UserPointsService userPointsService;

    @Transactional
    @Override
    public CheckinDto dailyCheckin(String userId) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LambdaQueryWrapper<UserCheckinLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserCheckinLog::getCheckinDate, today);
        wrapper.eq(UserCheckinLog::getUserId, userId);
        UserCheckinLog userCheckinLog = getOne(wrapper);
        if (ObjectUtils.isEmpty(userCheckinLog)) {
            userCheckinLog = new UserCheckinLog();
            userCheckinLog.setUserId(userId);
            userCheckinLog.setCheckinDate(today);

            //获得昨天的签到信息
            LambdaQueryWrapper<UserCheckinLog> yesterdayWrapper = Wrappers.lambdaQuery();
            yesterdayWrapper.eq(UserCheckinLog::getCheckinDate, yesterday);
            yesterdayWrapper.eq(UserCheckinLog::getUserId, userId);
            UserCheckinLog yesterdayUserCheckinLog = getOne(yesterdayWrapper);
            int continuesDay = 1;
            if (!ObjectUtils.isEmpty(yesterdayUserCheckinLog)) {
                continuesDay = yesterdayUserCheckinLog.getContinuesDay() + 1;
            }
            userCheckinLog.setContinuesDay(continuesDay);
            save(userCheckinLog);
            CheckinDto checkinDto = new CheckinDto();
            checkinDto.setContinuesDay(continuesDay);
            PointsDto points = userPointsService.dailyCheckin(userId, continuesDay);
            checkinDto.setPoints(points);
            return checkinDto;
        } else {
            throw new BusinessException(ErrorCode.CHECKIN_ALREADY);
        }
    }
}
