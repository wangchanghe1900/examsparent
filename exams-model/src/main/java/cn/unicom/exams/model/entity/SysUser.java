package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author wangchanghe
 * @since 2019-12-14
 */
@Data
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 登录用户名
     */
    @NotBlank(message="登录名不能为空")
    private String username;

    /**
     * 真实姓名
     */
    @NotBlank(message="姓名不能为空")
    private String realname;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message="手机号不能为空")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;

    /**
     * 最后修改密码时间
     */
    private LocalDateTime lastmdpasstime;
    /**
     * 部门ID
     */
    @NotBlank(message="部门不能为空")
    private Long deptId;
    /**
     * 最后登录时间
     */
    //@TableField(fill=FieldFill.UPDATE)
    private LocalDateTime lastlogintime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createtime;



}
