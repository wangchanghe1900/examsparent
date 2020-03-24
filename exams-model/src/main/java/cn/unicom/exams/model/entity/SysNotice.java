package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否发布（发布、未发布）
     */
    private String status;

    /**
     * 创建人
     */
    @TableField("createUser")
    private String createUser;

    /**
     * 接收部门
     */
    @TableField("deptName")
    private String deptName;

    /**
     * 已读人数
     */
    @TableField("readerCount")
    private Integer readerCount;

    /**
     * 接收人数
     */
    @TableField("receiveCount")
    private Integer receiveCount;
    /**
     * 系统用户是否接收
     */
    @TableField("isSendSysUser")
    private String isSendSysUser;

    /**
     * 系统用户是否接收
     */
    @TableField("isSendEmp")
    private String isSendEmp;

    /**
     * 公告创建日期
     */
    @TableField("createTime")
    private LocalDateTime createTime;


}
