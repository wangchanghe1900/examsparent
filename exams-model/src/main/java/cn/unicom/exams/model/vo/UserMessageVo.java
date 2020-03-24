package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysUsermessages;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-03-23 18:03
 */
@Data
public class UserMessageVo extends SysUsermessages {
    private String title;
    private String userCode;
    private String userName;
}
