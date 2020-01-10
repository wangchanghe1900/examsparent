package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上级部门ID，一级部门为0
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptname;

    /**
     * 部门描述
     */
    private String content;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private Integer delFlag;


}
