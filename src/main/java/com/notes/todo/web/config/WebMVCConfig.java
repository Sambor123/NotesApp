package com.notes.todo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.notes.todo.web.interceptor.AuthorizationInterceptor;

@Configuration   //标注此文件为一个配置项，spring boot才会扫描到该配置。
public class WebMVCConfig extends WebMvcConfigurerAdapter {
	@Autowired
	AuthorizationInterceptor AuthorizationInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AuthorizationInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
