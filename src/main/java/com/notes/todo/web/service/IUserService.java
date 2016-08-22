package com.notes.todo.web.service;

import com.notes.todo.web.domain.User;

public interface IUserService {
	User getById(String id);

	User getByPhone(String phone);

	User save(User user);
}
