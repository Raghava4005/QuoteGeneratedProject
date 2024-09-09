package com.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.dto.LoginFormDTO;
import com.user.dto.QuoteApiResponseDTO;
import com.user.dto.RegisterFormDTO;
import com.user.dto.ResetPwdFormDTO;
import com.user.dto.UserDTO;
import com.user.service.DashboardService;
import com.user.service.UserManagementService;

@Controller
public class UserManagementController {

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/register")
	public String registerPage(Model model) {

		Map<Integer, String> countriesMap = userManagementService.getCountries();
		model.addAttribute("countries", countriesMap);

		RegisterFormDTO registerFormDTO = new RegisterFormDTO();
		model.addAttribute("register", registerFormDTO);

		return "register";
	}

	@GetMapping("/states/{countryId}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {

		Map<Integer, String> statesMap = userManagementService.getStates(countryId);
		System.out.println(statesMap);
		return statesMap;
	}

	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {

		Map<Integer, String> citiesMap = userManagementService.getCities(stateId);

		return citiesMap;
	}

	@PostMapping("/register")
	public String handleRegisterPage(RegisterFormDTO registerFormDTO, Model model) {

		boolean status = userManagementService.duplicateEmailCheck(registerFormDTO.getEmail());
		if (status) {
			model.addAttribute("emgs", "Duplicate Email Found..");
		} else {
			boolean saveUser = userManagementService.saveUser(registerFormDTO);
			if (saveUser) {
				model.addAttribute("smgs", " Registration Sucessful, Please check your email..");
			} else {
				model.addAttribute("emgs", "Registration Failed..!!!");
			}
		}
		model.addAttribute("register", new RegisterFormDTO());
		model.addAttribute("countries", userManagementService.getCountries());
		return "register";
	}

	@GetMapping("/")
	public String index(Model model) {
		LoginFormDTO loginFormDTO = new LoginFormDTO();

		model.addAttribute("loginForm", loginFormDTO);

		return "login";
	}

	@PostMapping("/login")
	public String handleLogin(LoginFormDTO loginFormDTO, Model model) {

		UserDTO userDTO = userManagementService.login(loginFormDTO);

		if (userDTO == null) {
			model.addAttribute("emgs", "Invalid Credentials..!!!");
			model.addAttribute("loginForm", new LoginFormDTO());
		} else {
			String pwdReset = userDTO.getPwdReset();
			if ("Yes".equals(pwdReset)) {
				return "redirect:/dashboard";
			} else {
				return "redirect:/reset-pwd-page?email=" + userDTO.getEmail();
			}
		}

		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {

		QuoteApiResponseDTO quoteApiResponseDTO = dashboardService.getQuote();

		model.addAttribute("quote", quoteApiResponseDTO);
		return "dashboard";
	}

	@GetMapping("/reset-pwd-page")
	public String resetPwdPage(@RequestParam String email, Model model) {

		ResetPwdFormDTO resetPwdFormDTO = new ResetPwdFormDTO();

		resetPwdFormDTO.setEmail(email);

		model.addAttribute("resetPwd", resetPwdFormDTO);

		return "resetPassword";

	}

	@PostMapping("/resetPwd")
	public String handleResetPwd(@ModelAttribute("resetPwd") ResetPwdFormDTO resetPwdFormDTO, Model model) {
		boolean resetPwd = userManagementService.resetPwd(resetPwdFormDTO);

		if (resetPwd) {
			return "redirect:/dashboard";
		}
		model.addAttribute("resetPwd", resetPwdFormDTO);
		return "resetPassword";

	}

}
