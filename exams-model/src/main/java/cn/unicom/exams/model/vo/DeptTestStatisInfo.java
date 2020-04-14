package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * 部门考试返回次数统计
 * @author 王长何
 * @create 2020-04-14 14:03
 */
@Data
public class DeptTestStatisInfo {
    private String deptName;
    private String testName;
    private Integer deptEmpCount;
    private Integer returnCount;
    private Integer avgCount;
}
