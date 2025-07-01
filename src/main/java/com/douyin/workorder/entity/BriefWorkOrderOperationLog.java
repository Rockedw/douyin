package com.douyin.workorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 工单操作日志表实体类
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("brief_work_order_operation_log")
public class BriefWorkOrderOperationLog {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    @TableField("work_order_id")
    private Long workOrderId;

    /**
     * 操作类型：1-提交，2-初审通过，3-初审驳回，4-二审通过，5-二审驳回，
     * 6-总监审批通过，7-总监审批驳回，8-脚本审核通过，9-脚本审核驳回，
     * 10-视频审核通过，11-视频审核驳回，12-修改工单，13-撤回工单，14-取消工单
     */
    @NotNull(message = "操作类型不能为空")
    @TableField("operation_type")
    private Integer operationType;

    /**
     * 操作前状态
     */
    @TableField("before_status")
    private Integer beforeStatus;

    /**
     * 操作后状态
     */
    @TableField("after_status")
    private Integer afterStatus;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人ID不能为空")
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @NotBlank(message = "操作人姓名不能为空")
    @TableField("operator_name")
    private String operatorName;

    /**
     * 操作描述
     */
    @TableField("operation_desc")
    private String operationDesc;

    /**
     * 操作备注/审批意见
     */
    @TableField("remark")
    private String remark;

    /**
     * 操作时间
     */
    @TableField(value = "operation_time", fill = FieldFill.INSERT)
    private LocalDateTime operationTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}