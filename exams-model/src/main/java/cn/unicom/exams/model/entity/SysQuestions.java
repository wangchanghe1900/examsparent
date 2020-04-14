package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class SysQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目名称   (非空)
     */
    @TableField("questionName")
    private String questionName;

    /**
     * 题目类型
     */
    @TableField("questionType")
    private String questionType;

    /**
     * 资源ID
     */
    private Long resId;

    /**
     * 题目状态
     */
    @TableField("questionStatus")
    private String questionStatus;

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
     * 排序索引
     */
    @TableField("sortId")
    private Integer sortId;
    /**
     * 答案   (非空)
     */
    @TableField("qAnswer")
    private String qAnswer;


}
