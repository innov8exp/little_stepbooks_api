package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.ProductBook;

import java.util.List;

public interface ProductBookService extends IService<ProductBook> {

    List<ProductBook> getProductBooksByBookId(String bookId);
}
