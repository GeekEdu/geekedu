package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.QuestionDeleteBatchForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.api.vo.ask.QuestionVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Question;
import com.zch.ask.mapper.QuestionMapper;
import com.zch.ask.service.IAnswerService;
import com.zch.ask.service.IQuestionService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.mvc.utils.CommonServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final IAnswerService answerService;

    @Override
    public QuestionAndCategoryVO getQuestionPage(Integer pageNum, Integer pageSize, String sort, String order,
                                                 String keywords,
                                                 String userId,
                                                 Integer categoryId,
                                                 Integer status,
                                                 List<String> createdTimes) {
        QuestionAndCategoryVO vo = new QuestionAndCategoryVO();
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
                || ObjectUtils.isNull(sort) || ObjectUtils.isNull(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res1 = AddressUtils.getAddress(request);
        String ip = res1.get("ip");
        String province = res1.get("province");
        String browser = res1.get("browser");
        String os = res1.get("os");
        // 查询问题个数
        long count = count();
        if (count == 0) {
            vo.setCategories(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
        }
        // 构造查询条件
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        // 传入 keywords
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Question::getTitle, keywords);
        }
        if (StringUtils.isNoneBlank(userId)) {
            wrapper.eq(Question::getUserId, Double.valueOf(userId));
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(Question::getCategoryId, categoryId);
        }
        // 状态有四种情况 其中传空和-1都是全查 传0或1就是查指定的
        if (ObjectUtils.isNotEmpty(status)) {
            if (!Objects.equals(status, -1)) {
                wrapper.eq(Question::getQuestionStatus, status);
            }
        }
        // 存在时间区间
        if (ObjectUtils.isNotNull(createdTimes) && CollUtils.isNotEmpty(createdTimes) && createdTimes.size() > 1) {
            String start = createdTimes.get(0);
            String end = createdTimes.get(1);
            // 格式化时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startParse = LocalDate.parse(start, formatter);
            LocalDate endParse = LocalDate.parse(end, formatter);
            LocalDateTime startTime = LocalDateTime.of(startParse, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(endParse, LocalTime.MAX);
            wrapper.between(Question::getCreatedTime, startTime, endTime);
        }
        // 增加排序 前端固定使用 id 倒序 这里还是兼容一下排序方式 但排序字段暂时不变
        wrapper.orderBy(true, "asc".equals(order), Question::getId);
        // 增加分页
        Page<Question> page = page(new Page<Question>(pageNum, pageSize), wrapper);
        List<Question> list = page.getRecords();
        if (CollUtils.isEmpty(list)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        // 查询问题分类
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategoryList("ASK_QUESTION");
        if (res == null || res.getData() == null) {
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
            res2.getData().setIpAddress(ip);
            res2.getData().setProvince(province);
            res2.getData().setBrowser(browser);
            res2.getData().setOs(os);
            vo1.setUser(res2.getData());
            vo1.setStatusText(item.getQuestionStatus() ? "已解决" : "未解决");
            questions.add(vo1);
        }
        vo.setCategories(res.getData());
        vo.getData().setData(questions);
        vo.getData().setTotal(count);
        return vo;
    }

    @Transactional
    @Override
    public Boolean deleteQuestionBatchIds(QuestionDeleteBatchForm form) {
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form.getIds())) {
            return false;
        }
        return removeBatchByIds(form.getIds());
    }

    @Override
    public List<AnswersVO> getAnswersById(Integer id) {
        Question question = getById(id);
        if (question.getAnswerCount() == 0) {
            return new ArrayList<>(0);
        }
        return answerService.getAnswersByQuestionId(id);
    }

    @Override
    public Boolean deleteAnswerById(Integer questionId, Integer answerId) {
        // 修改该问题下的回答数
        Question question = questionMapper.selectById(questionId);
        question.setAnswerCount(question.getAnswerCount() - 1);
        // 查看此时要删除的回答是否是正确答案，若是，则更新该问题为未解决
        Boolean isCorrect = answerService.isCorrectAnswer(answerId);
        question.setQuestionStatus(!isCorrect);
        updateById(question);
        return answerService.deleteAnswerByAnswerId(questionId, answerId);
    }

    @Override
    public Boolean setCorrectAnswer(Integer questionId, Integer answerId) {
        // 设置问题状态为已解决
        Question question = questionMapper.selectById(questionId);
        question.setQuestionStatus(true);
        updateById(question);
        return answerService.setAnswerCorrectByAnswerId(answerId);
    }
}
