package com.douyin.workorder.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 审批请求DTO
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
public class ApprovalRequestDTO {

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    /**
     * 审批结果：true-通过，false-驳回
     */
    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 审批人ID
     */
    @NotNull(message = "审批人ID不能为空")
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;
}