package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.*;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.service.mapper.*;
import cn.unicom.exams.service.service.ISysTestresultService;
import cn.unicom.exams.service.utils.DeptUtils;
import cn.unicom.exams.service.utils.QuestionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Service
public class SysTestresultServiceImpl extends ServiceImpl<SysTestresultMapper, SysTestresult> implements ISysTestresultService {
    @Resource
    private SysTestquestionsMapper testquestionsMapper;

    @Resource
    private SysTestresultMapper testresultMapper;

    @Resource
    private SysQuestionsMapper questionsMapper;

    @Resource
    private SysUnlearndurationMapper unlearndurationMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamScore judgementExaminfo(TestResultInfo testResultInfo) throws Exception{
        //1、更新sys_testquestions表
        QueryWrapper<SysTestquestions> qw=new QueryWrapper<>();
        qw.eq("t_id",testResultInfo.getExamID())
                .eq("emp_code",testResultInfo.getEmpID())
                .eq("status","未答");
        List<SysTestquestions> testquestions = testquestionsMapper.selectList(qw);
        for(AnswerInfo answerInfo : testResultInfo.getOptionList()){
            if(testquestions != null && testquestions.size()==0){
                SysTestquestions questions=new SysTestquestions();
                questions.setTId(testResultInfo.getExamID());
                questions.setEmpCode(testResultInfo.getEmpID());
                questions.setQId(Long.parseLong(answerInfo.getQuestionID().toString()));
                questions.setStatus("未答");
                questions.setAnswer(answerInfo.getAnswer());
                testquestionsMapper.insert(questions);
            }else{
                Optional<SysTestquestions> q = testquestions.stream().filter(t -> t.getQId().intValue() == answerInfo.getQuestionID()).findFirst();
                if(q.isPresent()){
                    SysTestquestions testquestion = q.get();
                    testquestion.setAnswer(answerInfo.getAnswer());
                    testquestionsMapper.updateById(testquestion);
                }

            }
        }
        //2、计算考试分数
        SysTestresult testresult=new SysTestresult();
        testresult.setTId(testResultInfo.getExamID());
        testresult.setEmpCode(testResultInfo.getEmpID());
        testresult.setTestTime(LocalDateTime.now());
        testresult.setTestDuration(testResultInfo.getDuration());
        //如果考试分数大于60分，则通过否则不通过
        Integer score=judgementScore(testResultInfo.getEmpID(),testResultInfo.getExamID(),testResultInfo.getTotalNum(),testResultInfo.getOptionList());
        //3、插入sys_testresult表
        testresult.setScore(score);
        String outcome="未通过";
        if(score>=testResultInfo.getPassNum()){
            outcome="通过";
        }
        testresult.setOutcome(outcome);
        testresult.setReturnCount(testResultInfo.getQuitTimes());
        testresultMapper.insert(testresult);
        //4、查询通过考试次数
        ExamScore examScore=new ExamScore();
        QueryWrapper<SysTestresult> resultQW=new QueryWrapper<>();
        resultQW.eq("t_id",testResultInfo.getExamID())
                .eq("emp_code",testResultInfo.getEmpID())
                .ge("score",60);
        Integer count = testresultMapper.selectCount(resultQW);
        examScore.setExamTotalNum(count);

        //5、本次考试返回资料观看次数
        examScore.setQuitTimes(testResultInfo.getQuitTimes());
        //6、本次分数及试卷ID
        examScore.setExamID(testResultInfo.getExamID());
        examScore.setScores(score);
        QueryWrapper<SysTestquestions> question=new QueryWrapper<>();
        question.eq("t_id",testResultInfo.getExamID())
                .eq("emp_code",testResultInfo.getEmpID())
                .eq("status","未答");
        List<SysTestquestions> sysTestquestions = testquestionsMapper.selectList(question);
        for(SysTestquestions q:sysTestquestions){
            q.setStatus("已答");
            q.setAnswerTime(LocalDateTime.now());
            testquestionsMapper.updateById(q);
        }
        //删除未学习记录表
        QueryWrapper<SysUnlearnduration> unlearnqw=new QueryWrapper<>();
        unlearnqw.eq("t_id",testResultInfo.getExamID())
                .eq("emp_code",testResultInfo.getEmpID());
        unlearndurationMapper.delete(unlearnqw);
        return examScore;
    }



    private Integer judgementScore(Long empID, Long examID, Integer totalQuestions, List<AnswerInfo> answerInfoList){
        QueryWrapper<SysTestquestions> queryWrapper=new QueryWrapper<>();
        List<Integer> ids=answerInfoList.stream().map(AnswerInfo::getQuestionID).collect(Collectors.toList());
        queryWrapper.eq("t_id",examID).eq("emp_code",empID).eq("status","未答").in("q_id",ids);
        List<SysTestquestions> sysTestquestions = testquestionsMapper.selectList(queryWrapper);
        List<Long> idList = sysTestquestions.stream().map(SysTestquestions::getId).collect(Collectors.toList());
        Map<Long, Integer> scoreMap = QuestionUtils.randomQuestionScore(idList, totalQuestions);
        Map<Long,Integer> testScore=new HashMap<>();
        QueryWrapper<SysQuestions> qe=new QueryWrapper<>();
        qe.in("id",ids);
        List<SysQuestions> questionsList = questionsMapper.selectList(qe);
        for(AnswerInfo info : answerInfoList){
            String answers = info.getAnswer();
            Optional<SysQuestions> first = questionsList.stream().filter(q -> q.getId().intValue() == info.getQuestionID() && q.getQAnswer().equals(info.getAnswer())).findFirst();
            if(first.isPresent()){
                Long id = first.get().getId();
                Optional<SysTestquestions> stq = sysTestquestions.stream().filter(t -> t.getQId() == id).findFirst();
                if(stq.isPresent()){
                    SysTestquestions ts = stq.get();
                    Integer value = scoreMap.get(ts.getId());
                    testScore.put(id,value);
                }

            }

        }
        int score=0;
        Set<Map.Entry<Long, Integer>> entries = testScore.entrySet();
        for(Map.Entry<Long, Integer> entry : entries){
            score += entry.getValue();
        }
        return score;
    }

    @Override
    public TestedInfo gettestedInfoByEmpID(Long empID, Integer showNum, Integer pageNum) throws Exception {
        Page<TestScoreInfo> ipage=new Page<>(pageNum,showNum);
        QueryWrapper<TestScoreInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("emp_code",empID)
                .groupBy("p.testName","p.res_id");
        IPage<TestScoreInfo> testedScoreByPage = testresultMapper.getTestedScoreByPage(ipage, queryWrapper);
        TestedInfo testedInfo=new TestedInfo();
        testedInfo.setEmpID(empID);
        testedInfo.setPageNum(pageNum);
        testedInfo.setShowNum(showNum);
        Long total=testedScoreByPage.getTotal();
        testedInfo.setTotalNum(total.intValue());
        testedInfo.setDataList(testedScoreByPage.getRecords());
        return testedInfo;
    }

    @Override
    public IPage<EmpTestResultInfo> getEmpTestResultInfoByPage(int page, int limit, Long deptID) throws Exception {
        Page<SysTestresult> ipage=new Page<>(page,limit);
        QueryWrapper<SysTestresult> queryWrapper=new QueryWrapper<>();
        List<SysDept> deptList = deptMapper.selectList(null);
        if(deptID != null){
            List<Long> idList = DeptUtils.getDeptInfoByParentID(deptList, deptID);
            idList.add(deptID);
            queryWrapper.in("c.id",idList);
        }
        queryWrapper.orderByAsc("c.id");

        return testresultMapper.getEmpTestResultInfoByPage(ipage,queryWrapper);
    }

    @Override
    public List<Integer> getSevenTestResultCount() throws Exception {
        return testresultMapper.getSevenTestResultCount();
    }
}
