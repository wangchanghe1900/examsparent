package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.entity.SysUser;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2019-12-31 16:58
 */
@Data
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
}
