package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.exam.TagsVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.exam.domain.po.Tags;
import com.zch.exam.enums.ExamTagsEnum;
import com.zch.exam.mapper.TagsMapper;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    private final TagsMapper tagsMapper;

    @Override
    public List<TagsVO> getTagsList(String type) {
        List<Tags> tags = tagsMapper.selectList(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getType, ExamTagsEnum.valueOf(type)));
        if (ObjectUtils.isNotNull(tags) || CollUtils.isEmpty(tags)) {
            return new ArrayList<>(0);
        }
        List<TagsVO> vos = tags.stream().map(item -> {
            TagsVO vo = new TagsVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public TagsVO getTagByCondition(Integer id, String type) {
        Tags tags = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getId, id)
                .eq(Tags::getType, ExamTagsEnum.valueOf(type)));
        if (ObjectUtils.isNotNull(tags)) {
            return null;
        }
        TagsVO vo = new TagsVO();
        BeanUtils.copyProperties(tags, vo);
        return vo;
    }
}
