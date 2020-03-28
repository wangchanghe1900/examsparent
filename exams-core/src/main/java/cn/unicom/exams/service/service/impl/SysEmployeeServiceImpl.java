package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.service.mapper.*;
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

    @Resource
    private SysTestpaperMapper testpaperMapper;

    @Resource
    private SysLearndurationMapper learndurationMapper;

    @Resource
    private SysTestquestionsMapper testquestionsMapper;

    @Resource
    private SysTestresultMapper testresultMapper;

    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;
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

    @Override
    public EmpTestInfo getEmpTestInfoByEmpCode(Long empCode) throws Exception {
        EmpTestInfo empTestInfo=new EmpTestInfo();
        QueryWrapper<TestPaperVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("e.employeeCode",empCode);
        Integer materialtotalnum=testpaperMapper.getEmpTestCountByEmpCode(queryWrapper);
        empTestInfo.setMaterialtotalnum(materialtotalnum);
        empTestInfo.setExamtotalnum(materialtotalnum);
        QueryWrapper<SysLearnduration> lqw=new QueryWrapper<>();
        lqw.eq("l.emp_code",empCode);
        Integer empLearnCount = learndurationMapper.getEmpLearnResourceByEmpCode(lqw);
        empTestInfo.setStudynum(empLearnCount);
        QueryWrapper<SysTestquestions> tq=new QueryWrapper<>();
        tq.eq("q.emp_code",empCode).eq("q.status","已答");
        Integer empTestedCount = testquestionsMapper.getEmpTestedByEmpCode(tq);
        empTestInfo.setExamnum(empTestedCount);
        QueryWrapper<SysLearnduration> learnqw=new QueryWrapper<>();
        learnqw.eq("l.emp_code",empCode);
        Integer empLearnLongCount = learndurationMapper.getEmpLearnLongDurationByEmpCode(learnqw);
        empTestInfo.setStudyduration(empLearnLongCount);
        QueryWrapper<SysLearnduration> lnq=new QueryWrapper<>();
        lnq.eq("l.emp_code",empCode);
        Integer empLearnTimes = learndurationMapper.getEmpLearnTimesByEmpCode(lnq);
        empTestInfo.setStudytimes(empLearnTimes);
        QueryWrapper<SysTestresult> testresultQueryWrapper=new QueryWrapper<>();
        testresultQueryWrapper.eq("r.emp_code",empCode);
        Integer examTimesByEmpCode = testresultMapper.getEmpExamTimesByEmpCode(testresultQueryWrapper);
        empTestInfo.setExamtimes(examTimesByEmpCode);
        return empTestInfo;
    }

    @Override
    public UnLearnResource getUnLearnResourceByPage(int page, int limit, Long empCode) throws Exception {
        Page<UnLearnResource> ipage=new Page<>(page,limit);
        QueryWrapper<SysUnlearnduration> unlearnqw=new QueryWrapper<>();
        unlearnqw.eq("u.emp_code",empCode).orderByDesc("t.updateTime");
        IPage<Material> empUnlearnResourceByPage = unlearndurationMapper.getEmpUnlearnResourceByPage(ipage, unlearnqw);
        UnLearnResource unLearnResource=new UnLearnResource();
        unLearnResource.setPageNum(page);
        unLearnResource.setShowNum(limit);
        unLearnResource.setTotalNum(empUnlearnResourceByPage.getTotal());
        unLearnResource.setMaterialList(empUnlearnResourceByPage.getRecords());
        QueryWrapper<SysEmployee>  qw=new QueryWrapper<>();
        qw.eq("employeeCode",empCode);
        SysEmployee employee = employeeMapper.selectOne(qw);
        unLearnResource.setEmpID(empCode);
        unLearnResource.setDepartmentId(employee.getDeptId());
        return unLearnResource;
    }

    @Override
    public LearnedResource getLearnedResourceByPage(int page, int limit, Long empCode) throws Exception {
        Page<SysLearnduration> ipage=new Page<>(page,limit);
        QueryWrapper<SysLearnduration> qw=new QueryWrapper<>();
        qw.eq("u.emp_code",empCode).orderByDesc("t.updateTime");
        IPage<LearnedMaterial> empLearnedResourceByPage = learndurationMapper.getEmpLearnedResourceByPage(ipage, qw);
        LearnedResource learnedResource=new LearnedResource();
        learnedResource.setPageNum(page);
        learnedResource.setShowNum(limit);
        learnedResource.setTotalNum(empLearnedResourceByPage.getTotal());
        learnedResource.setLearnedMaterialList(empLearnedResourceByPage.getRecords());
        QueryWrapper<SysEmployee>  empqw=new QueryWrapper<>();
        empqw.eq("employeeCode",empCode);
        SysEmployee employee = employeeMapper.selectOne(empqw);
        learnedResource.setEmpID(empCode);
        learnedResource.setDepartmentId(employee.getDeptId());
        return learnedResource;
    }
}
