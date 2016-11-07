package com.lc.biz.controller;

import com.lc.biz.entity.Test;
import com.lc.biz.service.TestService;
import com.lc.spring.annotion.Autowired;
import com.lc.spring.annotion.Controller;
import com.lc.spring.annotion.RequestMapping;

/**
 * Created by liuchengli on 2016/10/29.
 */
@Controller("testController")
public class TestController {
    @Autowired("testService")
    private TestService testService;

    @RequestMapping("insertInCredit")
    public void insertInCredit(){
        Test test= new Test();
        testService.insert(test);
    }
    @RequestMapping("updateInCredit")
    public void updateInCredit(){
        Test test= new Test();
        testService.update(test);
    }

}
