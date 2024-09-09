package com.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entities.UserManagement;

public interface UserManagementRepo extends JpaRepository<UserManagement, Integer> {
	
	public UserManagement findByEmailAndPwd(String email, String pwd);
	
	public UserManagement findByEmail(String email);

}
