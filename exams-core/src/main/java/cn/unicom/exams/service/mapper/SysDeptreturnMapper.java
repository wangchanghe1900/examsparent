package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysDeptreturn;
import cn.unicom.exams.model.vo.DeptTestStatisInfo;
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
 * @author 王长何
 * @since 2020-03-20
 */
public interface SysDeptreturnMapper extends BaseMapper<SysDeptreturn> {
    /**
     * 统计部门考试返回次数
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<DeptTestStatisInfo> getDeptTestStatisInfo(Page<SysDeptreturn> page, @Param(Constants.WRAPPER) Wrapper<SysDeptreturn> wrapper) throws Exception;

}
