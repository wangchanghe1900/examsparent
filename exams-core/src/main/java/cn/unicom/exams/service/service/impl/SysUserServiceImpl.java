package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.service.mapper.SysUserMapper;
import cn.unicom.exams.service.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author wangchanghe
 * @since 2019-12-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public IPage<UserInfo> getSysUserByPage(int page, int limit, UserVo userVo) {
        Page<UserVo> ipage=new Page<>(page,limit);
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if(userVo!= null) {
            queryWrapper.eq(StringUtils.isNotEmpty(userVo.getUsername()),"username", userVo.getUsername())
                    .likeRight(StringUtils.isNotEmpty(userVo.getRealname()),"realname",userVo.getRealname())
                    .eq(StringUtils.isNotEmpty(userVo.getMobile()),"mobile", userVo.getMobile());
        }
        queryWrapper.orderByDesc("a.id");
        return sysUserMapper.getSysUserByPage(ipage,queryWrapper);
    }

    @Override
    public IPage<UserInfo> getUserInfoByPage(int page, int limit, UserVo userVo) {
        Page<UserVo> ipage=new Page<>(page,limit);
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if(userVo!= null) {
            queryWrapper.eq(StringUtils.isNotEmpty(userVo.getUsername()),"username", userVo.getUsername())
                    .likeRight(StringUtils.isNotEmpty(userVo.getRealname()),"realname",userVo.getRealname())
                    .eq(StringUtils.isNotEmpty(userVo.getMobile()),"mobile", userVo.getMobile());
        }
        queryWrapper.orderByDesc("a.id");
        return sysUserMapper.getUserInfoByPage(ipage,queryWrapper);
    }

    @Override
    public UserInfo getUserVoByCondition(UserVo userVo) {
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(userVo.getUsername()),"username",userVo.getUsername())
                .likeRight(StringUtils.isNotEmpty(userVo.getRealname()),"realname",userVo.getRealname())
                 .eq(StringUtils.isNotEmpty(userVo.getMobile()),"mobile",userVo.getMobile());
        return sysUserMapper.getUserInfoByCondition(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUser(UserVo userVo) {
        SysUser user=new SysUser();
        user.setUsername(userVo.getUsername());
        user.setEmail(userVo.getEmail());
        user.setMobile(userVo.getMobile());
        user.setRealname(userVo.getRealname());
        user.setStatus(userVo.getStatus());
        return null;
    }
}
