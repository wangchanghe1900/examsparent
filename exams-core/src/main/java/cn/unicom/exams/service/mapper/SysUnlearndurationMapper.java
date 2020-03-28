package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysUnlearnduration;
import cn.unicom.exams.model.vo.Material;
import cn.unicom.exams.model.vo.UnLearnResource;
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
public interface SysUnlearndurationMapper extends BaseMapper<SysUnlearnduration> {
    /**
     * 查询员工未学资料及考试信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
   public IPage<Material> getEmpUnlearnResourceByPage(Page<UnLearnResource> page, @Param(Constants.WRAPPER) Wrapper<SysUnlearnduration> wrapper) throws Exception;
}
