package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;

import java.io.IOException;
import java.util.List;

public interface BookQRCodeService extends IService<BookQRCode> {

    /**
     * 创建关联关系
     *
     * @param createDto createDto
     */
    void createBookQRCode(BookQRCodeCreateDto createDto) throws IOException;

    /**
     * 创建book&code关联关系
     *
     * @param bookId bookId
     * @return qrCode
     */
    String linkQrCode(String bookId) throws IOException;

    /**
     * 删除book&code关联关系
     *
     * @param qrcode qrcode
     */
    void deleteByQrCode(String qrcode);

    /**
     * 批量生成关联关系
     *
     * @param bookId bookId
     * @param size   size
     */
    void generateBatch(String bookId, int size) throws IOException;

    /**
     * list数据
     */
    List<BookQRCodeDto> listByBookId(String bookId, String qrcode);
}
