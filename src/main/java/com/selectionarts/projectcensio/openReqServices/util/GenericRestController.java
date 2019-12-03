package com.selectionarts.projectcensio.openReqServices.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Component
public class GenericRestController <T> {

    private Class<T> genericsType;
    private String url;
    private HttpMethod httpMethod;
    private Object objectToSend;
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
 
    public void createRequest(Class<T> genericsType, String url, HttpMethod httpMethod, Object objectToSend) {
    		this.genericsType = genericsType;
        this.url = url;
        this.httpMethod = httpMethod;
        this.objectToSend = objectToSend;
    }
    
    public T sendRequest() 
    {
    	return this.sendRequest("nottoken");
    }

    public T sendRequest(String token) 
    {
    	  TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

    	    SSLContext sslContext = null;
			try {
				sslContext = org.apache.http.ssl.SSLContexts.custom()
				                .loadTrustMaterial(null, acceptingTrustStrategy)
				                .build();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

    	    CloseableHttpClient httpClient = HttpClients.custom()
    	                    .setSSLSocketFactory(csf)
    	                    .build();

    	    HttpComponentsClientHttpRequestFactory requestFactory =
    	                    new HttpComponentsClientHttpRequestFactory();

    	    requestFactory.setHttpClient(httpClient);
    	
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Accept", "*/*");
        httpHeaders.set("Authorization", token);
        
        try {
        	HttpEntity h = new HttpEntity(objectToSend, httpHeaders);            ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, h, genericsType);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
            	this.log.debug("response: " + response.getBody());
            	this.log.debug("statuscode: " + response.getStatusCode());
                return response.getBody();
            } else {
                if (response == null) {
                	this.log.error("response == null");
                } else {
                	this.log.debug("response: " + response.getBody());
                	this.log.debug("statuscode: " + response.getStatusCode());
                	return response.getBody();
                }
            }
        } catch (Exception ex) {
        	this.log.error("The request: " + url + " ends with an error: " + ex.getMessage());
        }

        return  null;
    }

}
