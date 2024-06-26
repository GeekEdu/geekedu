package com.zch.ask.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentAnswerForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.ask.CommentsForm;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.ask.CommentsSimpleVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Comments;
import com.zch.ask.enums.CommentsEnum;
import com.zch.ask.mapper.CommentsMapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

    private final CommentsMapper commentsMapper;

    private final UserFeignClient userFeignClient;

    private final CourseFeignClient courseFeignClient;

    @Override
    public List<CommentsVO> getCommentsByAnswerId(Integer id) {
        List<Comments> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getAnswerId, id));
        if (CollUtils.isEmpty(comments)) {
            return new ArrayList<>(0);
        }
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res1 = AddressUtils.getAddress(request);
        String ip = res1.get("ip");
        String province = res1.get("province");
        String browser = res1.get("browser");
        String os = res1.get("os");
        List<CommentsVO> vos = new ArrayList<>(comments.size());
        for (Comments comment: comments) {
            CommentsVO vo = new CommentsVO();
            BeanUtils.copyProperties(comment, vo);
            Response<UserSimpleVO> user = userFeignClient.getUserById(comment.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
            user.getData().setIpAddress(ip);
            user.getData().setProvince(province);
            user.getData().setBrowser(browser);
            user.getData().setOs(os);
            vo.setCType(comment.getCType().getDesc());
            vo.setUser(user.getData());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public Boolean deleteComments(Integer id, String type) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return remove(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getId, id)
                .eq(Comments::getCType, CommentsEnum.valueOf(type)));
    }

    @Override
    public Page<CommentsVO> getCommentsPage(Integer pageNum, Integer pageSize, String cType, List<String> createdTime) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        Page<CommentsVO> response = new Page<>();
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res1 = AddressUtils.getAddress(request);
        String ip = res1.get("ip");
        String province = res1.get("province");
        String browser = res1.get("browser");
        String os = res1.get("os");
        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(cType)) {
            wrapper.eq(Comments::getCType, CommentsEnum.valueOf(cType));
        }
        // 时间处理
        if (ObjectUtils.isNotNull(createdTime) && CollUtils.isNotEmpty(createdTime) && createdTime.size() > 1) {
            List<LocalDateTime> times = timeHandle(createdTime);
            // 增加条件
            wrapper.between(Comments::getCreatedTime, times.get(0), times.get(1));
        }
        Page<Comments> page = page(new Page<Comments>(pageNum, pageSize), wrapper);
        List<Comments> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return new Page<>();
        }
        List<CommentsVO> list = new ArrayList<>(records.size());
        for (Comments comment : records) {
            CommentsVO vo = new CommentsVO();
            BeanUtils.copyProperties(comment, vo);
            // 查询用户信息
            Response<UserSimpleVO> user = userFeignClient.getUserById(comment.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
            user.getData().setIpAddress(ip);
            user.getData().setProvince(province);
            user.getData().setBrowser(browser);
            user.getData().setOs(os);
            vo.setUser(user.getData());
            // 判断类型，根据类型查找对应信息
            if (StringUtils.isNotBlank(cType)) {
                switch (cType) {
                    case "REPLAY_COURSE":
                        Response<CourseSimpleVO> res = courseFeignClient.getCourseSimpleById(comment.getRelationId());
                        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                            vo.setCourse(null);
                        }
                        vo.setCourse(res.getData());
                        break;
                }
            }
            list.add(vo);
        }
        BeanUtils.copyProperties(page, response);
        response.setRecords(list);
        return response;
    }

    @Override
    public Boolean deleteCommentsBatch(CommentsBatchDelForm form) {
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form.getIds()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        for (Integer id : form.getIds()) {
            remove(new LambdaQueryWrapper<Comments>()
                    .eq(Comments::getId, id)
                    .eq(Comments::getCType, CommentsEnum.valueOf(form.getType()))
                    .eq(Comments::getIsDelete, 0));
        }
        return true;
    }

    @Override
    public void insertComments(CommentsForm form) {

    }

    @Override
    public Integer commentAnswer(Integer id, CommentAnswerForm form) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        Comments comments = new Comments();
        comments.setAnswerId(id);
        comments.setCType(CommentsEnum.ASK_QUESTION);
        comments.setContent(form.getContent());
        comments.setUserId(userId);
        comments.setCreatedBy(userId);
        comments.setUpdatedBy(userId);
        save(comments);
        Comments one = commentsMapper.selectOne(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getCType, CommentsEnum.ASK_QUESTION)
                .eq(Comments::getAnswerId, id)
                .eq(Comments::getUserId, userId)
                .orderByDesc(Comments::getCreatedTime)
                .last(" limit 1"));
        return one.getId();
    }

    @Override
    public Page<CommentsVO> getCommentsPage(Integer id, Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        Page<CommentsVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        Page<Comments> page = page(new Page<Comments>(pageNum, pageSize), new LambdaQueryWrapper<Comments>()
                .eq(Comments::getAnswerId, id));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
        }
        List<Comments> records = page.getRecords();
        List<CommentsVO> list = records.stream().map(item -> {
            CommentsVO vo1 = new CommentsVO();
            BeanUtils.copyProperties(item, vo1);
            // 查询用户信息
            Response<UserSimpleVO> user = userFeignClient.getUserById(item.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo1.setUser(null);
            }
            vo1.setUser(user.getData());
            return vo1;
        }).collect(Collectors.toList());
        vo.setRecords(list);
        vo.setTotal(count);
        return vo;
    }

    @Override
    public CommentsFullVO getCommentsList(Integer id, String cType, Integer pageNum, Integer pageSize) {
        CommentsFullVO vo = new CommentsFullVO();
        List<Comments> cList = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getRelationId, id)
                .eq(Comments::getCType, CommentsEnum.valueOf(cType)));
        if (ObjectUtils.isNull(cList) || CollUtils.isEmpty(cList)) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        Page<Comments> page = page(new Page<Comments>(pageNum, pageSize), new LambdaQueryWrapper<Comments>()
                .eq(Comments::getRelationId, id)
                .eq(Comments::getCType, CommentsEnum.valueOf(cType)));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<CommentsSimpleVO> list = new ArrayList<>(page.getRecords().size());
        Map<Long, UserSimpleVO> map = new HashMap<>(0);
        for (Comments item : page.getRecords()) {
            CommentsSimpleVO vo1 = new CommentsSimpleVO();
            BeanUtils.copyProperties(item, vo1);
            list.add(vo1);
            Response<UserSimpleVO> user = userFeignClient.getUserById(item.getUserId() + "");
            if (ObjectUtils.isNull(user) || ObjectUtils.isNull(user.getData())) {
                vo.setUsers(new HashMap<>(0));
            }
            map.put(item.getUserId(), user.getData());
        }
        vo.getData().setData(list);
        vo.getData().setTotal(page.getTotal());
        vo.setUsers(map);
        return vo;
    }

    @Override
    public Boolean addComment(Integer relationId, String cType, AddCommentForm form) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        Comments comments = new Comments();
//        comments.setUserId(userId);
        comments.setUserId(userId);
        comments.setCType(CommentsEnum.valueOf(cType));
        comments.setContent(form.getContent());
        comments.setRelationId(relationId);
        comments.setCreatedBy(userId);
        comments.setCreatedBy(userId);
        return save(comments);
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
