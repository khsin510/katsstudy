package com.javaMavenTest.bbs;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class bbsDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public List<bbsVo> getList(){ 
		System.out.println("@@getList");
	List<bbsVo>  list = sqlSession.selectList("bbs.getList");
	return list;
	}	
	
	public int insertFirst(){ 
		System.out.println("@@insertFirst");
	return sqlSession.insert("bbs.insertFirst");
	
	}	
	
	public void insertTwo(){ // throws Exception으로 에러내용을 컨트롤러로 전달 함
		System.out.println("@@insertTwo");
	sqlSession.insert("bbs.insertTwo");
	
	}	
	
}