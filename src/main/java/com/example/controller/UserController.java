package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.WebSecurityConfig;
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

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AdminDao adminDao;
	
	@ApiOperation(value = "获得业主列表", notes = "")
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public BaseBean<List<UserBean>> getUserList() {
		List<UserBean> users = userDao.findAll();
		return ResultUtils.resultSucceed(users);
	}

	@ApiOperation(value = "创建新业主（默认密码：111111）", notes = "")
	@RequestMapping(value = "/{number}", method = RequestMethod.PUT)
	public BaseBean<UserBean> addUser(@PathVariable Integer number) {
		if (accountDao.findAccountByNumber(number) == null) {
			AccountBean accountBean=new AccountBean();
			accountBean.setNumber(number);
			accountBean.setPwd("111111");
			AccountBean save = accountDao.save(accountBean);
			UserBean userBean = new UserBean();
			userBean.setNumber(save.getNumber());
			// 默认性别未知
			userBean.setSex(2);
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("门牌号已存在");
		}
	}
	
	@ApiOperation(value = "管理员登陆", notes = "")
	@ApiImplicitParam(name = "adminBean", value = "管理员对象AdminBean", required = true, dataType = "AdminBean")

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<String> userLogin(@RequestBody AdminBean adminBean,HttpSession session) {
		AdminBean admin = adminDao.findAdminBeanByNumber(adminBean.getAdminCode());
		if (admin != null) {
			if (admin.getPwd().equals(adminBean.getPwd())) {
				session.setAttribute(WebSecurityConfig.SESSION_KEY, adminBean);
				return ResultUtils.resultSucceed("登陆成功");
			}else {
				return ResultUtils.resultError("密码错误！");
			}
		} else {
			return ResultUtils.resultError("账户不存在！");
		}
	}
	
	@ApiOperation(value = "用户密码重置（默认密码：111111）", notes = "")
	@RequestMapping(value = "/editPwd/{number}", method = RequestMethod.PUT)
	public BaseBean<String> editUserPwd(@PathVariable Integer number) {
		if (accountDao.findAccountByNumber(number) != null) {
			AccountBean accountBean=accountDao.findAccountByNumber(number);
			accountBean.setPwd("111111");
			accountDao.save(accountBean);
			return ResultUtils.resultSucceed("用户密码充值成功！默认密码：111111！");
		} else {
			return ResultUtils.resultError("门牌号不存在");
		}
	}
	
	@ApiOperation(value = "获取指定用户信息", notes = "")
	@RequestMapping(value = "/getUser/{number}", method = RequestMethod.GET)
	public BaseBean<UserBean> getUser(@PathVariable Integer number) {
		if (userDao.findUserByNumber(number) != null) {
			return ResultUtils.resultSucceed(userDao.findUserByNumber(number));
		} else {
			return ResultUtils.resultError("没有该用户");
		}
	}
	
}
