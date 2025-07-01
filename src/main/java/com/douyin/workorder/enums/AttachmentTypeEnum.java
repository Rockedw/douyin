package com.douyin.workorder.enums;

/**
 * 附件类型枚举
 * 
 * @author douyin
 * @since 2023-07-01
 */
public enum AttachmentTypeEnum {

    IMAGE(1, "图片"),
    VIDEO(2, "视频"),
    DOCUMENT(3, "文档"),
    OTHER(4, "其他");

    private final Integer code;
    private final String desc;

    AttachmentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据类型码获取枚举
     */
    public static AttachmentTypeEnum getByCode(Integer code) {
        for (AttachmentTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}