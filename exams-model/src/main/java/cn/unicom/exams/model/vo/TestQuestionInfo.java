package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-30 11:53
 */
@Data
public class TestQuestionInfo {
    private Long questionID;
    private String questionType;
    private String question;
    private List<OptionsInfo> options;
}
