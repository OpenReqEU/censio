package com.selectionarts.projectcensio.util;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Component
public class ZXingHelper {
	
    @Value( "${censio.email-link}" )
    private String emaillink;
    
	public byte[] getQRCodeImage(String text, int width, int height) {
		
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			text = emaillink + "eDemocracy/public/" + text;
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}
}
