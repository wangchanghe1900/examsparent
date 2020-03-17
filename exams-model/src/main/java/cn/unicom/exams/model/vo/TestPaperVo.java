package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysTestpaper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-03-02 18:51
 */
@Data
public class TestPaperVo extends SysTestpaper {
    private String userName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String imginfo;

    private String resourceName;
}
