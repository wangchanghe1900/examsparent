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
