package com.javaMavenTest.json;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaMavenTest.common.CommonController;

@Controller
public class jsonController {
	@Autowired
	CommonController commonController;
	
	@ResponseBody //�������·� ������ �ϰڴٰ� ���� 
	@RequestMapping(value = "/json",  produces="text/html;charset=UTF-8")
	public String json() {		
		return "��������";
	}
	
	
	
}