package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-04-13 14:25
 */
@Data
public class LearnedResourceEmpInfo {
    private String resourceName;
    private String employeeName;
    private String deptName;
    private Integer learnTimes;
    private Integer learnLong;
}
