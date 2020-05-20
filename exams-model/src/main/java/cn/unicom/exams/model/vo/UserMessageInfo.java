package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysNotice;
import cn.unicom.exams.model.entity.SysUsermessages;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-23 18:09
 */
@Data
public class UserMessageInfo extends SysUsermessages {
    /**
     * 系统公告信息
     */
    private SysNotice notice;

    /**
     * 接收人信息
     */
    private String realName;
}
