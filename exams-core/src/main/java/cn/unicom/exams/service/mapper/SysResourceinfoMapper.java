package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.vo.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王长何
 * @since 2020-02-26
 */
public interface SysResourceinfoMapper extends BaseMapper<SysResourceinfo> {
    /**
     * 根据条件提取资源信息并分页
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<ResourceInfo> getResourceInfoByPage(Page<ResourceVo> page, @Param(Constants.WRAPPER) Wrapper<ResourceVo> wrapper) throws Exception;

    /**
     * 查询部门所有学习资源
     * @param wrapper
     * @return
     * @throws Exception
     */
    public List<DeptResourceInfo> getDeptResourceInfoByCondition(@Param(Constants.WRAPPER) Wrapper<ResourceVo> wrapper) throws Exception;

}
