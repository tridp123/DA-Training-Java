package com.springjpa.controller;

import java.security.Principal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.security.util.WebUtils;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
public class WebController {

	public static final Logger log = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	LocationService locationRepository = new LocationServiceImpl();

	
	@GetMapping(value = {"/","/welcome"})
	public ModelAndView welcomePage(Model model) {
		ModelAndView modelAndView = new ModelAndView("welcomePage");
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        modelAndView.addObject(model);
        return modelAndView;
    }
	
	
	@GetMapping(value = {"/admin"})
	  public ModelAndView adminPage(Model model, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("adminPage");
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        modelAndView.addObject(model);
        return modelAndView;
    }

	@GetMapping(value = {"/login"})
	 public ModelAndView loginPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("loginPage");
        return modelAndView;
    }

	@GetMapping(value = {"/logoutsuccessful"})
	public ModelAndView logoutSuccessfulPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("logoutSuccessfulPage");
        model.addAttribute("title", "Logout");
        modelAndView.addObject(model);
        return modelAndView;
    }
	
	@GetMapping(value = {"/userInfo"})
	 public ModelAndView userInfo(Model model, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("userInfoPage");
        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        modelAndView.addObject(model);
        return modelAndView;
    }
	
	@GetMapping(value = {"/403"})
	 public ModelAndView accessDenied(Model model, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("403Page");
        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
            String userInfo = WebUtils.toString(loginedUser);
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
            modelAndView.addObject(model);
        }
        return modelAndView;
    }
	
	@RequestMapping("/save")
	public String process() {
		// save a Location info Cassandra
		// sample data
		LocationCas l1 = new LocationCas(UUID.randomUUID(), "USA", "New York", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		LocationCas l2 = new LocationCas(UUID.randomUUID(), "Japan", "Tokyo", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		LocationCas l3 = new LocationCas(UUID.randomUUID(), "Laos", "Vieng Chan", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());

		locationRepository.saveLocationCas(l1);
		locationRepository.saveLocationCas(l2);
		locationRepository.saveLocationCas(l3);

		return "Done";
	}
	
	
}
