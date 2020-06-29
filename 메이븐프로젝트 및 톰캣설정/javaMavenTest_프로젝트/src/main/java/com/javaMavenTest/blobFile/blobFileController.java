package com.javaMavenTest.blobFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class blobFileController {

	@Autowired
	private blobFileDao dao;
	
	/**
	 * 파일태그를 위한 폼태그
	 * @return
	 */
	@RequestMapping(value="/formFile")
	public String formFile() {
	    return "views/formFile";
	}

	/**
	 * 파일처리 컨트롤러
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/saveImage")
	public String saveImage(blobFileVo vo) {
	    try {
	        Map<String, Object> hmap = new HashMap<String, Object>();
	        System.out.println("@@TEST : " + vo.getImgFile());
	        System.out.println("@@TEST : " + vo.getImgFile().getBytes());
	        hmap.put("img", vo.getImgFile().getBytes());
	        dao.saveImage(hmap);   
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "redirect:/formFile";
	}
	
	/**
	 * 임의의 뷰페이지
	 * @return
	 */
	@RequestMapping(value="/view")
	public String view() {
	    return "views/view";
	}

	/**
	 * 이미지태그의 src 컨트롤러
	 * @return
	 */
	@RequestMapping(value="/getByteImage")
	public ResponseEntity<byte[]> getByteImage() {
	    Map<String, Object> map = dao.getByteImage();
	    System.out.println("@@TEST : " + map.get("img"));
	    System.out.println("@@TEST : " + (byte[]) map.get("img"));
	     //  byte[] imageContent = (byte[]) map.get("img");
	     //  final HttpHeaders headers = new HttpHeaders();
	    //   headers.setContentType(MediaType.IMAGE_PNG);
	      // return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	       return new ResponseEntity<byte[]>((byte[]) map.get("img"),HttpStatus.OK);
	}
	
	/**
	 * 임의의 뷰리스트
	 * @return
	 */
	@RequestMapping(value="/view2")
	public ModelAndView view2() {
		
	List<String> paramList = new ArrayList<String>();
		ModelAndView model = new ModelAndView("views/view2");
		
		 List<Map<String,Object>> list = dao.getByteImageList();		 
		 Iterator<Map<String,Object>> itr = list.iterator();
		 
		while(itr.hasNext()){
			
			Map<String,Object> element = (Map<String,Object>)itr.next();
			byte[] encoded=org.apache.commons.codec.binary.Base64.encodeBase64((byte[])element.get("img"));
			String encodedString = new String(encoded);

			paramList.add(encodedString);
			model.addObject("image",paramList);
		}
		return model;
	}
	
	@RequestMapping(value="/view3")
	public ModelAndView view3() {
		
	List<String> paramList = new ArrayList<String>();
		ModelAndView model = new ModelAndView("views/view2");
		
	//	Map map = dao.getByteImageList();
		
		/* List<Map<String,Object>> list = dao.getByteImageList();	
		 
		 for(int i=0;i<list.size();i++){
			 System.out.println("@@t : " + list.get(i));
			 Map<String,Object> map = (Map<String,Object>)list.get(i);
			 byte[] encoded=org.apache.commons.codec.binary.Base64.encodeBase64((byte[])map.get("img"));
			 String encodedString = new String(encoded);
			 paramList.add(encodedString);
		 }
		 model.addObject("image",paramList);*/
	
		return model;
	}
	
	
	
}
