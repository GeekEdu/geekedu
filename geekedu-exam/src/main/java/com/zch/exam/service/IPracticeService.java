package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.PracticeVO;
import com.zch.exam.domain.po.Practice;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface IPracticeService extends IService<Practice> {

    /**
     * 条件分页查找练习列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    Page<PracticeVO> getPracticePage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId);

    /**
     * 根据id查找练习明细
     * @param id
     * @return
     */
    PracticeVO getPracticeById(Integer id);

    /**
     * 返回分类列表
     * @return
     */
    List<CTagsVO> getTagList();

}
