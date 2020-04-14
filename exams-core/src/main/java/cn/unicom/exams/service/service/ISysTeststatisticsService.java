package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysTeststatistics;
import cn.unicom.exams.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
public interface ISysTeststatisticsService extends IService<SysTeststatistics> {
    /**
     * 查询试卷统计信息(分页)
     * @param page
     * @param limit
     * @param testStatisticsVo
     * @return
     * @throws Exception
     */

    public IPage<TestStatisticsInfo> getTestStatisticsInfoByPage(int page, int limit, TestStatisticsVo testStatisticsVo) throws Exception;

    /**
     * 查询未考人员信息
     * @param page
     * @param limit
     * @param untestVo
     * @return
     * @throws Exception
     */
    public IPage<UnTestInfo> getUnTestInfoByPage(int page, int limit, UntestVo untestVo)throws Exception;



}
