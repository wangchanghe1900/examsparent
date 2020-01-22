package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptInfo;
import cn.unicom.exams.model.vo.DeptVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.service.service.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-11 11:51
 */
@Controller
@RequestMapping("/dept")
public class DepatmentController {

    @Resource
    private ISysDeptService deptService;

    @PostMapping("/deptnamelist")
    @ResponseBody
    public List<SysDept> deptnamelist(Integer delFlag){
        QueryWrapper<SysDept> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("del_flag",delFlag);
        queryWrapper.orderByAsc("order_num");
        return deptService.list(queryWrapper);
    }

    @GetMapping("/deptList")
    public String deptList(){
        return "dept/deptList";
    }

    @GetMapping("/getAllDeptInfo")
    @ResponseBody
    public List<DeptInfo> getAllDeptInfo(){
        List<DeptInfo> allDeptInfo = deptService.getAllDeptInfo();
        return allDeptInfo;
    }

    @PostMapping("/editDept")
    @ResponseBody
    public Response editDept(DeptVo deptVo){
        try{
            if(deptVo!=null){
                SysDept dept=new SysDept();
                dept.setId(deptVo.getId());
                dept.setDeptname(deptVo.getTitle());
                dept.setContent(deptVo.getContent());
                dept.setParentId(deptVo.getParentId());
                dept.setOrderNum(deptVo.getOrdernum());
                deptService.updateById(dept);
                return new Response(200,"修改成功");
            }else{
                return new Response(500,"参数为空");
            }
        }catch(Exception e){
            return new Response(500,e.getMessage());
        }

    }

    @PostMapping("/delDept")
    @ResponseBody
    public Response delDept(DeptVo deptVo){
        try{
            if(deptVo!=null  && deptVo.getId()!=null){
                SysDept dept=new SysDept();
                dept.setId(deptVo.getId());
                dept.setDelFlag(-1);
                deptService.updateById(dept);
                return new Response(200,"删除成功");
            }else{
                return new Response(500,"参数为空");
            }
        }catch(Exception e){
            return new Response(500,e.getMessage());
        }

    }

    @GetMapping("/addDeptList")
    public String addDeptList(){
        return "dept/deptAdd";
    }

    @GetMapping("/getDeptAllInfo")
    @ResponseBody
    public Object getDeptAllInfo(){
        List<DeptInfo> allDeptInfo = deptService.getAllDeptInfo();
        return buildTree(allDeptInfo);
    }

    private Object buildTree(List<DeptInfo> list) {
        List<HashMap<String, Object>> result=new ArrayList<>();
        for (DeptInfo dept : list) {
            HashMap<String, Object> node=new HashMap<>();
            node.put("id", dept.getId());    //节点id
            node.put("name",dept.getTitle());    //节点数据名称
            node.put("open", true);        //是否展开
            node.put("checked", false);    //是否选中，前台也可以设置是否选中
            if(dept.getChildren().size() != 0) {    //如果下级节点不为空，
                node.put("children",buildTree(dept.getChildren()));    //递归加载下级节点
            }
            result.add(node);
        }
        return result;
    }

    @PostMapping("/saveDept")
    @ResponseBody
    public Response saveDept(DeptVo deptVo){
        if(deptVo!=null){
            SysDept dept=new SysDept();
            dept.setParentId(deptVo.getParentId());
            dept.setDeptname(deptVo.getTitle());
            dept.setContent(deptVo.getContent());
            dept.setOrderNum(deptVo.getOrdernum());
            dept.setDelFlag(0);
            deptService.save(dept);
            return new Response(200,"新增成功！");
        }
        return new Response(500,"系统错误");
    }




}
