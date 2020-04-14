package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysDeptduration;
import cn.unicom.exams.model.vo.DeptResourceStatisInfo;
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
public interface SysDeptdurationMapper extends BaseMapper<SysDeptduration> {
    /**
     * 部门学习资源信息统计
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<DeptResourceStatisInfo> getDeptResourceStatisInfo(Page<SysDeptduration> page, @Param(Constants.WRAPPER) Wrapper<SysDeptduration> wrapper) throws Exception;

}
