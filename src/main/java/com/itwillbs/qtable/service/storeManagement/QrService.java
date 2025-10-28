package com.itwillbs.qtable.service.storeManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QrService {
	
	@Value("${uploadPath}")
	private String uploadPath;

	@Value("${app.public-base-url}")
	private String publicBaseUrl;
	
	public String buildUrl(int storeIdx) {
	    return UriComponentsBuilder
	            .fromUriString(publicBaseUrl)         
	            .pathSegment("on_site_payment")       
	            .queryParam("store_idx", storeIdx)
	            .build()                               
	            .encode()                               
	            .toUriString();
	}
	
	
	public String generateQRCode(String url, int storeIdx) throws WriterException, IOException{
		
		/* QR SIZE */
		int width = 300;
		int height = 300;
		
		/* QR 이미지 저장 경로 */
		String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		
		String projectPath = System.getProperty("user.dir");
		File dir = new File(projectPath, uploadPath + "/" + datePath);
		if(!dir.exists()) dir.mkdirs();
		
		/* QR 생성 */
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
		
		/* QR 네이밍 And Write */
		String fileName = "store_" + storeIdx + "_" + UUID.randomUUID() + ".png";
		Path filePath = FileSystems.getDefault().getPath(dir.getAbsolutePath(), fileName);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
		
		return "/upload/" + datePath + "/" + fileName;
	}
	
	
	

	
	
	
	
	
	
	
}
