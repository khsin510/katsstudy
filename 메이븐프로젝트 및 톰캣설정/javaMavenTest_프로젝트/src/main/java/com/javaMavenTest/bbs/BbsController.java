package com.javaMavenTest.bbs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;


@Controller
public class BbsController {
	
	@Autowired
	bbsDao dao;
	
	
	@RequestMapping(value = "/bbs")
	public String bbs(Model model) { // 뷰에서 사용할 model 변수
		List<bbsVo> list = dao.getList();
		System.out.println("LF a무지개");
		model.addAttribute("list",list);
		return "views/bbs";
	}
	@Transactional(rollbackFor={Exception.class})//Exception이 발생해도 롤백 시킴
	@RequestMapping(value = "/bbsTran")
	public String bbsTran(Model model)throws Exception { 
		try{
			int a = dao.insertFirst(); // 트렌젝션 테스트
			System.out.println("@@TEST1 : " + a);
			dao.insertTwo(); // 트렌젝션 테스트 two에서 인서트 오류 시 First 인서트도 취소안됨
			return "views/bbs";
		}catch(Exception e){ //try catch일때는 트렉젠션이 롤벡이 안된다. Exception발생한걸 throw new Exception()로 던져야 롤백됨
			// Exception이 try catch를 쓰게되면 메소드 내에서 처리가 되므로 트렌젝션이 발동하지 않음
			e.printStackTrace();
			throw new Exception();// 메소드밖으로 Exception을 던지게 함으로 써 트렌젝션 발동
			//return "views/error";
		}
	}
	@Transactional
	@RequestMapping(value = "/bbsTran2")
	public String bbsTran2(Model model){ 		
		int b = dao.insertFirst(); // 트렌젝션 테스트
		System.out.println("@@TEST2 : " + b);
		dao.insertTwo(); // 트렌젝션 테스트 two에서 인서트 오류 시 First 인서트도 취소가 됨
		return "views/bbs";		
	}
	@Transactional
	@RequestMapping(value = "/bbsTran3")
	public String bbsTran3(Model model) { 
		try{
			int a = dao.insertFirst(); // 트렌젝션 테스트
			System.out.println("@@TEST1 : " + a);
			dao.insertTwo(); // 트렌젝션 테스트 two에서 인서트 오류 시 First 인서트도 취소안됨
			return "views/bbs";
		}catch(Exception e){ 
			// Exception이 try catch를 쓰게되면 메소드 내에서 처리가 되므로 트렌젝션이 발동하지 않음
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 무조건 롤백 하도록 설정
			return "views/error";
		}
	}
	
	//작성된 PDF 불러와 페이징 붙혀 파일로 저장
    public void pagingImgPdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //한글 쓸 수 있도록 설정   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font11  = new Font(bfont,11);
        PdfContentByte over;
        Phrase p;
        Phrase p2;
        Image mark = Image.getInstance("D:/fileData/pdfCreate/c1.png"); //상단배너
        Image mark2 = Image.getInstance("D:/fileData/pdfCreate/c2.png"); //하단배너
        for(int i=1; i<=reader.getNumberOfPages();i++){
        over = stamper.getOverContent(i);
        p = new Phrase(i + "/" + reader.getNumberOfPages(), font11);
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 295, 30, 0);
        if(i!= 1){ // 첫페이지는 배너 안찍음
        p2 = new Phrase(new Chunk(mark,286,62));
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p2, 10, 740, 0);
        p2 = new Phrase(new Chunk(mark2,240,33));
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p2, 60, 40, 0);
        }
        }
        
        stamper.close();
        reader.close();
//페이지 매기기전 원본파일 삭제 진행
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("파일삭제 성공"); 
        		}else{ 
        			System.out.println("파일삭제 실패"); 
        			} 
        	}else{ 
        		System.out.println("파일이 존재하지 않습니다."); 
        		}       
    }
    
	 // 양식 4
 	@RequestMapping(value = "/pdfCreate4")
 	  public String pdfCreate4(HttpServletRequest req, ModelMap modelMap) throws Exception {
 		
 		 //경로 및 파일명
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table4.pdf";
	    
	  //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //파일경로 없으면 생성
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4용지 설정 및 좌우상하 여백 설정
	     Document document = new Document(PageSize.A4, 60, 60, 100, 60);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //한글 쓸 수 있도록 설정   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //폰트설정
	     Font font24  = new Font(bfont, 24);
	     Font font18  = new Font(bfont, 18);
	     Font font13  = new Font(bfont, 13);
	     Font font12  = new Font(bfont, 12);
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold12  = new Font(bfont, 12,Font.BOLD);
	     Font fontBold13  = new Font(bfont, 13,Font.BOLD);
	     Font fontBold18  = new Font(bfont, 18,Font.BOLD);
	     Font fontBold24  = new Font(bfont, 24,Font.BOLD);
	     Font fontBold28  = new Font(bfont, 28,Font.BOLD);
	     Font fontBold32  = new Font(bfont, 32,Font.BOLD);
	     
	     Paragraph title = new Paragraph("국제공시험기관 인정서",fontBold28);
	     title.setAlignment(Element.ALIGN_CENTER);
	     document.add(title);
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     Paragraph title2 = new Paragraph("전주정보문화산업진흥원",fontBold24);
	     title2.setAlignment(Element.ALIGN_CENTER);
	     document.add(title2);
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     
	     
	     PdfPTable content = new PdfPTable(2);
	     PdfPCell cell;
	     content.setHorizontalAlignment(content.ALIGN_LEFT);
	     content.setTotalWidth(450f);
	     content.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
         cell = new PdfPCell(new Phrase("인　 정　 번　호 : ",fontBold13));
         cell.setBorder(0);
         content.addCell(cell);
         cell = new PdfPCell(new Phrase("KT875",font13));
         cell.setBorder(0);
         content.addCell(cell);
         document.add(content);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content2 = new PdfPTable(2);
	     PdfPCell cell2;
	     content2.setHorizontalAlignment(content2.ALIGN_LEFT);
	     content2.setTotalWidth(450f);
	     content2.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
         cell2 = new PdfPCell(new Phrase("법 인 등 록 번 호 :\n (또는 고유번호) ",fontBold13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         cell2 = new PdfPCell(new Phrase("215122-0000326",font13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         document.add(content2);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content3 = new PdfPTable(2);
	     PdfPCell cell3;
	     content3.setHorizontalAlignment(content3.ALIGN_LEFT);
	     content3.setTotalWidth(450f);
	     content3.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell3 = new PdfPCell(new Phrase("사 업 장 소 재 지 : ",fontBold13));
	     cell3.setBorder(0);
         content3.addCell(cell3);
         cell3 = new PdfPCell(new Phrase("전라북도 전주시 완산구 아중로 33",font13));
         cell3.setBorder(0);
         content3.addCell(cell3);
         document.add(content3);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content4 = new PdfPTable(2);
	     PdfPCell cell4;
	     content4.setHorizontalAlignment(content4.ALIGN_LEFT);
	     content4.setTotalWidth(450f);
	     content4.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell4 = new PdfPCell(new Phrase("최 초 인 정 일 자 : ",fontBold13));
	     cell4.setBorder(0);
	     content4.addCell(cell4);
	     cell4 = new PdfPCell(new Phrase("2019년 10월 29일",font13));
	     cell4.setBorder(0);
         content4.addCell(cell4);
         document.add(content4);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content5 = new PdfPTable(2);
	     PdfPCell cell5;
	     content5.setHorizontalAlignment(content5.ALIGN_LEFT);
	     content5.setTotalWidth(450f);
	     content5.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell5 = new PdfPCell(new Phrase("인 정 유 효 기 간 : ",fontBold13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
	     cell5 = new PdfPCell(new Phrase("2019년 10월 29일 ~ 2023년 10월 28일",font13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
         document.add(content5);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content6 = new PdfPTable(2);
	     PdfPCell cell6;
	     content6.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content6.setTotalWidth(450f);
	     content6.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell6 = new PdfPCell(new Phrase("인정분야 및 범위 : ",fontBold13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
	     cell6 = new PdfPCell(new Phrase("별첨",font13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
         document.add(content6);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content7 = new PdfPTable(2);
	     PdfPCell cell7;
	     content7.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content7.setTotalWidth(450f);
	     content7.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell7 = new PdfPCell(new Phrase(" 발　　행　　일  :",fontBold13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
	     cell7 = new PdfPCell(new Phrase("2019년 10월 29일",font13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
         document.add(content7);
         document.add(new Paragraph("　",font12));
         
         
         document.add(new Paragraph("　",font12));
         document.add(new Paragraph("　",font12));
         
         document.add(new Paragraph("　상기 기관을 국가표준기본법 제23조 및 KS Q ISO/IEC 17025:2006에 의거하여 국제공인시험기관으로 인정합니다. 또한 ISO-ILAC-IAF 공동성명에 언급된 바와 같이 인정된 분야 및 범위에 대한 기술적 능력과 시험기관의 품질경영시스템이 적절함을 인정합니다.",font12));
         
                
         document.newPage(); // 새로운 페이지로
         document.add(new Paragraph("제 KT875호",font12));
         document.add(new Paragraph("　",font12));
         document.add(new Paragraph("03. 전기시험",fontBold12));
         document.add(new Paragraph("　93.012 소프트웨어 시험",font12));
         document.add(new Paragraph("　",font12));
         
         PdfPTable contentTable = new PdfPTable(3);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("규격번호",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setFixedHeight(40);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("규격명",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("시험범위",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ISO/IEC 25023 : 2016",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("Systems and software engineering - Systems and software Quality Requirements and Evalustion(SQuaRE) _ Measurement of system and software product quality",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("-",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	       
	        document.add(contentTable);
	       
	       
	        document.add(new Paragraph("끝",font12)); 
	     document.close();
	    
	     String a="D:/fileData/pdfCreate/simple_table4.pdf";
	     String b="D:/fileData/pdfCreate/simple_table4_paging.pdf";
	     pagingImgPdf(a,b);
	     
 		return "views/bbs";
 	}
 	
	//작성된 PDF 불러와 워터마크 붙혀 새로운 파일로 저장
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //한글 쓸 수 있도록 설정   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font9  = new Font(bfont, 9);
        
        PdfContentByte over = stamper.getOverContent(1);
        Phrase p = new Phrase("수신자　　　(주)디티앤씨 대표이사, 한국생산선기술연구원장(김진수 선임 평가사)", font9);
        ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p, 60, 250, 0);
        over.saveState();
        //투명도세팅
        //PdfGState gs1 = new PdfGState();
        //gs1.setFillOpacity(0.5f);
        //over.setGState(gs1);
        //ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 450, 0);
        over.restoreState();
        stamper.close();
        reader.close();
        
      //페이지 매기기전 원본파일 삭제 진행
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("파일삭제 성공"); 
        		}else{ 
        			System.out.println("파일삭제 실패"); 
        			} 
        	}else{ 
        		System.out.println("파일이 존재하지 않습니다."); 
        		}       
        
    }
	
  //작성된 PDF 불러와 페이징 붙혀 파일로 저장
    public void pagingPdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //한글 쓸 수 있도록 설정   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font12  = new Font(bfont,12);
        PdfContentByte over;
        Phrase p;
        for(int i=1; i<=reader.getNumberOfPages();i++){
        over = stamper.getOverContent(i);
        p = new Phrase("KAS─P─030 (" + i + "/" + reader.getNumberOfPages() + ")", font12);
        ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p, 60, 759, 0);
        }
       // over.saveState();       
        //over.restoreState();
        //KAS─P─030 (1/2)
        stamper.close();
        reader.close();
//페이지 매기기전 원본파일 삭제 진행
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("파일삭제 성공"); 
        		}else{ 
        			System.out.println("파일삭제 실패"); 
        			} 
        	}else{ 
        		System.out.println("파일이 존재하지 않습니다."); 
        		}       
    }
    
 // 양식 3
 	@RequestMapping(value = "/pdfCreate3")
 	  public String pdfCreate3(HttpServletRequest req, ModelMap modelMap) throws Exception {
 		
 		 //경로 및 파일명
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table3.pdf";
	    
	  //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //파일경로 없으면 생성
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4용지 설정 및 좌우상하 여백 설정
	     Document document = new Document(PageSize.A4, 60, 60, 120, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //한글 쓸 수 있도록 설정   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //폰트설정
	     Font font24  = new Font(bfont, 24);
	     Font font18  = new Font(bfont, 18);
	     Font font13  = new Font(bfont, 13);
	     Font font12  = new Font(bfont, 12);
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold12  = new Font(bfont, 12,Font.BOLD);
	     Font fontBold13  = new Font(bfont, 13,Font.BOLD);
	     Font fontBold18  = new Font(bfont, 18,Font.BOLD);
	     Font fontBold24  = new Font(bfont, 24,Font.BOLD);
	     Font fontBold32  = new Font(bfont, 32,Font.BOLD);
	     
	     Paragraph title = new Paragraph("KAS 공인 제품인증기관 인증서",fontBold18);
	     title.setAlignment(Element.ALIGN_CENTER);
	     document.add(title);
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     document.add(new Paragraph("　",font12));
	     
	     
	     
	     PdfPTable content = new PdfPTable(2);
	     PdfPCell cell;
	     content.setHorizontalAlignment(content.ALIGN_LEFT);
	     content.setTotalWidth(450f);
	     content.setWidths(new float[]{60f, 390f});//컬럼별 크기지정 
         cell = new PdfPCell(new Phrase("기관명 : ",fontBold13));
         cell.setBorder(0);
         content.addCell(cell);
         cell = new PdfPCell(new Phrase("한국가스안전공사 에너지안전실증연구센터",font13));
         cell.setBorder(0);
         content.addCell(cell);
         document.add(content);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content2 = new PdfPTable(2);
	     PdfPCell cell2;
	     content2.setHorizontalAlignment(content2.ALIGN_LEFT);
	     content2.setTotalWidth(450f);
	     content2.setWidths(new float[]{108f, 342f});//컬럼별 크기지정 
         cell2 = new PdfPCell(new Phrase("법인등록번호 : ",fontBold13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         cell2 = new PdfPCell(new Phrase("114671-0001406",font13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         document.add(content2);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content3 = new PdfPTable(2);
	     PdfPCell cell3;
	     content3.setHorizontalAlignment(content3.ALIGN_LEFT);
	     content3.setTotalWidth(450f);
	     content3.setWidths(new float[]{75f, 375f});//컬럼별 크기지정 
	     cell3 = new PdfPCell(new Phrase("법인주소 : ",fontBold13));
	     cell3.setBorder(0);
         content3.addCell(cell3);
         cell3 = new PdfPCell(new Phrase("충청북도 음성군 맹동면 원중로 1390",font13));
         cell3.setBorder(0);
         content3.addCell(cell3);
         document.add(content3);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content4 = new PdfPTable(2);
	     PdfPCell cell4;
	     content4.setHorizontalAlignment(content4.ALIGN_LEFT);
	     content4.setTotalWidth(450f);
	     content4.setWidths(new float[]{110f, 340f});//컬럼별 크기지정 
	     cell4 = new PdfPCell(new Phrase("사업장 소재지 : ",fontBold13));
	     cell4.setBorder(0);
	     content4.addCell(cell4);
	     cell4 = new PdfPCell(new Phrase("강원도 영우러군 주천명 송학주천로 1467-51 에너지안전실증연구센터",font13));
	     cell4.setBorder(0);
         content4.addCell(cell4);
         document.add(content4);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content5 = new PdfPTable(2);
	     PdfPCell cell5;
	     content5.setHorizontalAlignment(content5.ALIGN_LEFT);
	     content5.setTotalWidth(450f);
	     content5.setWidths(new float[]{130f, 330f});//컬럼별 크기지정 
	     cell5 = new PdfPCell(new Phrase("인정분야 및 범위 : ",fontBold13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
	     cell5 = new PdfPCell(new Phrase("별첨참조",font13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
         document.add(content5);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content6 = new PdfPTable(2);
	     PdfPCell cell6;
	     content6.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content6.setTotalWidth(450f);
	     content6.setWidths(new float[]{75f, 375f});//컬럼별 크기지정 
	     cell6 = new PdfPCell(new Phrase("인증유형 : ",fontBold13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
	     cell6 = new PdfPCell(new Phrase("스킴유형 3",font13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
         document.add(content6);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content7 = new PdfPTable(2);
	     PdfPCell cell7;
	     content7.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content7.setTotalWidth(450f);
	     content7.setWidths(new float[]{108f, 342f});//컬럼별 크기지정 
	     cell7 = new PdfPCell(new Phrase("인정유효기간 : ",fontBold13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
	     cell7 = new PdfPCell(new Phrase("2019. 6. 26~ 2023. 6. 25",font13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
         document.add(content7);
         document.add(new Paragraph("　",font12));
         
         PdfPTable content8 = new PdfPTable(2);
	     PdfPCell cell8;
	     content8.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content8.setTotalWidth(450f);
	     content8.setWidths(new float[]{75f, 375f});//컬럼별 크기지정 
	     cell8 = new PdfPCell(new Phrase("인증마크 : ",fontBold13));
	     cell8.setBorder(0);
	     content8.addCell(cell8);
	     cell8 = new PdfPCell(new Phrase("유",fontBold13));
	     cell8.setBorder(0);
	     content8.addCell(cell8);
         document.add(content8);         
         document.add(new Paragraph("　",font12));
         document.add(new Paragraph("　",font12));
         
         document.add(new Paragraph("　상기 기관을 국가표준법 제 21조, 제품인증기관 인정 및 사후관리 등에 관한 요령 제21조의 규정 및 KS Q ISO/IEC 17065의 인정요건에 의거하여 KAS 공인 제품인증기관으로 인정합니다.",font12));
         
         document.add(new Paragraph("　",font12));
         document.add(new Paragraph("　",font12));
         
         Paragraph date = new Paragraph("2019년 05월 26일",font12);
         date.setAlignment(Element.ALIGN_RIGHT);
	     document.add(date);
	     
         document.add(new Paragraph("　",font12));
         document.add(new Paragraph("　",font12));
         
         //마크
         Image mark = Image.getInstance("D:/fileData/pdfCreate/b3.png");
	        mark.scaleToFit(240	, 215); // 가로, 세로 크기
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
         
         document.newPage(); // 새로운 페이지로
         document.add(new Paragraph("○ 인증분야 및 범위(V-check 마크)",font12));
         document.add(new Paragraph("　　● 대분류: 25. 금속가공제품; 기계 및 가구 제외",font12));
         document.add(new Paragraph("　　- 인증스킴의 유형 : 스킴 유형 3",font12));
         document.add(new Paragraph("　",font12));
         
         PdfPTable contentTable = new PdfPTable(5);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("중분류",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setFixedHeight(40);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("소분류",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("제품명",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("제품의 범주",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("기준문서",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("259.기타 금속가공제품 제조업",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("2599. 그 외 기타 금속가공제품 제조업",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("방폭밸브류",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("외부 폭압 차단",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("화생방 방호설비 성능·인증 시험방법서 (국방부지침,2017.07)",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("역류방지밸브",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("양압 제어 및 외부 폭압 차단",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("가스차단접속관류",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("외부 폭압 차단",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("가스차단밸브류",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어외부 폭압 차단 및 공기흐름 제어",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("방폭문",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("외부 폭압 차단",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("가스차단문",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("유해가스 유입차단",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("가스입자여과기",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("유해가스 정화",font11));
	        contentTable.addCell(contentCell);
	        document.add(contentTable);
	        document.add(new Paragraph("　",font12));
	        document.add(new Paragraph("끝",font12)); 
	     document.close();
	    
	        String a = "D:/fileData/pdfCreate/simple_table3.pdf"; //워터마크 붙힐 pdf
	        String b = "D:/fileData/pdfCreate/simple_table3_paging.pdf"; //워터마큰 붙어서 나올 pdf
	        pagingPdf(a,b);
	    
	     
	     
 		return "views/bbs";
 	}
	// 양식 2
	@RequestMapping(value = "/pdfCreate2")
	  public String pdfCreate2(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //경로 및 파일명
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table2.pdf";
	    
	  //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //파일경로 없으면 생성
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4용지 설정 및 좌우상하 여백 설정
	     Document document = new Document(PageSize.A4, 60, 60, 55, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //한글 쓸 수 있도록 설정   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //폰트설정
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //배너 이미지 작업
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1_1.png");
	        png.scaleToFit(480	, 298); // 가로, 세로 크기
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //글자 작성
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("수신  수신자 참조",font11));
	        document.add(new Paragraph("(경유)",font11));
	        document.add(new Paragraph("제목  KOLAS 공인시험기관 (전환평가) 문서심사 통보 및 요청((주)디티앤씨)",font11));
	        
	        //라인 추가
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(480	, 100); // 가로, 세로 크기
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. KOLAS 공인시험기관 전환평가 신청 관련입니다.",font11));
	        document.add(new Paragraph("　",font11));// ㄱ + 한자1번 기호
	        document.add(new Paragraph("2. KOLAS 사무국은 선임평가사 배정 및 문서심사 의뢰와 관련하여 시넝기관이 이해관계 또는 기밀유지 등의 사유로 인한 변경 요청이 없음을 e-KOLAS 시스템을 통하여 확인하였습니다.",font11)); 
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("3. 아울러, 신청 서율에 대한 문서심사는 전환지침(국가기술표준원 공고 제2018-0267호, KOLAS 공인시험·교정기관(KS Q ISO/IEC 17025:2017) 전환계획공고)에 따라 인정기준관의 적합성 검토를 선임평가사에게 의뢰하오니 KOLAS 홈페이지의 신청 서류 일체를 검토한 후, 그 결과를 1개월 이내에 등록하여 주시기 바랍니다.",font11));
	        document.add(new Paragraph("　",font11));
	        //내용 테이블
	        PdfPTable contentTable = new PdfPTable(4);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("대상기관명",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("구분",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("신청분야",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("문서심사자",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("(주)디티앤씨",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("전환평가",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("신청서류 참조",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("김진수 선임평가사 \n (한국생산기술연구원)",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        document.add(contentTable);
	        
	        document.add(new Paragraph("끝.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        
	        //기관명마크        
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/b1.png");
	        mark.scaleToFit(180	, 160); // 가로, 세로 크기
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
	       // mark.setTransparency(transparency);
	        
	        //수신자
	        //document.add(new Paragraph("수신자　　　(주)디티앤씨 대표이사, 한국생산선기술연구원장(김진수 선임 평가사)",font11));
	      
	        
	      //라인 추가
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(480	, 100); // 가로, 세로 크기
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //하단 작성
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //테두리 없에기
	          table.setWidths(new float[]{4f, 4f, 5f, 6f, 10f});//컬럼별 크기지정
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("공업연구관",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("　",font9));
	          cell = new PdfPCell(new Phrase("적합성평가과 과장",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("전결 2019.10.14.",font8));
	          cell = new PdfPCell(new Phrase("　",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("김명곤",fontBold9));
	          table.addCell(new Phrase("임헌진",fontBold9));
	          document.add(table);
	          document.add(new Phrase("협조자",font9));
	          
	          //풋터 테이블
	          PdfPTable footerTable1 = new PdfPTable(5);
	          footerTable1.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable1.setWidths(new float[]{3f, 5f, 4f, 3f, 6f});//컬럼별 크기지정
	          footerTable1.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable1.addCell(new Phrase("시행",font9));
	          footerTable1.addCell(new Phrase("적합성평가과-5415",font9));
	          footerTable1.addCell(new Phrase("(2019. 10. 24.)",font9));
	          footerTable1.addCell(new Phrase("접수",font9));
	          footerTable1.addCell(new Phrase("　",font9));
	          document.add(footerTable1);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable2 = new PdfPTable(3);
	          footerTable2.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable2.setWidths(new float[]{3f, 14f, 8f});//컬럼별 크기지정
	          footerTable2.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable2.addCell(new Phrase("우 27737",font9));
	          footerTable2.addCell(new Phrase("충청북도 음성군맹동면 이수로 93, (국가기술표준원)",font9));
	          footerTable2.addCell(new Phrase("/ http://www.kats.go.kr",font9));
	          document.add(footerTable2);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable3 = new PdfPTable(4);
	          footerTable3.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable3.setWidths(new float[]{10, 10, 10, 6});//컬럼별 크기지정
	          footerTable3.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable3.addCell(new Phrase("전화번호 043-870-5472",font9));
	          footerTable3.addCell(new Phrase("팩스번호  043-870-5679",font9));
	          footerTable3.addCell(new Phrase("/ mgkim1004@korea.kr",font9));
	          footerTable3.addCell(new Phrase(" /비공개(6)",font9));
	          document.add(footerTable3);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable4 = new PdfPTable(1);
	          footerTable4.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable3.setHorizontalAlignment(table.ALIGN_MIDDLE);
	          footerTable4.addCell(new Phrase("　　　　　　2019 한·아세안 특별정상회의 및 제1차 한·메콩 정상회의(11.25-27 부산)",font9));
	          document.add(footerTable4);
	          
	          
	        document.close();
			
	        String a = "D:/fileData/pdfCreate/simple_table2.pdf"; //워터마크 붙힐 pdf
	        String b = "D:/fileData/pdfCreate/simple_table2_waterMark.pdf"; //워터마큰 붙어서 나올 pdf
	        manipulatePdf(a,b);
	    return "views/bbs";
	  }
	// 양식1
	@RequestMapping(value = "/pdfCreate1")
	  public String pdfCreate1(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //경로 및 파일명
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table1.pdf";
	    
	  //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //파일경로 없으면 생성
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4용지 설정 및 좌우상하 여백 설정
	     Document document = new Document(PageSize.A4, 60, 60, 55, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //한글 쓸 수 있도록 설정   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //폰트설정
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //배너 이미지 작업
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1_1.png");
	        png.scaleToFit(480	, 298); // 가로, 세로 크기
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //글자 작성
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("수신  (주)피티엘",font11));
	        document.add(new Paragraph("(경유)",font11));
	        document.add(new Paragraph("제목  KOLAS 공인시험기관(신규) 인전성 교부((주)피티엘)",font11));
	        
	        //라인 추가
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(480	, 100); // 가로, 세로 크기
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. 귀 기관의 KOLAS 공인시험기관 신규 신청 관련입니다.",font11));
	        document.add(new Paragraph("　",font11));// ㄱ + 한자1번 기호
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("2. 위와 관련하여 「국가표준기본법」 제23조(시험·검사기관 인정 등) 및 「KOLAS 공인",font11));                                                
	        document.add(new Paragraph("시험 및 검사기관 인정제도 운영요령」 제18조(인정신청 및 평가)에 따라 붙임과 같이",font11));
	        document.add(new Paragraph("귀 기관의 공인시험기관 (신규) 인정을 공고하고 인정서를 교부하여 드립니다.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("3. 또한, 최종 인정범위는 필요 시 e-KOLAS 업무시스템 (www.knab.go.kr)을 통하여 다운",font11));
	        document.add(new Paragraph("받으실 수 있음을 알려드립니다.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("붙임  1. 국가기술표준원 공고 제2019-0304호 1부.",font11));
	        document.add(new Paragraph("　　  2. 공인기관 인정서 1부.  끝.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        
	        //기관명마크
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/b1.png");
	        mark.scaleToFit(160	, 160); // 가로, 세로 크기
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
	        
	      
	        
	      //라인 추가
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(480	, 100); // 가로, 세로 크기
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //하단 작성
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //테두리 없에기
	          table.setWidths(new float[]{4f, 4f, 5f, 6f, 10f});//컬럼별 크기지정
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("공업연구관",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("　",font9));
	          cell = new PdfPCell(new Phrase("적합성평가과 과장",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("전결 2019.10.14.",font8));
	          cell = new PdfPCell(new Phrase("　",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("김명곤",fontBold9));
	          table.addCell(new Phrase("임헌진",fontBold9));
	          document.add(table);
	          document.add(new Phrase("협조자",font9));
	          
	          //풋터 테이블
	          PdfPTable footerTable1 = new PdfPTable(5);
	          footerTable1.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable1.setWidths(new float[]{3f, 5f, 4f, 3f, 6f});//컬럼별 크기지정
	          footerTable1.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable1.addCell(new Phrase("시행",font9));
	          footerTable1.addCell(new Phrase("적합성평가과-5221",font9));
	          footerTable1.addCell(new Phrase("(2019. 10. 14.)",font9));
	          footerTable1.addCell(new Phrase("접수",font9));
	          footerTable1.addCell(new Phrase("　",font9));
	          document.add(footerTable1);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable2 = new PdfPTable(3);
	          footerTable2.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable2.setWidths(new float[]{3f, 14f, 8f});//컬럼별 크기지정
	          footerTable2.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable2.addCell(new Phrase("우 27737",font9));
	          footerTable2.addCell(new Phrase("충청북도 음성군맹동면 이수로 93, (국가기술표준원)",font9));
	          footerTable2.addCell(new Phrase("/ http://www.kats.go.kr",font9));
	          document.add(footerTable2);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable3 = new PdfPTable(4);
	          footerTable3.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable3.setWidths(new float[]{10, 10, 10, 6});//컬럼별 크기지정
	          footerTable3.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable3.addCell(new Phrase("전화번호 043-870-5472",font9));
	          footerTable3.addCell(new Phrase("팩스번호  043-870-5679",font9));
	          footerTable3.addCell(new Phrase("/ mgkim1004@korea.kr",font9));
	          footerTable3.addCell(new Phrase(" /대국민공개",font9));
	          document.add(footerTable3);
	          document.add(new Paragraph("　",font11));
	          PdfPTable footerTable4 = new PdfPTable(1);
	          footerTable4.getDefaultCell().setBorder(0); //테두리 없에기
	          footerTable3.setHorizontalAlignment(table.ALIGN_MIDDLE);
	          footerTable4.addCell(new Phrase("　　　　　　2019 한·아세안 특별정상회의 및 제1차 한·메콩 정상회의(11.25-27 부산)",font9));
	          document.add(footerTable4);
	          
	          
	        document.close();
	    return "views/bbs";
	  }
	
	@RequestMapping(value = "/pdfCreate")
	  public String pdfCreate(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //경로 및 파일명
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table.pdf";
	    
	  //사용할 폰트	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //파일경로 없으면 생성
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4용지 설정 및 좌우상하 여백 설정
	     Document document = new Document(PageSize.A4, 50, 50, 50, 50);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //한글 쓸 수 있도록 설정   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //폰트설정
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //배너 이미지 작업
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1.png");
	        png.scaleToFit(500	, 400); // 가로, 세로 크기
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //글자 작성
	        document.add(new Paragraph("수신 수신자 참조",font11));
	        document.add(new Paragraph("(경유)",font11));
	        document.add(new Paragraph("제목 2019년 산업부 데이터 품질관리 추진실적 2차 점검결과 통보",font11));
	        
	        //라인 추가
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(500	, 100); // 가로, 세로 크기
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. 행정안전부 공공데이터정책과-1238127389호 관련입니다.",font11));
	        document.add(new Paragraph("　",font11));// ㄱ + 한자1번 기호
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("2. 위호 관련으로 우리부 2019년 데이터품질관리 계획에 따른 추진실적 점검결과를",font11));
	        document.add(new Paragraph("붙임과 같이 통보하오니, 데이터 품질관리 업무 수행시 참고하시기 바랍니다.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("3. 아울러, 2019년 공공데이터 품질관리 수준평가에 적극 대응하여 주시기 바랍니다.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("붙임  1. 2019년 산업부 품질관리 게획 2차점검 결과.",font11));
	        document.add(new Paragraph("　　  2. 2019년 데이터품질 수준평가 자체점검결과.  끝.",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        document.add(new Paragraph("　",font11));
	        
	        //기관명 및 마크
	        PdfPTable organNameMark = new PdfPTable(3);
	        organNameMark.getDefaultCell().setBorder(0);
	        organNameMark.addCell(new Phrase("　",font9));
	        PdfPCell organcell;
	        organcell = new PdfPCell(new Phrase("정보관리담당관",fontBold15));
	        organcell.setVerticalAlignment(organcell.ALIGN_BOTTOM);
	        organcell.setBorder(0);
	        organNameMark.addCell(organcell);
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/a4.png");
	        organNameMark.addCell(mark);
	        document.add(organNameMark); 
	        //document.add(new Paragraph("수신자 홍길동, 김민수, 치약, 칫솔, 등등등",font9));
	        
	        //수신자
	        PdfPTable receiver = new PdfPTable(2);
	        receiver.getDefaultCell().setBorder(0); //테두리 없에기
	        receiver.setWidths(new float[]{1f, 10f});//컬럼별 크기지정
	        receiver.addCell(new Phrase("수신자",font9));
	        receiver.addCell(new Phrase("무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), 무역위원회(무역구제청책과장), ",font9));
	        document.add(receiver);
	        
	      //라인 추가
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(500	, 100); // 가로, 세로 크기
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //하단 작성
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //테두리 없에기
	          table.setWidths(new float[]{4f, 4f, 4f, 6f, 10f});//컬럼별 크기지정
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("주무관",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("　",font9));
	          cell = new PdfPCell(new Phrase("정보관리담당관",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("전결 2019.10.1.",font8));
	          cell = new PdfPCell(new Phrase("　",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("박민병",fontBold9));
	          table.addCell(new Phrase("신우찬",fontBold9));
	          
	          document.add(table);  
	        
	        document.close();
	    return "views/bbs";
	  }
	
	
}