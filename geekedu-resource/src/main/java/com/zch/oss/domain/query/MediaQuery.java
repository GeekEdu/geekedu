package com.zch.oss.domain.query;


import com.zch.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MediaQuery extends PageQuery {

    private String mediaName;

}
