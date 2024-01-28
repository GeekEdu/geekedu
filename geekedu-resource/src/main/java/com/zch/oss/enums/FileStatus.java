package com.zch.oss.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.zch.common.mvc.exception.BadRequestException;
import lombok.Getter;

import static com.zch.oss.enums.FileErrorInfo.Msg.INVALID_FILE_STATUS;

@Getter
public enum FileStatus {
    UPLOADING(1, "上传中"),
    UPLOADED(2, "已上传"),
    PROCESSED(3, "已处理"),
    ;
    @EnumValue
    private final int value;
    private final String desc;

    FileStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static FileStatus of(int value) {
        switch (value) {
            case 1:
                return UPLOADING;
            case 2:
                return UPLOADED;
            case 3:
                return PROCESSED;
            default:
                throw new BadRequestException(INVALID_FILE_STATUS);
        }
    }
}
