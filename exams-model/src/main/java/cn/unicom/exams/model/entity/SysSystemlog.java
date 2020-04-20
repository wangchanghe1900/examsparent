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
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysSystemlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号
     */
    @TableField("userCode")
    private String userCode;

    /**
     * 请求URL
     */
    @TableField("requestURL")
    private String requestURL;

    /**
     * 请求地址
     */
    @TableField("requestAddress")
    private String requestAddress;

    /**
     * 操作类型
     */
    @TableField("operatorType")
    private String operatorType;

    /**
     * 操作日期
     */
    @TableField("operatorDateTime")
    private LocalDateTime operatorDateTime;

    /**
     * 操作内容
     */
    private String content;


}
