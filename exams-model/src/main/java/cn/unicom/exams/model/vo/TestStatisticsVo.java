package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysTeststatistics;
import lombok.Data;

/**
 * @author 王长何
 * @create 2020-04-09 14:16
 */
@Data
public class TestStatisticsVo extends SysTeststatistics {
    private String testName;
}
