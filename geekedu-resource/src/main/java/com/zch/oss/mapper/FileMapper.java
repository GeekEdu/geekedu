package com.zch.oss.mapper;

// import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.oss.domain.po.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Poison02
 * @date 2024/1/4
 */
@Mapper
// public interface FileMapper extends BaseMapper<File> {
public interface FileMapper {

    int insertFileInfo(File fileInfo);

    File selectFileInfoById(Long id);

    int deleteFileInfo(Long id);

}
