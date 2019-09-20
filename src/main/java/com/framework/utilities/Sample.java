package com.framework.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		verifyPDFContent("D:/pdf/patient1m_MOR01_AttentionScreen-Child_05242019085229.pdf", "Name");

	}

	
	//Please pass url and text to be vaildated to this method
public static void verifyPDFContent(String strURL, String reqTextInPDF) {

	try {
		/*URL url = new URL(strURL);
		BufferedInputStream file = new BufferedInputStream(url.openStream());
		PDFParser parser = new PDFParser(file);*/
		
		File file = new File(strURL);
		PDDocument doc = PDDocument.load(file);
		String text = new PDFTextStripper().getText(doc);
		doc.close();
		System.out.println(text);
		if(text.contains(reqTextInPDF))
		{
			System.out.println("Requested text "+reqTextInPDF+" is available in pdf : Validated");
		}
		else
		{
			System.out.println("Requested text is not available");
		}
		
		Assert.assertTrue(text.contains(reqTextInPDF));
		
	
	} catch (IOException e) {
	

	}
}


}
