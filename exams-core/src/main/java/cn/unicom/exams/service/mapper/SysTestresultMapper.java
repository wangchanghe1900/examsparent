package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysTestresult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
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

}
