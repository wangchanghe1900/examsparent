package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长河
 * @since 2020-01-17
 */
public interface ISysEmployeeService extends IService<SysEmployee> {
    /**
     * 根据条件查询员工信息带分页
     * @param page
     * @param limit
     * @param employeeVo
     * @return
     * @throws Exception
     */
    public IPage<EmployeeInfo> getEmployeeInfoByPage(int page, int limit, EmployeeVo employeeVo) throws Exception;

    /**
     * 根据员工code查询员工考试信息及已考信息
     * @param empCode
     * @return
     * @throws Exception
     */
    public EmpTestInfo getEmpTestInfoByEmpCode(Long empCode) throws Exception;

    /**
     *查询员工未学资源及考试信息
     * @param page
     * @param limit
     * @param empCode
     * @return
     * @throws Exception
     */
    public UnLearnResource getUnLearnResourceByPage(int page, int limit,Long empCode) throws Exception;

    /**
     *查询已学资源信息及考试信息
     * @param page
     * @param limit
     * @param empCode
     * @return
     * @throws Exception
     */
    public LearnedResource getLearnedResourceByPage(int page, int limit,Long empCode) throws Exception;

}
