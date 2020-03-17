package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王长何
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class SysTestpaper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷名称 
     */
    @TableField("testName")
    private String testName;

    /**
     * 考试资料ID
     */
    private Long resId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 考题数量
     */
    @TableField("testCount")
    private Integer testCount;
    /**
     * 试卷状态（发布、未发布）
     */
    @TableField("imgUrl")
    private String imgUrl;
    /**
     * 试卷状态（发布、未发布）
     */
    @TableField("testStatus")
    private String testStatus;
    /**
     * 考试开始日期
     */
    @TableField("examsStartTime")
    private LocalDateTime examsStartTime;
    /**
     * 考试结束日期
     */
    @TableField("examsEndTime")
    private LocalDateTime examsEndTime;
    /**
     * 创建日期
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;
    /**
     * 试卷说明
     */
    private String remark;


}
