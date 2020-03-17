package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
import cn.unicom.exams.service.mapper.SysQuestionsMapper;
import cn.unicom.exams.service.service.ISysQuestionsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-11
 */
@Service
public class SysQuestionsServiceImpl extends ServiceImpl<SysQuestionsMapper, SysQuestions> implements ISysQuestionsService {
    @Resource
    private SysQuestionsMapper questionsMapper;
    @Override
    public IPage<QuestionInfo> getQuestionInfoByPage(int page, int limit, QuestionVo questionVo) throws Exception {
        Page<QuestionVo> ipage=new Page<>(page,limit);
        QueryWrapper<QuestionVo> queryWrapper=new QueryWrapper<>();
        if(questionVo==null){
            return null;
        }
        if("admin".equals(questionVo.getUserName())){
            queryWrapper.
                    likeRight(StringUtils.isNotEmpty(questionVo.getQuestionName()),"q.questionName",questionVo.getQuestionName());

        }else{
           queryWrapper.
                   likeRight(StringUtils.isNotEmpty(questionVo.getQuestionName()),"q.questionName",questionVo.getQuestionName())
                   .eq(questionVo.getDeptId()!=null,"r.dept_id",questionVo.getDeptId());
        }
        queryWrapper.orderByDesc("q.createTime");
        return questionsMapper.getQuestionInfoByPage(ipage,queryWrapper);
    }
}
