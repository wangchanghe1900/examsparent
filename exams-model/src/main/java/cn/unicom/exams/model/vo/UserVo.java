package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.entity.SysUser;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 王长何
 * @create 2019-12-31 8:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends SysUser implements Serializable {
    private static final long serialVersionUID = -5102427442733321505L;
    /**
     * 部门信息
     */
    @TableField(exist = false)
    private String deptname;
    /**
     * 用户权限ID
     */
    @TableField(exist = false)
    private String  roles;

    /**
     * 验证码
     */
    @TableField(exist = false)
    private String  code;





}
