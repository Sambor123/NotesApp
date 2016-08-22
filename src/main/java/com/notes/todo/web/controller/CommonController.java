package com.notes.todo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notes.todo.core.common.Result;

@RestController
@RequestMapping
public class CommonController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value="notLogin")
	public Result notLogin(){
		Result result=new Result();
		result.setStatus(Result.FAILED);
		result.setTipCode("notLogin");
		result.setTipMsg("未登录，非法操作");
		return result;
	}
}
