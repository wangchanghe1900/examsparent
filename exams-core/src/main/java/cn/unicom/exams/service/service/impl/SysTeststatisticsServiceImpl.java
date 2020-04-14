package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysTeststatistics;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.mapper.SysTeststatisticsMapper;
import cn.unicom.exams.service.service.ISysTeststatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Service
public class SysTeststatisticsServiceImpl extends ServiceImpl<SysTeststatisticsMapper, SysTeststatistics> implements ISysTeststatisticsService {
    @Resource
    private SysTeststatisticsMapper teststatisticsMapper;

    @Resource
    private SysDeptMapper deptMapper;
    @Override
    public IPage<TestStatisticsInfo> getTestStatisticsInfoByPage(int page, int limit, TestStatisticsVo testStatisticsVo) throws Exception {
        Page<TestStatisticsVo> ipage=new Page<>(page,limit);
        QueryWrapper<TestStatisticsVo> queryWrapper=new QueryWrapper<>();
        if(testStatisticsVo!=null){
            queryWrapper.likeRight(StringUtils.isNotEmpty(testStatisticsVo.getTestName()),"t.testName",testStatisticsVo.getTestName());
        }
        queryWrapper.orderByDesc("s.t_id");
        IPage<TestStatisticsInfo> testStatisticsInfoByPage = teststatisticsMapper.getTestStatisticsInfoByPage(ipage, queryWrapper);
        List<TestStatisticsInfo> records = testStatisticsInfoByPage.getRecords();
        for(TestStatisticsInfo info: records){
            Long deptId = info.getTestpaper().getDeptId();
            SysDept sysDept = deptMapper.selectById(deptId);
            info.setDeptName(sysDept.getDeptname());
        }
        return testStatisticsInfoByPage;
    }

    @Override
    public IPage<UnTestInfo> getUnTestInfoByPage(int page, int limit, UntestVo untestVo) throws Exception {
        Page<UntestVo> ipage=new Page<>(page,limit);
        QueryWrapper<UntestVo> queryWrapper=new QueryWrapper<>();
        if(untestVo!=null){
            queryWrapper.likeRight(StringUtils.isNotEmpty(untestVo.getEmployeeName()),"d.employeeName",untestVo.getEmployeeName())
                    .likeRight(StringUtils.isNotEmpty(untestVo.getTestName()),"b.testName",untestVo.getTestName())
                    .likeRight(StringUtils.isNotEmpty(untestVo.getResourceName()),"c.resourceName",untestVo.getResourceName())
                    .likeRight(StringUtils.isNotEmpty(untestVo.getDeptName()),"e.deptname",untestVo.getDeptName());
        }
        queryWrapper.orderByDesc("b.updateTime");
        return teststatisticsMapper.getUntestInfoByPage(ipage,queryWrapper);
    }

}
