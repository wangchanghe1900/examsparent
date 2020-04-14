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
public class SysTestresult implements Serializable {

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
     * 用户id
     */
    private Long empCode;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 是否通过
     */
    private String outcome;
    /**
     * 答题时长(分)
     */
    @TableField("testDuration")
    private Integer testDuration;
    /**
     * 返回次数
     */
    @TableField("returnCount")
    private Integer returnCount;

    /**
     * 考试日期
     */
    @TableField("testTime")
    private LocalDateTime testTime;


}
