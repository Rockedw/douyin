package com.douyin.workorder.enums;

/**
 * 工单状态枚举
 * 
 * @author douyin
 * @since 2023-07-01
 */
public enum WorkOrderStatusEnum {

    DRAFT(0, "草稿"),
    PENDING_FIRST_REVIEW(1, "待初审"),
    FIRST_REVIEW_PASSED(2, "初审通过"),
    FIRST_REVIEW_REJECTED(3, "初审驳回"),
    PENDING_SECOND_REVIEW(4, "待二审"),
    SECOND_REVIEW_PASSED(5, "二审通过"),
    SECOND_REVIEW_REJECTED(6, "二审驳回"),
    PENDING_DIRECTOR_APPROVAL(7, "待总监审批"),
    DIRECTOR_APPROVAL_PASSED(8, "总监审批通过"),
    DIRECTOR_APPROVAL_REJECTED(9, "总监审批驳回"),
    PENDING_SCRIPT_REVIEW(10, "待脚本审核"),
    SCRIPT_REVIEW_PASSED(11, "脚本审核通过"),
    SCRIPT_REVIEW_REJECTED(12, "脚本审核驳回"),
    PENDING_VIDEO_REVIEW(13, "待视频审核"),
    VIDEO_REVIEW_PASSED(14, "视频审核通过"),
    VIDEO_REVIEW_REJECTED(15, "视频审核驳回"),
    COMPLETED(16, "已完成"),
    CANCELLED(17, "已取消");

    private final Integer code;
    private final String desc;

    WorkOrderStatusEnum(Integer code, String desc) {
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
     * 根据状态码获取枚举
     */
    public static WorkOrderStatusEnum getByCode(Integer code) {
        for (WorkOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为待审批状态
     */
    public boolean isPendingApproval() {
        return this == PENDING_FIRST_REVIEW || 
               this == PENDING_SECOND_REVIEW || 
               this == PENDING_DIRECTOR_APPROVAL ||
               this == PENDING_SCRIPT_REVIEW ||
               this == PENDING_VIDEO_REVIEW;
    }

    /**
     * 获取下一个审批通过状态
     */
    public WorkOrderStatusEnum getNextApprovedStatus() {
        switch (this) {
            case PENDING_FIRST_REVIEW:
                return FIRST_REVIEW_PASSED;
            case PENDING_SECOND_REVIEW:
                return SECOND_REVIEW_PASSED;
            case PENDING_DIRECTOR_APPROVAL:
                return DIRECTOR_APPROVAL_PASSED;
            case PENDING_SCRIPT_REVIEW:
                return SCRIPT_REVIEW_PASSED;
            case PENDING_VIDEO_REVIEW:
                return VIDEO_REVIEW_PASSED;
            default:
                return null;
        }
    }

    /**
     * 获取审批驳回状态
     */
    public WorkOrderStatusEnum getRejectedStatus() {
        switch (this) {
            case PENDING_FIRST_REVIEW:
                return FIRST_REVIEW_REJECTED;
            case PENDING_SECOND_REVIEW:
                return SECOND_REVIEW_REJECTED;
            case PENDING_DIRECTOR_APPROVAL:
                return DIRECTOR_APPROVAL_REJECTED;
            case PENDING_SCRIPT_REVIEW:
                return SCRIPT_REVIEW_REJECTED;
            case PENDING_VIDEO_REVIEW:
                return VIDEO_REVIEW_REJECTED;
            default:
                return null;
        }
    }
}