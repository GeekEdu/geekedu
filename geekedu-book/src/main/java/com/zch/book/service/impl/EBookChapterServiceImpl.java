package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.book.EBookChapterVO;
import com.zch.book.domain.po.EBookChapter;
import com.zch.book.mapper.EBookChapterMapper;
import com.zch.book.service.IEBookChapterService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EBookChapterServiceImpl extends ServiceImpl<EBookChapterMapper, EBookChapter> implements IEBookChapterService {

    private final EBookChapterMapper chapterMapper;

    @Override
    public List<EBookChapterVO> getChapterListByBookId(Integer bookId) {
        if (ObjectUtils.isNull(bookId)) {
            return new ArrayList<>(0);
        }
        List<EBookChapter> list = chapterMapper.selectList(new LambdaQueryWrapper<EBookChapter>()
                .eq(EBookChapter::getBookId, bookId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<EBookChapterVO> result = list.stream().map(item -> {
            EBookChapterVO vo = new EBookChapterVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        if (ObjectUtils.isNull(result) || CollUtils.isEmpty(result)) {
            return new ArrayList<>(0);
        }
        return result;
    }

    @Override
    public EBookChapterVO getChapterById(Integer cId) {
        if (ObjectUtils.isNull(cId)) {
            return null;
        }
        EBookChapter chapter = getById(cId);
        EBookChapterVO vo = new EBookChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        return vo;
    }
}
