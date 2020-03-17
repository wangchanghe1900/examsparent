package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.entity.SysTestpaper;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-02 18:51
 */
@Data
public class TestPaperInfo extends SysTestpaper {
    /**
     * 资源信息
     */
    private SysResourceinfo resourceinfo;
    /**
     * 适用考试部门
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
}
