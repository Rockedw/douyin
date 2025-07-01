package com.douyin.workorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 工单附件表实体类
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("brief_work_order_attachment")
public class BriefWorkOrderAttachment {

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
     * 附件类型：1-图片，2-视频，3-文档，4-其他
     */
    @NotNull(message = "附件类型不能为空")
    @TableField("attachment_type")
    private Integer attachmentType;

    /**
     * 附件名称
     */
    @NotBlank(message = "附件名称不能为空")
    @TableField("attachment_name")
    private String attachmentName;

    /**
     * 附件原始名称
     */
    @NotBlank(message = "附件原始名称不能为空")
    @TableField("original_name")
    private String originalName;

    /**
     * 附件URL
     */
    @NotBlank(message = "附件URL不能为空")
    @TableField("attachment_url")
    private String attachmentUrl;

    /**
     * 附件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件扩展名
     */
    @TableField("file_extension")
    private String fileExtension;

    /**
     * 附件描述
     */
    @TableField("description")
    private String description;

    /**
     * 上传人ID
     */
    @NotNull(message = "上传人ID不能为空")
    @TableField("uploader_id")
    private Long uploaderId;

    /**
     * 上传人姓名
     */
    @NotBlank(message = "上传人姓名不能为空")
    @TableField("uploader_name")
    private String uploaderName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除标识：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}