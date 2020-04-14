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
public class SysDeptduration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 学习资源ID
     */
    private Long resId;

    /**
     * 学习资源ID
     */
    @TableField("totalEmployee")
    private Integer totalEmployee;
    /**
     * 学习总时长
     */
    @TableField("learnCount")
    private Long learnCount;

    /**
     * 人均时长
     */
    @TableField("AvgCount")
    private Long AvgCount;


}
