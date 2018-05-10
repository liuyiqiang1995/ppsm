package com.ppsm.mobile.listener;

import com.ppsm.mobile.service.impl.JsonParseToHtmlServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebContextListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent servletContextEvent){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/application.xml");
        JsonParseToHtmlServiceImpl jsonParseToHtmlServiceImpl = ctx.getBean(JsonParseToHtmlServiceImpl.class);
//        jsonParseToHtmlServiceImpl.loadHtml();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
