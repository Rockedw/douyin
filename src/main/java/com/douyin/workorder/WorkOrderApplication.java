package com.douyin.workorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DouYin工单系统启动类
 * 
 * @author douyin
 * @since 2023-07-01
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.douyin.workorder.mapper")
public class WorkOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkOrderApplication.class, args);
    }
}