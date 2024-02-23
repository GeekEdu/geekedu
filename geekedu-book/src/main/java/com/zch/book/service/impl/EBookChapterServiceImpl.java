package com.zch.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.book.domain.po.EBookChapter;
import com.zch.book.mapper.EBookChapterMapper;
import com.zch.book.service.IEBookChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EBookChapterServiceImpl extends ServiceImpl<EBookChapterMapper, EBookChapter> implements IEBookChapterService {
}
