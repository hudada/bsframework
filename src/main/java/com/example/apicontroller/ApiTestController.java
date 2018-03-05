package com.example.apicontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.Transform;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.BaseBean;
import com.example.bean.TestBean;
import com.example.bean.UserBean;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/test")
public class ApiTestController {

	
	@PersistenceContext  
    private EntityManager entityManager;  
	
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public BaseBean<TestBean> test() {
		String sql = "SELECT address, o.name FROM save_bean s left join out_type_bean o on "
				+ "s.flag = o.id";

		Query query = entityManager.createNativeQuery(sql);
		
		TestBean list = (TestBean) query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(TestBean.class)).uniqueResult();
		List<TestBean> list1 = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(TestBean.class)).list();
		return ResultUtils.resultSucceed(list);
	}
}
