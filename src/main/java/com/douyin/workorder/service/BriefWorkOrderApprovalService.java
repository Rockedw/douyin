package com.douyin.workorder.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.douyin.workorder.dto.ApprovalRequestDTO;
import com.douyin.workorder.dto.AttachmentUploadDTO;
import com.douyin.workorder.dto.WorkOrderUpdateRequestDTO;
import com.douyin.workorder.entity.BriefWorkOrder;
import com.douyin.workorder.entity.BriefWorkOrderAttachment;
import com.douyin.workorder.entity.BriefWorkOrderOperationLog;
import com.douyin.workorder.enums.OperationTypeEnum;
import com.douyin.workorder.enums.WorkOrderStatusEnum;
import com.douyin.workorder.mapper.BriefWorkOrderAttachmentMapper;
import com.douyin.workorder.mapper.BriefWorkOrderMapper;
import com.douyin.workorder.mapper.BriefWorkOrderOperationLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 工单审批服务类
 * 完善品牌方审批和代理方修改的完整功能
 * 
 * @author douyin
 * @since 2023-07-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BriefWorkOrderApprovalService {
    
    private final BriefWorkOrderMapper workOrderMapper;
    private final BriefWorkOrderAttachmentMapper workOrderAttachmentMapper;
    private final BriefWorkOrderOperationLogMapper workOrderOperationLogMapper;

    // ================================ 品牌方审批功能 ================================

    /**
     * 初审审批（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean firstReviewApproval(@Valid ApprovalRequestDTO request) {
        log.info("开始初审审批，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
        
        try {
            // 1. 参数验证和权限校验
            BriefWorkOrder workOrder = validateWorkOrderForApproval(request.getWorkOrderId(), 
                    WorkOrderStatusEnum.PENDING_FIRST_REVIEW);
            
            // 2. 更新工单状态
            WorkOrderStatusEnum targetStatus = request.getApproved() 
                    ? WorkOrderStatusEnum.FIRST_REVIEW_PASSED 
                    : WorkOrderStatusEnum.FIRST_REVIEW_REJECTED;
            
            updateWorkOrderStatus(workOrder, targetStatus, request.getApproverId(), request.getComment());
            
            // 3. 记录操作日志
            OperationTypeEnum operationType = request.getApproved() 
                    ? OperationTypeEnum.FIRST_REVIEW_PASS 
                    : OperationTypeEnum.FIRST_REVIEW_REJECT;
            
            recordOperationLog(request.getWorkOrderId(), operationType, 
                    WorkOrderStatusEnum.PENDING_FIRST_REVIEW.getCode(), 
                    targetStatus.getCode(), request.getApproverId(), 
                    request.getApproverName(), request.getComment());
            
            log.info("初审审批完成，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
            return true;
            
        } catch (Exception e) {
            log.error("初审审批失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("初审审批失败：" + e.getMessage());
        }
    }

    /**
     * 二审审批（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean secondReviewApproval(@Valid ApprovalRequestDTO request) {
        log.info("开始二审审批，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
        
        try {
            // 1. 验证工单状态
            BriefWorkOrder workOrder = validateWorkOrderForApproval(request.getWorkOrderId(), 
                    WorkOrderStatusEnum.PENDING_SECOND_REVIEW);
            
            // 2. 更新工单状态
            WorkOrderStatusEnum targetStatus = request.getApproved() 
                    ? WorkOrderStatusEnum.SECOND_REVIEW_PASSED 
                    : WorkOrderStatusEnum.SECOND_REVIEW_REJECTED;
            
            updateWorkOrderStatus(workOrder, targetStatus, request.getApproverId(), request.getComment());
            
            // 3. 记录操作日志
            OperationTypeEnum operationType = request.getApproved() 
                    ? OperationTypeEnum.SECOND_REVIEW_PASS 
                    : OperationTypeEnum.SECOND_REVIEW_REJECT;
            
            recordOperationLog(request.getWorkOrderId(), operationType, 
                    WorkOrderStatusEnum.PENDING_SECOND_REVIEW.getCode(), 
                    targetStatus.getCode(), request.getApproverId(), 
                    request.getApproverName(), request.getComment());
            
            log.info("二审审批完成，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
            return true;
            
        } catch (Exception e) {
            log.error("二审审批失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("二审审批失败：" + e.getMessage());
        }
    }

    /**
     * 总监审批（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean directorApproval(@Valid ApprovalRequestDTO request) {
        log.info("开始总监审批，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
        
        try {
            // 1. 验证工单状态
            BriefWorkOrder workOrder = validateWorkOrderForApproval(request.getWorkOrderId(), 
                    WorkOrderStatusEnum.PENDING_DIRECTOR_APPROVAL);
            
            // 2. 更新工单状态
            WorkOrderStatusEnum targetStatus = request.getApproved() 
                    ? WorkOrderStatusEnum.DIRECTOR_APPROVAL_PASSED 
                    : WorkOrderStatusEnum.DIRECTOR_APPROVAL_REJECTED;
            
            updateWorkOrderStatus(workOrder, targetStatus, request.getApproverId(), request.getComment());
            
            // 3. 记录操作日志
            OperationTypeEnum operationType = request.getApproved() 
                    ? OperationTypeEnum.DIRECTOR_APPROVAL_PASS 
                    : OperationTypeEnum.DIRECTOR_APPROVAL_REJECT;
            
            recordOperationLog(request.getWorkOrderId(), operationType, 
                    WorkOrderStatusEnum.PENDING_DIRECTOR_APPROVAL.getCode(), 
                    targetStatus.getCode(), request.getApproverId(), 
                    request.getApproverName(), request.getComment());
            
            log.info("总监审批完成，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
            return true;
            
        } catch (Exception e) {
            log.error("总监审批失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("总监审批失败：" + e.getMessage());
        }
    }

    /**
     * 脚本审核（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean scriptReview(@Valid ApprovalRequestDTO request) {
        log.info("开始脚本审核，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
        
        try {
            // 1. 验证工单状态
            BriefWorkOrder workOrder = validateWorkOrderForApproval(request.getWorkOrderId(), 
                    WorkOrderStatusEnum.PENDING_SCRIPT_REVIEW);
            
            // 2. 验证脚本内容
            if (StringUtils.isBlank(workOrder.getScriptContent())) {
                throw new IllegalArgumentException("脚本内容不能为空");
            }
            
            // 3. 更新工单状态
            WorkOrderStatusEnum targetStatus = request.getApproved() 
                    ? WorkOrderStatusEnum.SCRIPT_REVIEW_PASSED 
                    : WorkOrderStatusEnum.SCRIPT_REVIEW_REJECTED;
            
            updateWorkOrderStatus(workOrder, targetStatus, request.getApproverId(), request.getComment());
            
            // 4. 记录操作日志
            OperationTypeEnum operationType = request.getApproved() 
                    ? OperationTypeEnum.SCRIPT_REVIEW_PASS 
                    : OperationTypeEnum.SCRIPT_REVIEW_REJECT;
            
            recordOperationLog(request.getWorkOrderId(), operationType, 
                    WorkOrderStatusEnum.PENDING_SCRIPT_REVIEW.getCode(), 
                    targetStatus.getCode(), request.getApproverId(), 
                    request.getApproverName(), request.getComment());
            
            log.info("脚本审核完成，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
            return true;
            
        } catch (Exception e) {
            log.error("脚本审核失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("脚本审核失败：" + e.getMessage());
        }
    }

    /**
     * 视频审核（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean videoReview(@Valid ApprovalRequestDTO request) {
        log.info("开始视频审核，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
        
        try {
            // 1. 验证工单状态
            BriefWorkOrder workOrder = validateWorkOrderForApproval(request.getWorkOrderId(), 
                    WorkOrderStatusEnum.PENDING_VIDEO_REVIEW);
            
            // 2. 验证视频内容
            if (StringUtils.isBlank(workOrder.getVideoUrl())) {
                throw new IllegalArgumentException("视频URL不能为空");
            }
            
            // 3. 更新工单状态
            WorkOrderStatusEnum targetStatus = request.getApproved() 
                    ? WorkOrderStatusEnum.VIDEO_REVIEW_PASSED 
                    : WorkOrderStatusEnum.VIDEO_REVIEW_REJECTED;
            
            updateWorkOrderStatus(workOrder, targetStatus, request.getApproverId(), request.getComment());
            
            // 4. 记录操作日志
            OperationTypeEnum operationType = request.getApproved() 
                    ? OperationTypeEnum.VIDEO_REVIEW_PASS 
                    : OperationTypeEnum.VIDEO_REVIEW_REJECT;
            
            recordOperationLog(request.getWorkOrderId(), operationType, 
                    WorkOrderStatusEnum.PENDING_VIDEO_REVIEW.getCode(), 
                    targetStatus.getCode(), request.getApproverId(), 
                    request.getApproverName(), request.getComment());
            
            log.info("视频审核完成，工单ID：{}，审批结果：{}", request.getWorkOrderId(), request.getApproved());
            return true;
            
        } catch (Exception e) {
            log.error("视频审核失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("视频审核失败：" + e.getMessage());
        }
    }

    /**
     * 批量审批功能
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchApproval(@NotEmpty List<Long> workOrderIds, @NotNull Boolean approved, 
                               String comment, @NotNull Long approverId, String approverName) {
        log.info("开始批量审批，工单数量：{}，审批结果：{}", workOrderIds.size(), approved);
        
        try {
            int successCount = 0;
            List<String> failureMessages = new ArrayList<>();
            
            for (Long workOrderId : workOrderIds) {
                try {
                    ApprovalRequestDTO request = new ApprovalRequestDTO();
                    request.setWorkOrderId(workOrderId);
                    request.setApproved(approved);
                    request.setComment(comment);
                    request.setApproverId(approverId);
                    request.setApproverName(approverName);
                    
                    // 根据工单当前状态选择对应的审批方法
                    BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
                    if (workOrder == null) {
                        failureMessages.add("工单" + workOrderId + "不存在");
                        continue;
                    }
                    
                    WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
                    if (currentStatus == null || !currentStatus.isPendingApproval()) {
                        failureMessages.add("工单" + workOrderId + "当前状态不支持审批");
                        continue;
                    }
                    
                    // 调用对应的审批方法
                    boolean result = false;
                    switch (currentStatus) {
                        case PENDING_FIRST_REVIEW:
                            result = firstReviewApproval(request);
                            break;
                        case PENDING_SECOND_REVIEW:
                            result = secondReviewApproval(request);
                            break;
                        case PENDING_DIRECTOR_APPROVAL:
                            result = directorApproval(request);
                            break;
                        case PENDING_SCRIPT_REVIEW:
                            result = scriptReview(request);
                            break;
                        case PENDING_VIDEO_REVIEW:
                            result = videoReview(request);
                            break;
                        default:
                            failureMessages.add("工单" + workOrderId + "状态不支持当前审批类型");
                            continue;
                    }
                    
                    if (result) {
                        successCount++;
                    } else {
                        failureMessages.add("工单" + workOrderId + "审批失败");
                    }
                    
                } catch (Exception e) {
                    log.error("批量审批单个工单失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage());
                    failureMessages.add("工单" + workOrderId + "审批异常：" + e.getMessage());
                }
            }
            
            log.info("批量审批完成，成功：{}，失败：{}", successCount, failureMessages.size());
            
            if (!failureMessages.isEmpty()) {
                log.warn("批量审批部分失败：{}", String.join("; ", failureMessages));
            }
            
            return successCount > 0;
            
        } catch (Exception e) {
            log.error("批量审批失败，错误信息：{}", e.getMessage(), e);
            throw new RuntimeException("批量审批失败：" + e.getMessage());
        }
    }

    /**
     * 审批历史查询
     */
    public List<BriefWorkOrderOperationLog> getApprovalHistory(@NotNull Long workOrderId) {
        log.info("查询审批历史，工单ID：{}", workOrderId);
        
        try {
            // 验证工单是否存在
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            // 查询操作日志
            List<BriefWorkOrderOperationLog> operationLogs = workOrderOperationLogMapper.selectByWorkOrderId(workOrderId);
            
            log.info("查询审批历史完成，工单ID：{}，记录数：{}", workOrderId, operationLogs.size());
            return operationLogs;
            
        } catch (Exception e) {
            log.error("查询审批历史失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("查询审批历史失败：" + e.getMessage());
        }
    }

    // ================================ 代理方修改功能 ================================

    /**
     * 工单信息修改
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWorkOrderInfo(@Valid WorkOrderUpdateRequestDTO request) {
        log.info("开始修改工单信息，工单ID：{}", request.getWorkOrderId());
        
        try {
            // 1. 验证工单状态（只有草稿和驳回状态可以修改）
            BriefWorkOrder workOrder = workOrderMapper.selectById(request.getWorkOrderId());
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isModifiableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许修改");
            }
            
            // 2. 更新工单基本信息
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(request.getWorkOrderId());
            
            if (StringUtils.isNotBlank(request.getTitle())) {
                updateEntity.setTitle(request.getTitle());
            }
            if (StringUtils.isNotBlank(request.getDescription())) {
                updateEntity.setDescription(request.getDescription());
            }
            if (request.getPrice() != null) {
                updateEntity.setPrice(request.getPrice());
            }
            if (request.getScheduleStartTime() != null) {
                updateEntity.setScheduleStartTime(request.getScheduleStartTime());
            }
            if (request.getScheduleEndTime() != null) {
                updateEntity.setScheduleEndTime(request.getScheduleEndTime());
            }
            if (StringUtils.isNotBlank(request.getScriptContent())) {
                updateEntity.setScriptContent(request.getScriptContent());
            }
            if (StringUtils.isNotBlank(request.getVideoUrl())) {
                updateEntity.setVideoUrl(request.getVideoUrl());
            }
            
            updateEntity.setUpdaterId(request.getOperatorId());
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 3. 处理附件变更
            handleAttachmentChanges(request);
            
            // 4. 记录操作日志
            recordOperationLog(request.getWorkOrderId(), OperationTypeEnum.UPDATE_WORK_ORDER, 
                    currentStatus.getCode(), currentStatus.getCode(), 
                    request.getOperatorId(), request.getOperatorName(), request.getUpdateReason());
            
            log.info("修改工单信息完成，工单ID：{}", request.getWorkOrderId());
            return true;
            
        } catch (Exception e) {
            log.error("修改工单信息失败，工单ID：{}，错误信息：{}", request.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("修改工单信息失败：" + e.getMessage());
        }
    }

    /**
     * 达人信息更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTalentInfo(@NotNull Long workOrderId, @NotNull Long talentId, 
                                  @NotNull String talentName, Integer talentFansCount, 
                                  @NotNull Long operatorId, String operatorName) {
        log.info("开始更新达人信息，工单ID：{}，达人ID：{}", workOrderId, talentId);
        
        try {
            // 1. 验证工单状态
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isModifiableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许修改");
            }
            
            // 2. 更新达人信息
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(workOrderId);
            updateEntity.setTalentId(talentId);
            updateEntity.setTalentName(talentName);
            updateEntity.setTalentFansCount(talentFansCount);
            updateEntity.setUpdaterId(operatorId);
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 3. 记录操作日志
            recordOperationLog(workOrderId, OperationTypeEnum.UPDATE_WORK_ORDER, 
                    currentStatus.getCode(), currentStatus.getCode(), 
                    operatorId, operatorName, "更新达人信息");
            
            log.info("更新达人信息完成，工单ID：{}，达人ID：{}", workOrderId, talentId);
            return true;
            
        } catch (Exception e) {
            log.error("更新达人信息失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("更新达人信息失败：" + e.getMessage());
        }
    }

    /**
     * 价格信息调整
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePriceInfo(@NotNull Long workOrderId, @NotNull java.math.BigDecimal price, 
                                 @NotNull Long operatorId, String operatorName, String reason) {
        log.info("开始调整价格信息，工单ID：{}，新价格：{}", workOrderId, price);
        
        try {
            // 验证价格
            if (price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("价格必须大于0");
            }
            
            // 验证工单状态
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isModifiableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许修改");
            }
            
            // 更新价格
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(workOrderId);
            updateEntity.setPrice(price);
            updateEntity.setUpdaterId(operatorId);
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 记录操作日志
            String logDesc = String.format("调整价格：%s -> %s", workOrder.getPrice(), price);
            if (StringUtils.isNotBlank(reason)) {
                logDesc += "，原因：" + reason;
            }
            
            recordOperationLog(workOrderId, OperationTypeEnum.UPDATE_WORK_ORDER, 
                    currentStatus.getCode(), currentStatus.getCode(), 
                    operatorId, operatorName, logDesc);
            
            log.info("调整价格信息完成，工单ID：{}，新价格：{}", workOrderId, price);
            return true;
            
        } catch (Exception e) {
            log.error("调整价格信息失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("调整价格信息失败：" + e.getMessage());
        }
    }

    /**
     * 档期信息修改
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateScheduleInfo(@NotNull Long workOrderId, @NotNull LocalDateTime startTime, 
                                    @NotNull LocalDateTime endTime, @NotNull Long operatorId, 
                                    String operatorName, String reason) {
        log.info("开始修改档期信息，工单ID：{}，档期：{} - {}", workOrderId, startTime, endTime);
        
        try {
            // 验证档期时间
            if (startTime.isAfter(endTime)) {
                throw new IllegalArgumentException("档期开始时间不能晚于结束时间");
            }
            if (startTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("档期开始时间不能早于当前时间");
            }
            
            // 验证工单状态
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isModifiableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许修改");
            }
            
            // 更新档期信息
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(workOrderId);
            updateEntity.setScheduleStartTime(startTime);
            updateEntity.setScheduleEndTime(endTime);
            updateEntity.setUpdaterId(operatorId);
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 记录操作日志
            String logDesc = String.format("修改档期：%s-%s -> %s-%s", 
                    workOrder.getScheduleStartTime(), workOrder.getScheduleEndTime(), 
                    startTime, endTime);
            if (StringUtils.isNotBlank(reason)) {
                logDesc += "，原因：" + reason;
            }
            
            recordOperationLog(workOrderId, OperationTypeEnum.UPDATE_WORK_ORDER, 
                    currentStatus.getCode(), currentStatus.getCode(), 
                    operatorId, operatorName, logDesc);
            
            log.info("修改档期信息完成，工单ID：{}", workOrderId);
            return true;
            
        } catch (Exception e) {
            log.error("修改档期信息失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("修改档期信息失败：" + e.getMessage());
        }
    }

    /**
     * 附件上传管理
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean manageAttachments(@NotNull Long workOrderId, List<Long> deleteAttachmentIds, 
                                   List<BriefWorkOrderAttachment> newAttachments, 
                                   @NotNull Long operatorId, String operatorName) {
        log.info("开始管理附件，工单ID：{}，删除数量：{}，新增数量：{}", 
                workOrderId, 
                CollectionUtils.isEmpty(deleteAttachmentIds) ? 0 : deleteAttachmentIds.size(),
                CollectionUtils.isEmpty(newAttachments) ? 0 : newAttachments.size());
        
        try {
            // 验证工单状态
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isModifiableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许修改");
            }
            
            // 删除指定附件
            if (!CollectionUtils.isEmpty(deleteAttachmentIds)) {
                workOrderAttachmentMapper.batchDeleteByIds(deleteAttachmentIds);
                log.info("删除附件完成，数量：{}", deleteAttachmentIds.size());
            }
            
            // 新增附件
            if (!CollectionUtils.isEmpty(newAttachments)) {
                for (BriefWorkOrderAttachment attachment : newAttachments) {
                    attachment.setWorkOrderId(workOrderId);
                    attachment.setUploaderId(operatorId);
                    attachment.setUploaderName(operatorName);
                    attachment.setCreateTime(LocalDateTime.now());
                    workOrderAttachmentMapper.insert(attachment);
                }
                log.info("新增附件完成，数量：{}", newAttachments.size());
            }
            
            // 记录操作日志
            String logDesc = String.format("管理附件：删除%d个，新增%d个", 
                    CollectionUtils.isEmpty(deleteAttachmentIds) ? 0 : deleteAttachmentIds.size(),
                    CollectionUtils.isEmpty(newAttachments) ? 0 : newAttachments.size());
            
            recordOperationLog(workOrderId, OperationTypeEnum.UPDATE_WORK_ORDER, 
                    currentStatus.getCode(), currentStatus.getCode(), 
                    operatorId, operatorName, logDesc);
            
            log.info("管理附件完成，工单ID：{}", workOrderId);
            return true;
            
        } catch (Exception e) {
            log.error("管理附件失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("管理附件失败：" + e.getMessage());
        }
    }

    /**
     * 工单撤回功能
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawWorkOrder(@NotNull Long workOrderId, @NotNull Long operatorId, 
                                   String operatorName, String reason) {
        log.info("开始撤回工单，工单ID：{}", workOrderId);
        
        try {
            // 验证工单状态（只有待审批状态可以撤回）
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (currentStatus == null || !currentStatus.isPendingApproval()) {
                throw new IllegalArgumentException("当前工单状态不允许撤回");
            }
            
            // 更新工单状态为草稿
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(workOrderId);
            updateEntity.setStatus(WorkOrderStatusEnum.DRAFT.getCode());
            updateEntity.setCurrentApproverId(null);
            updateEntity.setApprovalComment(null);
            updateEntity.setUpdaterId(operatorId);
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 记录操作日志
            String logDesc = "撤回工单";
            if (StringUtils.isNotBlank(reason)) {
                logDesc += "，原因：" + reason;
            }
            
            recordOperationLog(workOrderId, OperationTypeEnum.WITHDRAW_WORK_ORDER, 
                    currentStatus.getCode(), WorkOrderStatusEnum.DRAFT.getCode(), 
                    operatorId, operatorName, logDesc);
            
            log.info("撤回工单完成，工单ID：{}", workOrderId);
            return true;
            
        } catch (Exception e) {
            log.error("撤回工单失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("撤回工单失败：" + e.getMessage());
        }
    }

    /**
     * 工单重新提交
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean resubmitWorkOrder(@NotNull Long workOrderId, @NotNull Long operatorId, 
                                   String operatorName, String reason) {
        log.info("开始重新提交工单，工单ID：{}", workOrderId);
        
        try {
            // 验证工单状态（只有草稿和驳回状态可以重新提交）
            BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                throw new IllegalArgumentException("工单不存在");
            }
            
            WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
            if (!isResubmittableStatus(currentStatus)) {
                throw new IllegalArgumentException("当前工单状态不允许重新提交");
            }
            
            // 验证必要信息是否完整
            validateWorkOrderForSubmit(workOrder);
            
            // 更新工单状态为待初审
            BriefWorkOrder updateEntity = new BriefWorkOrder();
            updateEntity.setId(workOrderId);
            updateEntity.setStatus(WorkOrderStatusEnum.PENDING_FIRST_REVIEW.getCode());
            updateEntity.setApprovalComment(null);
            updateEntity.setUpdaterId(operatorId);
            updateEntity.setUpdateTime(LocalDateTime.now());
            
            workOrderMapper.updateById(updateEntity);
            
            // 记录操作日志
            String logDesc = "重新提交工单";
            if (StringUtils.isNotBlank(reason)) {
                logDesc += "，说明：" + reason;
            }
            
            recordOperationLog(workOrderId, OperationTypeEnum.SUBMIT, 
                    currentStatus.getCode(), WorkOrderStatusEnum.PENDING_FIRST_REVIEW.getCode(), 
                    operatorId, operatorName, logDesc);
            
            log.info("重新提交工单完成，工单ID：{}", workOrderId);
            return true;
            
        } catch (Exception e) {
            log.error("重新提交工单失败，工单ID：{}，错误信息：{}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("重新提交工单失败：" + e.getMessage());
        }
    }

    // ================================ 私有方法 ================================

    /**
     * 验证工单审批条件
     */
    private BriefWorkOrder validateWorkOrderForApproval(Long workOrderId, WorkOrderStatusEnum expectedStatus) {
        if (workOrderId == null) {
            throw new IllegalArgumentException("工单ID不能为空");
        }
        
        BriefWorkOrder workOrder = workOrderMapper.selectById(workOrderId);
        if (workOrder == null) {
            throw new IllegalArgumentException("工单不存在");
        }
        
        WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(workOrder.getStatus());
        if (currentStatus != expectedStatus) {
            throw new IllegalArgumentException("工单当前状态不支持该操作，当前状态：" + 
                    (currentStatus != null ? currentStatus.getDesc() : "未知"));
        }
        
        return workOrder;
    }

    /**
     * 更新工单状态
     */
    private void updateWorkOrderStatus(BriefWorkOrder workOrder, WorkOrderStatusEnum targetStatus, 
                                     Long approverId, String comment) {
        BriefWorkOrder updateEntity = new BriefWorkOrder();
        updateEntity.setId(workOrder.getId());
        updateEntity.setStatus(targetStatus.getCode());
        updateEntity.setCurrentApproverId(approverId);
        updateEntity.setApprovalComment(comment);
        updateEntity.setUpdaterId(approverId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        
        workOrderMapper.updateById(updateEntity);
    }

    /**
     * 记录操作日志
     */
    private void recordOperationLog(Long workOrderId, OperationTypeEnum operationType, 
                                  Integer beforeStatus, Integer afterStatus, 
                                  Long operatorId, String operatorName, String remark) {
        BriefWorkOrderOperationLog log = new BriefWorkOrderOperationLog();
        log.setWorkOrderId(workOrderId);
        log.setOperationType(operationType.getCode());
        log.setBeforeStatus(beforeStatus);
        log.setAfterStatus(afterStatus);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperationDesc(operationType.getDesc());
        log.setRemark(remark);
        log.setOperationTime(LocalDateTime.now());
        log.setCreateTime(LocalDateTime.now());
        
        workOrderOperationLogMapper.insert(log);
    }

    /**
     * 处理附件变更
     */
    private void handleAttachmentChanges(WorkOrderUpdateRequestDTO request) {
        // 删除指定附件
        if (!CollectionUtils.isEmpty(request.getDeleteAttachmentIds())) {
            workOrderAttachmentMapper.batchDeleteByIds(request.getDeleteAttachmentIds());
        }
        
        // 新增附件
        if (!CollectionUtils.isEmpty(request.getNewAttachments())) {
            for (AttachmentUploadDTO attachmentDto : request.getNewAttachments()) {
                BriefWorkOrderAttachment attachment = new BriefWorkOrderAttachment();
                attachment.setWorkOrderId(request.getWorkOrderId());
                attachment.setAttachmentType(attachmentDto.getAttachmentType());
                attachment.setAttachmentName(attachmentDto.getAttachmentName());
                attachment.setOriginalName(attachmentDto.getOriginalName());
                attachment.setAttachmentUrl(attachmentDto.getAttachmentUrl());
                attachment.setFileSize(attachmentDto.getFileSize());
                attachment.setFileExtension(attachmentDto.getFileExtension());
                attachment.setDescription(attachmentDto.getDescription());
                attachment.setUploaderId(request.getOperatorId());
                attachment.setUploaderName(request.getOperatorName());
                attachment.setCreateTime(LocalDateTime.now());
                
                workOrderAttachmentMapper.insert(attachment);
            }
        }
    }

    /**
     * 判断工单状态是否可修改
     */
    private boolean isModifiableStatus(WorkOrderStatusEnum status) {
        return status == WorkOrderStatusEnum.DRAFT || 
               status == WorkOrderStatusEnum.FIRST_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.SECOND_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.DIRECTOR_APPROVAL_REJECTED ||
               status == WorkOrderStatusEnum.SCRIPT_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.VIDEO_REVIEW_REJECTED;
    }

    /**
     * 判断工单状态是否可重新提交
     */
    private boolean isResubmittableStatus(WorkOrderStatusEnum status) {
        return status == WorkOrderStatusEnum.DRAFT || 
               status == WorkOrderStatusEnum.FIRST_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.SECOND_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.DIRECTOR_APPROVAL_REJECTED ||
               status == WorkOrderStatusEnum.SCRIPT_REVIEW_REJECTED ||
               status == WorkOrderStatusEnum.VIDEO_REVIEW_REJECTED;
    }

    /**
     * 验证工单提交信息完整性
     */
    private void validateWorkOrderForSubmit(BriefWorkOrder workOrder) {
        if (StringUtils.isBlank(workOrder.getTitle())) {
            throw new IllegalArgumentException("工单标题不能为空");
        }
        if (workOrder.getTalentId() == null) {
            throw new IllegalArgumentException("达人ID不能为空");
        }
        if (StringUtils.isBlank(workOrder.getTalentName())) {
            throw new IllegalArgumentException("达人姓名不能为空");
        }
        if (workOrder.getPrice() == null || workOrder.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("价格必须大于0");
        }
        if (workOrder.getScheduleStartTime() == null || workOrder.getScheduleEndTime() == null) {
            throw new IllegalArgumentException("档期时间不能为空");
        }
        if (workOrder.getScheduleStartTime().isAfter(workOrder.getScheduleEndTime())) {
            throw new IllegalArgumentException("档期开始时间不能晚于结束时间");
        }
    }
}