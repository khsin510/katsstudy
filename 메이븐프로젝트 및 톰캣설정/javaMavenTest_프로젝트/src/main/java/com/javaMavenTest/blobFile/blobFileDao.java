package com.javaMavenTest.blobFile;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class blobFileDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public void saveImage(Map<String, Object> hmap) throws SQLException {
		sqlSession.insert("blobFile.saveImage",hmap);
	}

	public Map<String, Object> getByteImage() {
		return sqlSession.selectOne("blobFile.getByteImage");
	}
	
	public List<Map<String,Object>> getByteImageList() {
		return sqlSession.selectList("blobFile.getByteImageList");
	}
	
//	public Map<String,Object> getByteImageMap() {
//		return sqlSession.select("blobFile.getByteImageMap");
//	}
	
}