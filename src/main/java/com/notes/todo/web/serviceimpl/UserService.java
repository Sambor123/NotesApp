package com.notes.todo.web.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notes.todo.web.dao.UserDao;
import com.notes.todo.web.domain.User;
import com.notes.todo.web.service.IUserService;

@Service
public class UserService implements IUserService{

	@Autowired
	private UserDao userDao;
	@Override
	public User getById(String id) {
		// TODO Auto-generated method stub
		return userDao.findById(id);
	}
	@Override
	public User getByPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.findByPhone(phone);
	}
	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userDao.save(user);
	}

}
