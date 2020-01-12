package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.Response;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author wangchanghe
 * @since 2019-12-14
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 跟据条件查询用户信息包含权限等
     * @param page
     * @param limit
     * @param userVo
     * @return
     */
    public IPage<UserInfo> getSysUserByPage(int page, int limit, UserVo userVo);

    /**
     * 跟据条件查询用户基本信息
     * @param page
     * @param limit
     * @param userVo
     * @return
     */
    public IPage<UserInfo> getUserInfoByPage(int page, int limit, UserVo userVo);

    /**
     * 根据用户条件信息查询用户信息
     * @param userVo
     * @return
     */
    public UserInfo getUserVoByCondition(UserVo userVo);

    /**
     * 增加用户
     * @param userVo
     * @return
     */
    public Response addUser(UserVo userVo);

    /**
     * 更新用户信息
     * @param userVo
     * @return
     */
    public Response updateUser(UserVo userVo);


}
