package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
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
 * @since 2020-03-11
 */
public interface SysQuestionsMapper extends BaseMapper<SysQuestions> {
    /**
     * 根据条件提取考题详细信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<QuestionInfo> getQuestionInfoByPage(Page<QuestionVo> page, @Param(Constants.WRAPPER) Wrapper<QuestionVo> wrapper) throws Exception;

    /**
     *插入一条题目信息
     * @param questions
     * @return
     * @throws Exception
     */
    public void insertQuestion(@Param("questions") SysQuestions questions) throws Exception;


}
