package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.domain.book.mapper.BookQRCodeMapper;
import net.stepbooks.domain.book.service.BookQRCodeService;
import net.stepbooks.domain.book.vo.BookQRCodeResponse;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class BookQRCodeServiceImpl extends ServiceImpl<BookQRCodeMapper, BookQRCode> implements BookQRCodeService {

    @Value("${wechat.official-account-link}")
    private String officialAccountLink;

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public byte[] generateQRImage(String bookId, String qrCode) throws IOException {
        int size = 300;
        String format = "png";
        String QrData = officialAccountLink + "?code=" + qrCode;

        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 0);

            bitMatrix = new MultiFormatWriter().encode(QrData, BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
        ImageIO.write(image, format, outPutStream);
        return outPutStream.toByteArray();
    }

    @Override
    public String linkQrCode(String bookId) throws IOException {
        String qrCode = UUID.randomUUID().toString();
        BookQRCode bookQRCode = new BookQRCode();
        bookQRCode.setBookId(bookId);
        bookQRCode.setQrCode(qrCode);
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
    public List<BookQRCodeResponse> generateBatch(String bookId, int size) throws IOException {
        List<BookQRCode> bookQRCodes = new ArrayList<>();
        List<BookQRCodeResponse> bookQRCodeResponses = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String qrCode = this.linkQrCode(bookId);
            BookQRCode bookQRCode = new BookQRCode();
            bookQRCode.setBookId(bookId);
            bookQRCode.setQrCode(qrCode);
            bookQRCodes.add(bookQRCode);

            BookQRCodeResponse qrCodeResponse = new BookQRCodeResponse();
            qrCodeResponse.setBookQRCode(bookQRCode);
            qrCodeResponse.setQrImageData(this.generateQRImage(bookId, qrCode));
            bookQRCodeResponses.add(qrCodeResponse);
        }
        this.saveBatch(bookQRCodes);
        return bookQRCodeResponses;
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
