package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@Data
@TableName(value = "pc_config")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PcConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String webName;

    private String name;

    private Logo logo;

    private String url;

    private String pcUrl;

    private String wxUrl;

    private String icp;

    private String icpLink;

    private String icp2;

    private String icp2Link;

    private String userProtocol;

    private String userPrivateProtocol;

    private String aboutus;

    private Player player;

    private Member member;

    private Map<String, Integer> socialites;

    private Map<String, Integer> credit1Reward;

    private List<String> enabledAddons;

    private List<Sliders> sliders;

    private List<Navs> navs;

    private List<Links> links;

}
