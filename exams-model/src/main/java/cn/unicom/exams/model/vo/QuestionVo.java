package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysQuestions;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王长何
 * @create 2020-03-11 14:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionVo extends SysQuestions {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 所属部门
     */
    private Long deptId;

    /**
     * 所属资源名称
     */
    private String resourceName;
    /**
     * 选项A
     */
    private String optionA;

    /**
     * 选项B
     */
    private String optionB;
    /**
     * 选项C
     */
    private String optionC;
    /**
     * 选项D
     */
    private String optionD;
    /**
     * 选项E
     */
    private String optionE;
    /**
     * 选项F
     */
    private String optionF;
    /**
     * 选项G
     */
    private String optionG;

    /**
     * 答案：A
     */
   private String qAnswerA;
    /**
     * 答案：B
     */
    private String qAnswerB;
    /**
     * 答案：C
     */
    private String qAnswerC;
    /**
     * 答案：D
     */
    private String qAnswerD;
    /**
     * 答案：E
     */
    private String qAnswerE;
    /**
     * 答案：F
     */
    private String qAnswerF;
    /**
     * 答案：G
     */
    private String qAnswerG;


}
