package com.notes.todo.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.notes.todo.core.annotation.Authorization;
import com.notes.todo.core.common.Constant;
import com.notes.todo.web.redis.RedisTokenManager;
import com.notes.todo.web.redis.TokenManager;
import com.notes.todo.web.redis.TokenModel;

/**
 * 自定义拦截器，判断此次请求是否有权限
 * @author xiongbo
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	
	@Autowired
    private RedisTokenManager redisTokenManager;
	
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
        	//从header中得到token
            String authorization = request.getHeader(Constant.AUTHORIZATION);
            //验证token
            TokenModel model = redisTokenManager.getToken(authorization);
            if (redisTokenManager.checkToken(model)){
                //如果token验证成功，将token对应的用户id存在request中，便于之后注入
                request.setAttribute(Constant.CURRENT_USER_ID, model.getUserId());
                LOGGER.info("用户已经授权token"+model.getToken());
                return true;
            }
        	LOGGER.info("该页面需要登录，用户未登录");
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/notLogin");
        }
        return true;
    }
}