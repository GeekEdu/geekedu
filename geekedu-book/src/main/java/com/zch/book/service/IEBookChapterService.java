package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.book.EBookChapterVO;
import com.zch.book.domain.po.EBookChapter;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
public interface IEBookChapterService extends IService<EBookChapter> {

    /**
     * 查询章节列表
     * @param bookId
     * @return
     */
    List<EBookChapterVO> getChapterListByBookId(Integer bookId);

    /**
     * 根据id查询章节
     * @param cId
     * @return
     */
    EBookChapterVO getChapterById(Integer cId);

}
