package com.douyin.workorder.service;

import com.douyin.workorder.dto.ApprovalRequestDTO;
import com.douyin.workorder.entity.BriefWorkOrder;
import com.douyin.workorder.enums.WorkOrderStatusEnum;
import com.douyin.workorder.mapper.BriefWorkOrderAttachmentMapper;
import com.douyin.workorder.mapper.BriefWorkOrderMapper;
import com.douyin.workorder.mapper.BriefWorkOrderOperationLogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * BriefWorkOrderApprovalService 测试类
 * 
 * @author douyin
 * @since 2023-07-01
 */
@ExtendWith(MockitoExtension.class)
class BriefWorkOrderApprovalServiceTest {

    @Mock
    private BriefWorkOrderMapper workOrderMapper;

    @Mock
    private BriefWorkOrderAttachmentMapper workOrderAttachmentMapper;

    @Mock
    private BriefWorkOrderOperationLogMapper workOrderOperationLogMapper;

    @InjectMocks
    private BriefWorkOrderApprovalService approvalService;

    private BriefWorkOrder mockWorkOrder;

    @BeforeEach
    void setUp() {
        mockWorkOrder = new BriefWorkOrder();
        mockWorkOrder.setId(1L);
        mockWorkOrder.setOrderNo("WO20230701001");
        mockWorkOrder.setBrandId(100L);
        mockWorkOrder.setAgencyId(200L);
        mockWorkOrder.setTalentId(300L);
        mockWorkOrder.setTalentName("测试达人");
        mockWorkOrder.setTitle("测试工单");
        mockWorkOrder.setPrice(new BigDecimal("10000"));
        mockWorkOrder.setScheduleStartTime(LocalDateTime.now().plusDays(1));
        mockWorkOrder.setScheduleEndTime(LocalDateTime.now().plusDays(2));
        mockWorkOrder.setStatus(WorkOrderStatusEnum.PENDING_FIRST_REVIEW.getCode());
        mockWorkOrder.setCreateTime(LocalDateTime.now());
        mockWorkOrder.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testFirstReviewApproval_Success() {
        // 准备测试数据
        ApprovalRequestDTO request = new ApprovalRequestDTO();
        request.setWorkOrderId(1L);
        request.setApproved(true);
        request.setComment("初审通过");
        request.setApproverId(1001L);
        request.setApproverName("审批人");

        // Mock mapper 调用
        when(workOrderMapper.selectById(1L)).thenReturn(mockWorkOrder);
        when(workOrderMapper.updateById(any(BriefWorkOrder.class))).thenReturn(1);
        when(workOrderOperationLogMapper.insert(any())).thenReturn(1);

        // 执行测试
        boolean result = approvalService.firstReviewApproval(request);

        // 验证结果
        assertTrue(result);
        verify(workOrderMapper, times(1)).selectById(1L);
        verify(workOrderMapper, times(1)).updateById(any(BriefWorkOrder.class));
        verify(workOrderOperationLogMapper, times(1)).insert(any());
    }

    @Test
    void testFirstReviewApproval_WorkOrderNotFound() {
        // 准备测试数据
        ApprovalRequestDTO request = new ApprovalRequestDTO();
        request.setWorkOrderId(999L);
        request.setApproved(true);
        request.setApproverId(1001L);

        // Mock mapper 调用
        when(workOrderMapper.selectById(999L)).thenReturn(null);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            approvalService.firstReviewApproval(request);
        });

        assertTrue(exception.getMessage().contains("工单不存在"));
        verify(workOrderMapper, times(1)).selectById(999L);
        verify(workOrderMapper, never()).updateById(any());
    }

    @Test
    void testFirstReviewApproval_InvalidStatus() {
        // 准备测试数据 - 工单状态不正确
        mockWorkOrder.setStatus(WorkOrderStatusEnum.COMPLETED.getCode());
        
        ApprovalRequestDTO request = new ApprovalRequestDTO();
        request.setWorkOrderId(1L);
        request.setApproved(true);
        request.setApproverId(1001L);

        // Mock mapper 调用
        when(workOrderMapper.selectById(1L)).thenReturn(mockWorkOrder);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            approvalService.firstReviewApproval(request);
        });

        assertTrue(exception.getMessage().contains("工单当前状态不支持该操作"));
        verify(workOrderMapper, times(1)).selectById(1L);
        verify(workOrderMapper, never()).updateById(any());
    }

    @Test
    void testWithdrawWorkOrder_Success() {
        // Mock mapper 调用
        when(workOrderMapper.selectById(1L)).thenReturn(mockWorkOrder);
        when(workOrderMapper.updateById(any(BriefWorkOrder.class))).thenReturn(1);
        when(workOrderOperationLogMapper.insert(any())).thenReturn(1);

        // 执行测试
        boolean result = approvalService.withdrawWorkOrder(1L, 2001L, "操作人", "撤回原因");

        // 验证结果
        assertTrue(result);
    }

    @Test
    void testUpdatePriceInfo_InvalidPrice() {
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            approvalService.updatePriceInfo(1L, new BigDecimal("0"), 2001L, "操作人", "调价原因");
        });

        assertTrue(exception.getMessage().contains("价格必须大于0"));
    }
}