package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysTestquestions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷id
     */
    private Long tId;

    /**
     * 问题id 
     */
    private Long qId;

    /**
     * 用户ID
     */
    private Long empCode;

    /**
     * 答题状态（已答、未答）
     */
    private String status;
    /**
     * 答题结果
     */
    private String answer;

    /**
     * 答题日期
     */
    @TableField("answerTime")
    private LocalDateTime answerTime;


}
