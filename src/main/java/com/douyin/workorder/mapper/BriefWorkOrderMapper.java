package com.douyin.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyin.workorder.entity.BriefWorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单主表 Mapper 接口
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Mapper
public interface BriefWorkOrderMapper extends BaseMapper<BriefWorkOrder> {

    /**
     * 根据状态查询工单列表
     * 
     * @param status 工单状态
     * @return 工单列表
     */
    List<BriefWorkOrder> selectByStatus(@Param("status") Integer status);

    /**
     * 根据审批人ID查询待审批工单列表
     * 
     * @param approverId 审批人ID
     * @return 工单列表
     */
    List<BriefWorkOrder> selectByApproverId(@Param("approverId") Long approverId);

    /**
     * 批量更新工单状态
     * 
     * @param ids 工单ID列表
     * @param status 目标状态
     * @param approverId 审批人ID
     * @param comment 审批意见
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("status") Integer status, 
                         @Param("approverId") Long approverId, 
                         @Param("comment") String comment);

    /**
     * 根据品牌方ID和代理方ID查询工单列表
     * 
     * @param brandId 品牌方ID
     * @param agencyId 代理方ID
     * @return 工单列表
     */
    List<BriefWorkOrder> selectByBrandAndAgency(@Param("brandId") Long brandId, 
                                               @Param("agencyId") Long agencyId);
}