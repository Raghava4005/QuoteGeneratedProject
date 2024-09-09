package com.user.service;

import java.util.Map;

import com.user.dto.LoginFormDTO;
import com.user.dto.RegisterFormDTO;
import com.user.dto.ResetPwdFormDTO;
import com.user.dto.UserDTO;

public interface UserManagementService {

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStates(Integer countryId);

	public Map<Integer, String> getCities(Integer stateId);

	public boolean duplicateEmailCheck(String email);

	public boolean saveUser(RegisterFormDTO regFormDTO);

	public UserDTO login(LoginFormDTO loginFormDTO);

	public boolean resetPwd(ResetPwdFormDTO resetPwdDTO);

}
