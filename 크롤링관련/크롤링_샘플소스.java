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

	@ResponseBody //문자형태로 리턴을 하겠다고 선언 
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
		//String textSt = contents.text(); // 태그 제외 데이터만 가져옴
		String textSt = contents.html(); // 태그까지 포함해서 가져옴
		System.out.println("TEST");
		System.out.println(textSt);
		
		return textSt;
	}
	
	@ResponseBody //문자형태로 리턴을 하겠다고 선언 
	@RequestMapping(value = "/jsoup2",  produces="text/html;charset=UTF-8")
	public void jsoup2() {
		// Jsoup를 이용해서 네이버 스포츠 크롤링
        String url = "https://sports.news.naver.com/wfootball/index.nhn";
        Document doc = null;
         
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  System.out.println("doc : " + doc);
        // 주요 뉴스로 나오는 태그를 찾아서 가져오도록 한다.
        Elements element = doc.select("div.home_news");
         
        // 1. 헤더 부분의 제목을 가져온다.
        String title = element.select("h2").text().substring(0, 4);
 
        System.out.println("============================================================");
        //System.out.println(title);
        System.out.println(element);
        System.out.println("============================================================");
         
        for(Element el : element.select("li")) {    // 하위 뉴스 기사들을 for문 돌면서 출력
            System.out.println(el.text());
        }
         
        System.out.println("============================================================");
    }
	
		
	@ResponseBody //문자형태로 리턴을 하겠다고 선언 
	@RequestMapping(value = "/jsoup3",  produces="text/html;charset=UTF-8")
	public String jsoup3() {
		String value = "=================슬라임 관련 위해제품 수집정보 크롤링======================<br>";
		int page = 0;
		// Jsoup를 이용해서 구글 슬라임 검색결과 첫페이지 크롤링
		// start = 구글 페이징 0, 10, 20, 30 ....
		// q = 검색어 

		while(true){
        //String url = "https://www.google.com/search?q=%EC%8A%AC%EB%9D%BC%EC%9E%84&start="+page;
		String url = "https://www.google.com/search?q=자전거&start="+page;
        System.out.println("page : " + page);
        
        page+=10;
        Document doc = null;
        
        try { 
        	Thread.sleep(2000); // 지정시간에 요청이 너무 많을경우 차단당하므로 sleep을 주어 간격을 넣어줌
        	doc = Jsoup.connect(url).get();
        } catch (IOException e ) {
        	e.printStackTrace(); 
        	}catch(InterruptedException e){
        		e.getMessage();
        	}
         /*
         구글 검색 후 글
         <div class="rc">
	         <div calss="r">
	 		 	<h3 class="LC20lb"/> 글 제목
	 		 	<div class="TbwUpd">
	         		<citi class="iUh30"> 링크
	         	</div>
	         </div>
	         <div class="s">
	         <span class="st">
	         	<span class="f"> 내용
	         </sapn>
	         </div>
         </div>
         */
        Elements total = doc.select("div.rc");
        //System.out.println("total : " + total.text().length());
        if(total.text().length() == 0){break;}//페이지 이동 하다 결과가 없는 페이지가 나오면 while문을 빠져나간다.
        
	  for(Element link : total) {
		   if(link.select("h3.LC20lb").text()!= null && !link.select("h3.LC20lb").text().equals("")){// 제목이 없지 않을경우
			   if( // 해당 키워드에 맞는 데이터만 가져온다.
				  link.select("h3.LC20lb").text().contains("유해")
				||link.select("h3.LC20lb").text().contains("검출")
				||link.select("h3.LC20lb").text().contains("안전")
				||link.select("h3.LC20lb").text().contains("문제")
				||link.select("span.st").text().contains("유해")
				||link.select("span.st").text().contains("검출")
				||link.select("span.st").text().contains("안전")
				||link.select("span.st").text().contains("문제")
				){
				
			   	value+="<a href=\""+link.attr("href")+"\">"+link.select("h3.LC20lb").text()+"</a><br>"+link.select("span.st").text()+"<p>";
			   }
		  	}		  
	  }
		}//end while
		
        value +="============================================================";
        return value; 
        
    }

	@ResponseBody //문자형태로 리턴을 하겠다고 선언 
	@RequestMapping(value = "/jsoup4",  produces="text/html;charset=UTF-8")
	public String jsoup4() {
		String value = "=================슬라임 관련 위해제품 수집정보 크롤링======================<br>";
		int page = 0;
		// Jsoup를 이용해서 구글 슬라임 검색결과 첫페이지 크롤링
		// start = 구글 페이징 0, 10, 20, 30 ....
		// q = 검색어 

		while(true){
        //String url = "https://www.google.com/search?q=%EC%8A%AC%EB%9D%BC%EC%9E%84&start="+page;
		String url = "https://www.google.com/search?q=자전거&start="+page;
        System.out.println("page : " + page);
        
        page+=10;
        Document doc = null;
        
        try { 
        	Thread.sleep(2000); // 지정시간에 요청이 너무 많을경우 차단당하므로 sleep을 주어 간격을 넣어줌
        	doc = Jsoup.connect(url).get();
        } catch (IOException e ) {
        	e.printStackTrace(); 
        	}catch(InterruptedException e){
        		e.getMessage();
        	}
         /*
         구글 검색 후 글
         <div class="rc">
	         <div calss="r">
	 		 	<h3 class="LC20lb"/> 글 제목
	 		 	<div class="TbwUpd">
	         		<citi class="iUh30"> 링크
	         	</div>
	         </div>
	         <div class="s">
	         <span class="st">
	         	<span class="f"> 내용
	         </sapn>
	         </div>
         </div>
         */
        Elements total = doc.select("div.rc");
        //System.out.println("total : " + total.text().length());
        if(total.text().length() == 0){break;}//페이지 이동 하다 결과가 없는 페이지가 나오면 while문을 빠져나간다.
        
	  for(Element link : total) {
		   if(link.select("h3.LC20lb").text()!= null && !link.select("h3.LC20lb").text().equals("")){// 제목이 없지 않을경우
			   if( // 해당 키워드에 맞는 데이터만 가져온다.
				  link.select("h3.LC20lb").text().contains("유해")
				||link.select("h3.LC20lb").text().contains("검출")
				||link.select("h3.LC20lb").text().contains("안전")
				||link.select("h3.LC20lb").text().contains("문제")
				||link.select("span.st").text().contains("유해")
				||link.select("span.st").text().contains("검출")
				||link.select("span.st").text().contains("안전")
				||link.select("span.st").text().contains("문제")
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

