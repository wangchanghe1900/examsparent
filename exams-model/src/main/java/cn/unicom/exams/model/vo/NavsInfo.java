package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-07 13:25
 */
@Data
public class NavsInfo {
    /**
     * 导航菜单名称
     */
    private String title;
    /**
     * 导航菜单图标
     */
    private String icon;
    /**
     * 导航菜单链接
     */
    private String href;
    /**
     * 导航菜单是否展开
     */
    private Boolean spread;
    /**
     * 子菜单
     */
    private List<NavsInfo> children;
}
