package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysEmployee;
import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-04-10 20:29
 */
@Data
public class DeptEmpInfo {
    private Long id;//部门ID
    private String deptName;//部门名称;
    private List<SysEmployee> empList;//员工信息
}
