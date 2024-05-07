package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

import java.util.List;

public interface VirtualGoodsService extends IService<VirtualGoodsEntity> {

    VirtualGoodsEntity create(VirtualGoodsEntity entity);

    VirtualGoodsEntity update(String id, VirtualGoodsEntity entity);

    List<VirtualGoodsDto> listAll(String virtualCategoryId);

}
