package com.zch.api.vo.system.index;

import com.zch.common.mvc.entity.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockItemVO<T> extends BaseVO {

    private String title;

    private List<T> items = new ArrayList<>(0);

}
