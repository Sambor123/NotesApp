package com.notes.todo.web.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	//使用数据库
	@Autowired
	DataSource dataSource;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//            .antMatchers("*").permitAll();
		
//		http
//        .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//        .requestCache()
//        .requestCache(new NullRequestCache())
//        .and()
//        .httpBasic();
		http.csrf().disable();
    }
}
