package com.zch.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.seckill.CaptchaForm;
import com.zch.api.dto.trade.seckill.SecKillForm;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.pay.PayInfoVO;
import com.zch.api.vo.trade.seckill.SecKillV2VO;
import com.zch.api.vo.trade.seckill.SecondKillVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.result.Response;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.redis.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.trade.domain.po.SecondKill;
import com.zch.trade.enums.OrderStatusEnum;
import com.zch.trade.mapper.SecondKillMapper;
import com.zch.trade.service.ISecondKillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zch.common.core.constants.ErrorInfo.Msg.EXPIRE_CAPTCHA_CODE;
import static com.zch.common.core.constants.ErrorInfo.Msg.INVALID_VERIFY_CODE;
import static com.zch.common.redis.constants.RedisConstants.CAPTCHA_MAP;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SecondKillServiceImpl extends ServiceImpl<SecondKillMapper, SecondKill> implements ISecondKillService {

    private final TradeFeignClient tradeFeignClient;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public Page<SecondKillVO> querySecKillList(Integer pageNum, Integer pageSize, String sort, String order, String keywords) {
        Page<SecondKillVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<SecondKill> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(SecondKill::getGoodsTitle, keywords);
        }
        if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
            wrapper.orderBy(true, "asc".equals(order), SecondKill::getCreatedTime);
        }
        Page<SecondKill> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            SecondKillVO vo1 = new SecondKillVO();
            BeanUtils.copyProperties(item, vo1);
            // 看当前秒杀是否开始
            if (LocalDateTime.now().isBefore(item.getStartAt())) {
                vo1.setIsStart(false);
            } else {
                vo1.setIsStart(true);
            }
            // 看当前秒杀是否结束
            if (LocalDateTime.now().isAfter(item.getEndAt())) {
                vo1.setIsOver(true);
            } else {
                vo1.setIsOver(false);
            }
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(count);
        return vo;
    }

    @Override
    public SecondKillVO querySecKillDetail(Integer id) {
        SecondKill kill = getById(id);
        if (ObjectUtils.isNull(kill)) {
            return null;
        }
        SecondKillVO vo = new SecondKillVO();
        BeanUtils.copyProperties(kill, vo);
        // 看当前秒杀是否开始
        if (LocalDateTime.now().isBefore(kill.getStartAt())) {
            vo.setIsStart(false);
        } else {
            vo.setIsStart(true);
        }
        // 看当前秒杀是否结束
        if (LocalDateTime.now().isAfter(kill.getEndAt())) {
            vo.setIsOver(true);
        } else {
            vo.setIsOver(false);
        }
        return vo;
    }

    @Override
    public Boolean addSecKill(SecKillForm form) {
        SecondKill kill = new SecondKill();
        kill.setTotal(form.getTotal());
        kill.setGoodsCover(form.getGoodsCover());
        kill.setGoodsId(form.getGoodsId());
        kill.setGoodsTitle(form.getGoodsTitle());
        kill.setGoodsType(form.getGoodsType());
        kill.setOriginPrice(form.getOriginPrice());
        kill.setPrice(form.getPrice());
        kill.setStock(form.getStock());
        kill.setEndAt(form.getEndAt());
        kill.setStartAt(form.getStartAt());
        switch (kill.getGoodsType()) {
            case "course" -> kill.setGoodsTypeText("录播课程");
            case "live" -> kill.setGoodsTypeText("直播课程");
            case "book" -> kill.setGoodsTypeText("电子书");
        }
        return save(kill);
    }

    @Override
    public Boolean updateSecKill(Integer id, SecKillForm form) {
        SecondKill kill = getById(id);
        kill.setTotal(form.getTotal());
        kill.setGoodsCover(form.getGoodsCover());
        kill.setGoodsId(form.getGoodsId());
        kill.setGoodsTitle(form.getGoodsTitle());
        kill.setGoodsType(form.getGoodsType());
        kill.setOriginPrice(form.getOriginPrice());
        kill.setPrice(form.getPrice());
        kill.setStock(form.getStock());
        kill.setEndAt(form.getEndAt());
        kill.setStartAt(form.getStartAt());
        switch (kill.getGoodsType()) {
            case "course" -> kill.setGoodsTypeText("录播课程");
            case "live" -> kill.setGoodsTypeText("直播课程");
            case "book" -> kill.setGoodsTypeText("电子书");
        }
        return updateById(kill);
    }

    @Override
    public Boolean deleteSecKill(Integer id) {
        return removeById(id);
    }

    @Override
    public SecKillV2VO getV2Detail(Integer goodsId, String goodsType) {
        SecondKill secondKill = getOne(new LambdaQueryWrapper<SecondKill>()
                .eq(SecondKill::getGoodsId, goodsId)
                .eq(SecondKill::getGoodsType, goodsType)
                // 找出未过期的秒杀
                .gt(SecondKill::getEndAt, LocalDateTime.now())
                .last("limit 1"));
        if (ObjectUtils.isNull(secondKill)) {
            return null;
        }
        SecKillV2VO vo = new SecKillV2VO();
        SecondKillVO vo1 = new SecondKillVO();
        BeanUtils.copyProperties(secondKill, vo1);
        // 看当前秒杀是否开始
        if (LocalDateTime.now().isBefore(secondKill.getStartAt())) {
            vo1.setIsStart(false);
        } else {
            vo1.setIsStart(true);
        }
        // 看当前秒杀是否结束
        if (LocalDateTime.now().isAfter(secondKill.getEndAt())) {
            vo1.setIsOver(true);
        } else {
            vo1.setIsOver(false);
        }
        vo.setData(vo1);
        // 查询用户支付信息，若下了秒杀单，就会产生支付信息
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        Response<OrderVO> res = tradeFeignClient.queryOrderInfoByGoods(goodsId, goodsType, userId, true, null, null);
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
            return vo;
        }
        Response<PayInfoVO> res2 = tradeFeignClient.queryPayInfo(res.getData().getOrderId());
        if (ObjectUtils.isNotNull(res2) && ObjectUtils.isNotNull(res2.getData())) {
            if (! res2.getData().getIsPaid() && "未支付".equals(res.getData().getOrderStatusText())) {
                vo.setOrderId(res2.getData().getOrderId());
                vo.setOrderStatus(OrderStatusEnum.ORDERED_NO_PAY.getCode());
            }
        }
        return vo;
    }

    @Override
    public Integer startSecKill(Integer id, CaptchaForm form) {
        // 用户id
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        Map<String, String> cacheObject = RedisUtils.getCacheMap(CAPTCHA_MAP);
        if (ObjectUtils.isEmpty(cacheObject)) {
            throw new CommonException(EXPIRE_CAPTCHA_CODE);
        }
        // 校验 验证码是否相同
        boolean checkCaptcha = checkCaptcha(form.getCaptcha(), form.getCaptchaKey(), cacheObject);
        if (! checkCaptcha) {
            throw new CommonException(INVALID_VERIFY_CODE);
        }
        /**
         * 秒杀逻辑
         * 1. 判断是否在秒杀时间范围内
         * 2. 判断库存
         * 3. 做到一人一单，同一个用户只能下单一单
         * 4. 解决分布式集群问题，使用分布式锁
         * 5. 创建订单
         */
        SecondKill secondKill = getById(id);
        // 判断秒杀时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = secondKill.getStartAt();
        LocalDateTime end = secondKill.getEndAt();
        if (start.isAfter(now)) {
            throw new CommonException("秒杀还未开始！");
        }
        if (end.isBefore(now)) {
            throw new CommonException("秒杀已经结束！");
        }
        // 判断库存 使用CAS乐观锁机制控制并发问题
        if (secondKill.getStock() <= 0) {
            throw new CommonException("库存不足，请下次再来！");
        } else {
            // 扣减库存
            update(new LambdaUpdateWrapper<SecondKill>()
                    .eq(SecondKill::getId, id)
                    .gt(SecondKill::getStock, 0)
                    .set(SecondKill::getStock, secondKill.getStock() - 1));
        }
        // 确保一人一单，这里去查订单库，条件是：用户id，商品id，秒杀，创建时间在秒杀时间段内，最后一个条件很重要
        // 因为同一用户会下不同的订单，查出来的订单可能很多，那么只能限制在当前秒杀时间内
        Response<OrderVO> orderInfo = tradeFeignClient.queryOrderInfoByGoods(secondKill.getGoodsId(), secondKill.getGoodsType(), userId,
                true, start, end);
        if (ObjectUtils.isNotNull(orderInfo) && ObjectUtils.isNotNull(orderInfo.getData())) {
            throw new CommonException("禁止重复下单！");
        }
        // 创建订单，通过使用MQ通知
        /*CreateOrderForm formMsg = new CreateOrderForm();
        formMsg.setGoodsId(secondKill.getGoodsId());
        formMsg.setGoodsType(secondKill.getGoodsType());
        formMsg.setGoodsName(secondKill.getGoodsTitle());
        formMsg.setPromoCode("");
        formMsg.setPayment("alipay");
        formMsg.setIsSeckill(true);
        Message<CreateOrderForm> msg = MessageBuilder.withPayload(formMsg).build();
        rocketMQTemplate.asyncSend("seckill-2MySQL-topic", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.debug("秒杀成功，准备创建订单！");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("秒杀失败！，请重新秒杀！");
            }
        });*/
        return secondKill.getGoodsId();
    }

    /**
     * 校验验证码是否相同
     * @param imageCaptcha
     * @param imageKey
     * @param cacheObject
     * @return
     */
    private boolean checkCaptcha(String imageCaptcha, String imageKey, Map<String, String> cacheObject) {
        String redisCaptcha = cacheObject.get("code");
        String redisKey = cacheObject.get("key");
        if (com.zch.common.core.utils.StringUtils.isSameStringByUpperToLower(imageCaptcha, redisCaptcha)
                && Objects.equals(imageKey, redisKey)) {
            return true;
        }
        return false;
    }
}
