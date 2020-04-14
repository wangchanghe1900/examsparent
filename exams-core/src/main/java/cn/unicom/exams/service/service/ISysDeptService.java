package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptEmpInfo;
import cn.unicom.exams.model.vo.DeptInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface ISysDeptService extends IService<SysDept> {
    /**
     * 查询所有部门信息
     * @return
     */
    public List<DeptInfo> getAllDeptInfo() throws Exception;

    /**
     * 查询各个部门包含员工信息
     * @return
     * @throws Exception
     */
    public List<DeptInfo> getAllDeptAndEmpInfo() throws Exception;

}
