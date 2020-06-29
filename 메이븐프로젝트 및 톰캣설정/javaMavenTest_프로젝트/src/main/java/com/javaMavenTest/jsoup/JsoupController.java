package com.javaMavenTest.jsoup;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaMavenTest.common.CommonController;

@Controller
public class JsoupController {
	
	@Autowired
	CommonController common;
	
	@Autowired
	JsoupDao jsuopDao;

	@ResponseBody //�������·� ������ �ϰڴٰ� ���� 
	@RequestMapping(value = "/jsoup1",  produces="text/html;charset=UTF-8")
	public String jsoup() {
		Document doc = null;
		try{
		doc = Jsoup.connect("http://www.safetykorea.kr/release/certificationsearch").get();		
		}catch(IOException e){
			e.printStackTrace();
		}
		Elements contents = doc.select(".contents_area");
		System.out.println("TEST : " + contents);
		//String textSt = contents.text(); // �±� ���� �����͸� ������
		String textSt = contents.html(); // �±ױ��� �����ؼ� ������
		System.out.println("TEST");
		System.out.println(textSt);
		
		return textSt;
	}
	
	@ResponseBody //�������·� ������ �ϰڴٰ� ���� 
	@RequestMapping(value = "/jsoup2",  produces="text/html;charset=UTF-8")
	public void jsoup2() {
		// Jsoup�� �̿��ؼ� ���̹� ������ ũ�Ѹ�
        String url = "https://sports.news.naver.com/wfootball/index.nhn";
        Document doc = null;
         
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  System.out.println("doc : " + doc);
        // �ֿ� ������ ������ �±׸� ã�Ƽ� ���������� �Ѵ�.
        Elements element = doc.select("div.home_news");
         
        // 1. ��� �κ��� ������ �����´�.
        String title = element.select("h2").text().substring(0, 4);
 
        System.out.println("============================================================");
        //System.out.println(title);
        System.out.println(element);
        System.out.println("============================================================");
         
        for(Element el : element.select("li")) {    // ���� ���� ������ for�� ���鼭 ���
            System.out.println(el.text());
        }
         
        System.out.println("============================================================");
    }
	
		
	@ResponseBody //�������·� ������ �ϰڴٰ� ���� 
	@RequestMapping(value = "/jsoup3",  produces="text/html;charset=UTF-8")
	public String jsoup3() {
		String value = "=================������ ���� ������ǰ �������� ũ�Ѹ�======================<br>";
		int page = 0;
		// Jsoup�� �̿��ؼ� ���� ������ �˻���� ù������ ũ�Ѹ�
		// start = ���� ����¡ 0, 10, 20, 30 ....
		// q = �˻��� 

		while(true){
        //String url = "https://www.google.com/search?q=%EC%8A%AC%EB%9D%BC%EC%9E%84&start="+page;
		String url = "https://www.google.com/search?q=������&start="+page;
        System.out.println("page : " + page);
        
        page+=10;
        Document doc = null;
        
        try { 
        	Thread.sleep(2000); // �����ð��� ��û�� �ʹ� ������� ���ܴ��ϹǷ� sleep�� �־� ������ �־���
        	doc = Jsoup.connect(url).get();
        } catch (IOException e ) {
        	e.printStackTrace(); 
        	}catch(InterruptedException e){
        		e.getMessage();
        	}
         /*
         ���� �˻� �� ��
         <div class="rc">
	         <div calss="r">
	 		 	<h3 class="LC20lb"/> �� ����
	 		 	<div class="TbwUpd">
	         		<citi class="iUh30"> ��ũ
	         	</div>
	         </div>
	         <div class="s">
	         <span class="st">
	         	<span class="f"> ����
	         </sapn>
	         </div>
         </div>
         */
        Elements total = doc.select("div.rc");
        //System.out.println("total : " + total.text().length());
        if(total.text().length() == 0){break;}//������ �̵� �ϴ� ����� ���� �������� ������ while���� ����������.
        
	  for(Element link : total) {
		   if(link.select("h3.LC20lb").text()!= null && !link.select("h3.LC20lb").text().equals("")){// ������ ���� �������
			   if( // �ش� Ű���忡 �´� �����͸� �����´�.
				  link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				){
				
			   	value+="<a href=\""+link.attr("href")+"\">"+link.select("h3.LC20lb").text()+"</a><br>"+link.select("span.st").text()+"<p>";
			   }
		  	}		  
	  }
		}//end while
		
        value +="============================================================";
        return value; 
        
    }

	@ResponseBody //�������·� ������ �ϰڴٰ� ���� 
	@RequestMapping(value = "/jsoup4",  produces="text/html;charset=UTF-8")
	public String jsoup4() {
		String value = "=================������ ���� ������ǰ �������� ũ�Ѹ�======================<br>";
		int page = 0;
		// Jsoup�� �̿��ؼ� ���� ������ �˻���� ù������ ũ�Ѹ�
		// start = ���� ����¡ 0, 10, 20, 30 ....
		// q = �˻��� 

		while(true){
        //String url = "https://www.google.com/search?q=%EC%8A%AC%EB%9D%BC%EC%9E%84&start="+page;
		String url = "https://www.google.com/search?q=������&start="+page;
        System.out.println("page : " + page);
        
        page+=10;
        Document doc = null;
        
        try { 
        	Thread.sleep(2000); // �����ð��� ��û�� �ʹ� ������� ���ܴ��ϹǷ� sleep�� �־� ������ �־���
        	doc = Jsoup.connect(url).get();
        } catch (IOException e ) {
        	e.printStackTrace(); 
        	}catch(InterruptedException e){
        		e.getMessage();
        	}
         /*
         ���� �˻� �� ��
         <div class="rc">
	         <div calss="r">
	 		 	<h3 class="LC20lb"/> �� ����
	 		 	<div class="TbwUpd">
	         		<citi class="iUh30"> ��ũ
	         	</div>
	         </div>
	         <div class="s">
	         <span class="st">
	         	<span class="f"> ����
	         </sapn>
	         </div>
         </div>
         */
        Elements total = doc.select("div.rc");
        //System.out.println("total : " + total.text().length());
        if(total.text().length() == 0){break;}//������ �̵� �ϴ� ����� ���� �������� ������ while���� ����������.
        
	  for(Element link : total) {
		   if(link.select("h3.LC20lb").text()!= null && !link.select("h3.LC20lb").text().equals("")){// ������ ���� �������
			   if( // �ش� Ű���忡 �´� �����͸� �����´�.
				  link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("h3.LC20lb").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				||link.select("span.st").text().contains("����")
				){
				
			   	value+="<a href=\""+link.attr("href")+"\">"+link.select("h3.LC20lb").text()+"</a><br>"+link.select("span.st").text()+"<p>";
			   }
		  	}		  
	  }
		}//end while
		
        value +="============================================================";
        return value; 
        
    }
	
}

