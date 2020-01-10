package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-07 13:22
 */
@Data
public class NavsMenuInfo {
    /**
     * 父菜单名称
     */
    private String parentName;
    /**
     * 子菜单列表
     */
    private List<NavsInfo> navsList;

}
