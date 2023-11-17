package net.stepbooks.domain.address.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.address.entity.UserAddress;
import net.stepbooks.domain.address.mapper.UserAddressMapper;
import net.stepbooks.domain.address.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddress> findByUserId(String userId) {
        return list(Wrappers.<UserAddress>lambdaQuery().eq(UserAddress::getUserId, userId));
    }
}
