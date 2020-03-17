package cn.unicom.exams.model.vo;

import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-03 21:43
 */
@Data
public class DeptResourceInfo {
    private Long id;
    private Long parent_id;
    private String deptname;
    private Long rid;
    private String resourceName;
    private String resourceTypes;
    private Long dept_id;

}
