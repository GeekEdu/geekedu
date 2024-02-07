package com.zch.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.oss.domain.po.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/4
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

    /**
     * 查询总条数
     * @return
     */
    long selectCount();

    /**
     * 分页查询所有图片
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<File> selectFileByPageCondition(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

}
