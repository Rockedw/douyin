package com.douyin.workorder.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 附件上传DTO
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Data
public class AttachmentUploadDTO {

    /**
     * 附件类型：1-图片，2-视频，3-文档，4-其他
     */
    @NotNull(message = "附件类型不能为空")
    private Integer attachmentType;

    /**
     * 附件名称
     */
    @NotBlank(message = "附件名称不能为空")
    private String attachmentName;

    /**
     * 附件原始名称
     */
    @NotBlank(message = "附件原始名称不能为空")
    private String originalName;

    /**
     * 附件URL
     */
    @NotBlank(message = "附件URL不能为空")
    private String attachmentUrl;

    /**
     * 附件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * 附件描述
     */
    private String description;
}