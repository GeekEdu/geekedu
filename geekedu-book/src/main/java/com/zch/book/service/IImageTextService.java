package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.ImageTextForm;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextSimpleVO;
import com.zch.api.vo.book.ImageTextSingleVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.book.domain.po.ImageText;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/21
 */
public interface IImageTextService extends IService<ImageText> {

    /**
     * 条件分页查询图文列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    ImageTextAndCategoryVO getImageTextPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                       String keywords,
                                                       Integer categoryId);

    /**
     * 根据id获取图文信息
     * @param id
     * @return
     */
    ImageTextVO getImageTextById(Integer id);

    /**
     * 获取简单图文
     * @param id
     * @return
     */
    ImageTextSimpleVO getSimpleImageText(Integer id);

    /**
     * 根据id删除图文
     * @param id
     * @return
     */
    Boolean deleteImageTextById(Integer id);

    /**
     * 根据id更新图文
     * @param id
     * @param form
     * @return
     */
    Boolean updateImageTextById(Integer id, ImageTextForm form);

    Boolean insertImageText(ImageTextForm form);

    /**
     * 后台 返回分类列表
     * @return
     */
    List<CategoryVO> getCategoryList();

    /**
     * 后台 返回分类明细
     * @param categoryId
     * @return
     */
    CategorySimpleVO getCategoryDetail(Integer categoryId);

    /**
     * 后台 删除分类
     * @param categoryId
     * @return
     */
    Boolean deleteCategory(Integer categoryId);

    /**
     * 后台 更新分类
     * @param categoryId
     * @param form
     * @return
     */
    Boolean updateCategory(Integer categoryId, CategoryForm form);

    /**
     * 后台 新增分类
     * @param form
     * @return
     */
    Boolean addCategory(CategoryForm form);

    /**
     * 后台 获取图文评论列表，只需要查出来全部图文有关评论即可， 不需要划分等级评论
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    Page<BCommentVO> getCommentList(Integer pageNum, Integer pageSize, String cType, List<String> createdTime);

    /**
     * 根据id删除评论
     * @param commentId
     * @param cType
     * @return
     */
    Boolean deleteComment(Integer commentId, String cType);

    //=================================================================================
    /**
     * 前台 获取图文列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    ImageTextAndCategoryVO getImageTextList(Integer pageNum, Integer pageSize, String scene, Integer categoryId);

    /**
     * 获取图文明细
     * @param id
     * @return
     */
    ImageTextSingleVO getImageTextDetailById(Integer id);

    /**
     * 获取 图文评论列表
     * @param relationId
     * @param pageNum
     * @param pageSize
     * @param commentId
     * @return
     */
    Page<CommentVO> getImageTextCommentList(Integer relationId, Integer pageNum, Integer pageSize, Integer commentId);

    /**
     * 发表评论
     * @param id
     * @param form
     * @return
     */
    Integer addComment(Integer id, AddCommentForm form);

}
