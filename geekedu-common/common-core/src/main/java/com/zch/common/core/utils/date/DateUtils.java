package com.zch.common.core.utils.date;

import cn.hutool.core.date.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Poison02
 * @date 2024/2/26
 */
public class DateUtils extends DateUtil {

    /**
     * 生成以今天为起点的前一周的时间数据 时间从小到大
     * @return
     */
    public static Map<String, Integer> generateLastWeek() {
        Map<String, Integer> orderSum = new TreeMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            String formattedDate = date.format(formatter);
            orderSum.put(formattedDate, 0);
        }

        return orderSum;
    }

}
