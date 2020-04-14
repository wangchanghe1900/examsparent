package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-27 11:31
 */
@Data
public class EmpTestInfo {
    /**
     * 所有材料数量
     */
    private Integer materialTotalNum;

    /**
     * 已学习的材料数量
     */
    private Integer studyNum;

    /**
     * 本学员需要学习的所有考试数量
     */
    private Integer examTotalNum;

    /**
     * 已经考试的数量
     */
    private Integer examNum;

    /**
     * 已参加观看文章和视频总时长(分钟数）
     */
    private Integer studyTimes;

    /**
     * 已观看文章和视频总次数
     */
    private Integer studyDuration;

    /**
     * 已考试总次数
     */
    private Integer examTimes;
    /**
     * 考试已达标(通过)试卷数量
     */
    private Integer examPassNum;
}
