/**
 * Copyright (C) 2016-2016  Hover Winter<hoverwinter@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.carpela.network.pt.cracker.http.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import me.carpela.network.pt.cracker.*;
import me.carpela.network.pt.cracker.bean.TrackerRequestParameter;
import me.carpela.network.pt.cracker.config.Config;
import me.carpela.network.pt.cracker.tools.StringUtils;

public class BasicHttpAgent implements Agent{

	private static Logger logger = Logger.getLogger(BasicHttpAgent.class.getName());

	private HttpClient httpClient = new DefaultHttpClient();
	
	/*
	 * Send a packet to tracker with given parameter.
	 * @see me.carpela.network.pt.cracker.Agent#sendPacket(java.lang.String, me.carpela.network.pt.cracker.bean.TrackerRequestParameter)
	 */
	@Override
	public boolean sendPacket(String url, TrackerRequestParameter param) {
		if(!checkTRP(param))
		{
			logger.error("Packet not sent: request parameter illegal!");
			return false;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uploaded", ""+param.getUploaded()));
		params.add(new BasicNameValuePair("downloaded", ""+param.getDownloaded()));
		params.add(new BasicNameValuePair("left", ""+param.getLeft()));
		
		params.add(new BasicNameValuePair("peer_id", Config.peer_id));
		params.add(new BasicNameValuePair("port", ""+Config.port));
		if(StringUtils.isNotBlank(param.getEvent()))
		{
			params.add(new BasicNameValuePair("event", param.getEvent()));
		}	
		
		String result = sendGet(url , params);
		
		logger.info("Packet sent, with reponse: "+result);
		return true;
	}

	/*
	 *  Some basic check of TrackerRequestParameter:
	 * 		- numbers >= 0
	 * 		- strings shouldn't be empty
	 *  When failed, no packet will be sent.
	 *  
	 */
	private boolean checkTRP(TrackerRequestParameter param) {
		return param.getDownloaded() >= 0 &&
				param.getUploaded() >= 0 &&
				param.getLeft() >= 0;
	}
	
	/*
	 * Actually send the HTTP Get request.
	 *
	 */
	private String sendGet(String url, List<NameValuePair> params)
	{
		String body = null;  
        try {  
            HttpGet httpget = new HttpGet(url);  
            /* set HTTP parameters */
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));  
            httpget.setURI(new URI(httpget.getURI().toString() + "&" + str));  
            /* set HTTP header */
            httpget.addHeader("User-Agent", Config.client);
            httpget.addHeader("Accept", "*/*");
            httpget.addHeader("Host", httpget.getURI().getHost());
            httpget.addHeader("Accept-Encoding", "gzip;q=1.0, deflate, identity");
            /* execute the reqeust */
            HttpResponse httpresponse = httpClient.execute(httpget);  
            HttpEntity entity = httpresponse.getEntity();  
            body = EntityUtils.toString(entity);  
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.error("Failed to send HTTP request.");
        }
        return body;  
	}
}
