package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.*;
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
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private SysEmployeeMapper employeeMapper;

    @Resource
    private SysTestquestionsMapper testquestionsMapper;

    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;

    @Resource
    private SysQuestionsMapper questionsMapper;

    @Resource
    private SysDeptMapper deptMapper;

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
        //1、查询考试部门及下属部门ID
        QueryWrapper<SysDept> deptqw=new QueryWrapper<>();
        //deptqw.eq("parent_id",sysTestpaper.getDeptId());
        List<SysDept> deptList = deptMapper.selectList(deptqw);
        List<Long> deptIDList = getDeptInfoByParentID(deptList,sysTestpaper.getDeptId());//deptList.stream().map(SysDept::getId).collect(Collectors.toList());
        deptIDList.add(sysTestpaper.getDeptId());
        //2、根据部门ID查询员工信息
        QueryWrapper<SysEmployee> employeeQueryWrapper=new QueryWrapper<>();
        employeeQueryWrapper.eq("employeeStatus","正常").in("dept_id",deptIDList);
        List<SysEmployee> sysEmployees = employeeMapper.selectList(employeeQueryWrapper);
        if("发布".equals(status)){
            QueryWrapper<SysTestquestions> qWrapper=new QueryWrapper<>();
            qWrapper.eq("t_id",testId)
                    .eq("status","未答");
            testquestionsMapper.delete(qWrapper);
            QueryWrapper<SysUnlearnduration> qw=new QueryWrapper<>();
            qw.eq("t_id",testId);
            unlearndurationMapper.delete(qw);
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
                        .eq("emp_code",employee.getEmployeeCode())
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

    @Override
    public ExamInfo getExamInfoByEmpCode(Long empID, Long examID, Integer showNum, Integer pageNum) throws Exception {
        //1、查询testquestions表是否有未答的试题
        ExamInfo examInfo=new ExamInfo();
        QueryWrapper<SysTestquestions> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("t_id",examID).eq("emp_code",empID)
                .eq("status","未答");
        List<SysTestquestions> sysTestquestions = testquestionsMapper.selectList(queryWrapper);
        if(sysTestquestions==null||sysTestquestions.size()==0){
            //2、如果没有则查询书卷中考题数量及对应资源的试题数量
             QueryWrapper<SysTestpaper> testqw=new QueryWrapper<>();
             testqw.eq("id",examID);
            SysTestpaper sysTestpaper = testpaperMapper.selectOne(testqw);
            Long resId=sysTestpaper.getResId();
            QueryWrapper<SysQuestions> questionsQueryWrapper=new QueryWrapper<>();
            questionsQueryWrapper.eq("res_id",resId).eq("questionStatus","启用");
            List<SysQuestions> questionsList = questionsMapper.selectList(questionsQueryWrapper);
            List<Long> questioIds = questionsList.stream().map(SysQuestions::getId).collect(Collectors.toList());
            Set<Long> idsSet = QuestionUtils.randomQuestions(questioIds, sysTestpaper.getTestCount());
            //3、随机提取试卷中考题数量，写入testquestions表
            for(Long id:idsSet){
                SysTestquestions testquestions=new SysTestquestions();
                testquestions.setEmpCode(empID);
                testquestions.setQId(id);
                testquestions.setTId(examID);
                testquestions.setStatus("未答");
                testquestionsMapper.insert(testquestions);
            }
            queryWrapper.eq("t_id",examID).eq("emp_code",empID)
                    .eq("status","未答");
           sysTestquestions = testquestionsMapper.selectList(queryWrapper);
        }
        //4、如果有数据查询testquestions表及questions表查询出关联试题及资源名称
        List<Long> questionIds = sysTestquestions.stream().map(SysTestquestions::getQId).collect(Collectors.toList());
        Page<QuestionVo> page=new Page<>(pageNum,showNum);
        QueryWrapper<QuestionVo> questionqw=new QueryWrapper<>();
        questionqw.in("q.id",questionIds).orderByAsc("q.sortId");
        IPage<QuestionInfo> questionInfoByPage = questionsMapper.getQuestionInfoByPage(page, questionqw);
        examInfo.setEmpID(empID);
        examInfo.setExamID(examID);
        examInfo.setPageNum(pageNum);
        Long totalNum=questionInfoByPage.getTotal();
        examInfo.setTotalNum(totalNum.intValue());
        List<QuestionInfo> questionInfos = questionInfoByPage.getRecords();
        List<TestQuestionInfo> testQuestionInfos = new ArrayList<>();
        int orderNum=(pageNum-1) * showNum;
        //System.out.println("orderNum = " + orderNum);
        for(QuestionInfo questionInfo: questionInfos){
            TestQuestionInfo testQuestionInfo=new TestQuestionInfo();
            testQuestionInfo.setOrderNum(++orderNum);
            testQuestionInfo.setQuestionID(questionInfo.getId());
            testQuestionInfo.setQuestionType(questionInfo.getQuestionType());
            testQuestionInfo.setQuestion(questionInfo.getQuestionName());
            List<SysOptions> optionsList = questionInfo.getOptionsList();
            List<OptionsInfo> optionsInfos=new ArrayList<>();
            for(SysOptions op :optionsList){
                OptionsInfo info=new OptionsInfo();
                info.setOrder(op.getOptionNO());
                info.setDesc(op.getOptionContent());
                optionsInfos.add(info);
            }
            testQuestionInfo.setOption(optionsInfos);
            examInfo.setMaterialID(questionInfo.getResourceinfo().getId());
            examInfo.setMaterialType(Integer.valueOf(questionInfo.getResourceinfo().getResourceType()));
            examInfo.setMaterialURL(questionInfo.getResourceinfo().getUrl());
            testQuestionInfos.add(testQuestionInfo);
        }
        examInfo.setQuestionsList(testQuestionInfos);
        return examInfo;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTestInfoById(Long examsID,String url) throws Exception {
        QueryWrapper<SysUnlearnduration> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("t_id",examsID);
        unlearndurationMapper.delete(queryWrapper);
        QueryWrapper<SysTestquestions> qw=new QueryWrapper<>();
        qw.eq("t_id",examsID).eq("status","未答");
        testquestionsMapper.delete(qw);
        File file=new File(url);
        file.delete();
        testpaperMapper.deleteById(examsID);


    }

    private List<Long> getDeptInfoByParentID(List<SysDept> deptList,Long parentID){
        List<Long> idList=new ArrayList<>();
        List<SysDept> childrenList = deptList.stream().filter(d -> d.getParentId().intValue() == parentID.intValue()).collect(Collectors.toList());
        if(childrenList.size()>0){
            for(SysDept dept : childrenList){
                idList.add(dept.getId());
                List<Long> childrenIdList = getDeptInfoByParentID(deptList, dept.getId());
                if(childrenIdList.size()>0){
                    idList.addAll(childrenIdList);
                }
            }

        }
        return idList;
    }
}
