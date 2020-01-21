package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptInfo;
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
        return deptService.getAllDeptInfo();
    }

    @GetMapping("/show")
    public String show(Long id){
        return "dept/detail";
    }
}
