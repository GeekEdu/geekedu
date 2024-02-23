package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.book.EBookAndCategoryVO;
import com.zch.book.domain.po.EBook;

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

}
