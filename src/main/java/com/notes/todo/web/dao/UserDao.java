package com.notes.todo.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.notes.todo.web.domain.User;


/**
 * 用户User CrudRepository定义
 * 
 * @author Xiongbo
 *
 */
@Transactional
public interface UserDao extends CrudRepository<User, String> {
	User findById(String id);

	User findByPhone(String phone);
	
	User save(User user);
}
