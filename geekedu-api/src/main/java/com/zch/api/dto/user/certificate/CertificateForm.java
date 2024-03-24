package com.zch.api.dto.user.certificate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@Data
public class CertificateForm implements Serializable {

    private String name;

    private String params;

    private String templateImage;

    private List<Object> relation;

}
