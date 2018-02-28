package com.example.apicontroller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/user")
public class ApiUserController {

	@Autowired
	private UserDao userDao;


	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> addUser(@RequestBody UserBean userBean) {
		if (userDao.findUserByNumber(userBean.getNumber()) == null) {
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("该账号已存在！");
		}
	}


	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> userLogin(@RequestBody UserBean userBean) {
		UserBean select = userDao.findUserByNumberAndPwd(userBean.getNumber(),userBean.getPwd());
		if(select==null) {
			return ResultUtils.resultError("账号或密码错误");
		}else {
			return ResultUtils.resultSucceed(select);
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editPass", method = RequestMethod.POST)
	public BaseBean<UserBean> editPass(HttpServletRequest request) {
		String oldPass = request.getParameter("oldPass");
		String newPass = request.getParameter("newPass");
		String number = request.getParameter("number");
		UserBean userBean = userDao.findUserByNumber(number);
		if (userBean.getPwd().equals(oldPass)) {
			userBean.setPwd(newPass);
			userDao.save(userBean);
			return ResultUtils.resultSucceed("密码修改成功！");
		} else {
			return ResultUtils.resultError("旧密码有误！");
		}

	}


	/**
	 * 获取用户信息
	 * 
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public BaseBean<UserBean> getUser(@PathVariable String number) {
		UserBean user = userDao.findUserByNumber(number);
		return ResultUtils.resultSucceed(user);
	}


}
