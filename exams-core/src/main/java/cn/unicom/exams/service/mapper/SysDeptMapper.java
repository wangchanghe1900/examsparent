package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptEmpInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 查询部门下所有员工信息
     * @return
     * @throws Exception
     */
    public List<DeptEmpInfo> getAllDeptAndEmpInfo() throws Exception;

}
