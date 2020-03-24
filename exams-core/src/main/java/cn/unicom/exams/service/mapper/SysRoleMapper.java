package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户ID查询角色
     * @param uId
     * @return
     * @throws Exception
     */
    public List<SysRole> getRoleInfoByUId(@Param(Constants.WRAPPER) Wrapper<UserVo> wrapper) throws Exception;


}
