package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@Data
@TableName("navs")
public class Navs {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer sort;

    private String url;

    private String platform;

    private String activeRoutes;

    private Integer parentId;

    private Integer blank;

    private List<Integer> children = new ArrayList<>(0);

}
