package cn.unicom.exams.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-01-16 15:37
 */
@Data
public class ButtonInfo {
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
    private Boolean isResetPwd;

    /**
     * 新增按钮权限
     */
    private Boolean isAdd;


    /**
     * 更新按钮权限
     */
    private Boolean isUpdate;
    /**
     * 查找按钮权限
     */
    private Boolean isFind;
    /**
     * 批量删除按钮权限
     */
    private Boolean isBatchDel;

    /**
     * 设置权限按钮权限
     */
    private Boolean isSetPower;
}
