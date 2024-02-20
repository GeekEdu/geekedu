package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Answer;
import com.zch.ask.mapper.AnswerMapper;
import com.zch.ask.service.IAnswerService;
import com.zch.ask.service.ICommentsService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    private final AnswerMapper answerMapper;

    private final UserFeignClient userFeignClient;

    private final ICommentsService commentsService;

    @Override
    public List<AnswersVO> getAnswersByQuestionId(Integer id) {
        List<Answer> answers = answerMapper.selectList(new LambdaQueryWrapper<Answer>()
                .eq(Answer::getQuestionId, id));
        if (CollUtils.isEmpty(answers)) {
            return new ArrayList<>(0);
        }
        List<AnswersVO> vos = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            AnswersVO vo = new AnswersVO();
            BeanUtils.copyProperties(answer, vo);
            // 查询用户信息
            Response<UserSimpleVO> user = userFeignClient.getUserById(answer.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
            vo.setUser(user.getData());
            // 整理图片信息
            if (StringUtils.isNotBlank(answer.getImages())) {
                List<String> imageList = List.of(answer.getImages().split(","));
                vo.setImageList(imageList);
            }
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<CommentsVO> getCommentsByAnswerId(Integer id) {
        Answer answer = answerMapper.selectById(id);
        if (answer.getCommentCount() == 0) {
            return new ArrayList<>(0);
        }
        return commentsService.getCommentsByAnswerId(id);
    }

    @Override
    public Boolean deleteAnswerByAnswerId(Integer questionId, Integer answerId) {
        if (ObjectUtils.isNull(questionId) || ObjectUtils.isNull(answerId)) {
            return false;
        }
        return remove(new LambdaQueryWrapper<Answer>()
                .eq(Answer::getQuestionId, questionId)
                .eq(Answer::getId, answerId));
    }

    @Override
    public Boolean deleteCommentsById(Integer answerId, Integer commentsId, String type) {
        // 修改该回答的评论数，需要减1
        Answer answer = answerMapper.selectById(answerId);
        answer.setCommentCount(answer.getCommentCount() - 1);
        updateById(answer);
        return commentsService.deleteComments(commentsId, type);
    }

    @Override
    public Boolean setAnswerCorrectByAnswerId(Integer answerId) {
        Answer answer = answerMapper.selectById(answerId);
        answer.setIsCorrect(true);
        updateById(answer);
        return true;
    }


}
