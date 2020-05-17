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
    private Long empID;
    /**
     *
     *登录名称
     */
    private String userName;


    /**
     *员工所属（部门|组）ID
     */
    private Long deptID;
    /**
     *员工所属部门|组名称
     */
    private String deptName;
    /**
     *员工图片
     */
    private String userImg;

    /**
     * 员工Code
     */
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
    private String validcode;
    /**
     * 用户姓名
     */
    private String empName;
}
