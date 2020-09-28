package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.entity.SysRoleMenu;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.service.mapper.SysRoleMapper;
import cn.unicom.exams.service.mapper.SysRoleMenuMapper;
import cn.unicom.exams.service.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    @Override
    public List<SysRole> getRoleInfoByUId(Long uId) throws Exception {
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("u.user_id",uId);
        return roleMapper.getRoleInfoByUId(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleById(Long id) throws Exception {
        roleMapper.deleteById(id);
        //int i=9/0;
        QueryWrapper<SysRoleMenu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        roleMenuMapper.delete(queryWrapper);
        return true;
    }
}
