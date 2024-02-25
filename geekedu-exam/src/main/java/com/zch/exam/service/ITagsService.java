package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.TagsVO;
import com.zch.exam.domain.po.Tags;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface ITagsService extends IService<Tags> {

    List<TagsVO> getTagsList(String type);

    TagsVO getTagByCondition(Integer id, String type);

}
