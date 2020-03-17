package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-03 19:16
 */
@Data
public class CheckResourceInfo {
    private String title;
    private Long id;
    private Boolean spread;
    private Boolean checked;
    private List<CheckResourceInfo> children;
}
