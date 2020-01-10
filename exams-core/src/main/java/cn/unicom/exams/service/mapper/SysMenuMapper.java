package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据用户名查询首页菜单信息
     * @param wrapper
     * @return
     */
    public List<SysMenu> getTopSysmenuByName(@Param(Constants.WRAPPER) Wrapper<UserVo> wrapper);
}
