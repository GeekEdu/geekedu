package com.zch.label.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.label.CategoryForm;
import com.zch.common.core.utils.*;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.exception.DbException;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.po.CategoryTag;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryPageVO;
import com.zch.label.domain.vo.TagPageVO;
import com.zch.label.enums.CategoryEnum;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.mapper.CategoryTagMapper;
import com.zch.label.mapper.TagMapper;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zch.common.core.constants.ErrorInfo.Msg.*;


/**
 * @author Poison02
 * @date 2024/1/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
