package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-30 11:45
 */
@Data
public class ExamInfo {
    private Long empID;
    private Long examID;
    private Integer pageNum;
    private Integer totalNum;
    private Long materialID;
    private Integer materialType;
    private String materialURL;
    private List<TestQuestionInfo> questionsList;
}
