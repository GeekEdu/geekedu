package com.zch.api.vo.exam.practice;

import com.zch.api.vo.exam.ChapterVO;
import com.zch.api.vo.exam.PracticeVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PracticeDetailVO extends BaseVO {

    private PracticeVO practice;

    private Boolean canDo = true;

    private List<ChapterVO> chapters = new ArrayList<>(0);

    // TODO
    private Map<String, Object> practice_user_record = new HashMap<>(0);
    private Map<String, Object> user_chapter_records = new HashMap<>(0);

}
