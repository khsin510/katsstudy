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
	public String bbs(Model model) { // �信�� ����� model ����
		List<bbsVo> list = dao.getList();
		System.out.println("LF a������");
		model.addAttribute("list",list);
		return "views/bbs";
	}
	@Transactional(rollbackFor={Exception.class})//Exception�� �߻��ص� �ѹ� ��Ŵ
	@RequestMapping(value = "/bbsTran")
	public String bbsTran(Model model)throws Exception { 
		try{
			int a = dao.insertFirst(); // Ʈ������ �׽�Ʈ
			System.out.println("@@TEST1 : " + a);
			dao.insertTwo(); // Ʈ������ �׽�Ʈ two���� �μ�Ʈ ���� �� First �μ�Ʈ�� ��Ҿȵ�
			return "views/bbs";
		}catch(Exception e){ //try catch�϶��� Ʈ�������� �Ѻ��� �ȵȴ�. Exception�߻��Ѱ� throw new Exception()�� ������ �ѹ��
			// Exception�� try catch�� ���ԵǸ� �޼ҵ� ������ ó���� �ǹǷ� Ʈ�������� �ߵ����� ����
			e.printStackTrace();
			throw new Exception();// �޼ҵ������ Exception�� ������ ������ �� Ʈ������ �ߵ�
			//return "views/error";
		}
	}
	@Transactional
	@RequestMapping(value = "/bbsTran2")
	public String bbsTran2(Model model){ 		
		int b = dao.insertFirst(); // Ʈ������ �׽�Ʈ
		System.out.println("@@TEST2 : " + b);
		dao.insertTwo(); // Ʈ������ �׽�Ʈ two���� �μ�Ʈ ���� �� First �μ�Ʈ�� ��Ұ� ��
		return "views/bbs";		
	}
	@Transactional
	@RequestMapping(value = "/bbsTran3")
	public String bbsTran3(Model model) { 
		try{
			int a = dao.insertFirst(); // Ʈ������ �׽�Ʈ
			System.out.println("@@TEST1 : " + a);
			dao.insertTwo(); // Ʈ������ �׽�Ʈ two���� �μ�Ʈ ���� �� First �μ�Ʈ�� ��Ҿȵ�
			return "views/bbs";
		}catch(Exception e){ 
			// Exception�� try catch�� ���ԵǸ� �޼ҵ� ������ ó���� �ǹǷ� Ʈ�������� �ߵ����� ����
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// ������ �ѹ� �ϵ��� ����
			return "views/error";
		}
	}
	
	//�ۼ��� PDF �ҷ��� ����¡ ���� ���Ϸ� ����
    public void pagingImgPdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //�ѱ� �� �� �ֵ��� ����   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font11  = new Font(bfont,11);
        PdfContentByte over;
        Phrase p;
        Phrase p2;
        Image mark = Image.getInstance("D:/fileData/pdfCreate/c1.png"); //��ܹ��
        Image mark2 = Image.getInstance("D:/fileData/pdfCreate/c2.png"); //�ϴܹ��
        for(int i=1; i<=reader.getNumberOfPages();i++){
        over = stamper.getOverContent(i);
        p = new Phrase(i + "/" + reader.getNumberOfPages(), font11);
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 295, 30, 0);
        if(i!= 1){ // ù�������� ��� ������
        p2 = new Phrase(new Chunk(mark,286,62));
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p2, 10, 740, 0);
        p2 = new Phrase(new Chunk(mark2,240,33));
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p2, 60, 40, 0);
        }
        }
        
        stamper.close();
        reader.close();
//������ �ű���� �������� ���� ����
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("���ϻ��� ����"); 
        		}else{ 
        			System.out.println("���ϻ��� ����"); 
        			} 
        	}else{ 
        		System.out.println("������ �������� �ʽ��ϴ�."); 
        		}       
    }
    
	 // ��� 4
 	@RequestMapping(value = "/pdfCreate4")
 	  public String pdfCreate4(HttpServletRequest req, ModelMap modelMap) throws Exception {
 		
 		 //��� �� ���ϸ�
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table4.pdf";
	    
	  //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //���ϰ�� ������ ����
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4���� ���� �� �¿���� ���� ����
	     Document document = new Document(PageSize.A4, 60, 60, 100, 60);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //�ѱ� �� �� �ֵ��� ����   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //��Ʈ����
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
	     
	     Paragraph title = new Paragraph("������������ ������",fontBold28);
	     title.setAlignment(Element.ALIGN_CENTER);
	     document.add(title);
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     Paragraph title2 = new Paragraph("����������ȭ��������",fontBold24);
	     title2.setAlignment(Element.ALIGN_CENTER);
	     document.add(title2);
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     
	     
	     PdfPTable content = new PdfPTable(2);
	     PdfPCell cell;
	     content.setHorizontalAlignment(content.ALIGN_LEFT);
	     content.setTotalWidth(450f);
	     content.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
         cell = new PdfPCell(new Phrase("�Ρ� ���� ����ȣ : ",fontBold13));
         cell.setBorder(0);
         content.addCell(cell);
         cell = new PdfPCell(new Phrase("KT875",font13));
         cell.setBorder(0);
         content.addCell(cell);
         document.add(content);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content2 = new PdfPTable(2);
	     PdfPCell cell2;
	     content2.setHorizontalAlignment(content2.ALIGN_LEFT);
	     content2.setTotalWidth(450f);
	     content2.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
         cell2 = new PdfPCell(new Phrase("�� �� �� �� �� ȣ :\n (�Ǵ� ������ȣ) ",fontBold13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         cell2 = new PdfPCell(new Phrase("215122-0000326",font13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         document.add(content2);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content3 = new PdfPTable(2);
	     PdfPCell cell3;
	     content3.setHorizontalAlignment(content3.ALIGN_LEFT);
	     content3.setTotalWidth(450f);
	     content3.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell3 = new PdfPCell(new Phrase("�� �� �� �� �� �� : ",fontBold13));
	     cell3.setBorder(0);
         content3.addCell(cell3);
         cell3 = new PdfPCell(new Phrase("����ϵ� ���ֽ� �ϻ걸 ���߷� 33",font13));
         cell3.setBorder(0);
         content3.addCell(cell3);
         document.add(content3);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content4 = new PdfPTable(2);
	     PdfPCell cell4;
	     content4.setHorizontalAlignment(content4.ALIGN_LEFT);
	     content4.setTotalWidth(450f);
	     content4.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell4 = new PdfPCell(new Phrase("�� �� �� �� �� �� : ",fontBold13));
	     cell4.setBorder(0);
	     content4.addCell(cell4);
	     cell4 = new PdfPCell(new Phrase("2019�� 10�� 29��",font13));
	     cell4.setBorder(0);
         content4.addCell(cell4);
         document.add(content4);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content5 = new PdfPTable(2);
	     PdfPCell cell5;
	     content5.setHorizontalAlignment(content5.ALIGN_LEFT);
	     content5.setTotalWidth(450f);
	     content5.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell5 = new PdfPCell(new Phrase("�� �� �� ȿ �� �� : ",fontBold13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
	     cell5 = new PdfPCell(new Phrase("2019�� 10�� 29�� ~ 2023�� 10�� 28��",font13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
         document.add(content5);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content6 = new PdfPTable(2);
	     PdfPCell cell6;
	     content6.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content6.setTotalWidth(450f);
	     content6.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell6 = new PdfPCell(new Phrase("�����о� �� ���� : ",fontBold13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
	     cell6 = new PdfPCell(new Phrase("��÷",font13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
         document.add(content6);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content7 = new PdfPTable(2);
	     PdfPCell cell7;
	     content7.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content7.setTotalWidth(450f);
	     content7.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell7 = new PdfPCell(new Phrase(" �ߡ����ࡡ����  :",fontBold13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
	     cell7 = new PdfPCell(new Phrase("2019�� 10�� 29��",font13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
         document.add(content7);
         document.add(new Paragraph("��",font12));
         
         
         document.add(new Paragraph("��",font12));
         document.add(new Paragraph("��",font12));
         
         document.add(new Paragraph("����� ����� ����ǥ�ر⺻�� ��23�� �� KS Q ISO/IEC 17025:2006�� �ǰ��Ͽ� �������ν��������� �����մϴ�. ���� ISO-ILAC-IAF �������� ��޵� �ٿ� ���� ������ �о� �� ������ ���� ����� �ɷ°� �������� ǰ���濵�ý����� �������� �����մϴ�.",font12));
         
                
         document.newPage(); // ���ο� ��������
         document.add(new Paragraph("�� KT875ȣ",font12));
         document.add(new Paragraph("��",font12));
         document.add(new Paragraph("03. �������",fontBold12));
         document.add(new Paragraph("��93.012 ����Ʈ���� ����",font12));
         document.add(new Paragraph("��",font12));
         
         PdfPTable contentTable = new PdfPTable(3);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("�԰ݹ�ȣ",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setFixedHeight(40);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�԰ݸ�",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�������",fontBold12));
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
	       
	       
	        document.add(new Paragraph("��",font12)); 
	     document.close();
	    
	     String a="D:/fileData/pdfCreate/simple_table4.pdf";
	     String b="D:/fileData/pdfCreate/simple_table4_paging.pdf";
	     pagingImgPdf(a,b);
	     
 		return "views/bbs";
 	}
 	
	//�ۼ��� PDF �ҷ��� ���͸�ũ ���� ���ο� ���Ϸ� ����
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //�ѱ� �� �� �ֵ��� ����   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font9  = new Font(bfont, 9);
        
        PdfContentByte over = stamper.getOverContent(1);
        Phrase p = new Phrase("�����ڡ�����(��)��Ƽ�ؾ� ��ǥ�̻�, �ѱ����꼱�����������(������ ���� �򰡻�)", font9);
        ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p, 60, 250, 0);
        over.saveState();
        //��������
        //PdfGState gs1 = new PdfGState();
        //gs1.setFillOpacity(0.5f);
        //over.setGState(gs1);
        //ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 450, 0);
        over.restoreState();
        stamper.close();
        reader.close();
        
      //������ �ű���� �������� ���� ����
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("���ϻ��� ����"); 
        		}else{ 
        			System.out.println("���ϻ��� ����"); 
        			} 
        	}else{ 
        		System.out.println("������ �������� �ʽ��ϴ�."); 
        		}       
        
    }
	
  //�ۼ��� PDF �ҷ��� ����¡ ���� ���Ϸ� ����
    public void pagingPdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        //PdfContentByte under = stamper.getUnderContent(1);
        
        //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";
        //�ѱ� �� �� �ֵ��� ����   
	    BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font12  = new Font(bfont,12);
        PdfContentByte over;
        Phrase p;
        for(int i=1; i<=reader.getNumberOfPages();i++){
        over = stamper.getOverContent(i);
        p = new Phrase("KAS��P��030 (" + i + "/" + reader.getNumberOfPages() + ")", font12);
        ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p, 60, 759, 0);
        }
       // over.saveState();       
        //over.restoreState();
        //KAS��P��030 (1/2)
        stamper.close();
        reader.close();
//������ �ű���� �������� ���� ����
        File file = new File(src); 
        if( file.exists() ){ 
        	if(file.delete()){
        		System.out.println("���ϻ��� ����"); 
        		}else{ 
        			System.out.println("���ϻ��� ����"); 
        			} 
        	}else{ 
        		System.out.println("������ �������� �ʽ��ϴ�."); 
        		}       
    }
    
 // ��� 3
 	@RequestMapping(value = "/pdfCreate3")
 	  public String pdfCreate3(HttpServletRequest req, ModelMap modelMap) throws Exception {
 		
 		 //��� �� ���ϸ�
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table3.pdf";
	    
	  //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //���ϰ�� ������ ����
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4���� ���� �� �¿���� ���� ����
	     Document document = new Document(PageSize.A4, 60, 60, 120, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //�ѱ� �� �� �ֵ��� ����   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //��Ʈ����
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
	     
	     Paragraph title = new Paragraph("KAS ���� ��ǰ������� ������",fontBold18);
	     title.setAlignment(Element.ALIGN_CENTER);
	     document.add(title);
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     document.add(new Paragraph("��",font12));
	     
	     
	     
	     PdfPTable content = new PdfPTable(2);
	     PdfPCell cell;
	     content.setHorizontalAlignment(content.ALIGN_LEFT);
	     content.setTotalWidth(450f);
	     content.setWidths(new float[]{60f, 390f});//�÷��� ũ������ 
         cell = new PdfPCell(new Phrase("����� : ",fontBold13));
         cell.setBorder(0);
         content.addCell(cell);
         cell = new PdfPCell(new Phrase("�ѱ������������� ����������������������",font13));
         cell.setBorder(0);
         content.addCell(cell);
         document.add(content);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content2 = new PdfPTable(2);
	     PdfPCell cell2;
	     content2.setHorizontalAlignment(content2.ALIGN_LEFT);
	     content2.setTotalWidth(450f);
	     content2.setWidths(new float[]{108f, 342f});//�÷��� ũ������ 
         cell2 = new PdfPCell(new Phrase("���ε�Ϲ�ȣ : ",fontBold13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         cell2 = new PdfPCell(new Phrase("114671-0001406",font13));
         cell2.setBorder(0);
         content2.addCell(cell2);
         document.add(content2);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content3 = new PdfPTable(2);
	     PdfPCell cell3;
	     content3.setHorizontalAlignment(content3.ALIGN_LEFT);
	     content3.setTotalWidth(450f);
	     content3.setWidths(new float[]{75f, 375f});//�÷��� ũ������ 
	     cell3 = new PdfPCell(new Phrase("�����ּ� : ",fontBold13));
	     cell3.setBorder(0);
         content3.addCell(cell3);
         cell3 = new PdfPCell(new Phrase("��û�ϵ� ������ �͵��� ���߷� 1390",font13));
         cell3.setBorder(0);
         content3.addCell(cell3);
         document.add(content3);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content4 = new PdfPTable(2);
	     PdfPCell cell4;
	     content4.setHorizontalAlignment(content4.ALIGN_LEFT);
	     content4.setTotalWidth(450f);
	     content4.setWidths(new float[]{110f, 340f});//�÷��� ũ������ 
	     cell4 = new PdfPCell(new Phrase("����� ������ : ",fontBold13));
	     cell4.setBorder(0);
	     content4.addCell(cell4);
	     cell4 = new PdfPCell(new Phrase("������ ���췯�� ��õ�� ������õ�� 1467-51 ����������������������",font13));
	     cell4.setBorder(0);
         content4.addCell(cell4);
         document.add(content4);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content5 = new PdfPTable(2);
	     PdfPCell cell5;
	     content5.setHorizontalAlignment(content5.ALIGN_LEFT);
	     content5.setTotalWidth(450f);
	     content5.setWidths(new float[]{130f, 330f});//�÷��� ũ������ 
	     cell5 = new PdfPCell(new Phrase("�����о� �� ���� : ",fontBold13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
	     cell5 = new PdfPCell(new Phrase("��÷����",font13));
	     cell5.setBorder(0);
	     content5.addCell(cell5);
         document.add(content5);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content6 = new PdfPTable(2);
	     PdfPCell cell6;
	     content6.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content6.setTotalWidth(450f);
	     content6.setWidths(new float[]{75f, 375f});//�÷��� ũ������ 
	     cell6 = new PdfPCell(new Phrase("�������� : ",fontBold13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
	     cell6 = new PdfPCell(new Phrase("��Ŵ���� 3",font13));
	     cell6.setBorder(0);
	     content6.addCell(cell6);
         document.add(content6);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content7 = new PdfPTable(2);
	     PdfPCell cell7;
	     content7.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content7.setTotalWidth(450f);
	     content7.setWidths(new float[]{108f, 342f});//�÷��� ũ������ 
	     cell7 = new PdfPCell(new Phrase("������ȿ�Ⱓ : ",fontBold13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
	     cell7 = new PdfPCell(new Phrase("2019. 6. 26~ 2023. 6. 25",font13));
	     cell7.setBorder(0);
	     content7.addCell(cell7);
         document.add(content7);
         document.add(new Paragraph("��",font12));
         
         PdfPTable content8 = new PdfPTable(2);
	     PdfPCell cell8;
	     content8.setHorizontalAlignment(content6.ALIGN_LEFT);
	     content8.setTotalWidth(450f);
	     content8.setWidths(new float[]{75f, 375f});//�÷��� ũ������ 
	     cell8 = new PdfPCell(new Phrase("������ũ : ",fontBold13));
	     cell8.setBorder(0);
	     content8.addCell(cell8);
	     cell8 = new PdfPCell(new Phrase("��",fontBold13));
	     cell8.setBorder(0);
	     content8.addCell(cell8);
         document.add(content8);         
         document.add(new Paragraph("��",font12));
         document.add(new Paragraph("��",font12));
         
         document.add(new Paragraph("����� ����� ����ǥ�ع� �� 21��, ��ǰ������� ���� �� ���İ��� � ���� ��� ��21���� ���� �� KS Q ISO/IEC 17065�� ������ǿ� �ǰ��Ͽ� KAS ���� ��ǰ����������� �����մϴ�.",font12));
         
         document.add(new Paragraph("��",font12));
         document.add(new Paragraph("��",font12));
         
         Paragraph date = new Paragraph("2019�� 05�� 26��",font12);
         date.setAlignment(Element.ALIGN_RIGHT);
	     document.add(date);
	     
         document.add(new Paragraph("��",font12));
         document.add(new Paragraph("��",font12));
         
         //��ũ
         Image mark = Image.getInstance("D:/fileData/pdfCreate/b3.png");
	        mark.scaleToFit(240	, 215); // ����, ���� ũ��
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
         
         document.newPage(); // ���ο� ��������
         document.add(new Paragraph("�� �����о� �� ����(V-check ��ũ)",font12));
         document.add(new Paragraph("������ ��з�: 25. �ݼӰ�����ǰ; ��� �� ���� ����",font12));
         document.add(new Paragraph("����- ������Ŵ�� ���� : ��Ŵ ���� 3",font12));
         document.add(new Paragraph("��",font12));
         
         PdfPTable contentTable = new PdfPTable(5);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("�ߺз�",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setFixedHeight(40);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�Һз�",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��ǰ��",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��ǰ�� ����",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("���ع���",fontBold12));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("259.��Ÿ �ݼӰ�����ǰ ������",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("2599. �� �� ��Ÿ �ݼӰ�����ǰ ������",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��������",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�ܺ� ���� ����",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("ȭ���� ��ȣ���� ���ɡ����� �������� (�������ħ,2017.07)",font11));
	        contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE);
	        contentCell.setRowspan(7);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�����������",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��� ���� �� �ܺ� ���� ����",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�����������Ӱ���",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�ܺ� ���� ����",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�������ܹ���",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����ܺ� ���� ���� �� �����帧 ����",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("������",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�ܺ� ���� ����",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�������ܹ�",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("���ذ��� ��������",font11));
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�������ڿ�����",font11));
	        contentCell.setFixedHeight(71);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("���ذ��� ��ȭ",font11));
	        contentTable.addCell(contentCell);
	        document.add(contentTable);
	        document.add(new Paragraph("��",font12));
	        document.add(new Paragraph("��",font12)); 
	     document.close();
	    
	        String a = "D:/fileData/pdfCreate/simple_table3.pdf"; //���͸�ũ ���� pdf
	        String b = "D:/fileData/pdfCreate/simple_table3_paging.pdf"; //���͸�ū �پ ���� pdf
	        pagingPdf(a,b);
	    
	     
	     
 		return "views/bbs";
 	}
	// ��� 2
	@RequestMapping(value = "/pdfCreate2")
	  public String pdfCreate2(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //��� �� ���ϸ�
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table2.pdf";
	    
	  //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //���ϰ�� ������ ����
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4���� ���� �� �¿���� ���� ����
	     Document document = new Document(PageSize.A4, 60, 60, 55, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //�ѱ� �� �� �ֵ��� ����   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //��Ʈ����
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //��� �̹��� �۾�
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1_1.png");
	        png.scaleToFit(480	, 298); // ����, ���� ũ��
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //���� �ۼ�
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("����  ������ ����",font11));
	        document.add(new Paragraph("(����)",font11));
	        document.add(new Paragraph("����  KOLAS ���ν����� (��ȯ��) �����ɻ� �뺸 �� ��û((��)��Ƽ�ؾ�)",font11));
	        
	        //���� �߰�
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(480	, 100); // ����, ���� ũ��
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. KOLAS ���ν����� ��ȯ�� ��û �����Դϴ�.",font11));
	        document.add(new Paragraph("��",font11));// �� + ����1�� ��ȣ
	        document.add(new Paragraph("2. KOLAS �繫���� �����򰡻� ���� �� �����ɻ� �Ƿڿ� �����Ͽ� �óձ���� ���ذ��� �Ǵ� ������� ���� ������ ���� ���� ��û�� ������ e-KOLAS �ý����� ���Ͽ� Ȯ���Ͽ����ϴ�.",font11)); 
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("3. �ƿ﷯, ��û ������ ���� �����ɻ�� ��ȯ��ħ(�������ǥ�ؿ� ���� ��2018-0267ȣ, KOLAS ���ν��衤�������(KS Q ISO/IEC 17025:2017) ��ȯ��ȹ����)�� ���� �������ذ��� ���ռ� ���並 �����򰡻翡�� �Ƿ��Ͽ��� KOLAS Ȩ�������� ��û ���� ��ü�� ������ ��, �� ����� 1���� �̳��� ����Ͽ� �ֽñ� �ٶ��ϴ�.",font11));
	        document.add(new Paragraph("��",font11));
	        //���� ���̺�
	        PdfPTable contentTable = new PdfPTable(4);
	        contentTable.setHorizontalAlignment(contentTable.ALIGN_CENTER);
	        contentTable.setTotalWidth(450f);
	        contentTable.setLockedWidth(true);
	        PdfPCell contentCell= new PdfPCell(new Phrase("�������",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("����",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��û�о�",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("�����ɻ���",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("(��)��Ƽ�ؾ�",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��ȯ��",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("��û���� ����",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        contentCell= new PdfPCell(new Phrase("������ �����򰡻� \n (�ѱ�������������)",font11));
	        contentCell.setHorizontalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setVerticalAlignment(contentCell.ALIGN_CENTER);
	        contentCell.setFixedHeight(21);
	        contentTable.addCell(contentCell);
	        document.add(contentTable);
	        
	        document.add(new Paragraph("��.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        
	        //�����ũ        
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/b1.png");
	        mark.scaleToFit(180	, 160); // ����, ���� ũ��
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
	       // mark.setTransparency(transparency);
	        
	        //������
	        //document.add(new Paragraph("�����ڡ�����(��)��Ƽ�ؾ� ��ǥ�̻�, �ѱ����꼱�����������(������ ���� �򰡻�)",font11));
	      
	        
	      //���� �߰�
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(480	, 100); // ����, ���� ũ��
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //�ϴ� �ۼ�
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //�׵θ� ������
	          table.setWidths(new float[]{4f, 4f, 5f, 6f, 10f});//�÷��� ũ������
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("����������",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("��",font9));
	          cell = new PdfPCell(new Phrase("���ռ��򰡰� ����",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("���� 2019.10.14.",font8));
	          cell = new PdfPCell(new Phrase("��",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("����",fontBold9));
	          table.addCell(new Phrase("������",fontBold9));
	          document.add(table);
	          document.add(new Phrase("������",font9));
	          
	          //ǲ�� ���̺�
	          PdfPTable footerTable1 = new PdfPTable(5);
	          footerTable1.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable1.setWidths(new float[]{3f, 5f, 4f, 3f, 6f});//�÷��� ũ������
	          footerTable1.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable1.addCell(new Phrase("����",font9));
	          footerTable1.addCell(new Phrase("���ռ��򰡰�-5415",font9));
	          footerTable1.addCell(new Phrase("(2019. 10. 24.)",font9));
	          footerTable1.addCell(new Phrase("����",font9));
	          footerTable1.addCell(new Phrase("��",font9));
	          document.add(footerTable1);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable2 = new PdfPTable(3);
	          footerTable2.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable2.setWidths(new float[]{3f, 14f, 8f});//�÷��� ũ������
	          footerTable2.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable2.addCell(new Phrase("�� 27737",font9));
	          footerTable2.addCell(new Phrase("��û�ϵ� �������͵��� �̼��� 93, (�������ǥ�ؿ�)",font9));
	          footerTable2.addCell(new Phrase("/ http://www.kats.go.kr",font9));
	          document.add(footerTable2);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable3 = new PdfPTable(4);
	          footerTable3.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable3.setWidths(new float[]{10, 10, 10, 6});//�÷��� ũ������
	          footerTable3.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable3.addCell(new Phrase("��ȭ��ȣ 043-870-5472",font9));
	          footerTable3.addCell(new Phrase("�ѽ���ȣ  043-870-5679",font9));
	          footerTable3.addCell(new Phrase("/ mgkim1004@korea.kr",font9));
	          footerTable3.addCell(new Phrase(" /�����(6)",font9));
	          document.add(footerTable3);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable4 = new PdfPTable(1);
	          footerTable4.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable3.setHorizontalAlignment(table.ALIGN_MIDDLE);
	          footerTable4.addCell(new Phrase("������������2019 �ѡ��Ƽ��� Ư������ȸ�� �� ��1�� �ѡ����� ����ȸ��(11.25-27 �λ�)",font9));
	          document.add(footerTable4);
	          
	          
	        document.close();
			
	        String a = "D:/fileData/pdfCreate/simple_table2.pdf"; //���͸�ũ ���� pdf
	        String b = "D:/fileData/pdfCreate/simple_table2_waterMark.pdf"; //���͸�ū �پ ���� pdf
	        manipulatePdf(a,b);
	    return "views/bbs";
	  }
	// ���1
	@RequestMapping(value = "/pdfCreate1")
	  public String pdfCreate1(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //��� �� ���ϸ�
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table1.pdf";
	    
	  //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //���ϰ�� ������ ����
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4���� ���� �� �¿���� ���� ����
	     Document document = new Document(PageSize.A4, 60, 60, 55, 30);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //�ѱ� �� �� �ֵ��� ����   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //��Ʈ����
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //��� �̹��� �۾�
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1_1.png");
	        png.scaleToFit(480	, 298); // ����, ���� ũ��
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //���� �ۼ�
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("����  (��)��Ƽ��",font11));
	        document.add(new Paragraph("(����)",font11));
	        document.add(new Paragraph("����  KOLAS ���ν�����(�ű�) ������ ����((��)��Ƽ��)",font11));
	        
	        //���� �߰�
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(480	, 100); // ����, ���� ũ��
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. �� ����� KOLAS ���ν����� �ű� ��û �����Դϴ�.",font11));
	        document.add(new Paragraph("��",font11));// �� + ����1�� ��ȣ
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("2. ���� �����Ͽ� ������ǥ�ر⺻���� ��23��(���衤�˻��� ���� ��) �� ��KOLAS ����",font11));                                                
	        document.add(new Paragraph("���� �� �˻��� �������� ���ɡ� ��18��(������û �� ��)�� ���� ���Ӱ� ����",font11));
	        document.add(new Paragraph("�� ����� ���ν����� (�ű�) ������ �����ϰ� �������� �����Ͽ� �帳�ϴ�.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("3. ����, ���� ���������� �ʿ� �� e-KOLAS �����ý��� (www.knab.go.kr)�� ���Ͽ� �ٿ�",font11));
	        document.add(new Paragraph("������ �� ������ �˷��帳�ϴ�.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("����  1. �������ǥ�ؿ� ���� ��2019-0304ȣ 1��.",font11));
	        document.add(new Paragraph("����  2. ���α�� ������ 1��.  ��.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        
	        //�����ũ
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/b1.png");
	        mark.scaleToFit(160	, 160); // ����, ���� ũ��
	        mark.setAlignment(mark.ALIGN_CENTER);
	        document.add(mark);
	        
	      
	        
	      //���� �߰�
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(480	, 100); // ����, ���� ũ��
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //�ϴ� �ۼ�
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //�׵θ� ������
	          table.setWidths(new float[]{4f, 4f, 5f, 6f, 10f});//�÷��� ũ������
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("����������",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("��",font9));
	          cell = new PdfPCell(new Phrase("���ռ��򰡰� ����",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("���� 2019.10.14.",font8));
	          cell = new PdfPCell(new Phrase("��",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("����",fontBold9));
	          table.addCell(new Phrase("������",fontBold9));
	          document.add(table);
	          document.add(new Phrase("������",font9));
	          
	          //ǲ�� ���̺�
	          PdfPTable footerTable1 = new PdfPTable(5);
	          footerTable1.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable1.setWidths(new float[]{3f, 5f, 4f, 3f, 6f});//�÷��� ũ������
	          footerTable1.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable1.addCell(new Phrase("����",font9));
	          footerTable1.addCell(new Phrase("���ռ��򰡰�-5221",font9));
	          footerTable1.addCell(new Phrase("(2019. 10. 14.)",font9));
	          footerTable1.addCell(new Phrase("����",font9));
	          footerTable1.addCell(new Phrase("��",font9));
	          document.add(footerTable1);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable2 = new PdfPTable(3);
	          footerTable2.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable2.setWidths(new float[]{3f, 14f, 8f});//�÷��� ũ������
	          footerTable2.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable2.addCell(new Phrase("�� 27737",font9));
	          footerTable2.addCell(new Phrase("��û�ϵ� �������͵��� �̼��� 93, (�������ǥ�ؿ�)",font9));
	          footerTable2.addCell(new Phrase("/ http://www.kats.go.kr",font9));
	          document.add(footerTable2);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable3 = new PdfPTable(4);
	          footerTable3.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable3.setWidths(new float[]{10, 10, 10, 6});//�÷��� ũ������
	          footerTable3.setHorizontalAlignment(table.ALIGN_LEFT);
	          footerTable3.addCell(new Phrase("��ȭ��ȣ 043-870-5472",font9));
	          footerTable3.addCell(new Phrase("�ѽ���ȣ  043-870-5679",font9));
	          footerTable3.addCell(new Phrase("/ mgkim1004@korea.kr",font9));
	          footerTable3.addCell(new Phrase(" /�뱹�ΰ���",font9));
	          document.add(footerTable3);
	          document.add(new Paragraph("��",font11));
	          PdfPTable footerTable4 = new PdfPTable(1);
	          footerTable4.getDefaultCell().setBorder(0); //�׵θ� ������
	          footerTable3.setHorizontalAlignment(table.ALIGN_MIDDLE);
	          footerTable4.addCell(new Phrase("������������2019 �ѡ��Ƽ��� Ư������ȸ�� �� ��1�� �ѡ����� ����ȸ��(11.25-27 �λ�)",font9));
	          document.add(footerTable4);
	          
	          
	        document.close();
	    return "views/bbs";
	  }
	
	@RequestMapping(value = "/pdfCreate")
	  public String pdfCreate(HttpServletRequest req, ModelMap modelMap) throws Exception {
		
	   //��� �� ���ϸ�
		String fileName="";
	    String dir="D:/fileData/pdfCreate/";
	    fileName = "simple_table.pdf";
	    
	  //����� ��Ʈ	    
	    String font_regular="D:/fileData/pdfCreate/NanumGothic-Regular.ttf";

	    
	  //���ϰ�� ������ ����
	    File directory = new File(dir);
	    if(!directory.exists()) directory.mkdirs(); 
	     
	    //A4���� ���� �� �¿���� ���� ����
	     Document document = new Document(PageSize.A4, 50, 50, 50, 50);
	     PdfWriter.getInstance(document, new FileOutputStream(dir+"/"+fileName));
	     document.open();
	        
	     //�ѱ� �� �� �ֵ��� ����   
	     BaseFont bfont = BaseFont.createFont(font_regular, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	     //��Ʈ����
	     Font font11  = new Font(bfont, 11);
	     Font font9  = new Font(bfont, 9);
	     Font font8  = new Font(bfont, 8);
	     Font fontBold9  = new Font(bfont, 9,Font.BOLD);
	     Font fontBold15  = new Font(bfont, 15,Font.BOLD);
	    	 
	        //��� �̹��� �۾�
	        Image png = Image.getInstance("D:/fileData/pdfCreate/a1.png");
	        png.scaleToFit(500	, 400); // ����, ���� ũ��
	        png.setAlignment(png.ALIGN_CENTER);
	        document.add(png);
	        
	        //���� �ۼ�
	        document.add(new Paragraph("���� ������ ����",font11));
	        document.add(new Paragraph("(����)",font11));
	        document.add(new Paragraph("���� 2019�� ����� ������ ǰ������ �������� 2�� ���˰�� �뺸",font11));
	        
	        //���� �߰�
	        Image png2 = Image.getInstance("D:/fileData/pdfCreate/a2.png");
	        png2.scaleToFit(500	, 100); // ����, ���� ũ��
	        png2.setAlignment(png2.ALIGN_CENTER);
	        document.add(png2);
	        
	        document.add(new Paragraph("1. ���������� ������������å��-1238127389ȣ �����Դϴ�.",font11));
	        document.add(new Paragraph("��",font11));// �� + ����1�� ��ȣ
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("2. ��ȣ �������� �츮�� 2019�� ������ǰ������ ��ȹ�� ���� �������� ���˰����",font11));
	        document.add(new Paragraph("���Ӱ� ���� �뺸�Ͽ���, ������ ǰ������ ���� ����� �����Ͻñ� �ٶ��ϴ�.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("3. �ƿ﷯, 2019�� ���������� ǰ������ �����򰡿� ���� �����Ͽ� �ֽñ� �ٶ��ϴ�.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("����  1. 2019�� ����� ǰ������ ��ȹ 2������ ���.",font11));
	        document.add(new Paragraph("����  2. 2019�� ������ǰ�� ������ ��ü���˰��.  ��.",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        document.add(new Paragraph("��",font11));
	        
	        //����� �� ��ũ
	        PdfPTable organNameMark = new PdfPTable(3);
	        organNameMark.getDefaultCell().setBorder(0);
	        organNameMark.addCell(new Phrase("��",font9));
	        PdfPCell organcell;
	        organcell = new PdfPCell(new Phrase("������������",fontBold15));
	        organcell.setVerticalAlignment(organcell.ALIGN_BOTTOM);
	        organcell.setBorder(0);
	        organNameMark.addCell(organcell);
	        Image mark = Image.getInstance("D:/fileData/pdfCreate/a4.png");
	        organNameMark.addCell(mark);
	        document.add(organNameMark); 
	        //document.add(new Paragraph("������ ȫ�浿, ��μ�, ġ��, ĩ��, ����",font9));
	        
	        //������
	        PdfPTable receiver = new PdfPTable(2);
	        receiver.getDefaultCell().setBorder(0); //�׵θ� ������
	        receiver.setWidths(new float[]{1f, 10f});//�÷��� ũ������
	        receiver.addCell(new Phrase("������",font9));
	        receiver.addCell(new Phrase("��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ��������ȸ(��������ûå����), ",font9));
	        document.add(receiver);
	        
	      //���� �߰�
	        Image png3 = Image.getInstance("D:/fileData/pdfCreate/a3.png");
	        png3.scaleToFit(500	, 100); // ����, ���� ũ��
	        png3.setAlignment(png3.ALIGN_CENTER);
	        document.add(png3);
	        
	        //�ϴ� �ۼ�
	        PdfPTable table = new PdfPTable(5);	 
	          table.getDefaultCell().setBorder(0); //�׵θ� ������
	          table.setWidths(new float[]{4f, 4f, 4f, 6f, 10f});//�÷��� ũ������
	          table.setHorizontalAlignment(table.ALIGN_LEFT);
	          PdfPCell cell;
	          cell = new PdfPCell(new Phrase("�ֹ���",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("��",font9));
	          cell = new PdfPCell(new Phrase("������������",font9));
	          cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("���� 2019.10.1.",font8));
	          cell = new PdfPCell(new Phrase("��",font9));
	          cell.setRowspan(2);
	          cell.setBorder(0);
	          table.addCell(cell);
	          table.addCell(new Phrase("�ڹκ�",fontBold9));
	          table.addCell(new Phrase("�ſ���",fontBold9));
	          
	          document.add(table);  
	        
	        document.close();
	    return "views/bbs";
	  }
	
	
}