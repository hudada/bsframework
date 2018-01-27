package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.AccountBean;
import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/user")
public class ApiUserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountDao accountDao;

	@ApiOperation(value = "注册新业主", notes = "")
	@ApiImplicitParam(name = "accountBean", value = "业主实体对象AccountBean", required = true, dataType = "AccountBean")
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> addUser(@RequestBody AccountBean accountBean) {
		if (accountDao.findAccountByNumber(accountBean.getNumber()) == null) {
			AccountBean save = accountDao.save(accountBean);
			UserBean userBean = new UserBean();
			userBean.setNumber(save.getNumber());
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("门牌号已存在");
		}
	}

	@ApiOperation(value = "删除业主", notes = "")
	@ApiImplicitParam(name = "number", value = "门牌号", required = true, dataType = "Integer")
	@RequestMapping(value = "/{number}", method = RequestMethod.GET, produces = "application/json")
	public BaseBean<UserBean> deleteUser(@PathVariable Integer number) {
		userDao.delete(userDao.findUserByNumber(number));
		accountDao.delete(accountDao.findAccountByNumber(number));
		return ResultUtils.resultSucceed(null);
	}

	@ApiOperation(value = "修改个人信息", notes = "")
	@ApiImplicitParam(name = "userBean", value = "业主实体对象UserBean", required = true, dataType = "UserBean")
	@RequestMapping(value = "/", method = RequestMethod.PUT, produces = "application/json")
	public UserBean putUser(@RequestBody UserBean userBean) {
		UserBean userBean2 = userDao.findUserByNumber(userBean.getNumber());
		userBean2.setName(userBean.getName());
		userBean2.setTel(userBean.getTel());
		return userDao.save(userBean2);
	}
}
