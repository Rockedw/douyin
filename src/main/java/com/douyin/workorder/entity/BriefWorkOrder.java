package com.douyin.workorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 工单主表实体类
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("brief_work_order")
public class BriefWorkOrder {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    @NotBlank(message = "工单编号不能为空")
    @TableField("order_no")
    private String orderNo;

    /**
     * 品牌方ID
     */
    @NotNull(message = "品牌方ID不能为空")
    @TableField("brand_id")
    private Long brandId;

    /**
     * 代理方ID
     */
    @NotNull(message = "代理方ID不能为空")
    @TableField("agency_id")
    private Long agencyId;

    /**
     * 达人ID
     */
    @NotNull(message = "达人ID不能为空")
    @TableField("talent_id")
    private Long talentId;

    /**
     * 达人姓名
     */
    @NotBlank(message = "达人姓名不能为空")
    @TableField("talent_name")
    private String talentName;

    /**
     * 达人粉丝数
     */
    @TableField("talent_fans_count")
    private Integer talentFansCount;

    /**
     * 工单标题
     */
    @NotBlank(message = "工单标题不能为空")
    @TableField("title")
    private String title;

    /**
     * 工单描述
     */
    @TableField("description")
    private String description;

    /**
     * 价格（单位：分）
     */
    @NotNull(message = "价格不能为空")
    @TableField("price")
    private BigDecimal price;

    /**
     * 档期开始时间
     */
    @NotNull(message = "档期开始时间不能为空")
    @TableField("schedule_start_time")
    private LocalDateTime scheduleStartTime;

    /**
     * 档期结束时间
     */
    @NotNull(message = "档期结束时间不能为空")
    @TableField("schedule_end_time")
    private LocalDateTime scheduleEndTime;

    /**
     * 工单状态：0-草稿，1-待初审，2-初审通过，3-初审驳回，4-待二审，5-二审通过，6-二审驳回，
     * 7-待总监审批，8-总监审批通过，9-总监审批驳回，10-待脚本审核，11-脚本审核通过，12-脚本审核驳回，
     * 13-待视频审核，14-视频审核通过，15-视频审核驳回，16-已完成，17-已取消
     */
    @NotNull(message = "工单状态不能为空")
    @TableField("status")
    private Integer status;

    /**
     * 当前审批人ID
     */
    @TableField("current_approver_id")
    private Long currentApproverId;

    /**
     * 审批意见
     */
    @TableField("approval_comment")
    private String approvalComment;

    /**
     * 脚本内容
     */
    @TableField("script_content")
    private String scriptContent;

    /**
     * 视频URL
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 创建人ID
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updater_id", fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}