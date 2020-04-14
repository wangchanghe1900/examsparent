package cn.unicom.exams.model.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-03-27 18:40
 */
@Data
public class LearnedMaterial {
    //学习材料ID、学习材料类型、名称、学习材料url、试卷ID、试卷名称、试卷题目数、考题ID、学习材料的图片、发布时间、学习次数、学习时长(分钟）、考试次数、最高分数、上一次分数
    private Long materialID;
    private Integer materialType;
    private String materialName;
    private String materialURL;
    private Long examID;
    private String examName;
    private Integer examNum;
    private String materialImg;
    private LocalDateTime publishDate;
    private Integer studyTimes;
    private Integer studyDuration;
    private Integer examTimes;
    private Float maxScores;
    private Float lastScores;

}
