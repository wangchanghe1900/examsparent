package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysEmployee;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-17 14:48
 */
@Data
public class EmployeeInfo extends SysEmployee {
    /**
     * 部门信息
     */
    private SysDept sysDept;

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
