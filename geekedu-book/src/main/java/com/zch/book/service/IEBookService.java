package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.EBookForm;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.book.comment.CommentVO;
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
