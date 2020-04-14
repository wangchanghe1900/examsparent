package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysLearnduration;
import cn.unicom.exams.model.vo.LearnedResourceEmpInfo;
import cn.unicom.exams.model.vo.UnLearnResource;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.mapper.SysLearndurationMapper;
import cn.unicom.exams.service.service.ISysLearndurationService;
import cn.unicom.exams.service.utils.DeptUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class SysLearndurationServiceImpl extends ServiceImpl<SysLearndurationMapper, SysLearnduration> implements ISysLearndurationService {
    @Resource
    private SysLearndurationMapper learndurationMapper;

    @Resource
    private SysDeptMapper deptMapper;
    @Override
    public void saveLearnInfo(Long empID, Long testID, Long resourceID, Integer learnDuration) throws Exception {
        SysLearnduration learned=new SysLearnduration();
        learned.setEmpCode(empID);
        learned.setTId(testID);
        learned.setResId(resourceID);
        learned.setLearnLong(learnDuration);
        learned.setLearnTime(LocalDateTime.now());
        learndurationMapper.insert(learned);
    }

    @Override
    public IPage<LearnedResourceEmpInfo> getLearnedResEmpByPage(int page, int limit, Long deptID) throws Exception {
        Page<SysLearnduration> ipage=new Page<>(page,limit);
        QueryWrapper<SysLearnduration> queryWrapper=new QueryWrapper<>();
        List<SysDept> deptList = deptMapper.selectList(null);
        if(deptID!=null){
            List<Long> idList = DeptUtils.getDeptInfoByParentID(deptList, deptID);
            idList.add(deptID);
            queryWrapper.in("c.id",idList);
        }
        queryWrapper.orderByAsc("c.id");

        return learndurationMapper.getLearnedResEmpByPage(ipage,queryWrapper);
    }
}
