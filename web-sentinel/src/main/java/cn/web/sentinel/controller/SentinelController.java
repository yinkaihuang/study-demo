package cn.web.sentinel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
     * @Auther: willow
     * @Date: 2019/5/7 17:42
     * @Description:
     */
    @Controller
    public class SentinelController {
        @RequestMapping("/sentinel")
        @ResponseBody
        public String sentinel(){
            return "sentinel ....";
        }
    }