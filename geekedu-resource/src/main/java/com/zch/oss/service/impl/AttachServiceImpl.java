package com.zch.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.vo.resources.AttachVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.oss.domain.po.Attach;
import com.zch.oss.mapper.AttachMapper;
import com.zch.oss.service.IAttachService;
import com.zch.oss.utils.MinioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AttachServiceImpl extends ServiceImpl<AttachMapper, Attach> implements IAttachService {

    private final MinioUtils minioUtils;

    private final TradeFeignClient tradeFeignClient;

    @Override
    public List<AttachVO> queryAttachList(Integer courseId) {
        List<Attach> list = list(new LambdaQueryWrapper<Attach>()
                .eq(Attach::getCourseId, courseId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list.stream().map(item -> {
            AttachVO vo = new AttachVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean uploadAttach(MultipartFile file, String name, Integer courseId) {
        // 构造对象
        Attach attach = new Attach();
        attach.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        attach.setCourseId(courseId);
        attach.setName(name);
        attach.setSize(file.getSize());
        // 调用util上传
        String path = minioUtils.upload(file);
        attach.setPath(path);
        return save(attach);
    }

    @Override
    public void downloadAttach(Integer id, String token, HttpServletResponse res) {
        // 查询文件信息
        Attach attach = getById(id);
//        // 根据token 查询用户信息
//        Long userId = (Long) StpUtil.getLoginIdByToken(token);
//        // 查询订单信息
//        Response<Boolean> response = tradeFeignClient.queryOrderIsPay(userId, attach.getCourseId(), "REPLAY_COURSE");
//        if (ObjectUtils.isNotNull(response) && ObjectUtils.isNotNull(response.getData()) && response.getData()) {
//            if (ObjectUtils.isNotNull(attach)) {
//                minioUtils.download(attach.getPath(), res);
//            }
//        }
        if (ObjectUtils.isNotNull(attach)) {
            minioUtils.download(attach.getPath(), res);
        }
    }

    @Override
    public Boolean deleteAttach(Integer id) {
        Attach attach = getById(id);
        minioUtils.remove(attach.getPath());
        return removeById(id);
    }
}
