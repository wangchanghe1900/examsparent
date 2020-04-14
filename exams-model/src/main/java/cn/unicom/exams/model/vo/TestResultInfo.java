package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-31 10:52
 */
@Data
public class TestResultInfo {
    private Long empID;  //员工ID
    private Long examID;  //试卷ID
    private Integer totalNum; //试题总数
    private Integer answerNum; //答题数量
    private Integer quitTimes; //返回次数
    private Integer duration; //答题时长(分)
    private Integer passNum;//考试通过分数
    private List<AnswerInfo> optionList;  //答案列表（考题ID，答题选项（可未空）

}
