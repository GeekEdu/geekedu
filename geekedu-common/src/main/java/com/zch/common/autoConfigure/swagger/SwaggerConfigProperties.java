package com.zch.common.autoConfigure.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2023/12/28
 */
@Data
@ConfigurationProperties(prefix = "geekedu.swagger")
public class SwaggerConfigProperties implements Serializable {

    private Boolean enable = false;

    private Boolean enableResponseWrap = false;

    public String packagePath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;

    public String version;

}
