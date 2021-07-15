package com.lehansun.pet.project.controller.config;

import com.lehansun.pet.project.dao.config.DaoConfiguration;
import com.lehansun.pet.project.service.config.ServiceConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {DaoConfiguration.class, ServiceConfig.class, ServletConfig.class};
    }

    //    Указываем, какие классы необходимо использовать для конфигурации проекта
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {ServletConfig.class};
    }

    //    Все запросы от пользователя будут перенаправлены на DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

}
