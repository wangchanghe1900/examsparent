package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.TestPaperInfo;
import cn.unicom.exams.model.vo.TestPaperVo;
import cn.unicom.exams.service.mapper.*;
import cn.unicom.exams.service.service.ISysTestpaperService;
import cn.unicom.exams.service.utils.QuestionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-02
 */
@Service
public class SysTestpaperServiceImpl extends ServiceImpl<SysTestpaperMapper, SysTestpaper> implements ISysTestpaperService {
    @Resource
    private SysTestpaperMapper testpaperMapper;
    @Resource
    private SysResourceinfoMapper resourceinfoMapper;

    @Resource
    private SysEmployeeMapper employeeMapper;

    @Resource
    private SysTestquestionsMapper testquestionsMapper;

    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;

    @Resource
    private SysQuestionsMapper questionsMapper;
    @Override
    public IPage<TestPaperInfo> getTestPaperInfoByPage(int page, int limit, TestPaperVo testPaperVo) throws Exception{
        Page<TestPaperVo> iPage=new Page<>(page,limit);
        QueryWrapper<TestPaperVo> queryWrapper=new QueryWrapper<>();
        if(testPaperVo!=null){
            if("admin".equals(testPaperVo.getUserName())){
                queryWrapper.likeRight(StringUtils.isNotEmpty(testPaperVo.getTestName()),"testName",testPaperVo.getTestName());

            }else{
                queryWrapper.likeRight(StringUtils.isNotEmpty(testPaperVo.getTestName()),"testName",testPaperVo.getTestName())
                        .eq(testPaperVo.getDeptId()!=null,"a.dept_id",testPaperVo.getDeptId());
            }
            queryWrapper.orderByDesc("a.createTime").orderByDesc("a.id");
        }
        return testpaperMapper.getTestPaperInfoByPage(iPage,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTest(Long testId,String status) throws Exception {
        SysTestpaper sysTestpaper = testpaperMapper.selectById(testId);
        QueryWrapper<SysEmployee> employeeQueryWrapper=new QueryWrapper<>();
        employeeQueryWrapper.eq("dept_id",sysTestpaper.getDeptId());
        List<SysEmployee> sysEmployees = employeeMapper.selectList(employeeQueryWrapper);
        if("发布".equals(status)){
            QueryWrapper<SysQuestions> questionsQueryWrapper=new QueryWrapper<>();
            questionsQueryWrapper.eq("res_id",sysTestpaper.getResId()).eq("questionStatus","启用");
            List<SysQuestions> questions = questionsMapper.selectList(questionsQueryWrapper);
            List<Long> idsList = questions.stream().map(SysQuestions::getId).collect(Collectors.toList());
            for(SysEmployee employee:sysEmployees){
                Set<Long> idsSet = QuestionUtils.randomQuestions(idsList, sysTestpaper.getTestCount());
                for(Long questionId:idsSet){
                    SysTestquestions testquestions=new SysTestquestions();
                    testquestions.setTId(sysTestpaper.getId());
                    testquestions.setEmpCode(Long.parseLong(employee.getEmployeeCode()));
                    testquestions.setQId(questionId);
                    testquestions.setStatus("未答");
                    testquestionsMapper.insert(testquestions);
                }
                SysUnlearnduration unlearn=new SysUnlearnduration();
                unlearn.setTId(sysTestpaper.getId());
                unlearn.setEmpCode(Long.parseLong(employee.getEmployeeCode()));
                unlearn.setResId(sysTestpaper.getResId());
                unlearndurationMapper.insert(unlearn);
            }
            sysTestpaper.setTestStatus("发布");
            sysTestpaper.setUpdateTime(LocalDateTime.now());
            testpaperMapper.updateById(sysTestpaper);
        }else{
            for(SysEmployee employee:sysEmployees){
                QueryWrapper<SysTestquestions> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("t_id",sysTestpaper.getId())
                        .eq("u_id",employee.getEmployeeCode())
                        .eq("status","未答");
                testquestionsMapper.delete(queryWrapper);
                QueryWrapper<SysUnlearnduration> unlearnQuery=new QueryWrapper<>();
                unlearnQuery.eq("emp_code",employee.getEmployeeCode())
                        .eq("t_id",sysTestpaper.getId())
                        .eq("res_id",sysTestpaper.getResId());
                unlearndurationMapper.delete(unlearnQuery);
            }
            sysTestpaper.setTestStatus("未发布");
            sysTestpaper.setUpdateTime(LocalDateTime.now());
            testpaperMapper.updateById(sysTestpaper);
        }

    }
}
