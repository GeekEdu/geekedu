package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.DelCommentBatchForm;
import com.zch.api.dto.book.EBookForm;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.dto.user.CollectForm;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.book.domain.po.EBook;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
public interface IEBookService extends IService<EBook> {

    /**
     * 条件分页查询电子书
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    EBookAndCategoryVO getEBookPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                               String keywords,
                                               Integer categoryId);

    /**
     * 返回章节列表
     * @param bookId
     * @return
     */
    List<EBookChapterVO> getChapterList(Integer bookId);

    /**
     * 条件分页查询文章列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param bookId
     * @param chapterId
     * @return
     */
    EBookArticleFullVO getArticlePage(Integer pageNum, Integer pageSize, String sort, String order,
                                      Integer bookId,
                                      Integer chapterId);

    /**
     * 新建电子书
     * @param form
     * @return
     */
    EBookVO insertEBook(EBookForm form);

    /**
     * 删除电子书
     * @param id
     * @return
     */
    Boolean deleteEBook(Integer id);

    /**
     * 更新电子书
     * @param id
     * @param form
     * @return
     */
    Boolean updateEBook(Integer id, EBookForm form);

    /**
     * 根据id获取电子书明细
     * @param id
     * @return
     */
    EBookVO getEBookById(Integer id);

    /**
     * 根据id获取电子书文章明细
     * @param id
     * @return
     */
    EBookArticleVO getEBookArticleById(Integer id);

    /**
     * 返回简单 电子书
     * @param id
     * @return
     */
    EBookSimpleVO getEBookSimple(Integer id);

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
     * 后台 获取电子书或者文章评论列表，只需要查出来全部图文有关评论即可， 不需要划分等级评论
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

    /**
     * 批量删除评论
     * @param form
     * @return
     */
    Boolean deleteCommentBatch(DelCommentBatchForm form);

    //==============================================================================================
    // 前台
    /**
     * 电子书列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    EBookAndCategoryVO getBookList(Integer pageNum, Integer pageSize, String scene, Integer categoryId);

    /**
     * 获取某电子书明细
     * @param id
     * @return
     */
    EBookFullVO getBookDetailById(Integer id);

    /**
     * 获取某电子书的评论列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    BCommentFullVO getBookComments(Integer id, Integer pageNum, Integer pageSize);

    /**
     * 判断是否收藏
     * @param bookId
     * @param type
     * @return
     */
    Boolean checkBookCollectionStatus(Integer bookId, String type);

    /**
     * 点击收藏或者不收藏
     * @param form
     * @return
     */
    Boolean hitBookCollectionIcon(CollectForm form);

    /**
     * 添加电子书 评论
     * @param bookId
     * @param form
     * @return
     */
    Integer addBookComment(Integer bookId, AddCommentForm form);

    /**
     * 阅读 电子书文章
     * @param articleId
     * @return
     */
    ArticleFullVO readArticle(Integer articleId);

    /**
     * 返回文章评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<CommentVO> getArticleComments(Integer articleId, Integer pageNum, Integer pageSize, Integer commentId);

    /**
     * 新增文章评论
     * @param articleId
     * @param form
     * @return
     */
    Integer addArticleComment(Integer articleId, AddCommentForm form);

}
