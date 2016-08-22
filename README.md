# Notes.Do API接口文档
------
注：验证身份即发送token头给后台，android中给http请求添加一个header键值对token={你的token},第一次登录成功后后台会返回一个token给你，你将该token保存到preference中。下次需要时发给后台，主要用来维持用户登录状态

## 用户模块
------

### 注册用户
- URL: http://114.215.135.153:8888/user/
- 请求方法：POST
- 参数：phone,nickname,password,rePassword
- 是否需要验证身份：否
- 返回结果样例：{
  "status": "success",
  "tipCode": "",
  "tipMsg": "",
  "data": null
}

### 登录用户

- URL: http://114.215.135.153:8888/user/login
- 请求方法：POST
- 参数：phone,password
- 是否需要验证身份:否
- 返回结果：{
  "status": "success",
  "tipCode": "",
  "tipMsg": "",
  "data": null
}


### 获取指定id用户数据
- URL: http://114.215.135.153:8888/user/{id}
- 请求方法：GET
- 参数：id
- 是否需要验证身份:是
- 返回结果：- {"status":"success","tipCode":"","tipMsg":"","data":{"score":null,"city":null,"phone":"18813073879","sex":null,"nickname":"xiaobo","avator":null,"age":null,"username":"xiaobo"}}

### 获取当前用户信息（我的个人信息）
- URL: http://114.215.135.153:8888/user/me
- 请求方法：GET
- 参数：无
- 是否需要登录验证:是
- 返回结果：- {"status":"success","tipCode":"","tipMsg":"","data":{"score":null,"city":null,"phone":"18813073879","sex":null,"nickname":"xiaobo","avator":null,"age":null,"username":"xiaobo"}}

## ErrorCode

### 错误代码对照表

---
