package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.EmployeeInfo;
import cn.unicom.exams.model.vo.EmployeeVo;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
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

}
