package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.EBookArticleForm;
import com.zch.api.vo.book.EBookArticleFullVO;
import com.zch.api.vo.book.EBookArticleVO;
import com.zch.book.domain.po.EBookArticle;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
public interface IEBookArticleService extends IService<EBookArticle> {

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
    EBookArticleFullVO getArticlePageCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                               Integer bookId,
                                               Integer chapterId);

    /**
     * 根据id获取文章明细
     * @param id
     * @return
     */
    EBookArticleVO getEBookArticleById(Integer id);

    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    Boolean deleteArticleById(Integer id);

    /**
     * 新建文章
     * @return
     */
    Boolean addArticle(EBookArticleForm form);

    /**
     * 更新文章
     * @param id
     * @param form
     * @return
     */
    EBookArticleVO updateArticle(Integer id, EBookArticleForm form);

    /**
     * 查找文章列表
     * @param bookId
     * @param chapterId
     * @return
     */
    List<EBookArticleVO> getArticleList(Integer bookId, Integer chapterId);

    /**
     * 查找原本文章id列表，只根据电子书id
     * @param bookId
     * @return
     */
    List<Integer> getArticleIdList(Integer bookId);
}
