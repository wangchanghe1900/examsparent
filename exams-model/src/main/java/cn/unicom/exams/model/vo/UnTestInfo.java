package cn.unicom.exams.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-04-10 15:07
 */
@Data
public class UnTestInfo {
    private Long tId;
    private String empCode;
    private String testName;
    private LocalDateTime publishDate;
    private String resourceName;
    private String employeeName;
    private String deptName;
}
