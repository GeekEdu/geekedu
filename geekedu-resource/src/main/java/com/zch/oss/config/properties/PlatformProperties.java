package com.zch.oss.config.properties;

import com.zch.oss.enums.Platform;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@Component
@ConfigurationProperties(prefix = "geekedu.platform")
public class PlatformProperties {

    private Platform file;

    private Platform media;

}
