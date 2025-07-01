package com.douyin.workorder.enums;

/**
 * 操作类型枚举
 * 
 * @author douyin
 * @since 2023-07-01
 */
public enum OperationTypeEnum {

    SUBMIT(1, "提交"),
    FIRST_REVIEW_PASS(2, "初审通过"),
    FIRST_REVIEW_REJECT(3, "初审驳回"),
    SECOND_REVIEW_PASS(4, "二审通过"),
    SECOND_REVIEW_REJECT(5, "二审驳回"),
    DIRECTOR_APPROVAL_PASS(6, "总监审批通过"),
    DIRECTOR_APPROVAL_REJECT(7, "总监审批驳回"),
    SCRIPT_REVIEW_PASS(8, "脚本审核通过"),
    SCRIPT_REVIEW_REJECT(9, "脚本审核驳回"),
    VIDEO_REVIEW_PASS(10, "视频审核通过"),
    VIDEO_REVIEW_REJECT(11, "视频审核驳回"),
    UPDATE_WORK_ORDER(12, "修改工单"),
    WITHDRAW_WORK_ORDER(13, "撤回工单"),
    CANCEL_WORK_ORDER(14, "取消工单");

    private final Integer code;
    private final String desc;

    OperationTypeEnum(Integer code, String desc) {
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
     * 根据操作码获取枚举
     */
    public static OperationTypeEnum getByCode(Integer code) {
        for (OperationTypeEnum operation : values()) {
            if (operation.getCode().equals(code)) {
                return operation;
            }
        }
        return null;
    }
}