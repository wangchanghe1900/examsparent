package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-04-01 09:02
 */
@Data
public class TestedInfo {
    private Long empID;
    private Integer showNum;
    private Integer pageNum;
    private Integer totalNum;
    private List<TestScoreInfo> dataList; //显示资源ID、考试名称、考试次数、最低分、最高分、平均分
}
