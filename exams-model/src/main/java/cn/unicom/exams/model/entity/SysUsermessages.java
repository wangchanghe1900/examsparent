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
public class SysUsermessages implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息创建人
     */
    @TableField("sendUser")
    private String sendUser;

    /**
     * 消息内容
     */
    @TableField("messageContent")
    private String messageContent;

    /**
     * 接收人ID
     */
    @TableField("receviceUserId")
    private Long receviceUserId;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 发送日期
     */
    @TableField("createDate")
    private LocalDateTime createDate;

    /**
     * 是否已读 是|否
     */
    @TableField("isRead")
    private String isRead;


}
