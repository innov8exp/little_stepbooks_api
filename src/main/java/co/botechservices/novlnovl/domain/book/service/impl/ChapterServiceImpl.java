package co.botechservices.novlnovl.domain.book.service.impl;

import co.botechservices.novlnovl.domain.book.dto.ChapterCountDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterNameAndKeyDto;
import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.book.entity.ChapterEntity;
import co.botechservices.novlnovl.domain.book.service.BookService;
import co.botechservices.novlnovl.domain.book.service.ChapterService;
import co.botechservices.novlnovl.domain.file.service.FileService;
import co.botechservices.novlnovl.domain.order.entity.ConsumptionEntity;
import co.botechservices.novlnovl.domain.order.service.ConsumptionService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.enums.UploadType;
import co.botechservices.novlnovl.infrastructure.mapper.ChapterMapper;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    public static final String DIVIDE_STRING = "####NOVLNOVL####";
    public static final String STORE_PATH = "book-assets/books/";
    private final BookService bookService;
    private final ChapterMapper chapterMapper;
    private final FileService fileService;
    private final ConsumptionService consumptionService;
    @Value("${aws.cdn}")
    private String cdnUrl;

    @Override
    public List<ChapterDto> getChaptersByBookId(String bookId, String userId) {
        List<ChapterEntity> chapterEntities = chapterMapper.selectList(Wrappers.<ChapterEntity>lambdaQuery()
                .eq(ChapterEntity::getBookId, bookId));
        List<ConsumptionEntity> consumptionEntities = consumptionService.findByBookAndUser(bookId, userId);
        return chapterEntities.stream().map(chapterEntity -> {
            ChapterDto chapterDto = BaseAssembler.convert(chapterEntity, ChapterDto.class);
            boolean unlocked = consumptionEntities.stream()
                    .anyMatch(consumptionEntity -> consumptionEntity.getChapterId().equals(chapterDto.getId()));
            chapterDto.setUnlocked(unlocked);
            return chapterDto;
        }).collect(Collectors.toList());
    }

    @Override
    public ChapterEntity findChapterById(String chapterId) {
        return chapterMapper.selectById(chapterId);
    }

    @Override
    public void createChapter(ChapterDto dto) {
        UUID uuid = UUID.randomUUID();
        String bookId = dto.getBookId();
        BookEntity book = bookService.findBookById(bookId);
        ChapterEntity entity = BaseAssembler.convert(dto, ChapterEntity.class);
        String key = STORE_PATH + book.getBookName() + "/" + uuid + ".txt";
        fileService.uploadContent(key, dto.getContent());
        entity.setContentLink(cdnUrl + "/" + key);
        entity.setStoreKey(key);
        entity.setCreatedAt(LocalDateTime.now());
        chapterMapper.insert(entity);
    }

    @Transactional
    @Override
    public void updateChapter(String id, ChapterDto dto) {
        ChapterEntity entity = BaseAssembler.convert(dto, ChapterEntity.class);
        ChapterEntity originChapter = this.findChapterById(id);
        try {
            fileService.delete(originChapter.getStoreKey());
        } catch (Exception e) {
            log.error("delete file error", e);
        }
        fileService.uploadContent(originChapter.getStoreKey(), dto.getContent());
        ChapterEntity chapterEntity = chapterMapper.selectById(id);
        BeanUtils.copyProperties(entity, chapterEntity, "id", "createdAt");
        chapterEntity.setModifiedAt(LocalDateTime.now());
        chapterMapper.updateById(chapterEntity);
    }

    @Override
    public void updateChapterType(String id, Boolean needPay) {
        ChapterEntity chapter = this.findChapterById(id);
        chapter.setNeedPay(needPay);
        chapter.setModifiedAt(LocalDateTime.now());
        chapterMapper.updateById(chapter);
    }

    @Override
    public void deleteChapter(String id) {
        ChapterEntity chapter = findChapterById(id);
        try {
            fileService.delete(chapter.getStoreKey());
        } catch (Exception e) {
            log.error("delete file error", e);
        }
        chapterMapper.deleteById(id);
    }

    @Override
    public IPage<ChapterEntity> findChaptersInPaging(Page<ChapterEntity> page, String bookId) {
        return chapterMapper.findChaptersInPaging(page, bookId);
    }

    @Transactional
    @Override
    public void uploadContent(String bookId, UploadType uploadType, MultipartFile file) {
        BookEntity book = bookService.findBookById(bookId);
        List<ChapterEntity> chapterEntities = chapterMapper.selectList(Wrappers.<ChapterEntity>lambdaQuery()
                .eq(ChapterEntity::getBookId, bookId));
        int totalCount = 0;
        if (uploadType.equals(UploadType.NEW)) {
            if (!ObjectUtils.isEmpty(chapterEntities)) {
                List<String> keys = chapterEntities.stream()
                        .map(ChapterEntity::getStoreKey)
                        .collect(Collectors.toList());
                List<String> ids = chapterEntities.stream()
                        .map(BaseEntity::getId)
                        .collect(Collectors.toList());
                try {
                    fileService.deleteKeys(keys);
                } catch (Exception e) {
                    log.error("delete file error", e);
                }
                chapterMapper.deleteBatchIds(ids);
            }
        } else {
            totalCount = chapterEntities.size();
        }

        List<ChapterNameAndKeyDto> chapterNames = parseAndUploadFile(book.getBookName(), file, totalCount);
        for (int i = 0; i < chapterNames.size(); i++) {
//            String key = STORE_PATH + book.getBookName() + "/" + uuid + ".txt";
            int chapterIndex = i + 1 + totalCount;
            ChapterNameAndKeyDto nameAndKey = chapterNames.get(i);
            ChapterEntity chapterEntity = ChapterEntity.builder()
                    .bookId(bookId)
                    .chapterNumber(chapterIndex)
                    .chapterName(nameAndKey.getChapterName())
                    .storeKey(nameAndKey.getStoreKey())
                    .contentLink(cdnUrl + "/" + nameAndKey.getStoreKey())
                    .build();
            chapterMapper.insert(chapterEntity);
        }
    }

    @Override
    public String readChapter(String id) {
        ChapterEntity chapter = findChapterById(id);
        String contentLink = chapter.getContentLink();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(contentLink);
        uriComponentsBuilder.queryParam("random", RandomUtils.nextLong());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uriComponentsBuilder.toUriString(), String.class);
    }

    @Override
    public List<ChapterCountDto> findCategoryCountsByBookId(String[] bookIds) {
        return chapterMapper.findChapterCountByBooks(Arrays.asList(bookIds));
    }

    @Override
    public Integer getMaxChapterNumber(String bookId) {
        List<ChapterEntity> chapterEntities = chapterMapper.selectList(Wrappers.<ChapterEntity>lambdaQuery()
                .eq(ChapterEntity::getBookId, bookId)
                .orderByDesc(ChapterEntity::getChapterNumber));
        if (!ObjectUtils.isEmpty(chapterEntities)) {
            ChapterEntity chapterEntity = chapterEntities.get(0);
            return chapterEntity.getChapterNumber();
        }
        return 0;
    }

    private List<ChapterNameAndKeyDto> parseAndUploadFile(String bookName, MultipartFile file, int currentIndex) {
        List<String> keys = new ArrayList<>();
        List<String> chapterNames = new ArrayList<>();
        List<String> chapterContents = new ArrayList<>();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder chapterContent = new StringBuilder();
            String chapterName;
            String line;
            boolean pickChapterNameInNextLoop = true;
            int chapterIndex = currentIndex + 1;
            while ((line = br.readLine()) != null) {
                if (pickChapterNameInNextLoop) {
                    chapterName = line.trim();
                    if (ObjectUtils.isEmpty(chapterName)) {
                        pickChapterNameInNextLoop = true;
                    } else {
                        chapterNames.add(chapterName);
                        pickChapterNameInNextLoop = false;
                    }
                }
                // new chapter
                if (line.contains(DIVIDE_STRING)) {
                    UUID uuid = UUID.randomUUID();
                    String key = STORE_PATH + bookName + "/" + uuid + ".txt";
                    chapterContents.add(chapterContent.toString());
                    keys.add(key);
                    chapterIndex++;
                    // empty the StringBuilder for preparing store the next chapter content
                    chapterContent = new StringBuilder();
                    pickChapterNameInNextLoop = true;
                    continue;
                }
                chapterContent.append(line).append("\n");
            }
            chapterContents.add(chapterContent.toString());
            UUID uuid = UUID.randomUUID();
            String key = STORE_PATH + bookName + "/" + uuid + ".txt";
            keys.add(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileService.uploadContents(keys, chapterContents);
        List<ChapterNameAndKeyDto> chapterNameAndKeys = new ArrayList<>();
        for (int i = 0; i < chapterNames.size(); i++) {
            String chapterName = chapterNames.get(i);
            String key = keys.get(i);
            ChapterNameAndKeyDto chapterNameAndKeyDto = ChapterNameAndKeyDto.builder()
                    .chapterName(chapterName)
                    .storeKey(key).build();
            chapterNameAndKeys.add(chapterNameAndKeyDto);
        }
        return chapterNameAndKeys;
    }
}
