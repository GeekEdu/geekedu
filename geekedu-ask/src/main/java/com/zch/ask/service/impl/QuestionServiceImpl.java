package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.api.vo.ask.QuestionVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Question;
import com.zch.ask.mapper.QuestionMapper;
import com.zch.ask.service.IQuestionService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    private final QuestionMapper questionMapper;

    private final LabelFeignClient labelFeignClient;

    private final UserFeignClient userFeignClient;

    @Override
    public QuestionAndCategoryVO getQuestionPage(Integer pageNum, Integer pageSize, String sort, String order,
                                                 String keywords, Long userId, Integer categoryId, Integer status) {
        QuestionAndCategoryVO vo = new QuestionAndCategoryVO();
        if (ObjectUtils.isNull(sort) || ObjectUtils.isNull(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        int page = (pageNum - 1) * pageSize;
        // 查询问题个数
        Long count = questionMapper.selectCount(new LambdaQueryWrapper<Question>()
                .eq(Question::getIsDelete, 0));
        if (count == 0) {
            vo.setCategories(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
        }
        // 查询问题分类
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategoryList("ASK_QUESTION");
        if (res == null || res.getData() == null) {
            vo.getData().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        // 查询问题
        Integer questionStatus = -1;
        if (! Objects.equals(status, -1)) {
            questionStatus = status;
        }
        List<Question> list = questionMapper.getQuestionPageByCondition(page, pageSize, sort, order, userId, categoryId, questionStatus, keywords);
        if (CollUtils.isEmpty(list)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        // 组装问题数据
        List<QuestionVO> questions = new ArrayList<>(list.size());
        for (Question item : list) {
            Response<CategorySimpleVO> response = labelFeignClient.getCategoryById(item.getCategoryId(), "ASK_QUESTION");
            Response<UserSimpleVO> res2 = userFeignClient.getUserById(item.getUserId() + "");
            if (response == null || response.getData() == null || res2 == null || res2.getData() == null) {
                vo.getData().setData(new ArrayList<>(0));
                vo.setCategories(new ArrayList<>(0));
                return vo;
            }
            CategorySimpleVO data = response.getData();
            QuestionVO vo1 = new QuestionVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setCategory(data);
            vo1.setUser(res2.getData());
            vo1.setStatusText(item.getQuestionStatus() ? "已解决" : "未解决");
            questions.add(vo1);
        }
        vo.setCategories(res.getData());
        vo.getData().setData(questions);
        vo.getData().setTotal(count);
        return vo;
    }
}
