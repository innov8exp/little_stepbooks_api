package net.stepbooks.domain.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.points.entity.UserPointsLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

public interface UserPointsLogMapper extends BaseMapper<UserPointsLog> {

    @Select({
            "select",
            "sum(points_change) as total",
            "from",
            "step_user_points_log",
            "where",
            "user_id=#{userId} and expire_at > #{startYear} and expire_at <= #{endYear}"})
    int getTotalAmountByYear(@Param("userId") String userId,
                             @Param("startYear") LocalDate startYear,
                             @Param("endYear") LocalDate endYear);

}
