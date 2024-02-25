package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.QuestionsVO;
import com.zch.exam.domain.po.Questions;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface IQuestionsService extends IService<Questions> {

    QuestionsVO getQuestionsById(Integer id);

}
