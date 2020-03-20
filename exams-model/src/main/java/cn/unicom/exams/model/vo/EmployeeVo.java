package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysEmployee;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 王长何
 * @create 2020-03-17 14:47
 */
@Data
public class EmployeeVo extends SysEmployee {
    /**
     * 查询用户
     */
    private String queryName;

    /**
     * 入职开始日期
     */
    private LocalDate entryStartTime;

    /**
     * 入职结束日期
     */
    private LocalDate entryEndTime;
}
