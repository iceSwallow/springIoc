package com.lc.biz.service.impl;

import com.lc.biz.entity.Test;
import com.lc.biz.service.TestService;
import com.lc.spring.annotion.Service;

/**
 * Created by liuchengli on 2016/10/29.
 */
@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public void insert(Test test) {

        System.out.println("Service insert  is success");
    }

    @Override
    public void update(Test te) { System.out.println("Service update  is success");}
}
