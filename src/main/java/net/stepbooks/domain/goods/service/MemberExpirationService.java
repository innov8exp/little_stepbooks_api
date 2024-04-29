package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.MemberExpirationEntity;

public interface MemberExpirationService extends IService<MemberExpirationEntity> {

    /**
     * 获得指定用户的会员过期状态
     *
     * @param userId
     * @return
     */
    MemberExpirationEntity getExpirationByUserId(String userId);

    /**
     * 兑现会员
     *
     * @param userId
     * @param toAddMonth
     */
    void redeem(String userId, int toAddMonth);

}
