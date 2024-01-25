package com.zch.label.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.label.domain.po.Tag;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.mapper.CategoryTagMapper;
import com.zch.label.mapper.TagMapper;
import com.zch.label.service.ITagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author Poison02
 * @date 2024/1/8
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final TagMapper tagMapper;

    private final CategoryMapper categoryMapper;

    private final CategoryTagMapper categoryTagMapper;

}
