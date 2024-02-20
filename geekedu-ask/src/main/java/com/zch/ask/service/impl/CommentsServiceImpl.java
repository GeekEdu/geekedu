package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.ask.domain.po.Comments;
import com.zch.ask.enums.CommentsEnum;
import com.zch.ask.mapper.CommentsMapper;
import com.zch.ask.service.ICommentsService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
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
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

    private final CommentsMapper commentsMapper;

    private final UserFeignClient userFeignClient;

    @Override
    public List<CommentsVO> getCommentsByAnswerId(Integer id) {
        List<Comments> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getAnswerId, id));
        if (CollUtils.isEmpty(comments)) {
            return new ArrayList<>(0);
        }
        List<CommentsVO> vos = new ArrayList<>(comments.size());
        for (Comments comment: comments) {
            CommentsVO vo = new CommentsVO();
            BeanUtils.copyProperties(comment, vo);
            Response<UserSimpleVO> user = userFeignClient.getUserById(comment.getUserId() + "");
            if (ObjectUtils.isNull(user.getData())) {
                vo.setUser(null);
            }
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
}
