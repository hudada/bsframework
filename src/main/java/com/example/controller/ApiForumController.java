package com.example.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.ForumBean;
import com.example.dao.ForumDao;
import com.example.utils.ResultUtils;

@RestController
@RequestMapping(value = "/api/forum")
public class ApiForumController {

	@Autowired
	private ForumDao forumDao;
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	

	/**
	 * 获取当前所有文章（及回复数）
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseBean<List<ForumBean>> getNotices() {
		
		EntityManager em = emf.createEntityManager();
//		String sql = "SELECT F.*,COUNT(*) FROM forum_bean F LEFT JOIN reply_bean R ON F.`id`=R.`forum_id` GROUP BY F.`id`";
		String sql = "select * form forum_bean";
		Query query = em.createNamedQuery(sql);
		List list=query.getResultList();
		
		List<ForumBean> forums=forumDao.getForumAndCount();
		return ResultUtils.resultSucceed(forums);
	}
}
