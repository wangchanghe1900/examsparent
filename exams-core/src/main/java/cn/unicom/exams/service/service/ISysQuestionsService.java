package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-11
 */
public interface ISysQuestionsService extends IService<SysQuestions> {
    /**
     * 根据条件提取试题信息（分页）
     * @param page
     * @param limit
     * @param questionVo
     * @return
     * @throws Exception
     */
    public IPage<QuestionInfo> getQuestionInfoByPage(int page, int limit, QuestionVo questionVo) throws Exception;

}
