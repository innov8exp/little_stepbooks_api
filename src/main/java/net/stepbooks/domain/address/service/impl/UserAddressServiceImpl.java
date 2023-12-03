package net.stepbooks.domain.address.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.address.entity.UserAddress;
import net.stepbooks.domain.address.mapper.UserAddressMapper;
import net.stepbooks.domain.address.service.UserAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddress> findByUserId(String userId) {
        return list(Wrappers.<UserAddress>lambdaQuery().eq(UserAddress::getUserId, userId).orderByDesc(UserAddress::getIsDefault));
    }

    @Override
    public UserAddress findDefaultByUserId(String userId) {
        return getOne(Wrappers.<UserAddress>lambdaQuery().eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, true));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserAddress(UserAddress userAddress) {
        // 更改其它地址为非默认
        if (userAddress.getIsDefault()) {
            update(Wrappers.<UserAddress>lambdaUpdate().eq(UserAddress::getUserId, userAddress.getUserId())
                    .set(UserAddress::getIsDefault, false));
        }
        // 如果没有默认地址，设置为默认
        UserAddress defaultAddress = findDefaultByUserId(userAddress.getUserId());
        if (ObjectUtils.isEmpty(defaultAddress)) {
            userAddress.setIsDefault(true);
        }
        save(userAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAddress(UserAddress userAddress) {
        // 更改其它地址为非默认
        if (userAddress.getIsDefault()) {
            update(Wrappers.<UserAddress>lambdaUpdate().eq(UserAddress::getUserId, userAddress.getUserId())
                    .set(UserAddress::getIsDefault, false));
        }
        updateById(userAddress);
    }
}
