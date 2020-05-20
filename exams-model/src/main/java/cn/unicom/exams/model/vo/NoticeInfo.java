package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysNotice;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-21 13:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeInfo extends SysNotice {
    /**
     * 发送部门名称
     */
    private List<String> deptList=new ArrayList<>();
    /**
     * 发送部门ID
     */
    private List<Long> deptIds=new ArrayList<>();
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
    @TableField(exist = false)
    private Boolean isDetail;
}
