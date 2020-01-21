package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-21 14:32
 */
@Data
public class DeptInfo {
    private Long parentId;
    private String title;
    private Long id;
    private String content;
    private Integer orderNum;
    private Boolean spread;
    private Boolean checked;
    private List<DeptInfo> children;
}
