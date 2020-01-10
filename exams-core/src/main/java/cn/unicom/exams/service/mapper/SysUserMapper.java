package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author wangchanghe
 * @since 2019-12-14
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据条件查询用户信息,返回嵌套信息如：List<Role>
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<UserInfo> getSysUserByPage(Page<UserVo> page, @Param(Constants.WRAPPER) Wrapper<UserVo> wrapper);

    /**
     * 根据条件查询用户基本信息
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<UserInfo> getUserInfoByPage(Page<UserVo> page, @Param(Constants.WRAPPER) Wrapper<UserVo> wrapper);

    /**
     * 根据条件查询用户信息
     * @param wrapper
     * @return
     */
    public UserInfo getUserInfoByCondition(@Param(Constants.WRAPPER) Wrapper<UserVo> wrapper);

}
