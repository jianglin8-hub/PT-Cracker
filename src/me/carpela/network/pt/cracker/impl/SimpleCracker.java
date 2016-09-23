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

package me.carpela.network.pt.cracker.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import me.carpela.network.pt.cracker.Agent;
import me.carpela.network.pt.cracker.Cracker;
import me.carpela.network.pt.cracker.bean.TrackerRequestParameter;
import me.carpela.network.pt.cracker.config.Config;
import me.carpela.network.pt.cracker.config.TrackerEvent;
import me.carpela.network.pt.cracker.http.impl.BasicHttpAgent;
import me.carpela.network.pt.cracker.tools.URLEncoder;
import me.carpela.network.pt.cracker.tools.ttorrent.Torrent;

public class SimpleCracker implements Cracker, Runnable {

	private static Logger logger = Logger.getLogger(SimpleCracker.class.getName());
	
	private Agent agent = new BasicHttpAgent(); 
	/* the torrent file that this cracker will work on */
	private File torrent;
	/* the number of this cracker, for logging */
	private int number;
	
	/* -1 means just send Config.defaultUploaed MB */
	private long traffic;
	
	public SimpleCracker(File torrent){
		this.torrent = torrent;
		this.traffic = -1;
		this.number = 1;
	}
	
	public SimpleCracker(File torrent, long traffic, int number) {
		this.torrent = torrent;
		this.traffic = traffic;
		this.number = number;
	}
	
	/*
	 * Just send a STARTED event with left=0 to tracker
	 * @see me.carpela.network.pt.cracker.Cracker#startup(me.carpela.network.pt.cracker.bean.TrackerRequestParameter)
	 */
	@Override
	public boolean startup(TrackerRequestParameter trp) {
		trp.setDownloaded(0);
		trp.setUploaded(0);
		trp.setLeft(0);
		trp.setEvent(TrackerEvent.START);
		agent.sendPacket(trp.getUrl(), trp);
		
		logger.info("Cracker-"+this.number+" startup!");
		return true;
	}
	
	/*
	 * While traffic is not sent out, send a packet with uploaed = (random rate between minRate and maxRate) * interval.
	 * @see me.carpela.network.pt.cracker.Cracker#doCrack(me.carpela.network.pt.cracker.bean.TrackerRequestParameter)
	 */
	@Override
	public boolean doCrack(TrackerRequestParameter trp) {
		do {
			try {
				Thread.sleep(Config.interval * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(traffic > 0) {
				long rate = (long) (Math.random() * (Config.maxRate - Config.minRate)) + Config.minRate;
				long uploaded = traffic*1024 > rate*Config.interval ? rate*Config.interval : traffic*1024;
				trp.setUploaded( rate * Config.interval * 1024);
				traffic -= uploaded / 1024;
				logger.info("Cracker-"+this.number+" send "+uploaded+" KB with "+rate+" KB/s in "+Config.interval+" s. Traffic= "+this.traffic);
			} else {
				trp.setUploaded(Config.defaultUploaded);
				logger.info("Cracker-"+this.number+" send "+Config.defaultUploaded+" in "+Config.interval+"s");
			}
			
			trp.setEvent(TrackerEvent.WORKING);
			agent.sendPacket(trp.getUrl(), trp);
			
		} while(traffic > 0);
		return true;
	}
	
	@Override
	public boolean shutdown(TrackerRequestParameter trp){
		trp.setDownloaded(0);
		trp.setUploaded(0);
		trp.setLeft(0);
		trp.setEvent(TrackerEvent.STOP);
		agent.sendPacket(trp.getUrl(), trp);

		logger.info("Cracker-"+this.number+" shutdown!");
		return true;
	}

	@Override
	public void run() {
		Torrent tt = null;
		
		try {
			tt = Torrent.load(this.torrent);
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			logger.error("Cracker-"+this.number+" exit: failed to parse torrent file "+this.torrent);
			return;
		}
		
		URI url = tt.getAnnounceList().get(0).get(0);
		if(url == null)
		{
			logger.error("Cracker-"+this.number+" exit: failed to get announce url from "+this.torrent);
			return;
		}
		
		TrackerRequestParameter trp = new TrackerRequestParameter();
		trp.setUrl(url.toString()+"&info_hash="+URLEncoder.encodeBytes(tt.getInfoHash()));
		
		logger.info("Cracker-"+this.number+"["+trp.getUrl()+"] is ready. ");
		/* startup */
		startup(trp);
		try {
			Thread.sleep(Config.interval * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/* crack */
		doCrack(trp);
		/* shutdown */
		try {
			Thread.sleep(Config.interval * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdown(trp);
	}
}
