package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysEmployee;
import cn.unicom.exams.model.vo.EmployeeInfo;
import cn.unicom.exams.model.vo.EmployeeVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王长河
 * @since 2020-01-17
 */
public interface SysEmployeeMapper extends BaseMapper<SysEmployee> {
    /**
     * 根据条件查询员工信息并分页
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */

    public IPage<EmployeeInfo> getEmployeeInfoByPage(Page<EmployeeVo> page, @Param(Constants.WRAPPER) Wrapper<EmployeeVo> wrapper) throws Exception;

}
