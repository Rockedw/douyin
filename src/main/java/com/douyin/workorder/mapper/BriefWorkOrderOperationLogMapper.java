package com.douyin.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyin.workorder.entity.BriefWorkOrderOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单操作日志表 Mapper 接口
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Mapper
public interface BriefWorkOrderOperationLogMapper extends BaseMapper<BriefWorkOrderOperationLog> {

    /**
     * 根据工单ID查询操作日志
     * 
     * @param workOrderId 工单ID
     * @return 操作日志列表
     */
    List<BriefWorkOrderOperationLog> selectByWorkOrderId(@Param("workOrderId") Long workOrderId);

    /**
     * 根据操作人ID查询操作日志
     * 
     * @param operatorId 操作人ID
     * @return 操作日志列表
     */
    List<BriefWorkOrderOperationLog> selectByOperatorId(@Param("operatorId") Long operatorId);

    /**
     * 根据操作类型查询操作日志
     * 
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<BriefWorkOrderOperationLog> selectByOperationType(@Param("operationType") Integer operationType);
}