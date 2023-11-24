package net.stepbooks.domain.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.interfaces.client.dto.UserDto;

public interface UserMapper extends BaseMapper<User> {

    UserDto getUserAndChildAgeInfoByUsername(String username);
}
