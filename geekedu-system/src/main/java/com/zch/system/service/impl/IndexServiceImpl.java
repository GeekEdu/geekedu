package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.api.vo.path.LearnPathVO;
import com.zch.api.vo.system.index.BlockItemVO;
import com.zch.api.vo.system.index.BlockVO;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.system.domain.po.Block;
import com.zch.system.mapper.BlockMapper;
import com.zch.system.service.IIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zch.common.redis.constants.RedisConstants.FRONTED_INDEX_DATA_KEY;
import static com.zch.system.constants.BlockConstants.*;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IndexServiceImpl implements IIndexService {

    private final BlockMapper blockMapper;

    private final CourseFeignClient courseFeignClient;

    private final BookFeignClient bookFeignClient;

    @Override
    public List<BlockVO> getBlockList() {
        List<BlockVO> vo = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 先查缓存，再查数据库
        String object = RedisUtils.getCacheObject(FRONTED_INDEX_DATA_KEY);
        if (StringUtils.isNotBlank(object)) {
            try {
                vo = objectMapper.readValue(object, new TypeReference<List<BlockVO>>() {});
                return vo;
            } catch (Exception e) {
                log.error("首页数据反序列化失败！" + e);
            }
        }
        List<Block> blocks = blockMapper.selectList(new LambdaQueryWrapper<Block>());
        if (ObjectUtils.isNull(blocks) || CollUtils.isEmpty(blocks)) {
            return vo;
        }
        for (Block item : blocks) {
            BlockVO vo1 = new BlockVO<>();
            vo1.setId(item.getId());
            if (item.getSign().equals(PC_VOD_V1)) {
                vo1.setSign(PC_VOD_V1);
                String[] strs = item.getItemIds().split(",");
                BlockItemVO<CourseVO> course = new BlockItemVO<>();
                course.setTitle("推荐课程");
                List<CourseVO> items = course.getItems();
                for (String str : strs) {
                    items.add(courseFeignClient.getCourseById(Integer.valueOf(str)).getData());
                }
                course.setItems(items);
                vo1.setItems(course);
            } else if (item.getSign().equals(PC_LIVE_V1)) {
                vo1.setSign(PC_LIVE_V1);
                String[] strs = item.getItemIds().split(",");
                BlockItemVO<LiveCourseVO> live = new BlockItemVO<>();
                live.setTitle("连载直播");
                List<LiveCourseVO> items = live.getItems();
                for (String str : strs) {
                    items.add(courseFeignClient.getLiveCourseDetail(Integer.valueOf(str)).getData());
                }
                live.setItems(items);
                vo1.setItems(live);
            } else if (item.getSign().equals(PC_BOOK_V1)) {
                vo1.setSign(PC_BOOK_V1);
                String[] strs = item.getItemIds().split(",");
                BlockItemVO<EBookVO> book = new BlockItemVO<>();
                book.setTitle("电子书");
                List<EBookVO> items = book.getItems();
                for (String str : strs) {
                    items.add(bookFeignClient.getEBookById(Integer.valueOf(str)).getData());
                }
                book.setItems(items);
                vo1.setItems(book);
            } else if (item.getSign().equals(PC_TOPIC_V1)) {
                vo1.setSign(PC_TOPIC_V1);
                String[] strs = item.getItemIds().split(",");
                BlockItemVO<ImageTextVO> imageText = new BlockItemVO<>();
                imageText.setTitle("图文");
                List<ImageTextVO> items = imageText.getItems();
                for (String str : strs) {
                    items.add(bookFeignClient.getImageTextById(Integer.valueOf(str)).getData());
                }
                imageText.setItems(items);
                vo1.setItems(imageText);
            } else if (item.getSign().equals(PC_LEARN_PATH_V1)) {
                vo1.setSign(PC_LEARN_PATH_V1);
                String[] strs = item.getItemIds().split(",");
                BlockItemVO<LearnPathVO> path = new BlockItemVO<>();
                path.setTitle("学习路径");
                List<LearnPathVO> items = path.getItems();
                for (String str : strs) {
                    items.add(bookFeignClient.getPathDetail(Integer.valueOf(str)).getData());
                }
                path.setItems(items);
                vo1.setItems(path);
            }
            vo.add(vo1);
        }
        // 将vo序列化存入redis中
        try {
            String json = objectMapper.writeValueAsString(vo);
            RedisUtils.setCacheObject(FRONTED_INDEX_DATA_KEY, json);
        } catch (Exception e) {
            log.error("首页数据json序列化失败！" + e);
        }
        return vo;
    }
}
