package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.vo.ResourceInfo;
import cn.unicom.exams.model.vo.ResourceVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
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

}
