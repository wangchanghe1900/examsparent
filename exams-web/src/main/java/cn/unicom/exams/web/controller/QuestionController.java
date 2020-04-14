package cn.unicom.exams.web.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.unicom.exams.model.entity.SysOptions;
import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.QuestionInfo;
import cn.unicom.exams.model.vo.QuestionVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysOptionsService;
import cn.unicom.exams.service.service.ISysQuestionsService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王长何
 * @create 2020-03-10 18:12
 */
@Controller
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @Autowired
    private ISysQuestionsService questionsService;

    @Autowired
    private ISysOptionsService optionsService;

    @GetMapping("/questionList")
    public String questionList(){
        return "question/questionList";
    }

    @GetMapping("/getQuestionCount")
    @ResponseBody
    public Integer getQuestionCount(QuestionVo questionVo){
        QueryWrapper<SysQuestions> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(questionVo.getQuestionStatus()),"questionStatus",questionVo.getQuestionStatus())
                .eq(questionVo.getResId()!=null,"res_id",questionVo.getResId());
        List<SysQuestions> questionList = questionsService.list(queryWrapper);
        return questionList.size();
    }

    @GetMapping("/getquestionList")
    @ResponseBody
    public WebResponse getquestionList(int page, int limit,QuestionVo questionVo){
        try{
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            questionVo.setUserName(user.getUsername());
            questionVo.setDeptId(user.getDeptId());
            IPage<QuestionInfo> questionInfoByPage = questionsService.getQuestionInfoByPage(page, limit, questionVo);
            ButtonInfo questionpower = buttonAuthorUtils.getButtonAuthority("question");
            Long count=questionInfoByPage.getTotal();
            Integer record=questionInfoByPage.getRecords().size();
            for (int i = 0; i < record; i++) {
                questionInfoByPage.getRecords().get(i).setIsDel(questionpower.getIsDel());
                questionInfoByPage.getRecords().get(i).setIsDetail(questionpower.getIsDetail());
                questionInfoByPage.getRecords().get(i).setIsEdit(questionpower.getIsEdit());
            }
            WebResponse response=new WebResponse(0,"",count.intValue(),questionInfoByPage.getRecords());
            return response;
        }catch (Exception e){
            log.error(e.getMessage());
            return new WebResponse(500,"提取考题时系统错误！",0);
        }
    }

    @GetMapping("/addSingleList")
    public String addSingleList(){
        return "question/singleAdd";
    }

    @PostMapping("/saveQuestionInfo")
    @ResponseBody
    public Response saveQuestionInfo(String questionInfo){
        try {
            questionInfo="["+questionInfo+"]";
            List<QuestionVo> questionList = JSON.parseArray(questionInfo, QuestionVo.class);
            if (questionList.size() == 0) {
                return new Response(500, "参数不正确，保存失败");
            }
            QuestionVo questionVo = questionList.get(0);
            SysQuestions questions = new QuestionInfo();

            questions.setQuestionName(questionVo.getQuestionName());
            questions.setQuestionType(questionVo.getQuestionType());
            if("单选题".equals(questionVo.getQuestionType())){
                questions.setSortId(1);
            }else if("多选题".equals(questionVo.getQuestionType())){
                questions.setSortId(2);
            }else{
                questions.setSortId(3);
            }
            questions.setResId(questionVo.getResId());
            questions.setUpdateTime(LocalDateTime.now());
            List<SysOptions> optionsList=new ArrayList<>();
            if (questionVo.getQuestionStatus() != null) {
                questions.setQuestionStatus("启用");
            } else {
                questions.setQuestionStatus("不启用");
            }
            if(questionVo.getQAnswer()!=null){
                questions.setQAnswer(questionVo.getQAnswer());
            }
            String multipleAnswer="";
            if(questionVo.getQAnswerA()!=null){
                multipleAnswer+="A,";
            }
            if(questionVo.getQAnswerB()!=null){
                multipleAnswer+="B,";
            }
            if(questionVo.getQAnswerC()!=null){
                multipleAnswer+="C,";
            }
            if(questionVo.getQAnswerD()!=null){
                multipleAnswer+="D,";
            }
            if(questionVo.getQAnswerE()!=null){
                multipleAnswer+="E,";
            }
            if(questionVo.getQAnswerF()!=null){
                multipleAnswer+="F,";
            }
            if(questionVo.getQAnswerG()!=null){
                multipleAnswer+="G,";
            }
            if(!"".equals(multipleAnswer)){
                multipleAnswer=multipleAnswer.substring(0,multipleAnswer.length()-1);
                questions.setQAnswer(multipleAnswer);
            }
            if(questionVo.getId() == null){
                questions.setCreateTime(LocalDateTime.now());
                questionsService.save(questions);
            }else{
                questions.setId(questionVo.getId());
                questionsService.updateById(questions);
                QueryWrapper<SysOptions> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("ques_id",questionVo.getId());
                optionsService.remove(queryWrapper);
            }

            if(questionVo.getOptionA()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("A");
                options.setOptionContent(questionVo.getOptionA());
                optionsList.add(options);
            }
            if(questionVo.getOptionB()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("B");
                options.setOptionContent(questionVo.getOptionB());
                optionsList.add(options);
            }
            if(questionVo.getOptionC()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("C");
                options.setOptionContent(questionVo.getOptionC());
                optionsList.add(options);
            }
            if(questionVo.getOptionD()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("D");
                options.setOptionContent(questionVo.getOptionD());
                optionsList.add(options);
            }
            if(questionVo.getOptionE()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("E");
                options.setOptionContent(questionVo.getOptionE());
                optionsList.add(options);
            }
            if(questionVo.getOptionF()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("F");
                options.setOptionContent(questionVo.getOptionF());
                optionsList.add(options);
            }
            if(questionVo.getOptionG()!=null){
                SysOptions options=new SysOptions();
                options.setQuesId(questions.getId());
                options.setOptionNO("G");
                options.setOptionContent(questionVo.getOptionG());
                optionsList.add(options);
            }
           for(SysOptions option:optionsList){
               optionsService.save(option);
           }

            return new Response(200, "试题保存成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "保存考题失败，系统错误！");
        }

    }
    @PostMapping("/delQuestionByIds")
    @ResponseBody
    public Response delQuestionByIds(String ids){
        try {
            String[] idArr = ids.split(",");
            List<Integer> idList = new ArrayList<>();
            for (String s : idArr) {
                idList.add(Integer.valueOf(s));
                QueryWrapper<SysOptions> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("ques_id",Integer.valueOf(s));
                optionsService.remove(queryWrapper);
            }
            questionsService.removeByIds(idList);

            return new Response(200, "批量删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "批量删除失败！");
        }
    }

    @GetMapping("/delQuestionById")
    @ResponseBody
    public Response delQuestionById(Long id){
        try{
            QueryWrapper<SysOptions> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("ques_id",id);
            optionsService.remove(queryWrapper);
            questionsService.removeById(id);
            return new Response(200,"考题删除成功!");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"考题删除失败!");
        }
    }

    @GetMapping("/addMultipleList")
    public String addMultipleList(){
        return "question/multipleAdd";
    }
    //addJudgeList
    @GetMapping("/addJudgeList")
    public String addJudgeList(){
        return "question/judgeAdd";
    }

    @GetMapping("/editQuestionList")
    public String editQuestionList(String quesName){
        return "question/"+quesName;
    }

    @GetMapping("/showDetailList")
    public String showDetailList(){
        return "question/showDetail";
    }

    @GetMapping("/importQuestionList")
    public String importQuestionList(){
        return "question/importQuestions";
    }

    @PostMapping("/importQuestionExcel")
    @ResponseBody
    public Response importQuestionExcel(MultipartFile excelinfo, HttpServletRequest request){
        try{
            String contentType = excelinfo.getContentType();
            if (contentType==null||!(contentType.startsWith("application/vnd.ms-excel")||contentType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                //System.out.println("===不符合上传类型...===");
                return  new Response(500,"导入文件不符合上传类型");
            }
            ExcelReader reader = ExcelUtil.getReader(excelinfo.getInputStream());
            List<Map<String, Object>> maps = reader.readAll();
            //System.out.println("maps = " + maps);
            List<QuestionInfo> questionsList=new ArrayList<>();
            for(Map<String,Object> obj :maps){
                QuestionInfo questions=new QuestionInfo();
                List<SysOptions> optionsList=new ArrayList<>();
                for(Map.Entry<String,Object> entry: obj.entrySet()){
                    if("题目名称".equals(entry.getKey())){
                        questions.setQuestionName(entry.getValue().toString());
                    }
                    if("题目类型".equals(entry.getKey())){
                        questions.setQuestionType(entry.getValue().toString());
                        if("单选题".equals(questions.getQuestionType())){
                            questions.setSortId(1);
                        }else if("多选题".equals(questions.getQuestionType())){
                            questions.setSortId(2);
                        }else{
                            questions.setSortId(3);
                        }
                    }
                    if("所属资源ID".equals(entry.getKey())){
                        questions.setResId(Long.valueOf(entry.getValue().toString()));
                    }
                    if("是否启用".equals(entry.getKey())){
                        questions.setQuestionStatus(entry.getValue().toString());
                    }
                    if("标准答案".equals(entry.getKey())){
                        questions.setQAnswer(entry.getValue().toString().trim().toUpperCase());
                    }

                    if("选项A".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("A");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项B".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("B");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项C".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("C");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项D".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("D");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项E".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("E");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项F".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("F");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    if("选项G".equals(entry.getKey())){
                        if(entry.getValue()!=null && !"".equals(entry.getValue())){
                            SysOptions options=new SysOptions();
                            options.setOptionNO("G");
                            options.setOptionContent(StrUtil.upperFirst(entry.getValue().toString().trim()));
                            optionsList.add(options);
                        }
                    }
                    questions.setOptionsList(optionsList);
                }
                questionsList.add(questions);
            }
            //System.out.println(questionsList);
            questionsService.saveQuestionInfo(questionsList);
            return new Response(0,"题库导入成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"题库导入失败,请与系统管理员联系！");
        }

    }

    @GetMapping("/getQuestionCount2")
    @ResponseBody
    public Response getQuestionCount2(){
        try{
            int count = questionsService.count();
            return new Response(200,"",count);
        }catch (Exception e){
            log.error("提取系统试题数量错误："+e.getMessage());
            return  new Response(500,"提取系统试题数量错误");
        }
    }
}
