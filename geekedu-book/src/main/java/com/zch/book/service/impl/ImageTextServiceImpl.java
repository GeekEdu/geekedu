package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.ImageTextForm;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.dto.user.CollectForm;
import com.zch.api.dto.user.ThumbForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextSimpleVO;
import com.zch.api.vo.book.ImageTextSingleVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.book.domain.po.ImageText;
import com.zch.book.mapper.ImageTextMapper;
import com.zch.book.service.IBCommentService;
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
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final IBCommentService commentService;

    private final UserFeignClient userFeignClient;

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
        Response<List<CategorySimpleVO>> categoryList = labelFeignClient.getCategorySimpleList(IMAGE_TEXT);
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
        vo.setCategory(labelFeignClient.getCategoryById(imageText.getCategoryId(), "IMAGE_TEXT").getData());
        return vo;
    }

    @Override
    public ImageTextSimpleVO getSimpleImageText(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new ImageTextSimpleVO();
        }
        ImageText imageText = getById(id);
        ImageTextSimpleVO vo = new ImageTextSimpleVO();
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

    @Override
    public List<CategoryVO> getCategoryList() {
        Response<List<CategoryVO>> res = labelFeignClient.getCategoryList("IMAGE_TEXT");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData()) || CollUtils.isEmpty(res.getData())) {
            return new ArrayList<>(0);
        }
        return res.getData();
    }

    @Override
    public CategorySimpleVO getCategoryDetail(Integer categoryId) {
        Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(categoryId, "IMAGE_TEXT");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
            return new CategorySimpleVO();
        }
        return res.getData();
    }

    @Override
    public Boolean deleteCategory(Integer categoryId) {
        return labelFeignClient.deleteCategoryById(categoryId).getData();
    }

    @Override
    public Boolean updateCategory(Integer categoryId, CategoryForm form) {
        return labelFeignClient.updateCategoryById(categoryId, form).getData();
    }

    @Override
    public Boolean addCategory(CategoryForm form) {
        return labelFeignClient.addCategory(form).getData();
    }

    @Override
    public Page<BCommentVO> getCommentList(Integer pageNum, Integer pageSize, String cType, List<String> createdTime) {
        Page<BCommentVO> page = commentService.getBackendComments(pageNum, pageSize, cType, createdTime);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }
        List<BCommentVO> list = page.getRecords().stream().map(item -> {
            // 判断类型，根据类型查找对应信息
            if (StringUtils.isNotBlank(cType)) {
                if (cType.equals("IMAGE_TEXT")) {
                    ImageTextSimpleVO text = getSimpleImageText(item.getRelationId());
                    if (ObjectUtils.isNotNull(text)) {
                        item.setImageText(text);
                    }
                }
            }
            return item;
        }).collect(Collectors.toList());
        page.setRecords(list);
        return page;
    }

    @Override
    public Boolean deleteComment(Integer commentId, String cType) {
        return commentService.deleteCommentById(commentId, cType);
    }

    @Override
    public ImageTextAndCategoryVO getImageTextList(Integer pageNum, Integer pageSize, String scene, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(scene) || ObjectUtils.isNull(categoryId)) {
            pageNum = 1;
            pageSize = 10;
            scene = "default";
            categoryId = 0;
        }
        ImageTextAndCategoryVO vo = new ImageTextAndCategoryVO();
        // 查询图文分类
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategorySimpleList("IMAGE_TEXT");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData()) || CollUtils.isEmpty(res.getData())) {
            vo.setCategories(new ArrayList<>(0));
        }
        vo.setCategories(res.getData());
        long count = count();
        if (count == 0) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<ImageText> wrapper = new LambdaQueryWrapper<>();
        if (! Objects.equals(categoryId, 0)) {
            wrapper.eq(ImageText::getCategoryId, categoryId);
        }
        if (StringUtils.isNotBlank(scene)) {
            wrapper.orderBy(true, false, ImageText::getId);
        }
        Page<ImageText> page = page(new Page<ImageText>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<ImageText> records = page.getRecords();
        List<ImageTextVO> list = new ArrayList<>(records.size());
        list = records.stream().map(item -> {
            ImageTextVO vo1 = new ImageTextVO();
            BeanUtils.copyProperties(item, vo1);
            Response<CategorySimpleVO> res2 = labelFeignClient.getCategoryById(item.getCategoryId(), "IMAGE_TEXT");
            if (ObjectUtils.isNull(res2) || ObjectUtils.isNull(res2.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res2.getData());
            // 查询图文点赞和收藏数量
            Response<Long> thumbCount = userFeignClient.queryCount(item.getId(), "IMAGE_TEXT");
            Response<Long> collectionCount = userFeignClient.collectionCount(item.getId(), "IMAGE_TEXT");
            if (ObjectUtils.isNotNull(thumbCount) && ObjectUtils.isNotNull(thumbCount.getData())
                && ObjectUtils.isNotNull(collectionCount) && ObjectUtils.isNotNull(collectionCount.getData())) {
                vo1.setThumbCount(thumbCount.getData());
                vo1.setCollectCount(collectionCount.getData());
            }
            return vo1;
        }).collect(Collectors.toList());
        vo.getData().setData(list);
        vo.getData().setTotal(count);
        return vo;
    }

    @Override
    public ImageTextSingleVO getImageTextDetailById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new ImageTextSingleVO();
        }
        ImageText one = imageTextMapper.selectById(id);
        if (ObjectUtils.isNull(one)) {
            return new ImageTextSingleVO();
        }
        ImageTextVO vo1 = new ImageTextVO();
        BeanUtils.copyProperties(one, vo1);
        ImageTextSingleVO vo = new ImageTextSingleVO();
        // 查询图文点赞信息
        Response<Boolean> isThumb = userFeignClient.queryIsVote(id, "IMAGE_TEXT");
        Response<Long> thumbCount = userFeignClient.queryCount(id, "IMAGE_TEXT");
        if (ObjectUtils.isNotNull(isThumb) && ObjectUtils.isNotNull(isThumb.getData()) ) {
            vo.setIsThumb(isThumb.getData());
        }
        if (ObjectUtils.isNotNull(thumbCount) && ObjectUtils.isNotNull(thumbCount.getData())) {
            vo1.setThumbCount(thumbCount.getData());
        }
        // 查询图文收藏信息
        Response<Boolean> isCollect = userFeignClient.checkCollectStatus(id, "IMAGE_TEXT");
        Response<Long> collectionCount = userFeignClient.collectionCount(id, "IMAGE_TEXT");
        if (ObjectUtils.isNotNull(isCollect) && ObjectUtils.isNotNull(isCollect.getData()) ) {
            vo.setIsCollect(isCollect.getData());
        }
        if (ObjectUtils.isNotNull(collectionCount) && ObjectUtils.isNotNull(collectionCount.getData())) {
            vo1.setCollectCount(collectionCount.getData());
        }
        vo.setImageText(vo1);
        // 更新阅读数
        one.setReadCount(one.getReadCount() + 1);
        updateById(one);
        return vo;
    }

    @Override
    public Page<CommentVO> getImageTextCommentList(Integer relationId, Integer pageNum, Integer pageSize, Integer commentId) {
        if (ObjectUtils.isNull(relationId)) {
            return new Page<>();
        }
        ImageText imageText = imageTextMapper.selectById(relationId);
        if (ObjectUtils.isNull(imageText)) {
            return new Page<>();
        }
        return commentService.getCommentPage(relationId, pageNum, pageSize, commentId, "IMAGE_TEXT");
    }

    @Override
    public Integer addComment(Integer id, AddCommentForm form) {
        if (ObjectUtils.isNull(id)) {
            return 0;
        }
        ImageText imageText = imageTextMapper.selectById(id);
        if (ObjectUtils.isNull(imageText)) {
            return 0;
        }
        return commentService.addComment(id, form, "IMAGE_TEXT");
    }

    @Override
    public Boolean thumb(ThumbForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getId()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        return userFeignClient.thumbHandle(form).getData();
    }

    @Override
    public Boolean collect(CollectForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getId()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        return userFeignClient.hitCollectIcon(form).getData();
    }
}
