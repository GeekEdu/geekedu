package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.book.EBookArticleFullVO;
import com.zch.book.domain.po.EBookArticle;

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

}
