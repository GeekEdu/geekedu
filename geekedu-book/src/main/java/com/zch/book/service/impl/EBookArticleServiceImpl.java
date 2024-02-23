package com.zch.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.book.domain.po.EBookArticle;
import com.zch.book.mapper.EBookArticleMapper;
import com.zch.book.service.IEBookArticleService;
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
public class EBookArticleServiceImpl extends ServiceImpl<EBookArticleMapper, EBookArticle> implements IEBookArticleService {
}
