package com.example.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.bean.AccountBean;
import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.ForumAndReplyCount;
import com.example.bean.ForumBean;
import com.example.bean.NoticeBean;
import com.example.bean.PamentRecordBean;
import com.example.bean.ReplyBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.ForumDao;
import com.example.dao.NoticeDao;
import com.example.dao.ReplyDao;
import com.example.dao.UserDao;
import com.example.dao.pamentRecordDao;
import com.example.utils.ResultUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.spring.web.json.Json;

import com.example.WebSecurityConfig;

@Controller
public class PageLoginController {

	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private pamentRecordDao pamentRecordDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private ForumDao forumDao;
	@Autowired
	private ReplyDao replyDao;

	// 返回登录页面
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap map) {
		return "newlogin";
	}

	// 登录接口
	@RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<String> userLogin(@RequestBody AdminBean adminBean, HttpSession session) {
		AdminBean admin = adminDao.findAdminBeanByNumber(adminBean.getAdminCode());
		if (admin != null) {
			if (admin.getPwd().equals(adminBean.getPwd())) {
				session.setAttribute(WebSecurityConfig.SESSION_KEY, adminBean);
				return ResultUtils.resultSucceed("登陆成功");
			} else {
				return ResultUtils.resultError("密码错误！");
			}
		} else {
			return ResultUtils.resultError("账户不存在！");
		}
	}

	// 退出登录接口
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String loginOut(HttpSession session) {
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		return "redirect:/login";
	}
	
	private ModelMap getPub(ModelMap map,HttpSession session) {
		AdminBean admin = (AdminBean) session.getAttribute(WebSecurityConfig.SESSION_KEY);
		map.addAttribute("name", admin.getAdminCode());
		map.addAttribute("count0", userDao.count());
		map.addAttribute("count1", pamentRecordDao.count());
		map.addAttribute("count2", noticeDao.count());
		map.addAttribute("count3", forumDao.count());
		return map;
	}

	// 返回管理首页
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap map, HttpSession session) {
		getPub(map,session);
		return "newindex";
	}

	// ----------------------------------------------------------------
	// 添加用戶
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String addUser(ModelMap map, HttpSession session) {
		getPub(map,session);
		return "user/addform";
	}

	// 用戶管理
	@RequestMapping(value = "/userManager", method = RequestMethod.GET)
	public String userManager(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list", userDao.findAll());
		return "user/newtable";
	}

	// 修改用戶
	@RequestMapping(value = "/editUser/{number}", method = RequestMethod.GET)
	public String editUser(@PathVariable String number,ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("userBean", userDao.findUserByNumber(number));
		return "user/editform";
	}

	// 添加水费
	@RequestMapping(value = "/addRecord0", method = RequestMethod.GET)
	public String addRecord0(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add0";
	}

	// 添加电费
	@RequestMapping(value = "/addRecord1", method = RequestMethod.GET)
	public String addRecord1(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add1";
	}

	// 添加燃气费
	@RequestMapping(value = "/addRecord2", method = RequestMethod.GET)
	public String addRecord2(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add2";
	}

	// 添加停车费
	@RequestMapping(value = "/addRecord3", method = RequestMethod.GET)
	public String addRecord3(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add3";
	}

	// 添加物管费
	@RequestMapping(value = "/addRecord4", method = RequestMethod.GET)
	public String addRecord4(ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add4";
	}

	// 缴费管理
	@RequestMapping(value = "/recordManager", method = RequestMethod.GET)
	public String recordManager(ModelMap map, HttpSession session) {
		getPub(map,session);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<PamentRecordBean> list = pamentRecordDao.findOrderByState();
		for(PamentRecordBean bean:list) {
			bean.setDate(format.format(new Date(Long.parseLong(bean.getDate()))));
		}
		map.addAttribute("list", list);
		return "pamentRecord/newtable";
	}

	// 添加公告
	@RequestMapping(value = "/addNotice", method = RequestMethod.GET)
	public String addNotice(ModelMap map, HttpSession session) {
		getPub(map,session);
		return "notice/addform";
	}

	// 公告管理
	@RequestMapping(value = "/noticeManager", method = RequestMethod.GET)
	public String noticeManager(ModelMap map, HttpSession session) {
		getPub(map,session);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<NoticeBean> list = noticeDao.findAll();
		for(NoticeBean bean:list) {
			bean.setDate(format.format(new Date(Long.parseLong(bean.getDate()))));
		}
		map.addAttribute("list", list);
		return "notice/newtable";
	}

	// 修改
	@RequestMapping(value = "/editNotice/{id}", method = RequestMethod.GET)
	public String editNotice(@PathVariable String id,ModelMap map, HttpSession session) {
		getPub(map,session);
		map.addAttribute("noticeBean", noticeDao.findOne(Long.parseLong(id)));
		return "notice/editform";
	}

	// 论坛管理
	@RequestMapping(value = "/forumManager", method = RequestMethod.GET)
	public String forumManager(ModelMap map, HttpSession session) {
		getPub(map,session);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ForumAndReplyCount> result = new ArrayList<>();
		List<ForumBean> forums=forumDao.findAll();
		for (ForumBean forumBean : forums) {
			ForumAndReplyCount forumAndReplyCount = new ForumAndReplyCount();
			forumAndReplyCount.setId(forumBean.getId());
			forumAndReplyCount.setTitle(forumBean.getTitle());
			forumAndReplyCount.setInfo(forumBean.getInfo());
			forumAndReplyCount.setNumber(forumBean.getNumber());
			forumAndReplyCount.setDate(format.format(new Date(Long.parseLong(forumBean.getDate()))));
			forumAndReplyCount.setState(forumBean.getState());
			int count = replyDao.findCountByForumId(forumBean.getId()+"");
			forumAndReplyCount.setCount(count);
			result.add(forumAndReplyCount);
		}
		map.addAttribute("list", result);
		return "forum/newtable";
	}

	// 回复管理
	@RequestMapping(value = "/replyManager/{id}", method = RequestMethod.GET)
	public String replyManager(@PathVariable String id,ModelMap map, HttpSession session) {
		getPub(map,session);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ReplyBean> list = replyDao.findReplayByForumId(id);
		for(ReplyBean bean:list) {
			bean.setDate(format.format(new Date(Long.parseLong(bean.getDate()))));
		}
		map.addAttribute("list", list);
		map.addAttribute("sum", list.size());
		return "reply/newtable";
	}

}
