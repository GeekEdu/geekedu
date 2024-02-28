package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.user.TagVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.user.domain.po.Tag;
import com.zch.user.mapper.TagMapper;
import com.zch.user.service.ITagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final TagMapper tagMapper;

    @Override
    public List<TagVO> getTagList() {
        List<Tag> list = tagMapper.selectList(new LambdaQueryWrapper<Tag>());
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<TagVO> vos = list.stream().map(item -> {
            TagVO vo = new TagVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public TagVO getTagById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        Tag tag = tagMapper.selectById(id);
        if (ObjectUtils.isNull(tag)) {
            return null;
        }
        TagVO vo = new TagVO();
        BeanUtils.copyProperties(tag, vo);
        return vo;
    }
}
