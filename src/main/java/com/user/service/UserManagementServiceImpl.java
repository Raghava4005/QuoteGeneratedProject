package com.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.dto.LoginFormDTO;
import com.user.dto.RegisterFormDTO;
import com.user.dto.ResetPwdFormDTO;
import com.user.dto.UserDTO;
import com.user.entities.City;
import com.user.entities.Country;
import com.user.entities.State;
import com.user.entities.UserManagement;
import com.user.repo.CityRepo;
import com.user.repo.CountryRepo;
import com.user.repo.StateRepo;
import com.user.repo.UserManagementRepo;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	private UserManagementRepo userManagementRepo;
	@Autowired
	private CountryRepo countryRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Override
	public Map<Integer, String> getCountries() {

		Map<Integer, String> countryMap = new HashMap<Integer, String>();

		List<Country> countriesList = countryRepo.findAll();

		countriesList.stream().forEach(c -> {
			countryMap.put(c.getCounrtyId(), c.getCounrtyName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> stateMap = new HashMap<Integer, String>();

		List<State> statesList = stateRepo.findByCountryId(countryId);

		statesList.stream().forEach(s -> {
			stateMap.put(s.getStateId(), s.getStateName());
		});
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {

		Map<Integer, String> cityMap = new HashMap<Integer, String>();

		List<City> citiesList = cityRepo.findByStateId(stateId);

		citiesList.stream().forEach(c -> {
			cityMap.put(c.getCityId(), c.getCityName());
		});
		return cityMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		UserManagement byEmail = userManagementRepo.findByEmail(email);
		if (byEmail != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean saveUser(RegisterFormDTO regFormDTO) {

		UserManagement userManagement = new UserManagement();

		BeanUtils.copyProperties(regFormDTO, userManagement);

		Country country = countryRepo.findById(regFormDTO.getCountryId()).orElse(null);
		userManagement.setCountry(country);

		State state = stateRepo.findById(regFormDTO.getStateId()).orElse(null);
		userManagement.setState(state);

		City city = cityRepo.findById(regFormDTO.getCityId()).orElse(null);
		userManagement.setCity(city);

		String randomPassword = generateRandomPassword();

		userManagement.setPwd(randomPassword);

		userManagement.setPwdReset("No");

		UserManagement saveUser = userManagementRepo.save(userManagement);

		if (null != saveUser.getUserId()) {

			String subject = " Your Account created";

			String body = "Your Password To Login : " + randomPassword;

			String to = regFormDTO.getEmail();

			emailServiceImpl.sendEmail(subject, body, to);

			return true;

		}

		return false;
	}

	@Override
	public UserDTO login(LoginFormDTO loginFormDTO) {

		UserManagement userLogin = userManagementRepo.findByEmailAndPwd(loginFormDTO.getEmail(), loginFormDTO.getPwd());

		if (userLogin != null) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userLogin, userDTO);
			return userDTO;
		}

		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFormDTO resetPwdDTO) {

		String email = resetPwdDTO.getEmail();

		UserManagement user = userManagementRepo.findByEmail(email);
		user.setPwd(resetPwdDTO.getNewPwd());
		user.setPwdReset("Yes");

		userManagementRepo.save(user);

		return true;
	}

	private String generateRandomPassword() {

		String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";

		String alpha = upperCaseLetters + lowerCaseLetters;

		Random random = new Random();

		StringBuffer str = new StringBuffer();

		for (int i = 0; i < 5; i++) {
			int nextInt = random.nextInt(alpha.length());
			str.append(alpha.charAt(nextInt));
		}
		return str.toString();
	}

}
