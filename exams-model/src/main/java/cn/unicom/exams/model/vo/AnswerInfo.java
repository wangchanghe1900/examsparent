package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-31 11:00
 */
@Data
public class AnswerInfo {
    private Integer questNo; //考题ID
    private String answer;  //答题选项
}
