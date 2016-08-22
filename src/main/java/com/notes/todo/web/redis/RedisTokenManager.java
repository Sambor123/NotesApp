package com.notes.todo.web.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.stereotype.Component;

import com.notes.todo.core.common.Constant;
import com.notes.todo.core.utils.UUIDUtils;
import com.notes.todo.web.controller.UserController;

import javassist.expr.NewArray;

@Component
public class RedisTokenManager implements TokenManager{
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTokenManager.class);
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
    public TokenModel createToken(String userId) {
        //使用uuid作为源token
        String token = UUIDUtils.getUUID();
        TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        LOGGER.info("用户登录的token为"+token);
        stringRedisTemplate.boundValueOps(userId).set(token, Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }
    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
        	 LOGGER.info("authentication为空");
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        //使用userId和源token简单拼接成的token，可以增加加密措施
        String userId = param[0];
        String token = param[1];
        LOGGER.info(userId+":"+token);
        return new TokenModel(userId, token);
    }
    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        LOGGER.info("userId:"+model.getUserId());
        LOGGER.info("token:"+model.getToken());
        String token = stringRedisTemplate.boundValueOps(model.getUserId()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        stringRedisTemplate.boundValueOps(model.getUserId()).expire(Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }
    @Override
    public void deleteToken(String userId) {
        stringRedisTemplate.delete(userId);
    }

}
