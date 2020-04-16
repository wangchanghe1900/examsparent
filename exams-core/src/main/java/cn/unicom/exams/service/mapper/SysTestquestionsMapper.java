package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysLearnduration;
import cn.unicom.exams.model.entity.SysTestquestions;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
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
public interface SysTestquestionsMapper extends BaseMapper<SysTestquestions> {
    /**
     * 查询员工已考总数
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getEmpTestedByEmpCode(@Param(Constants.WRAPPER) Wrapper<SysTestquestions> wrapper) throws Exception;

    /**
     * 查询7天内员工答题数量
     * @return
     * @throws Exception
     */
    public List<Integer> getSevenAnswerCount() throws Exception;

}
