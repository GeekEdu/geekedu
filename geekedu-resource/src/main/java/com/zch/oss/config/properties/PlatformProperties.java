package com.zch.oss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@ConfigurationProperties(prefix = "geekedu.platform")
public class PlatformProperties {

    private String file;

    private String media;

}
