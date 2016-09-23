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

package me.carpela.network.pt.cracker.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

	private static Logger logger = Logger.getLogger(Config.class.getName());
	
	/* Network Speed in KB/s */
	public static long minRate = 1024*1;
	public static long maxRate = 1024*10;
	
	/* Request Interval in seconds */
	public static int interval = 30;
	
	/* Default uploaded traffic in bytes */
	public static long defaultUploaded = 953383458;
	
	/* Client Identifier */
	public static String client = "Transmission/2.84";
	
	/* Generated unique peer ID */
	public static String peer_id = "-TR2840-cxibsn5dhrfe";
	
	/* Port of this client for peer-peer communication */
	public static int port = 51413;
	
	/* Total uploaded bytes for current task (MB) */
	public static int total = 4096;
	
	/* Maxium torrent files & threads */
	public static int maxThreads = 10;
	
	/* Torrent files directory */
	public static String torrentsDir = "~/torrents";
	
	/* Torrent files */
	public static List<File> torrents = new ArrayList<File>();
	
	/* Load configuration from a .properties file */
	public static boolean parseConfig(String filename)
	{
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(filename));
			
			int tmpMin, tmpMax, tmpInterval, tmpTotal;
			
			try{
				tmpMin = Integer.parseInt(pps.getProperty("minRate"));
				tmpMax = Integer.parseInt(pps.getProperty("maxRate"));
				tmpInterval = Integer.parseInt(pps.getProperty("interval"));
				tmpTotal = Integer.parseInt(pps.getProperty("total"));
				
				if(tmpMax < tmpMin || tmpMax < 0 || tmpMin < 0){
					logger.warn("Rate is not properly set! (maxRate="+tmpMax+"KB/s, minRate="+tmpMin+"KB/s");
				} else {
					minRate = tmpMin;
					maxRate = tmpMax;
				}
				
				if(maxRate >= 1024*20 || minRate >= 1024*10){
					logger.warn("Rate is too high!");
				}
				
				if(tmpInterval < 30) {
					logger.warn("Interval is too low");
				} else {
					interval = tmpInterval;
				}
				
				if(tmpTotal < 512) {
					logger.warn("Total traffic is too low ("+tmpTotal+"), use default (4096MB).");
				} else {
					total = tmpTotal;
				}
			}catch(Exception e)
			{
				logger.error("There are some errors in your config file, please CHECK it! Use default configuration.");
			}

			torrentsDir = pps.getProperty("torrentsDir", torrentsDir);
		} catch (IOException e) {
			logger.warn("Config file not found, use default configuration.");
		}
		
		getAllTorrents(new File(torrentsDir));
		
		return true;
	}
	
	private static void getAllTorrents(File dir)
	{
		if(!dir.exists())
			return;
		
		File[] files = dir.listFiles();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile())
				torrents.add(files[i]);
			if(files[i].isDirectory())
			{
				getAllTorrents(files[i]);
			}
		}
		
	}
}
