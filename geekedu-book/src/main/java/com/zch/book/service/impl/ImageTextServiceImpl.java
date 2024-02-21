package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.book.ImageTextForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.book.domain.po.ImageText;
import com.zch.book.mapper.ImageTextMapper;
import com.zch.book.service.IImageTextService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageTextServiceImpl extends ServiceImpl<ImageTextMapper, ImageText> implements IImageTextService {

    private final ImageTextMapper imageTextMapper;

    private final LabelFeignClient labelFeignClient;

    private static final String IMAGE_TEXT = "IMAGE_TEXT";

    @Override
    public ImageTextAndCategoryVO getImageTextPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                              String keywords,
                                                              Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        ImageTextAndCategoryVO vo = new ImageTextAndCategoryVO();
        // 查找所有符合的分类
        Response<List<CategorySimpleVO>> categoryList = labelFeignClient.getCategoryList(IMAGE_TEXT);
        if (ObjectUtils.isNull(categoryList) || ObjectUtils.isNull(categoryList.getData())) {
            vo.setCategories(new ArrayList<>(0));
        }
        vo.setCategories(categoryList.getData());
        // 查询图文数
        long count = count();
        if (count == 0) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
        }
        // 构造wrapper进行查询
        LambdaQueryWrapper<ImageText> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(ImageText::getTitle, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(ImageText::getCategoryId, categoryId);
        }
        // 排序 统一按照id
        wrapper.orderBy(true, "asc".equals(order), ImageText::getId);
        // 分页查
        Page<ImageText> page = page(new Page<ImageText>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
        }
        List<ImageText> records = page.getRecords();
        List<ImageTextVO> list = new ArrayList<>(records.size());
        for (ImageText item : records) {
            ImageTextVO vo1 = new ImageTextVO();
            BeanUtils.copyProperties(item, vo1);
            Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(item.getCategoryId(), IMAGE_TEXT);
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res.getData());
            list.add(vo1);
        }
        vo.getData().setTotal(count);
        vo.getData().setData(list);
        return vo;
    }

    @Override
    public ImageTextVO getImageTextById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new ImageTextVO();
        }
        ImageText imageText = getById(id);
        ImageTextVO vo = new ImageTextVO();
        BeanUtils.copyProperties(imageText, vo);
        return vo;
    }

    @Override
    public Boolean deleteImageTextById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean updateImageTextById(Integer id, ImageTextForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(id)) {
            return false;
        }
        // 先查询，若有则继续
        ImageText getOne = getById(id);
        if (ObjectUtils.isNull(getOne)) {
            return false;
        }
        // 用户id
        Long userId = UserContext.getLoginId();
        // 构造参数
        ImageText imageText = new ImageText();
        BeanUtils.copyProperties(form, imageText);
        imageText.setId(getOne.getId());
        imageText.setSellType(form.getIsFree());
        imageText.setPrice(new BigDecimal(form.getPrice()));
        imageText.setUpdatedBy(userId);
        // 更新操作
        return updateById(imageText);
    }

    @Override
    public Boolean insertImageText(ImageTextForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        //  先查询同意分类下标题是否有重复的
        LambdaQueryWrapper<ImageText> wrapper = new LambdaQueryWrapper<ImageText>()
                .eq(ImageText::getCategoryId, form.getCategoryId())
                .eq(ImageText::getTitle, form.getTitle())
                .eq(ImageText::getIsDelete, 0);
        ImageText one = getOne(wrapper);
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        // 用户id
        Long userId = UserContext.getLoginId();
        ImageText imageText = new ImageText();
        BeanUtils.copyProperties(form, imageText);
        // 注意 数据库定义是 0-免费 1-收费
        imageText.setSellType(! form.getIsFree());
        imageText.setPrice(new BigDecimal(form.getPrice()));
        imageText.setCreatedBy(userId);
        imageText.setUpdatedBy(userId);
        return save(imageText);
    }
}
