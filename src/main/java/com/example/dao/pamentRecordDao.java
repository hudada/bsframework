package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.PamentRecordBean;


public interface pamentRecordDao extends JpaRepository<PamentRecordBean, Long> {
	@Query("from PamentRecordBean p where p.number=:number")
    List<PamentRecordBean> findAccountByNumber(@Param("number") Integer number);
}
