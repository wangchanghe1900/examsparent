package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.service.mapper.*;
import cn.unicom.exams.service.service.ISysEmployeeService;
import cn.unicom.exams.service.utils.MD5Utils;
import cn.unicom.exams.service.utils.SecurityCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private SysLearndurationMapper learndurationMapper;

    @Resource
    private SysTestquestionsMapper testquestionsMapper;

    @Resource
    private SysTestresultMapper testresultMapper;

    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private SysTeststatisticsMapper teststatisticsMapper;

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
        QueryWrapper<TestStatisticsVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("s.emp_code",empCode);
        Integer materialtotalnum=teststatisticsMapper.getEmpResourceCountByEmpCode(queryWrapper);//testpaperMapper.getEmpTestCountByEmpCode(queryWrapper);
        empTestInfo.setMaterialTotalNum(materialtotalnum);
        empTestInfo.setExamTotalNum(materialtotalnum);
        QueryWrapper<SysLearnduration> lqw=new QueryWrapper<>();
        lqw.eq("l.emp_code",empCode);
        Integer empLearnCount = learndurationMapper.getEmpLearnResourceByEmpCode(lqw);
        empTestInfo.setStudyNum(empLearnCount);
        QueryWrapper<SysTestquestions> tq=new QueryWrapper<>();
        tq.eq("q.emp_code",empCode).eq("q.status","已答");
        Integer empTestedCount = testquestionsMapper.getEmpTestedByEmpCode(tq);
        empTestInfo.setExamNum(empTestedCount);
        QueryWrapper<SysLearnduration> learnqw=new QueryWrapper<>();
        learnqw.eq("l.emp_code",empCode);
        Integer empLearnLongCount = learndurationMapper.getEmpLearnLongDurationByEmpCode(learnqw);
        empTestInfo.setStudyDuration(empLearnLongCount);
        QueryWrapper<SysLearnduration> lnq=new QueryWrapper<>();
        lnq.eq("l.emp_code",empCode);
        Integer empLearnTimes = learndurationMapper.getEmpLearnTimesByEmpCode(lnq);
        empTestInfo.setStudyTimes(empLearnTimes);
        QueryWrapper<SysTestresult> testresultQueryWrapper=new QueryWrapper<>();
        testresultQueryWrapper.eq("r.emp_code",empCode);
        Integer examTimesByEmpCode = testresultMapper.getEmpExamTimesByEmpCode(testresultQueryWrapper);
        empTestInfo.setExamTimes(examTimesByEmpCode);
        QueryWrapper<SysTestresult> resultqw=new QueryWrapper<>();
        resultqw.eq("emp_code",empCode);
        resultqw.eq("outcome","通过");
        Integer passCount = testresultMapper.getExamCountByEmpID(resultqw);
        empTestInfo.setExamPassNum(passCount);
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
        unLearnResource.setDepartmentID(employee.getDeptId());
        return unLearnResource;
    }

    @Override
    public LearnedResource getLearnedResourceByPage(int page, int limit, Long empCode) throws Exception {
        Page<SysLearnduration> ipage=new Page<>(page,limit);
        QueryWrapper<SysLearnduration> qw=new QueryWrapper<>();
        qw.eq("u.emp_code",empCode)
                .eq("tr.emp_code",empCode)
                .orderByDesc("t.updateTime");
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
        QueryWrapper<SysTestresult> resultqw=new QueryWrapper<>();
        resultqw.eq("emp_code",empCode);
        Integer examCount = testresultMapper.getExamCountByEmpID(resultqw);
        learnedResource.setExamNum(examCount);
        resultqw.eq("outcome","通过");
        Integer outcomeCount=testresultMapper.getExamCountByEmpID(resultqw);
        learnedResource.setExamPassNum(outcomeCount);
        return learnedResource;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer importEmpInfo(List<Map<String, Object>> maps) throws Exception {
        List<SysEmployee> empList=new ArrayList<>();
        for(Map<String,Object> empInfo:maps){
            Set<Map.Entry<String, Object>> entries = empInfo.entrySet();
            SysEmployee employee=new SysEmployee();
            for(Map.Entry<String, Object> info:entries){
                if("工号".equals(info.getKey())){
                    employee.setEmployeeCode(info.getValue().toString());
                    String salt = SecurityCode.getSecurityCode();
                    String pwd = MD5Utils.getAuthenticationInfo("Abcd#123!", salt);//初始密码：Abcd#123!
                    employee.setSalt(salt);
                    employee.setPassword(pwd);
                    employee.setEmployeeStatus("正常");
                    employee.setModfiyPwdTime(LocalDateTime.now());
                    employee.setUpdateTime(LocalDateTime.now());
                    employee.setLoginFailureTimes(0);
                    employee.setCreateTime(LocalDateTime.now());
                }
                if("手机号".equals(info.getKey())){
                    employee.setMobile(Long.valueOf(info.getValue().toString()));
                }
                if("姓名".equals(info.getKey())){
                    employee.setEmployeeName(info.getValue().toString());
                }
                if("部门".equals(info.getKey())){
                    String deptName=info.getValue().toString();
                    QueryWrapper<SysDept> qw=new QueryWrapper<>();
                    qw.eq("deptname",deptName);
                    SysDept sysDept = deptMapper.selectOne(qw);
                    if(sysDept == null){
                       throw new Exception("部门不正确");
                    }
                    employee.setDeptId(sysDept.getId());
                }
                if("职位".equals(info.getKey())){
                    employee.setCapacity(info.getValue().toString());
                }
                if("身份证".equals(info.getKey())){
                    employee.setCardId(info.getValue().toString());
                }
                if("银行卡号".equals(info.getKey())){
                    employee.setBankCard(info.getValue().toString());
                }
                if("银行名称".equals(info.getKey())){
                    employee.setBankName(info.getValue().toString());
                }
                if("昵称".equals(info.getKey())){
                    Object nickName=info.getValue();
                    if(nickName!=null){
                        employee.setNickName(info.getValue().toString());
                    }

                }
                if("固定办公电话".equals(info.getKey())){
                    Object officePhone=info.getValue();
                    if(officePhone!=null){
                        employee.setOfficePhone(info.getValue().toString());
                    }

                }
                if("入职时间".equals(info.getKey())){
                    Object entryTime=info.getValue();
                    if(entryTime!=null && !"".equals(entryTime)){
                        LocalDate entry=LocalDate.parse(info.getValue().toString().substring(0,10));
                        employee.setEntryTime(entry);
                    }

                }
                if("合同起始日".equals(info.getKey())){
                    Object contractTime=info.getValue();
                    if(contractTime!=null && !"".equals(contractTime)){
                        employee.setContractSTime(LocalDate.parse(info.getValue().toString().substring(0,10)));
                    }
                }
                if("合同终止日".equals(info.getKey())){
                    Object contractEndTime=info.getValue();
                    if(contractEndTime!=null && !"".equals(contractEndTime)){
                        employee.setContractETime(LocalDate.parse(info.getValue().toString().substring(0,10)));
                    }
                }
                if("主要工作".equals(info.getKey())){
                    Object mainwork=info.getValue();
                    if(mainwork!=null){
                        employee.setMainwork(info.getValue().toString());
                    }

                }
                if("民族".equals(info.getKey())){
                    Object natives=info.getValue();
                    if(natives!=null){
                        employee.setNation(info.getValue().toString());
                    }

                }
                if("籍贯".equals(info.getKey())){
                    Object nativePlace=info.getValue();
                    if(nativePlace!=null){
                        employee.setNativePlace(info.getValue().toString());
                    }

                }
                if("政治面貌".equals(info.getKey())){
                    Object political=info.getValue();
                    if(political!=null){
                        employee.setPolitical(info.getValue().toString());
                    }

                }
                if("最高学历".equals(info.getKey())){
                    Object education=info.getValue();
                    if(education!=null){
                        employee.setEducation(info.getValue().toString());
                    }

                }
                if("毕业院校".equals(info.getKey())){
                    Object graduateSchool=info.getValue();
                    if(graduateSchool!=null){
                        employee.setGraduateSchool(info.getValue().toString());
                    }
                }
                if("专业".equals(info.getKey())){
                    Object major=info.getValue();
                    if(major!=null){
                        employee.setMajor(info.getValue().toString());
                    }

                }
                if("毕业日期".equals(info.getKey())){
                    Object graduateDate=info.getValue();
                    if(graduateDate!=null && !"".equals(graduateDate)){
                        employee.setGraduateDate(LocalDate.parse(info.getValue().toString().substring(0,10)));
                    }
                }
                if("联系人".equals(info.getKey())){
                    Object contacts=info.getValue();
                    if(contacts!=null){
                        employee.setContacts(info.getValue().toString());
                    }

                }
                if("联系电话".equals(info.getKey())){
                    Object linkPhone=info.getValue();
                    if(linkPhone!=null){
                        employee.setLinkPhone(info.getValue().toString());
                    }

                }
                if("户口所在地".equals(info.getKey())){
                    Object registeredPlace=info.getValue();
                    if(registeredPlace!=null){
                        employee.setRegisteredPlace(info.getValue().toString());
                    }

                }
                if("派遣公司".equals(info.getKey())){
                    Object sendComPany=info.getValue();
                    if(sendComPany!=null){
                        employee.setSendCompany(info.getValue().toString());
                    }

                }
                if("派遣工号1".equals(info.getKey())){
                    Object jobNumber1=info.getValue();
                    if(jobNumber1!=null){
                        employee.setJobNumber1(info.getValue().toString());
                    }

                }
                if("派遣工号2".equals(info.getKey())){
                    Object jobNumber2=info.getValue();
                    if(jobNumber2 !=null){
                        employee.setJobNumber2(info.getValue().toString());
                    }

                }
                if("工时核算".equals(info.getKey())){
                    Object workType=info.getValue();
                    if(workType!=null){
                        employee.setWorkType(info.getValue().toString());
                    }

                }
                if("培训期数".equals(info.getKey())){
                    Object trainNum=info.getValue();
                    if(trainNum !=null ){
                        employee.setTrainNum(info.getValue().toString());
                    }
                }
                if("身份".equals(info.getKey())){
                    Object iden=info.getValue();
                    if(iden!=null){
                        employee.setIdentitys(info.getValue().toString());
                    }

                }
                if("学历编号".equals(info.getKey())){
                    Object educationNum=info.getValue();
                    if(educationNum!=null){
                        employee.setEducationNum(info.getValue().toString());
                    }

                }
                if("饭卡号".equals(info.getKey())){
                    Object mealCard=info.getValue();
                    if(mealCard!=null){
                        employee.setMealCard(info.getValue().toString());
                    }

                }

            }
            empList.add(employee);

        }
        for(SysEmployee emp:empList){
            employeeMapper.insert(emp);
        }
        return empList.size();
    }
}
