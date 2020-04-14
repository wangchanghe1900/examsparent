package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-04-01 09:07
 */
@Data
public class TestScoreInfo {
    //显示资源ID、考试名称、考试次数、最低分、最高分、平均分
    private Long materialID;
    private String examName;
    private Integer examTimes;
    private Integer minScore;
    private Integer maxScore;
    private Integer averageScore;
}
