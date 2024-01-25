package com.zch.label.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.label.TagForm;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.TagPageVO;


/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ITagService extends IService<Tag> {

}
