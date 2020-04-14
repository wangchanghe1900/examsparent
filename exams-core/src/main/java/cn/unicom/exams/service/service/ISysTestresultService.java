package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysTestresult;
import cn.unicom.exams.model.vo.EmpTestResultInfo;
import cn.unicom.exams.model.vo.ExamScore;
import cn.unicom.exams.model.vo.TestResultInfo;
import cn.unicom.exams.model.vo.TestedInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
public interface ISysTestresultService extends IService<SysTestresult> {
    /**
     * 考试结果反馈
     * @param testResultInfo
     * @return
     * @throws Exception
     */
     public ExamScore judgementExaminfo(TestResultInfo testResultInfo) throws Exception;

    /**
     * 根据员工ID，查询员工已考试信息
     * @param empID
     * @param showNum
     * @param pageNum
     * @return
     * @throws Exception
     */
     public TestedInfo gettestedInfoByEmpID(Long empID,Integer showNum,Integer pageNum) throws Exception;


    /**
     * 根据部门信息查询用户已考信息
     * @param page
     * @param limit
     * @param deptID
     * @return
     * @throws Exception
     */
    public IPage<EmpTestResultInfo> getEmpTestResultInfoByPage(int page, int limit, Long deptID) throws Exception;
}
