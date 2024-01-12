package com.zch.common.domain.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@Data
public class PageResult<T> implements Serializable {

    private Integer code;

    private Data data;

    private String message;

    public static <T> PageResult<T> success(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCode(0);

        Data data = new Data<T>();
        data.setList(page.getRecords());
        data.setTotal(page.getTotal());

        result.setData(data);
        result.setMessage(ResponseCode.SUCCESS.getMessage());
        return result;
    }

    @lombok.Data
    public static class Data<T> {

        private List<T> list;

        private long total;

    }

}
