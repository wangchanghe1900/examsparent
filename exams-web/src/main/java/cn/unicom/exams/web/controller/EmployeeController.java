package cn.unicom.exams.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王长何
 * @create 2020-01-22 15:52
 */
@Controller
@RequestMapping("/emp")
public class EmployeeController {//isDetail
    @GetMapping("/empList")
    public String empList(){
        return "employee/empList";
    }
}
