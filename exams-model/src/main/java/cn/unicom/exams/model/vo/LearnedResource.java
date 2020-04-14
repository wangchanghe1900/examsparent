package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-27 22:36
 */
@Data
public class LearnedResource {

    private  Long empID;
    private  Long departmentId;
    private Integer showNum;
    private Integer pageNum;
    private Long totalNum;
    private Integer examNum; //已考试的试卷数量
    private Integer examPassNum; //已达标（通过）的试卷数量
    private List<LearnedMaterial> learnedMaterialList;
}
