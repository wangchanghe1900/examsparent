package cn.unicom.exams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-04-13 17:13
 */
@Data
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class EmpTestResultInfo {
    private String testName;
    private String employeeName;
    private String deptName;
    private Integer score;
    private Integer testDuration;
    private Integer returnCount;
    private LocalDateTime testTime;
}
