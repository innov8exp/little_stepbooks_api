package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.domain.book.mapper.BookQRCodeMapper;
import net.stepbooks.domain.book.service.BookQRCodeService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class BookQRCodeServiceImpl extends ServiceImpl<BookQRCodeMapper, BookQRCode> implements BookQRCodeService {

    @Value("${wechat.official-account-link}")
    private String officialAccountLink;

    @Override
    public void createBookQRCode(BookQRCodeCreateDto createDto) throws IOException {
        this.generateBatch(createDto.getBookId(), createDto.getSize());
    }

    @Override
    public String linkQrCode(String bookId) throws IOException {
        String qrCode = UUID.randomUUID().toString();
        BookQRCode bookQRCode = new BookQRCode();
        bookQRCode.setBookId(bookId)
                .setQrCode(qrCode);
        this.save(bookQRCode);
        return qrCode;
    }

    @Override
    public void deleteByQrCode(String qrcode) {
        this.verifyExists(qrcode);
        LambdaQueryWrapper<BookQRCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BookQRCode::getQrCode, qrcode);
        this.remove(wrapper);
    }

    @Override
    public void generateBatch(String bookId, int size) throws IOException {
        List<BookQRCode> bookQRCodes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String qrCode = UUID.randomUUID().toString();
            BookQRCode bookQRCode = new BookQRCode();
            bookQRCode.setBookId(bookId)
                    .setQrCode(qrCode);
            bookQRCodes.add(bookQRCode);
        }
        this.saveBatch(bookQRCodes);
    }

    @Override
    public List<BookQRCodeDto> listByBookId(String bookId, String qrcode) {
        LambdaQueryWrapper<BookQRCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(bookId), BookQRCode::getBookId, bookId);
        wrapper.eq(ObjectUtils.isNotEmpty(qrcode), BookQRCode::getQrCode, qrcode);
        List<BookQRCode> bookQRCodes = this.baseMapper.selectList(wrapper);

        ArrayList<BookQRCodeDto> qrCodeDtos = new ArrayList<>();
        for (BookQRCode bookQRCode : bookQRCodes) {
            BookQRCodeDto bookQRCodeDto = new BookQRCodeDto();
            bookQRCodeDto.setBookId(bookQRCode.getBookId())
                    .setQrCode(bookQRCode.getQrCode())
                    .setUrl(officialAccountLink + "?code=" + bookQRCode.getQrCode());
            qrCodeDtos.add(bookQRCodeDto);
        }

        return qrCodeDtos;
    }

    private void verifyExists(String qrcode) {
        LambdaQueryWrapper<BookQRCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BookQRCode::getQrCode, qrcode);
        List<BookQRCode> list = this.list(wrapper);
        if (list.isEmpty()) {
            throw new BusinessException(ErrorCode.CODE_NOT_EXISTS_ERROR);
        }
    }
}
