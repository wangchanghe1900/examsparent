package cn.unicom.exams.model.entity;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王长河
 * @since 2020-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class SysEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码作为代码
     */
    @TableField("employeeCode")
    private String employeeCode;

    /**
     * 姓名
     */
    @TableField("employeeName")
    private String employeeName;

    /**
     * 部门Id
     */
    private Long deptId;

    /**
     * 职位
     */
    private String capacity;

    /**
     * 身份证
     */
    @TableField("cardId")
    private String cardId;

    /**
     * 银行卡号
     */
    @TableField("bankCard")
    private String bankCard;

    /**
     * 银行名称
     */
    @TableField("bankName")
    private String bankName;

    /**
     * 昵称
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 固定办公电话
     */
    @TableField("officePhone")
    private String officePhone;

    /**
     * 手机号
     */
    private Long mobile;

    /**
     * 入职时间
     */
    @TableField("entryTime")
    private LocalDate entryTime;

    /**
     * 合同起始日期
     */
    @TableField("contractSTime")
    private LocalDate contractSTime;

    /**
     * 合同终止日期
     */
    @TableField("contractETime")
    private LocalDate contractETime;

    /**
     * 主要工作
     */
    private String mainwork;

    /**
     * 民族
     */
    private String nation;

    /**
     * 籍贯
     */
    @TableField("nativePlace")
    private String nativePlace;

    /**
     * 政治面貌
     */
    private String political;

    /**
     * 学历
     */
    private String education;

    /**
     * 毕业院校
     */
    @TableField("graduateSchool")
    private String graduateSchool;

    /**
     * 专业
     */
    private String major;

    /**
     * 毕业日期
     */
    @TableField("graduateDate")
    private LocalDate graduateDate;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    @TableField("linkPhone")
    private String linkPhone;

    /**
     * 户口所在地
     */
    @TableField("registeredPlace")
    private String registeredPlace;

    /**
     * 派遣公司
     */
    @TableField("sendCompany")
    private String sendCompany;

    /**
     * 派遣工号1
     */
    @TableField("jobNumber1")
    private String jobNumber1;

    /**
     * 派遣工号2
     */
    @TableField("jobNumber2")
    private String jobNumber2;

    /**
     * 工时核算,标准|综合
     */
    @TableField("workType")
    private String workType;

    /**
     * 培训期数
     */
    @TableField("trainNum")
    private String trainNum;

    /**
     * 身份,实习|正式
     */
    private String identitys;

    /**
     * 学历编号
     */
    @TableField("educationNum")
    private String educationNum;

    /**
     * 饭卡号
     */
    @TableField("mealCard")
    private String mealCard;

    /**
     * 登录密码
     */
    @TableField("password")
    @JsonIgnore
    private String password;

    /**
     * 盐
     */
    @TableField("salt")
    @JsonIgnore
    private String salt;

    /**
     * 用户状态
     */
    @TableField("employeeStatus")
    private String employeeStatus;

    /**
     * 修改密码日期
     */
    @TableField("modfiyPwdTime")
    private LocalDateTime modfiyPwdTime;

    /**
     *
     * 最后登录日期
     */
    @TableField("lastLoginTime")
    private LocalDateTime lastLoginTime;

    /**
     * 更新日期
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 创建日期
     */
    @TableField("createTime")
    private LocalDateTime createTime;


}
