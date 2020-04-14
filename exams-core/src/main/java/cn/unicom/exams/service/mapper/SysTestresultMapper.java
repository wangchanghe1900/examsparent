package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysTestresult;
import cn.unicom.exams.model.vo.EmpTestResultInfo;
import cn.unicom.exams.model.vo.TestScoreInfo;
import cn.unicom.exams.model.vo.TestedInfo;
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
 * @since 2020-03-20
 */
public interface SysTestresultMapper extends BaseMapper<SysTestresult> {
    /**
     * 查询员工考试次数
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getEmpExamTimesByEmpCode(@Param(Constants.WRAPPER) Wrapper<SysTestresult> wrapper) throws Exception;

    /**
     * 统计员工已考信息  考试名称、考试次数、最低分、最高分、平均分等
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<TestScoreInfo> getTestedScoreByPage(Page<TestScoreInfo> page, @Param(Constants.WRAPPER) Wrapper<TestScoreInfo> wrapper) throws Exception;

    /**
     * 查询员工已考试卷数量
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getExamCountByEmpID(@Param(Constants.WRAPPER) Wrapper<SysTestresult> wrapper) throws Exception;

    /**
     * 查询员工已考信息
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<EmpTestResultInfo> getEmpTestResultInfoByPage(Page<SysTestresult> page,@Param(Constants.WRAPPER) Wrapper<SysTestresult> wrapper) throws Exception;

}
