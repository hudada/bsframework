package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.UserDao;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserDao userDao;

	@ApiOperation(value = "获取业主列表", notes = "")
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public BaseBean<List<UserBean>> getUserList() {
		List<UserBean> r = userDao.findAll();
		BaseBean<List<UserBean>> baseBean = new BaseBean<>();
		baseBean.setCode(0);
		baseBean.setData(r);
		baseBean.setMessage("succeed");
		return baseBean;
	}

	@ApiOperation(value = "添加新业主", notes = "")
	@ApiImplicitParam(name = "userBean", value = "业主实体对象UserBean", required = true, dataType = "UserBean")
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public UserBean addUser(@RequestBody UserBean userBean) {
		return userDao.save(userBean);
	}

	@ApiOperation(value = "修改业主", notes = "")
	@ApiImplicitParams({ @ApiImplicitParam(name = "number", value = "门牌号", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "userBean", value = "业主实体对象UserBean", required = true, dataType = "UserBean") })
	@RequestMapping(value = "/{number}", method = RequestMethod.PUT, produces = "application/json")
	public UserBean putUser(@PathVariable Integer number, @RequestBody UserBean userBean) {
		return userDao.save(userBean);
	}
}
