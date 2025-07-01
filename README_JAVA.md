# DouYin Work Order System - Java Spring Boot Module

这是抖音工单审批系统的 Java Spring Boot 实现，提供了完整的品牌方审批和代理方修改功能。

## 功能特性

### 品牌方审批功能
- ✅ 初审审批（通过/驳回）
- ✅ 二审审批（通过/驳回）
- ✅ 总监审批（通过/驳回）
- ✅ 脚本审核（通过/驳回）
- ✅ 视频审核（通过/驳回）
- ✅ 批量审批功能
- ✅ 审批历史查询

### 代理方修改功能
- ✅ 工单信息修改
- ✅ 达人信息更新
- ✅ 价格信息调整
- ✅ 档期信息修改
- ✅ 附件上传管理
- ✅ 工单撤回功能
- ✅ 工单重新提交

## 技术架构

- **框架**: Spring Boot 2.7.14
- **数据访问**: MyBatis Plus 3.5.3.1
- **数据库**: MySQL 8.0
- **事务管理**: Spring Transaction
- **参数验证**: Spring Validation
- **日志框架**: SLF4J + Logback
- **构建工具**: Maven 3.6+
- **Java版本**: JDK 8+

## 项目结构

```
src/main/java/com/douyin/workorder/
├── WorkOrderApplication.java          # Spring Boot 启动类
├── dto/                              # 数据传输对象
│   ├── ApprovalRequestDTO.java       # 审批请求DTO
│   ├── BatchApprovalRequestDTO.java  # 批量审批请求DTO
│   ├── WorkOrderUpdateRequestDTO.java # 工单更新请求DTO
│   └── AttachmentUploadDTO.java      # 附件上传DTO
├── entity/                           # 实体类
│   ├── BriefWorkOrder.java           # 工单主表实体
│   ├── BriefWorkOrderOperationLog.java # 操作日志实体
│   └── BriefWorkOrderAttachment.java # 附件表实体
├── enums/                            # 枚举类
│   ├── WorkOrderStatusEnum.java      # 工单状态枚举
│   ├── OperationTypeEnum.java        # 操作类型枚举
│   └── AttachmentTypeEnum.java       # 附件类型枚举
├── mapper/                           # MyBatis Mapper接口
│   ├── BriefWorkOrderMapper.java     # 工单数据访问接口
│   ├── BriefWorkOrderOperationLogMapper.java # 操作日志数据访问接口
│   └── BriefWorkOrderAttachmentMapper.java   # 附件数据访问接口
└── service/                          # 业务服务类
    └── BriefWorkOrderApprovalService.java    # 工单审批服务（核心业务类）
```

## 数据库表结构

### 1. brief_work_order (工单主表)
- 包含工单基本信息、状态、价格、档期等字段
- 支持逻辑删除和审计字段

### 2. brief_work_order_operation_log (操作日志表)  
- 记录所有工单操作历史
- 包含操作类型、操作人、操作时间等信息

### 3. brief_work_order_attachment (附件表)
- 存储工单相关附件信息
- 支持多种附件类型（图片、视频、文档等）

## 核心业务逻辑

### 工单状态流转
```
草稿 → 待初审 → 初审通过 → 待二审 → 二审通过 → 待总监审批 → 总监审批通过 
     ↓         ↓        ↓         ↓         ↓           ↓
   取消     初审驳回   二审驳回   总监审批驳回
     
总监审批通过 → 待脚本审核 → 脚本审核通过 → 待视频审核 → 视频审核通过 → 已完成
              ↓            ↓            ↓
            脚本审核驳回   视频审核驳回
```

### 权限控制
- 品牌方：拥有审批权限
- 代理方：拥有工单修改权限
- 系统自动验证操作权限和工单状态

## 快速开始

### 1. 环境要求
- JDK 8 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本

### 2. 数据库配置
修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/douyin_workorder?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: your_username
    password: your_password
```

### 3. 构建项目
```bash
mvn clean compile
```

### 4. 运行测试
```bash
mvn test
```

### 5. 启动应用
```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080/workorder` 启动。

## API 使用示例

### 初审审批
```java
ApprovalRequestDTO request = new ApprovalRequestDTO();
request.setWorkOrderId(1L);
request.setApproved(true);
request.setComment("初审通过");
request.setApproverId(1001L);
request.setApproverName("审批人");

boolean result = approvalService.firstReviewApproval(request);
```

### 批量审批
```java
List<Long> workOrderIds = Arrays.asList(1L, 2L, 3L);
boolean result = approvalService.batchApproval(workOrderIds, true, "批量通过", 1001L, "审批人");
```

### 工单信息修改
```java
WorkOrderUpdateRequestDTO request = new WorkOrderUpdateRequestDTO();
request.setWorkOrderId(1L);
request.setTitle("更新后的标题");
request.setPrice(new BigDecimal("20000"));
request.setOperatorId(2001L);
request.setOperatorName("操作人");

boolean result = approvalService.updateWorkOrderInfo(request);
```

## 注意事项

1. **事务管理**: 所有的业务操作都使用了 `@Transactional` 注解确保数据一致性
2. **参数验证**: 使用 JSR-303 注解进行参数验证
3. **操作日志**: 每个操作都会自动记录详细的操作日志
4. **异常处理**: 统一的异常处理机制，返回友好的错误信息
5. **状态验证**: 严格的工单状态验证，确保业务流程正确

## 扩展功能

系统支持以下扩展：
- 自定义审批流程
- 消息通知机制
- 工单统计分析
- 权限细粒度控制

## 许可证

本项目遵循 MIT 许可证。