package cn.unicom.exams.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysTeststatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷ID
     */
    private Long tId;

    /**
     * 已考人数
     */
    @TableField("testCount")
    private Integer testCount;
    /**
     * 未考人数
     */
    @TableField("untestCount")
    private Integer untestCount;
    /**
     * 及格率
     */
    private Integer passrate;

    /**
     * 平均分
     */
    private Integer avgscore;

    /**
     * 优秀率
     */
    private Integer finerate;


}
