package com.zch.book.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zch.book.domain.dto.BookMSDTO;
import com.zch.book.domain.dto.TopicMSDTO;
import com.zch.book.domain.po.EBook;
import com.zch.book.domain.po.ImageText;
import com.zch.book.service.IEBookService;
import com.zch.book.service.IImageTextService;
import com.zch.book.service.MeiliSearchService;
import com.zch.common.core.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步录播课和直播课数据到MeiliSearch
 * @author Poison02
 * @date 2024/4/2
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SyncMeiliSearchHandler {

    private final MeiliSearchService meiliSearchService;

    private final IEBookService bookService;

    private final IImageTextService imageTextService;

    @XxlJob("syncMeiliSearchHandler")
    public void syncMeiliSearchHandler() {
        meiliSearchService.insertIndex("book", "id");
        meiliSearchService.insertIndex("topic", "id");
        List<EBook> course = bookService.list();
        for (EBook item : course) {
            BookMSDTO temp = new BookMSDTO();
            BeanUtils.copyProperties(item, temp);
            meiliSearchService.insertBookDocument(temp);
        }
        List<ImageText> live = imageTextService.list();
        for (ImageText item : live) {
            TopicMSDTO temp = new TopicMSDTO();
            BeanUtils.copyProperties(item, temp);
            meiliSearchService.insertTopicDocument(temp);
        }
    }

}
