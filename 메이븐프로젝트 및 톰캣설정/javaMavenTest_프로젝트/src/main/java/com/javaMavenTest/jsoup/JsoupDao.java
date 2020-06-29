package com.javaMavenTest.jsoup;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JsoupDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public List<String> getList(){ 
	List<String>  list = sqlSession.selectList("bbs.getList");
	return list;
	}	
	
}