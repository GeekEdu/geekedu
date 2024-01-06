package com.zch.oss.enums;

// import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum FilePlatform {
    TENCENT(1, "/img-tx/"),
    ALI(2, "/img-ali/"),
    ;
    // @EnumValue
    int value;
    String path;

    FilePlatform(int value, String path) {
        this.value = value;
        this.path = path;
    }

    public static String returnPath(int value) {
        for (FilePlatform platform : FilePlatform.values()) {
            if (platform.getValue() == value) {
                return platform.getPath();
            }
        }
        return null;
    }
}
