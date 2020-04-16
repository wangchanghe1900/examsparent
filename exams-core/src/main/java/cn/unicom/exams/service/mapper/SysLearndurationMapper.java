package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysLearnduration;
import cn.unicom.exams.model.vo.EmployeeVo;
import cn.unicom.exams.model.vo.LearnedMaterial;
import cn.unicom.exams.model.vo.LearnedResourceEmpInfo;
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
 * @since 2020-03-20
 */
public interface SysLearndurationMapper extends BaseMapper<SysLearnduration> {
    /**
     * 查询员工已学资源数量
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getEmpLearnResourceByEmpCode(@Param(Constants.WRAPPER) Wrapper<SysLearnduration> wrapper) throws Exception;

    /**
     * 查询员工学习总时长
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getEmpLearnLongDurationByEmpCode(@Param(Constants.WRAPPER) Wrapper<SysLearnduration> wrapper) throws Exception;

    /**
     * 查询员工学习总次数
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getEmpLearnTimesByEmpCode(@Param(Constants.WRAPPER) Wrapper<SysLearnduration> wrapper) throws Exception;

    /**
     * 查询已学资源及考试信息（分页)
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<LearnedMaterial> getEmpLearnedResourceByPage(Page<SysLearnduration> page, @Param(Constants.WRAPPER) Wrapper<SysLearnduration> wrapper) throws Exception;

    /**
     *查询员工已学信息(分页)
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<LearnedResourceEmpInfo> getLearnedResEmpByPage(Page<SysLearnduration> page, @Param(Constants.WRAPPER) Wrapper<SysLearnduration> wrapper) throws Exception;

    /**
     * 查询员工7天内学习次数
     * @return
     * @throws Exception
     */
    public List<Integer> getSenvenLearnResourceCount() throws Exception;

}
