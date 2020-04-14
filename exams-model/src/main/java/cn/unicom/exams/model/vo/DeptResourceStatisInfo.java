package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * 部门学习资源统计
 * @author 王长何
 * @create 2020-04-14 13:35
 */
@Data
public class DeptResourceStatisInfo {
    private String deptName;
    private String resourceName;
    private Integer deptEmpCount;
    private Integer learnCount;
    private Integer AvgCount;
}
