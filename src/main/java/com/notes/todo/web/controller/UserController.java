package com.notes.todo.web.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notes.todo.core.annotation.Authorization;
import com.notes.todo.core.common.Result;
import com.notes.todo.core.utils.MD5Utils;
import com.notes.todo.core.utils.STDateUtils;
import com.notes.todo.core.utils.UUIDUtils;
import com.notes.todo.web.domain.User;
import com.notes.todo.web.redis.TokenManager;
import com.notes.todo.web.redis.TokenModel;
import com.notes.todo.web.service.IUserService;

@RestController
@RequestMapping(value="/user")
@CrossOrigin
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public Result register(@RequestParam(value="phone",required=true) String phone,
			@RequestParam(value="nickname",required=true)String nickname,
			@RequestParam(value="password",required=true)String password,
			@RequestParam(value="rePassword",required=true)String rePassword){
		Result result=new Result();
		if (phone == null || nickname == null || password == null
				|| rePassword == null) {
			result.setStatus(Result.FAILED);
			result.setTipCode("lackRegisterInfo");
			result.setTipMsg("注册信息不完整");
			return result;
		}
		if(!this.checkPhoneValid(phone)){
			result.setStatus(Result.FAILED);
			result.setTipCode("phoneNotValid");
			result.setTipMsg("手机号无效");
			return result;
		}
		if(!this.checkNicknameValid(nickname)){
			result.setStatus(Result.FAILED);
			result.setTipCode("nicknameNotValid");
			result.setTipMsg("用户昵称太短了");
			return result;
		}
		if(this.checkPhoneReg(phone)){
			result.setStatus(Result.FAILED);
			result.setTipCode("phoneRegistered");
			result.setTipMsg("手机号已经注册");
			return result;
		}
		if(!checkPasswordValid(password,rePassword)){
			result.setStatus(Result.FAILED);
			result.setTipCode("passwordNotValid");
			result.setTipMsg("密码输入无效或者两次密码不一致");
			return result;
		}
		User user=new User();
		String id = UUIDUtils.getUUID();
		String salt = UUIDUtils.getUUID();
		String registerTime = STDateUtils.getCurrentTime();
		user.setId(id);
		user.setSalt(salt);
		user.setPhone(phone);
		user.setPassword(MD5Utils.encrypt(password+salt));
		user.setAge(0);
		user.setAvator("");
		user.setRank(0);
		user.setRegisterTime(registerTime);
		user.setSex(0);
		if(userService.save(user)==null){
			result.setStatus(Result.FAILED);
			return result;
		}
		return result;
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result login(@RequestParam(value="phone",required=true) String phone,
			@RequestParam(value="password",required=true)String password){
		Result result=new Result();
		User user=userService.getByPhone(phone);
		if(!this.checkPhoneValid(phone)){
			result.setStatus(Result.FAILED);
			result.setTipCode("phoneNotValid");
			result.setTipMsg("手机号无效");
			return result;
		}
		if(user==null){
			result.setStatus(Result.FAILED);
			result.setTipCode("phoneNotRegister");
			result.setTipMsg("手机号未注册");
			return result;
		}
		if(!MD5Utils.encrypt(password+user.getSalt()).equals(user.getPassword())){
			result.setStatus(Result.FAILED);
			result.setTipCode("passwordNotVaolid");
			result.setTipMsg("密码错误");
			return result;
		}
		//生成一个token，保存用户登录状态
        TokenModel model = tokenManager.createToken(user.getId());
        result.setData(user);
		return result;
	}
	
	private boolean checkPasswordValid(String password, String rePassword) {
		return (password.length() >= 6 && password.length() <= 20 && password
				.equals(rePassword));
	}
	private boolean checkPhoneReg(String phone) {
		User user=userService.getByPhone(phone);
		return (user!=null);
	}
	private boolean checkNicknameValid(String nickname) {
		return nickname.length()>=3;
	}
	private boolean checkPhoneValid(String phone) {
		Pattern patternMobileNo = Pattern.compile("^1[34578]\\d{9}$");
		Matcher matcherMobileNo = patternMobileNo.matcher(phone);
		return matcherMobileNo.matches();
	}
}
