package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.vo.ResourceInfo;
import cn.unicom.exams.model.vo.ResourceVo;
import cn.unicom.exams.service.mapper.SysResourceinfoMapper;
import cn.unicom.exams.service.service.ISysResourceinfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-02-26
 */
@Service
public class SysResourceinfoServiceImpl extends ServiceImpl<SysResourceinfoMapper, SysResourceinfo> implements ISysResourceinfoService {

    @Resource
    private SysResourceinfoMapper sysResourceinfoMapper;

    @Override
    public IPage<ResourceInfo> getResourceInfoByPage(int page, int limit, ResourceVo resourceVo) throws Exception{
        Page<ResourceVo> ipage=new Page<>(page,limit);
        QueryWrapper<ResourceVo> queryWrapper=new QueryWrapper<>();
        if(resourceVo!=null){
            if(!resourceVo.getUserName().equals("admin")) {
                queryWrapper.eq(resourceVo.getDeptId() != null, "a.dept_id", resourceVo.getDeptId())
                        .eq(StringUtils.isNotEmpty(resourceVo.getResourceType()), "a.resourceType", resourceVo.getResourceType())
                        .likeRight(StringUtils.isNotEmpty(resourceVo.getResourceName()), "a.resourceName", resourceVo.getResourceName());
            }else{
                queryWrapper.eq(StringUtils.isNotEmpty(resourceVo.getResourceType()), "a.resourceType", resourceVo.getResourceType())
                        .likeRight(StringUtils.isNotEmpty(resourceVo.getResourceName()), "a.resourceName", resourceVo.getResourceName());
            }
        }
        queryWrapper.orderByDesc("a.createTime").orderByDesc("a.id");
        return sysResourceinfoMapper.getResourceInfoByPage(ipage,queryWrapper);
    }
}
