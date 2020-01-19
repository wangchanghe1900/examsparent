package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysRole;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-01-14 11:42
 */
@Data
public class RoleInfo extends SysRole {
    private Integer value;
   /* private String name;*/
    private String selected;
    private String disabled;
    /**
     * 编辑按钮权限
     */
    private Boolean isEdit;
    /**
     * 删除按钮权限
     */
    private Boolean isDel;

    /**
     * 重置密码按钮权限
     */
    private Boolean isSetPower;

    /**
     * 新增按钮权限
     */
    private Boolean isAdd;

    /**
     * 更新按钮权限
     */
    private Boolean isUpdate;
}
