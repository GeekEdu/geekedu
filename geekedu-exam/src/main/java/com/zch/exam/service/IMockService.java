package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.MockVO;
import com.zch.exam.domain.po.Mock;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface IMockService extends IService<Mock> {

    /**
     * 条件分页获取模拟考列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    Page<MockVO> getMockPage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId);

    /**
     * 根据id获取模拟考明细
     * @param id
     * @return
     */
    MockVO getMockById(Integer id);

    /**
     * 查找分类列表
     * @param ids
     * @return
     */
    List<CTagsVO> getTagList(List<Integer> ids);

}
