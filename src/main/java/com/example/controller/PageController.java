package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.AccountBean;
import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/page")
public class PageController {

	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap map) {
		map.put("welcomeMsg","谢谢你~加");
		return "login";
	}
	
	
}
