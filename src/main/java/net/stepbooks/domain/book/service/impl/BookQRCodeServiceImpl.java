package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.domain.book.enums.BookActiveStatus;
import net.stepbooks.domain.book.mapper.BookQRCodeMapper;
import net.stepbooks.domain.book.service.BookQRCodeService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookQRCodeServiceImpl extends ServiceImpl<BookQRCodeMapper, BookQRCode> implements BookQRCodeService {

    @Value("${wechat.official-account-link}")
    private String officialAccountLink;

    @Override
    public void createBookQRCode(BookQRCodeCreateDto createDto) {
        String bookId = createDto.getBookId();
        int size = createDto.getSize();
        List<BookQRCode> bookQRCodes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String qrCode = UUID.randomUUID().toString();
            BookQRCode bookQRCode = new BookQRCode();
            bookQRCode.setBookId(bookId);
            bookQRCode.setQrCode(qrCode);
            bookQRCode.setActiveStatus(BookActiveStatus.UNACTIVATED);
            bookQRCodes.add(bookQRCode);
        }
        this.saveBatch(bookQRCodes);
    }

    @Override
    public IPage<BookQRCodeDto> getPage(Page<BookQRCode> page, String bookId, String qrCode, String activeStatus) {
        LambdaQueryWrapper<BookQRCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(bookId), BookQRCode::getBookId, bookId);
        wrapper.eq(ObjectUtils.isNotEmpty(qrCode), BookQRCode::getQrCode, qrCode);
        wrapper.eq(ObjectUtils.isNotEmpty(activeStatus), BookQRCode::getActiveStatus, activeStatus);
        Page<BookQRCode> bookQRCodePage = this.baseMapper.selectPage(page, wrapper);

        Page<BookQRCodeDto> dtoPage = new Page<>();
        ArrayList<BookQRCodeDto> bookQRCodeDtos = new ArrayList<>();
        for (BookQRCode record : bookQRCodePage.getRecords()) {
            BookQRCodeDto dto = new BookQRCodeDto();
            dto.setBookId(record.getBookId());
            dto.setQrCode(record.getQrCode());
            dto.setQrCodeUrl(officialAccountLink + "?code=" + record.getQrCode());
            bookQRCodeDtos.add(dto);
        }
        dtoPage.setRecords(bookQRCodeDtos);
        dtoPage.setTotal(bookQRCodePage.getTotal());
        dtoPage.setSize(bookQRCodePage.getSize());
        dtoPage.setCurrent(bookQRCodePage.getCurrent());
        dtoPage.setPages(bookQRCodePage.getPages());
        return dtoPage;
    }

    @Override
    public void deleteByQrCode(String qrcode) {
        this.verifyExists(qrcode);
        LambdaQueryWrapper<BookQRCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BookQRCode::getQrCode, qrcode);
        this.remove(wrapper);
    }

    @Override
    public void active(String qrcode) {
        this.verifyExists(qrcode);
        LambdaUpdateWrapper<BookQRCode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(BookQRCode::getQrCode, qrcode);
        updateWrapper.set(BookQRCode::getActiveStatus, BookActiveStatus.ACTIVATED);
        this.update(updateWrapper);
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
