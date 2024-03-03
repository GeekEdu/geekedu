package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.CommentAnswerForm;
import com.zch.api.dto.ask.ReplyQuestionForm;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.ask.AnswerAndCommentsVO;
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
import com.zch.common.mvc.utils.CommonServletUtils;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        // 获取请求信息
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res1 = AddressUtils.getAddress(request);
        String ip = res1.get("ip");
        String province = res1.get("province");
        String browser = res1.get("browser");
        String os = res1.get("os");
        List<AnswersVO> vos = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            AnswersVO vo = new AnswersVO();
            BeanUtils.copyProperties(answer, vo);
            // 查询用户信息
            Response<UserSimpleVO> user = userFeignClient.getUserById(answer.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
            user.getData().setIpAddress(ip);
            user.getData().setProvince(province);
            user.getData().setBrowser(browser);
            user.getData().setOs(os);
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
    public List<AnswerAndCommentsVO> getAnswerAndComments(Integer id) {
        List<Answer> answers = answerMapper.selectList(new LambdaQueryWrapper<Answer>()
                .eq(Answer::getQuestionId, id));
        if (CollUtils.isEmpty(answers)) {
            return new ArrayList<>(0);
        }
        // 获取请求信息
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res1 = AddressUtils.getAddress(request);
        String ip = res1.get("ip");
        String province = res1.get("province");
        String browser = res1.get("browser");
        String os = res1.get("os");
        List<AnswerAndCommentsVO> vos = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            AnswerAndCommentsVO vo = new AnswerAndCommentsVO();
            BeanUtils.copyProperties(answer, vo);
            // 查询用户信息
            Response<UserSimpleVO> user = userFeignClient.getUserById(answer.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
            user.getData().setIpAddress(ip);
            user.getData().setProvince(province);
            user.getData().setBrowser(browser);
            user.getData().setOs(os);
            vo.setUser(user.getData());
            // 整理图片信息
            if (StringUtils.isNotBlank(answer.getImages())) {
                List<String> imageList = List.of(answer.getImages().split(","));
                vo.setImagesList(imageList);
            }
            // 评论
            List<CommentsVO> res = commentsService.getCommentsByAnswerId(answer.getId());
            if (ObjectUtils.isNull(res)) {
                vo.setComments(res);
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

    @Override
    public Boolean isCorrectAnswer(Integer answerId) {
        Answer answer = answerMapper.selectById(answerId);
        return answer.getIsCorrect();
    }

    @Override
    public Boolean replyQuestion(Integer id, ReplyQuestionForm form) {
//        Answer one = answerMapper.selectOne(new LambdaQueryWrapper<Answer>()
//                .eq(Answer::getQuestionId, id));
//        if (ObjectUtils.isNull(one)) {
//            return false;
//        }
        Long userId = UserContext.getLoginId();
        Answer answer = new Answer();
        answer.setContent(form.getContent());
        if (CollUtils.isNotEmpty(form.getImages())) {
            String join = String.join(",", form.getImages());
            answer.setImages(join);
        }
        answer.setQuestionId(id);
        answer.setCreatedBy(userId);
        answer.setUpdatedBy(userId);
        answer.setUserId(userId);
        return save(answer);
    }

    @Override
    public Boolean commentAnswer(Integer id, CommentAnswerForm form) {
        // 新增回答的评论数
        Answer answer = answerMapper.selectOne(new LambdaQueryWrapper<Answer>()
                .eq(Answer::getId, id)
                .eq(Answer::getUserId, form.getUserId()));
        if (ObjectUtils.isNull(answer)) {
            return false;
        }
        // 新增评论
        boolean isOk = commentsService.commentAnswer(id, form);
        if (isOk) {
            answer.setCommentCount(answer.getCommentCount() + 1);
            updateById(answer);
        }
        return isOk;
    }

    @Override
    public Page<CommentsVO> getCommentsPage(Integer id, Integer pageNum, Integer pageSize) {
        Answer answer = answerMapper.selectById(id);
        if (ObjectUtils.isNull(answer)) {
            return new Page<>();
        }
        return commentsService.getCommentsPage(id, pageNum, pageSize);
    }


}
