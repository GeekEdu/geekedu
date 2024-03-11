package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.QuestionDeleteBatchForm;
import com.zch.api.dto.ask.QuestionForm;
import com.zch.api.dto.ask.ReplyQuestionForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.ask.*;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Question;
import com.zch.ask.mapper.QuestionMapper;
import com.zch.ask.service.IAnswerService;
import com.zch.ask.service.IQuestionService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.mvc.utils.CommonServletUtils;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

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
                                                 List<String> createdTime) {
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
        if (StringUtils.isNotBlank(userId)) {
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
        if (ObjectUtils.isNotNull(createdTime) && CollUtils.isNotEmpty(createdTime) && createdTime.size() > 1) {
            List<LocalDateTime> times = timeHandle(createdTime);
            wrapper.between(Question::getCreatedTime, times.get(0), times.get(1));
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
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategorySimpleList("ASK_QUESTION");
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

    @Override
    public QuestionAndCategoryVO getV2Questions(Integer pageNum, Integer pageSize, String scene, Integer categoryId) {
        QuestionAndCategoryVO vo = new QuestionAndCategoryVO();
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize) || StringUtils.isBlank(scene)) {
            pageNum = 1;
            pageSize = 10;
            scene = "default";
        }
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategorySimpleList("ASK_QUESTION");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData()) || CollUtils.isEmpty(res.getData())) {
            vo.setCategories(new ArrayList<>(0));
        }
        vo.setCategories(res.getData());
        long count = count();
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            return vo;
        }
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        if (! Objects.equals(categoryId, 0)) {
            wrapper.eq(Question::getCategoryId, categoryId);
        }
        if (StringUtils.isNotBlank(scene)) {
            if ("default".equals(scene)) {
                // 默认 全部都要 这里为按照id升序
                wrapper.orderBy(true, true, Question::getId);
            } else if ("solved".equals(scene)) {
                // 已解决
                wrapper.eq(Question::getQuestionStatus, true);
            } else if ("unsolved".equals(scene)) {
                // 未解决
                wrapper.eq(Question::getQuestionStatus, false);
            } else if ("last_answer".equals(scene)) {
                // 最新回答
                wrapper.orderBy(true, false, Question::getCreatedTime);
            }
        }
        Page<Question> page = page(new Page<Question>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            return vo;
        }
        List<Question> records = page.getRecords();
        List<QuestionVO> list = records.stream().map(item -> {
            QuestionVO vo1 = new QuestionVO();
            BeanUtils.copyProperties(item, vo1);
            Response<CategorySimpleVO> res2 = labelFeignClient.getCategoryById(item.getCategoryId(), "ASK_QUESTION");
            if (ObjectUtils.isNull(res2) || ObjectUtils.isNull(res2.getData())) {
                vo1.setCategory(res2.getData());
            }
            Response<UserSimpleVO> res3 = userFeignClient.getUserById(item.getUserId() + "");
            if (ObjectUtils.isNull(res3) || ObjectUtils.isNull(res3.getData())) {
                vo1.setUser(res3.getData());
            }
            // 状态文本
            vo1.setStatusText(item.getQuestionStatus() ? "已解决" : "未解决");
            // 图片集合
            if (StringUtils.isNotBlank(item.getImages())) {
                String[] split = item.getImages().split(",");
                vo1.setImagesList(List.of(split));
            }
            return vo1;
        }).collect(Collectors.toList());
        vo.getData().setData(list);
        vo.getData().setTotal(count);
        return vo;
    }

    @Override
    public List<CategorySimpleVO> getTagList() {
        Response<List<CategorySimpleVO>> response = labelFeignClient.getCategorySimpleList("ASK_QUESTION");
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData()) || CollUtils.isEmpty(response.getData())) {
            return new ArrayList<>(0);
        }
        return response.getData();
    }

    @Override
    public QuestionFullVO getQuestionDetail(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        Question question = questionMapper.selectById(id);
        if (ObjectUtils.isNull(question)) {
            return null;
        }
        QuestionFullVO vo = new QuestionFullVO();
        // 查看当前用户是否是该 question 的 Master
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        if (Objects.equals(userId, question.getUserId())) {
            vo.setIsMaster(true);
        }
        QuestionVO vo1 = vo.getQuestion();
        BeanUtils.copyProperties(question, vo1);
        Response<CategorySimpleVO> res2 = labelFeignClient.getCategoryById(question.getCategoryId(), "ASK_QUESTION");
        if (ObjectUtils.isNull(res2) || ObjectUtils.isNull(res2.getData())) {
            vo1.setCategory(res2.getData());
        }
        Response<UserSimpleVO> res3 = userFeignClient.getUserById(question.getUserId() + "");
        if (ObjectUtils.isNull(res3) || ObjectUtils.isNull(res3.getData())) {
            vo1.setUser(res3.getData());
        }
        // 状态文本
        vo1.setStatusText(question.getQuestionStatus() ? "已解决" : "未解决");
        // 图片集合
        if (StringUtils.isNotBlank(question.getImages())) {
            String[] split = question.getImages().split(",");
            vo1.setImagesList(List.of(split));
        }
        vo.setQuestion(vo1);
        // 查找回答
        List<AnswerAndCommentsVO> res = answerService.getAnswerAndComments(question.getId());
        if (ObjectUtils.isNull(res) || CollUtils.isEmpty(res)) {
            vo.setAnswer(new ArrayList<>(0));
        }
        vo.setAnswer(res);
        return vo;
    }

    @Override
    public Integer addQuestion(QuestionForm form) {
        if (ObjectUtils.isNull(form)) {
            return 0;
        }
        Long userId = UserContext.getLoginId();
        Question question = new Question();
        Question one = questionMapper.selectOne(new LambdaQueryWrapper<Question>()
                .eq(Question::getTitle, form.getTitle())
                .eq(Question::getCategoryId, form.getCategoryId()));
        if (ObjectUtils.isNotNull(one)) {
            return 0;
        }
        question.setTitle(form.getTitle());
        question.setCategoryId(form.getCategoryId());
        question.setContent(form.getContent());
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form.getImages())) {
            question.setImages("");
        }
        String join = String.join(",", form.getImages());
        question.setImages(join);
        question.setRewardScore(form.getRewardScore() == null ? 0 : form.getRewardScore());
        question.setUserId(userId);
        question.setCreatedBy(userId);
        question.setUpdatedBy(userId);
        save(question);
        Question res = questionMapper.selectOne(new LambdaQueryWrapper<Question>()
                .eq(Question::getTitle, form.getTitle())
                .eq(Question::getCategoryId, form.getCategoryId()));
        return res.getId();
    }

    @Override
    public Boolean replyQuestion(Integer id, ReplyQuestionForm form) {
        // 增加问题的回答数
        Question question = questionMapper.selectById(id);
        if (ObjectUtils.isNull(question)) {
            return false;
        }
        // 新增回答
        boolean isOk = answerService.replyQuestion(id, form);
        if (isOk) {
            question.setAnswerCount(question.getAnswerCount() + 1);
            // 更新问题
            updateById(question);
        }
        return isOk;
    }

    @Override
    public Page<QuestionVO> getUsersQuestionList(Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        Page<QuestionVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        // 当前用户id
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        Page<Question> page = page(new Page<Question>(pageNum, pageSize), new LambdaQueryWrapper<Question>()
                .eq(Question::getUserId, userId));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            QuestionVO vo1 = new QuestionVO();
            BeanUtils.copyProperties(item, vo1);
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(page.getTotal());
        return vo;
    }

    /**
     * 对时间的处理
     * @param time
     * @return
     */
    public static List<LocalDateTime> timeHandle(List<String> time) {
        List<LocalDateTime> res = new ArrayList<>(2);
        String start = time.get(0);
        String end = time.get(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        res.add(startDateTime);
        res.add(endDateTime);
        return res;
    }

}
