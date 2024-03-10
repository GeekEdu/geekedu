package com.zch.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@Controller
@RequestMapping("/api/live")
public class LiveController {

    @RequestMapping("/index")
    public ModelAndView getLiveIndex() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

}
