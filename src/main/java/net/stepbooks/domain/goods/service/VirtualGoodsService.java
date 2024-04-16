package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

import java.util.List;

public interface VirtualGoodsService extends IService<VirtualGoodsEntity> {

    List<VirtualGoodsDto> listAll(String virtualCategoryId);

}
