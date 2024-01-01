package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;

public interface BookQRCodeService extends IService<BookQRCode> {

    /**
     * 创建book&code关联关系
     *
     * @param createDto createDto
     */
    void createBookQRCode(BookQRCodeCreateDto createDto);

    /**
     * 分页数据
     */
    IPage<BookQRCodeDto> getPage(Page<BookQRCode> page, String bookId, String qrCode, String activeStatus);

    /**
     * 删除book&code关联关系
     *
     * @param qrcode qrcode
     */
    void deleteByQrCode(String qrcode);

    /**
     * 激活book
     *
     * @param qrcode qrcode
     */
    void active(String qrcode);
}
