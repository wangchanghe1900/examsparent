package cn.unicom.exams.web.controller;

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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @RequiresPermissions("question:list")
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
    @RequiresPermissions("question:find")
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
    @RequiresPermissions("question:singleadd")
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
            questionsService.saveQuestionInfo(questionVo);
             return new Response(200, "试题保存成功！");
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500, "保存考题失败，系统错误！");
        }

    }
    @PostMapping("/delQuestionByIds")
    @RequiresPermissions("question:batchdel")
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
    @RequiresPermissions("question:delete")
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
    @RequiresPermissions("question:multipleadd")
    public String addMultipleList(){
        return "question/multipleAdd";
    }
    //addJudgeList
    @GetMapping("/addJudgeList")
    @RequiresPermissions("question:judgeadd")
    public String addJudgeList(){
        return "question/judgeAdd";
    }

    @GetMapping("/editQuestionList")
    @RequiresPermissions("question:edit")
    public String editQuestionList(String quesName){
        return "question/"+quesName;
    }

    @GetMapping("/showDetailList")
    @RequiresPermissions("question:detail")
    public String showDetailList(){
        return "question/showDetail";
    }

    @GetMapping("/importQuestionList")
    @RequiresPermissions("question:import")
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
            questionsService.saveQuestionList(questionsList);
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
