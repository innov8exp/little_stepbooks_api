package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.domain.book.vo.BookQRCodeResponse;
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
     * 分页数据
     */
    IPage<BookQRCodeDto> getPage(Page<BookQRCode> page, String bookId) throws IOException;

    /**
     * 生成带公众号带参数二维码（非微信api）
     *
     * @param bookId bookId
     * @param qrCode qrCode
     * @return byte[]
     */
    byte[] generateQRImage(String bookId, String qrCode) throws IOException;

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
    List<BookQRCodeResponse> generateBatch(String bookId, int size) throws IOException;
}
