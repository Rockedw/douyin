package com.douyin.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyin.workorder.entity.BriefWorkOrderAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单附件表 Mapper 接口
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Mapper
public interface BriefWorkOrderAttachmentMapper extends BaseMapper<BriefWorkOrderAttachment> {

    /**
     * 根据工单ID查询附件列表
     * 
     * @param workOrderId 工单ID
     * @return 附件列表
     */
    List<BriefWorkOrderAttachment> selectByWorkOrderId(@Param("workOrderId") Long workOrderId);

    /**
     * 根据附件类型查询附件列表
     * 
     * @param workOrderId 工单ID
     * @param attachmentType 附件类型
     * @return 附件列表
     */
    List<BriefWorkOrderAttachment> selectByTypeAndWorkOrderId(@Param("workOrderId") Long workOrderId, 
                                                             @Param("attachmentType") Integer attachmentType);

    /**
     * 批量删除附件
     * 
     * @param ids 附件ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);
}