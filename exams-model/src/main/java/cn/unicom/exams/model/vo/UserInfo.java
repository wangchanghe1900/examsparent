package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.entity.SysUser;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 王长何
 * @create 2019-12-31 16:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends SysUser {
    @TableField(exist = false)
    private String deptname;

    /**
     * 部门信息
     */
    @TableField(exist = false)
    private SysDept sysDept;

    /**
     * 角色信息
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 编辑按钮权限
     */
    @TableField(exist = false)
    private Boolean isEdit;
    /**
     * 删除按钮权限
     */
    @TableField(exist = false)
    private Boolean isDel;

    /**
     * 重置密码按钮权限
     */
    @TableField(exist = false)
    private Boolean isResetPwd;

    /**
     * 新增按钮权限
     */
    @TableField(exist = false)
    private Boolean isAdd;

    /**
     * 更新按钮权限
     */
    @TableField(exist = false)
    private Boolean isUpdate;
}
