package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;

import java.util.List;

public interface VirtualGoodsExpirationService extends IService<VirtualGoodsExpirationEntity> {

    /**
     * 获得指定用户未过期的虚拟产品
     *
     * @param userId
     * @return
     */
    List<VirtualGoodsExpirationEntity> validExpirations(String userId);

    /**
     * 兑现虚拟商品
     *
     * @param userId
     * @param goodsId
     * @param categoryId
     * @param toAddMonth
     */
    void redeem(String userId, String goodsId, String categoryId, int toAddMonth);

}
