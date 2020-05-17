package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-04-01 19:04
 */
@Data
public class ParamsVo {
    private Long empID;//员工ID
    private Integer pageNum;//页数
    private Integer showNum;//显示多少条
    private Long examID;
    private Long resourceID;
    private Integer studyDuration;
    private Long materalID;
    private String oldPassword;
    private String newPassword;
}
