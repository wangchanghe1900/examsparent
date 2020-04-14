package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysTeststatistics;
import cn.unicom.exams.model.vo.TestStatisticsInfo;
import cn.unicom.exams.model.vo.TestStatisticsVo;
import cn.unicom.exams.model.vo.UnTestInfo;
import cn.unicom.exams.model.vo.UntestVo;
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
public interface SysTeststatisticsMapper extends BaseMapper<SysTeststatistics> {
    /**
     * 查询试卷统计信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<TestStatisticsInfo> getTestStatisticsInfoByPage(Page<TestStatisticsVo> page, @Param(Constants.WRAPPER) Wrapper<TestStatisticsVo> wrapper) throws Exception;

    /**
     * 查询未考人员信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<UnTestInfo> getUntestInfoByPage(Page<UntestVo> page,@Param(Constants.WRAPPER) Wrapper<UntestVo> wrapper) throws Exception;

}
