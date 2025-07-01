package com.douyin.workorder.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单修改请求DTO
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
public class WorkOrderUpdateRequestDTO {

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    /**
     * 工单标题
     */
    private String title;

    /**
     * 工单描述
     */
    private String description;

    /**
     * 达人ID
     */
    private Long talentId;

    /**
     * 达人姓名
     */
    private String talentName;

    /**
     * 达人粉丝数
     */
    private Integer talentFansCount;

    /**
     * 价格（单位：分）
     */
    private BigDecimal price;

    /**
     * 档期开始时间
     */
    private LocalDateTime scheduleStartTime;

    /**
     * 档期结束时间
     */
    private LocalDateTime scheduleEndTime;

    /**
     * 脚本内容
     */
    private String scriptContent;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 修改原因
     */
    private String updateReason;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 要删除的附件ID列表
     */
    private List<Long> deleteAttachmentIds;

    /**
     * 新增附件列表
     */
    private List<AttachmentUploadDTO> newAttachments;
}