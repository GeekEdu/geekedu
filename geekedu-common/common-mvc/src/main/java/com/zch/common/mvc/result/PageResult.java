package com.zch.common.mvc.result;

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

    private Integer status;

    private Data<T> data;

    private String message;

    public static <T> PageResult<T> success(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setStatus(0);

        Data<T> data = new Data<T>();
        data.setData(page.getRecords());
        data.setTotal(page.getTotal());

        result.setData(data);
        result.setMessage(ResponseCode.SUCCESS.getMessage());
        return result;
    }

    @lombok.Data
    public static class Data<T> {

        private List<T> data;

        private long total;

    }

}
