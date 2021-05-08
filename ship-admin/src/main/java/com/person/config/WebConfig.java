package com.person.config;

import com.google.common.collect.Lists;
import com.person.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 自定义项目配置
 * @Author Xutong Li
 * @Date 2021/5/8
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static List<String> ignoreUrlList = Lists.newArrayList("/app/register","/app/unregister",
            "/user/login","/user/login/page","/static/**");


    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 加入拦截器
     * @Author Xutong Li
     * @Date 2021/5/8
     * @param registry
     * @return void
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(ignoreUrlList);
    }

    /**
     * 配置静态资源
     * @Author Xutong Li
     * @Date 2021/5/8
     * @param registry
     * @return void
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
