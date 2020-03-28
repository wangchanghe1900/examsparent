package cn.unicom.exams.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-26 12:03
 */
@Data
public class EmpInfo {
    /**
     * 员工ID
     */
    private Long id;
    /**
     *员工姓名
     */
    private String empName;
    /**
     *员工所属（部门|组）ID
     */
    private Long deptId;
    /**
     *员工所属部门|组名称
     */
    private String deptName;
    /**
     *员工图片
     */
    private String empImg;

    /**
     * 员工Code
     */
    @JsonIgnore
    private String empCode;
    /**
     * 员工密码
     */
    @JsonIgnore
    private String password;
    /**
     * 验证码
     */
    @JsonIgnore
    private String valicode;
}
