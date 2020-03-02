package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.entity.SysUser;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-02-26 12:30
 */
@Data
public class ResourceInfo extends SysResourceinfo {
    /**
     * 资源所属部门
     */
    @TableField(exist = false)
    private SysDept sysDept;
    /**
     * 上传资源人
     */
    @TableField(exist = false)
    private SysUser user;

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
     * 查看详情按钮权限
     */
    private Boolean isDetail;

}
