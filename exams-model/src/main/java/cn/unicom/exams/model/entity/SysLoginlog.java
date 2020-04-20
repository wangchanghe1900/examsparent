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
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLoginlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录工号
     */
    @TableField("userCode")
    private String userCode;

    /**
     * 登录状态
     */
    @TableField("loginStatus")
    private String loginStatus;

    /**
     * 请求路径
     */
    @TableField("requestPath")
    private String requestPath;

    /**
     * 请求地址
     */
    @TableField("requestAddress")
    private String requestAddress;

    /**
     * 登录日期
     */
    @TableField("loginDateTime")
    private LocalDateTime loginDateTime;


}
