package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysOptions;
import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
import cn.unicom.exams.service.mapper.SysOptionsMapper;
import cn.unicom.exams.service.mapper.SysQuestionsMapper;
import cn.unicom.exams.service.service.ISysQuestionsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-11
 */
@Service
public class SysQuestionsServiceImpl extends ServiceImpl<SysQuestionsMapper, SysQuestions> implements ISysQuestionsService {
    @Resource
    private SysQuestionsMapper questionsMapper;

    @Resource
    private SysOptionsMapper optionsMapper;

    @Override
    public IPage<QuestionInfo> getQuestionInfoByPage(int page, int limit, QuestionVo questionVo) throws Exception {
        Page<QuestionVo> ipage = new Page<>(page, limit);
        QueryWrapper<QuestionVo> queryWrapper = new QueryWrapper<>();
        if (questionVo == null) {
            return null;
        }
        if ("admin".equals(questionVo.getUserName())) {
            queryWrapper.
                    likeRight(StringUtils.isNotEmpty(questionVo.getQuestionName()), "q.questionName", questionVo.getQuestionName())
                    .likeRight(StringUtils.isNotEmpty(questionVo.getResourceName()), "r.resourceName", questionVo.getResourceName());

        } else {
            queryWrapper.
                    likeRight(StringUtils.isNotEmpty(questionVo.getQuestionName()), "q.questionName", questionVo.getQuestionName())
                    .eq(questionVo.getDeptId() != null, "r.dept_id", questionVo.getDeptId())
                    .likeRight(StringUtils.isNotEmpty(questionVo.getResourceName()), "r.resourceName", questionVo.getResourceName());
            ;
        }
        queryWrapper.orderByDesc("q.createTime");
        return questionsMapper.getQuestionInfoByPage(ipage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuestionList(List<QuestionInfo> questionInfoList) throws Exception {
        for (QuestionInfo questionInfo : questionInfoList) {
            SysQuestions questions = new SysQuestions();
            questions.setQuestionName(questionInfo.getQuestionName());
            questions.setQuestionType(questionInfo.getQuestionType());
            questions.setResId(questionInfo.getResId());
            questions.setQuestionStatus(questionInfo.getQuestionStatus());
            questions.setQAnswer(questionInfo.getQAnswer());
            questions.setSortId(questionInfo.getSortId());
            questions.setCreateTime(LocalDateTime.now());
            questions.setUpdateTime(LocalDateTime.now());
            questionsMapper.insert(questions);
            for (SysOptions options : questionInfo.getOptionsList()) {
                options.setQuesId(questions.getId());
                optionsMapper.insert(options);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuestionInfo(QuestionVo questionVo) throws Exception {
        SysQuestions questions = new SysQuestions();
        questions.setQuestionName(questionVo.getQuestionName());
        questions.setQuestionType(questionVo.getQuestionType());
        if ("单选题".equals(questionVo.getQuestionType())) {
            questions.setSortId(1);
        } else if ("多选题".equals(questionVo.getQuestionType())) {
            questions.setSortId(2);
        } else {
            questions.setSortId(3);
        }
        questions.setResId(questionVo.getResId());
        questions.setUpdateTime(LocalDateTime.now());
        List<SysOptions> optionsList = new ArrayList<>();
        if (questionVo.getQuestionStatus() != null) {
            questions.setQuestionStatus("启用");
        } else {
            questions.setQuestionStatus("不启用");
        }
        if (questionVo.getQAnswer() != null) {
            questions.setQAnswer(questionVo.getQAnswer());
        }
        String multipleAnswer = "";
        if (questionVo.getQAnswerA() != null) {
            multipleAnswer += "A,";
        }
        if (questionVo.getQAnswerB() != null) {
            multipleAnswer += "B,";
        }
        if (questionVo.getQAnswerC() != null) {
            multipleAnswer += "C,";
        }
        if (questionVo.getQAnswerD() != null) {
            multipleAnswer += "D,";
        }
        if (questionVo.getQAnswerE() != null) {
            multipleAnswer += "E,";
        }
        if (questionVo.getQAnswerF() != null) {
            multipleAnswer += "F,";
        }
        if (questionVo.getQAnswerG() != null) {
            multipleAnswer += "G,";
        }
        if (!"".equals(multipleAnswer)) {
            multipleAnswer = multipleAnswer.substring(0, multipleAnswer.length() - 1);
            questions.setQAnswer(multipleAnswer);
        }
        if (questionVo.getId() == null) {
            questions.setCreateTime(LocalDateTime.now());
            questionsMapper.insert(questions);
            //questionsMapper.insertQuestion(questions);
        } else {
            questions.setId(questionVo.getId());
            questionsMapper.updateById(questions);
            QueryWrapper<SysOptions> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ques_id", questionVo.getId());
            optionsMapper.delete(queryWrapper);
        }

        if (questionVo.getOptionA() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("A");
            options.setOptionContent(questionVo.getOptionA());
            optionsList.add(options);
        }
        if (questionVo.getOptionB() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("B");
            options.setOptionContent(questionVo.getOptionB());
            optionsList.add(options);
        }
        if (questionVo.getOptionC() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("C");
            options.setOptionContent(questionVo.getOptionC());
            optionsList.add(options);
        }
        if (questionVo.getOptionD() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("D");
            options.setOptionContent(questionVo.getOptionD());
            optionsList.add(options);
        }
        if (questionVo.getOptionE() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("E");
            options.setOptionContent(questionVo.getOptionE());
            optionsList.add(options);
        }
        if (questionVo.getOptionF() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("F");
            options.setOptionContent(questionVo.getOptionF());
            optionsList.add(options);
        }
        if (questionVo.getOptionG() != null) {
            SysOptions options = new SysOptions();
            options.setQuesId(questions.getId());
            options.setOptionNO("G");
            options.setOptionContent(questionVo.getOptionG());
            optionsList.add(options);
        }
        for (SysOptions option : optionsList) {
            optionsMapper.insert(option);
        }


    }
}
