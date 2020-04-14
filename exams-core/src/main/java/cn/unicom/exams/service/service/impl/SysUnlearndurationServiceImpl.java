package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysUnlearnduration;
import cn.unicom.exams.model.vo.UnLearnResource;
import cn.unicom.exams.model.vo.UnlearnEmpInfo;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.mapper.SysUnlearndurationMapper;
import cn.unicom.exams.service.service.ISysUnlearndurationService;
import cn.unicom.exams.service.utils.DeptUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Service
public class SysUnlearndurationServiceImpl extends ServiceImpl<SysUnlearndurationMapper, SysUnlearnduration> implements ISysUnlearndurationService {
    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;

    @Resource
    private SysDeptMapper deptMapper;
    @Override
    public IPage<UnlearnEmpInfo> getUnlearnResEmpInfoByPage(int page, int limit, Long deptID) throws Exception {
        Page<UnLearnResource> ipage=new Page<>(page,limit);
        QueryWrapper<UnLearnResource> queryWrapper=new QueryWrapper<>();
        List<SysDept> deptList = deptMapper.selectList(null);
        if(deptID != null){
            List<Long> idList = DeptUtils.getDeptInfoByParentID(deptList, deptID);
            idList.add(deptID);
            queryWrapper.in("c.id",idList);
        }
        queryWrapper.orderByAsc("c.id");
        return unlearndurationMapper.getUnlearnResEmpInfoByPage(ipage, queryWrapper);
    }
}
