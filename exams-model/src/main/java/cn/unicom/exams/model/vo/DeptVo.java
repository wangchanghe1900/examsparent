package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-01-22 10:22
 */
@Data
public class DeptVo {
    private String parentName;
    private Long parentId;
    private String title;
    private Long id;
    private String content;
    private Integer ordernum;

}
