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
public class PamentRecordController {

	@Autowired
	private pamentRecordDao payDao;
	@Autowired
	private UserDao userDao;

	@ApiOperation(value = "用户缴费接口", notes = "")
	@RequestMapping(value = "/editState/{id}", method = RequestMethod.PUT)
	public BaseBean<String> editRecordState(@PathVariable Long id) {
		PamentRecordBean recordBean = payDao.findOne(id);
		if (recordBean != null) {
			if (recordBean.getState() == 1) {
				return ResultUtils.resultError("该账单已缴清,请勿重复缴费！");
			} else {
				UserBean userbean = userDao.findUserByNumber(recordBean.getNumber());
				if (userbean.getBalance() >= recordBean.getAmount()) {
					userbean.setBalance(userbean.getBalance() - recordBean.getAmount());
					recordBean.setState(1);
					userDao.save(userbean);
					payDao.save(recordBean);
					return ResultUtils.resultSucceed("缴费成功！");
				} else {
					return ResultUtils.resultError("余额不足，请至个人中心充值！");
				}
			}
		} else {
			return ResultUtils.resultError("缴费记录不存在！");
		}
	}

	@ApiOperation(value = "查询指定用户缴费记录", notes = "")
	@RequestMapping(value = "/getRecordsByNumber/{number}", method = RequestMethod.GET)
	public BaseBean<List<PamentRecordBean>> getRecordsByNumber(@PathVariable String number) {
		List<PamentRecordBean> record = payDao.findAccountByNumber(number);
		return ResultUtils.resultSucceed(record);
	}

	@ApiOperation(value = "查询指定类型缴费记录", notes = "")
	@RequestMapping(value = "/getRecordsByType/{type}", method = RequestMethod.GET)
	public BaseBean<List<PamentRecordBean>> getRecordsByType(@PathVariable Integer type) {
		List<PamentRecordBean> record = payDao.findAccountByType(type);
		return ResultUtils.resultSucceed(record);
	}

	@ApiOperation(value = "查询指定状态缴费记录", notes = "")
	@RequestMapping(value = "/getRecordsByState/{state}", method = RequestMethod.GET)
	public BaseBean<List<PamentRecordBean>> getRecordsByState(@PathVariable Integer state) {
		List<PamentRecordBean> record = payDao.findAccountByState(state);
		return ResultUtils.resultSucceed(record);
	}

	@ApiOperation(value = "查询全部缴费记录", notes = "")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseBean<List<PamentRecordBean>> getAllRecords() {
		List<PamentRecordBean> record = payDao.findAll();
		return ResultUtils.resultSucceed(record);
	}

	@ApiOperation(value = "新增缴费记录", notes = "")
	@ApiImplicitParam(name = "pamentRecordBean", value = "管理员对象PamentRecordBean", required = true, dataType = "PamentRecordBean")

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<PamentRecordBean> addRecord(@RequestBody PamentRecordBean pamentRecordBean) {
		return ResultUtils.resultSucceed(payDao.save(pamentRecordBean));
	}

	@ApiOperation(value = "删除指定缴费记录", notes = "")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public BaseBean<List<String>> deleteRecord(@PathVariable Long id) {
		if (payDao.findOne(id) != null) {
			payDao.delete(id);
			return ResultUtils.resultSucceed("缴费记录已删除！");
		} else {
			return ResultUtils.resultError("未找到相关记录！");
		}

	}

	@ApiOperation(value = "修改缴费记录", notes = "")
	@ApiImplicitParam(name = "pamentRecordBean", value = "管理员对象PamentRecordBean", required = true, dataType = "PamentRecordBean")

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<PamentRecordBean> editRecord(@RequestBody PamentRecordBean pamentRecordBean) {
		if (payDao.findOne(pamentRecordBean.getId()) != null) {
			return ResultUtils.resultSucceed(payDao.save(pamentRecordBean));
		} else {
			return ResultUtils.resultError("缴费记录不存在！");

		}
	}

}
