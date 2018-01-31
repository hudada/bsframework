package com.example.controller;



import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.AccountBean;
import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.PamentRecordBean;
import com.example.bean.UserBean;
import com.example.dao.UserDao;
import com.example.dao.pamentRecordDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value = "/record")
public class PamentRecordController2 {

	@Autowired
	private pamentRecordDao payDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 用户缴费
	 * @param id 缴费记录id
	 * @return
	 */
	@RequestMapping(value = "/editState/{id}", method = RequestMethod.PUT)
	public BaseBean<String> editRecordState(@PathVariable Long id) {
		PamentRecordBean recordBean=payDao.findOne(id);
		if (recordBean.getState()==1) {
			return ResultUtils.resultError("该账单已缴清,请勿重复缴费！");
		}else {
			UserBean userbean=userDao.findUserByNumber(recordBean.getNumber());
			if (userbean.getBalance()>=recordBean.getAmount()) {
				userbean.setBalance(userbean.getBalance()-recordBean.getAmount());
				recordBean.setState(1);
				userDao.save(userbean);
				payDao.save(recordBean);
				return ResultUtils.resultSucceed("缴费成功！");
			}else {
				return ResultUtils.resultError("余额不足，请至个人中心充值！");
			}
		}
	}
	
	/**
	 * 缴费记录查询
	 * @param number 房屋号
	 * @return
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public BaseBean<List<PamentRecordBean>> getRecords(@PathVariable Integer number) {
		List<PamentRecordBean> record=payDao.findAccountByNumber(number);
		return ResultUtils.resultSucceed(record);
	}
//
//	@ApiOperation(value = "新增缴费记录", notes = "")
//	@ApiImplicitParam(name = "adminBean", value = "管理员对象AdminBean", required = true, dataType = "AdminBean")
//
//	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
//	public BaseBean<String> userLogin(@RequestBody AdminBean adminBean) {
//		AdminBean admin = adminDao.findAdminBeanByNumber(adminBean.getAdminCode());
//		if (admin != null) {
//			if (admin.getPwd().equals(adminBean.getPwd())) {
//				return ResultUtils.resultSucceed("登陆成功");
//			}else {
//				return ResultUtils.resultError("密码错误！");
//			}
//		} else {
//			return ResultUtils.resultError("账户不存在！");
//		}
//	}
}
