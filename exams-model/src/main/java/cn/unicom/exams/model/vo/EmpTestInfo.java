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
    private Integer materialtotalnum;

    /**
     * 已学习的材料数量
     */
    private Integer studynum;

    /**
     * 本学员需要学习的所有考试数量
     */
    private Integer examtotalnum;

    /**
     * 已经考试的数量
     */
    private Integer examnum;

    /**
     * 已参加观看文章和视频总时长(分钟数）
     */
    private Integer studytimes;

    /**
     * 已观看文章和视频总次数
     */
    private Integer studyduration;

    /**
     * 已考试总次数
     */
    private Integer examtimes;
}
