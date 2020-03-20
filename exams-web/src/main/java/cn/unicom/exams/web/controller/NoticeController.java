package cn.unicom.exams.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王长何
 * @create 2020-03-19 17:04
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @GetMapping("/noticeList")
    public String noticeList(){
        return "notice/noticeList";
    }

    @GetMapping("/messageList")
    public String messageList(){
        return "notice/messageList";
    }
}
