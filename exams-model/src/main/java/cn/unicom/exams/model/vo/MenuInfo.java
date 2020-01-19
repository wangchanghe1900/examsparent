package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-19 15:48
 */
@Data
public class MenuInfo {
    private String title;
    private Long id;
    private List<MenuInfo> children;
}
