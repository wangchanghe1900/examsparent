package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.EmployeeInfo;
import cn.unicom.exams.model.vo.EmployeeVo;
import cn.unicom.exams.service.mapper.SysEmployeeMapper;
import cn.unicom.exams.service.service.ISysEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长河
 * @since 2020-01-17
 */
@Service
public class SysEmployeeServiceImpl extends ServiceImpl<SysEmployeeMapper, SysEmployee> implements ISysEmployeeService {
    @Resource
    private SysEmployeeMapper employeeMapper;
    @Override
    public IPage<EmployeeInfo> getEmployeeInfoByPage(int page, int limit, EmployeeVo employeeVo) throws Exception {
        Page<EmployeeVo> ipage=new Page<>(page,limit);
        if(employeeVo==null){
            throw new Exception("员工查询信息为空!");
        }
        QueryWrapper<EmployeeVo> queryWrapper=new QueryWrapper<>();

        queryWrapper.eq(employeeVo.getDeptId()!=null,"e.dept_id",employeeVo.getDeptId())
                .likeRight(StringUtils.isNotEmpty(employeeVo.getEmployeeName()),"e.employeeName",employeeVo.getEmployeeName())
                .likeRight(employeeVo.getMobile()!=null,"e.mobile",employeeVo.getMobile())
                .likeRight(StringUtils.isNotEmpty(employeeVo.getCardId()),"e.cardId",employeeVo.getCardId())
                .ge(employeeVo.getEntryStartTime()!=null,"e.entryTime",employeeVo.getEntryStartTime())
                .le(employeeVo.getEntryEndTime()!=null,"e.entryTime",employeeVo.getEntryEndTime())
                .eq(StringUtils.isNotEmpty(employeeVo.getEmployeeStatus()),"e.employeeStatus",employeeVo.getEmployeeStatus())
                .eq(StringUtils.isNotEmpty(employeeVo.getWorkType()),"e.workType",employeeVo.getWorkType())
                .eq(StringUtils.isNotEmpty(employeeVo.getIdentitys()),"e.identitys",employeeVo.getIdentitys());

        queryWrapper.orderByDesc("e.id");
        return employeeMapper.getEmployeeInfoByPage(ipage,queryWrapper);
    }
}
