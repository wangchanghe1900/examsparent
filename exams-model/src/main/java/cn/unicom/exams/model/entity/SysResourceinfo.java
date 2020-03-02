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
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
public class SysResourceinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资料名称
     */
    @TableField("resourceName")
    private String resourceName;

    /**
     * 资料类型（文本、音频、视频）
     */
    @TableField("resourceType")
    private String resourceType;

    /**
     * 资料存储地址
     */
    private String url;

    /**
     * 上传用户ID
     */
    private Long uId;

    /**
     * 所属部门（班组）
     */
    private Long deptId;

    /**
     * 上传日期
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;


}
