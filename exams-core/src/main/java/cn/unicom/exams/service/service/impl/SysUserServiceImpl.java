package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.entity.SysUserRole;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.service.mapper.SysUserMapper;
import cn.unicom.exams.service.mapper.SysUserRoleMapper;
import cn.unicom.exams.service.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

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

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Override
    public IPage<UserInfo> getSysUserByPage(int page, int limit, UserVo userVo) {
        try{
            return getUserInfo(page, limit, userVo);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public IPage<UserInfo> getUserInfoByPage(int page, int limit, UserVo userVo) {
            try{
                return getUserInfo(page, limit, userVo);
            }catch (Exception e){
                return null;
            }
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
    public Response addUser(UserVo userVo) {
        SysUser user=new SysUser();
        try{
            if(userVo!=null && userVo.getUsername()!=null){
                QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("username",userVo.getUsername());
                List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
                if(sysUsers.size()>=1){
                   return new Response(500,"用户名已存在！");
                }else{
                    user.setUsername(userVo.getUsername());
                    user.setEmail(userVo.getEmail());
                    user.setMobile(userVo.getMobile());
                    user.setRealname(userVo.getRealname());
                    user.setStatus(userVo.getStatus());
                    user.setDeptId(userVo.getDeptId());
                    user.setLastmdpasstime(LocalDateTime.now());
                    user.setPassword(userVo.getPassword());
                    user.setSalt(userVo.getSalt());
                    int result = sysUserMapper.insert(user);
                    String roles = userVo.getRoles();
                    String[] rolearray = roles.split(",");
                    for(String s:rolearray ){
                        SysUserRole sysUserRole=new SysUserRole();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(Long.valueOf(s));
                        sysUserRoleMapper.insert(sysUserRole);

                    }
                    return new Response(200,"用户添加成功！");
                }
            }else{
                return new Response(500,"用户名为空！！");
            }

        }catch (Exception e){
            return new Response(500,"系统错误，请于系统管理员联系！");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateUser(UserVo userVo) {
        try {
            if(userVo==null){
                return new Response(500,"更新信息为空！");
            }
            if( userVo.getId()==null){
                return new Response(500,"用户ID为空！");
            }else {
                SysUser user=new SysUser();
                user.setId(userVo.getId());
                user.setDeptId(userVo.getDeptId());
                user.setStatus(userVo.getStatus());
                user.setMobile(userVo.getMobile());
                user.setUsername(userVo.getUsername());
                user.setRealname(userVo.getRealname());
                user.setEmail(userVo.getEmail());
                int result = sysUserMapper.updateById(user);
                UpdateWrapper<SysUserRole> updataWrapper=new UpdateWrapper<>();
                updataWrapper.eq("user_id",userVo.getId());
                sysUserRoleMapper.delete(updataWrapper);
                String roles = userVo.getRoles();
                String[] rolearray = roles.split(",");
                for(String s:rolearray ){
                    SysUserRole sysUserRole=new SysUserRole();
                    sysUserRole.setUserId(userVo.getId());
                    sysUserRole.setRoleId(Long.valueOf(s));
                    sysUserRoleMapper.insert(sysUserRole);

                }
                return new Response(200,"用户更新成功！");

            }

        }catch (Exception e){
            return new Response(500,"更新失败，请于系统管理员联系！");
        }
    }

    private IPage<UserInfo> getUserInfo(int page, int limit, UserVo userVo) throws Exception{
        Page<UserVo> ipage=new Page<>(page,limit);
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if(userVo!= null) {
            queryWrapper.ne("username","admin").eq(StringUtils.isNotEmpty(userVo.getUsername()),"username", userVo.getUsername())
                    .likeRight(StringUtils.isNotEmpty(userVo.getRealname()),"realname",userVo.getRealname())
                    .eq(StringUtils.isNotEmpty(userVo.getMobile()),"mobile", userVo.getMobile());
        }
        queryWrapper.orderByDesc("a.id");
        return sysUserMapper.getSysUserByPage(ipage,queryWrapper);
    }
}
