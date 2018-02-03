package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

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
import com.example.WebSecurityConfig;

@Controller
@RequestMapping(value = "/page/user")
public class PageUserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountDao accountDao;

	// 返回用户表信息
	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public String table(ModelMap map) {
		map.addAttribute("list", userDao.findAll());
		return "usertable";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage() {
		return "useradd";
	}

	// 添加用户
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> addUser(@RequestBody UserBean userBean) {
		if (accountDao.findAccountByNumber(userBean.getNumber()) == null) {
			AccountBean accountBean = new AccountBean();
			accountBean.setNumber(userBean.getNumber());
			accountBean.setPwd("111111");
			AccountBean save = accountDao.save(accountBean);
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("用户名已存在");
		}
	}

	// 修改用户
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> editUser(@RequestBody UserBean userBean) {
		UserBean user = userDao.findUserByNumber(userBean.getNumber());
		user.setName(userBean.getName());
		user.setSex(userBean.getSex());
		user.setTel(userBean.getTel());
		user.setBalance(userBean.getBalance());
		return ResultUtils.resultSucceed(userDao.save(user));
	}

	// 删除用户
	@RequestMapping(value = "/detele/{number}", method = RequestMethod.GET)
	public BaseBean<UserBean> delUser(@PathVariable String number) {
		UserBean user = userDao.findUserByNumber(number);
		userDao.delete(user);
		return ResultUtils.resultSucceed("");
	}
}
