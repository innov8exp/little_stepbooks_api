package net.stepbooks.domain.address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.address.entity.UserAddress;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {

    List<UserAddress> findByUserId(String userId);
}
