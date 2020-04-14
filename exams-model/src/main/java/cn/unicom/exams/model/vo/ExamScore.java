package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-31 11:38
 */
@Data
public class ExamScore {
    private Long examID; //书卷ID
    private Integer examTotalNum;//通过考试次数
    private Integer quitTimes; //返回资料观看次数、
    private Integer scores;  //分数
}
