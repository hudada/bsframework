package com.example.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bean.ForumBean;

public interface ForumDao extends JpaRepository<ForumBean, Long> {
	@Query(value = "SELECT F.*,COUNT(*) FROM FORUM_BEAN F LEFT JOIN reply_bean R ON F.`id`=R.`forum_id` GROUP BY F.`id`", nativeQuery = true)
	public ArrayList<ForumBean> getForumAndCount();
}
