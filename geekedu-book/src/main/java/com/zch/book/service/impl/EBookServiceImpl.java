package com.zch.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.book.domain.po.EBook;
import com.zch.book.mapper.EBookMapper;
import com.zch.book.service.IEBookService;
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
public class EBookServiceImpl extends ServiceImpl<EBookMapper, EBook> implements IEBookService {
}
