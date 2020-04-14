package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysTestpaper;
import cn.unicom.exams.model.entity.SysTeststatistics;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-04-09 13:20
 */
@Data
public class TestStatisticsInfo extends SysTeststatistics {
    private SysTestpaper testpaper;
    private String deptName;
}
