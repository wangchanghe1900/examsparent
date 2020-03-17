package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysTestpaper;
import cn.unicom.exams.model.vo.ResourceInfo;
import cn.unicom.exams.model.vo.ResourceVo;
import cn.unicom.exams.model.vo.TestPaperInfo;
import cn.unicom.exams.model.vo.TestPaperVo;
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
 * @since 2020-03-02
 */
public interface SysTestpaperMapper extends BaseMapper<SysTestpaper> {
    /**
     * 根据条件查询试卷信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<TestPaperInfo> getTestPaperInfoByPage(Page<TestPaperVo> page, @Param(Constants.WRAPPER) Wrapper<TestPaperVo> wrapper) throws Exception;

}
