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

package me.carpela.network.pt.cracker.main;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import me.carpela.network.pt.cracker.config.Config;
import me.carpela.network.pt.cracker.impl.SimpleCracker;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) 
	{
		String configFile;
		/* if no CLI-arguments supplied, use default file "ptcracker.config" in current directory */
		if(args.length != 1) {
			logger.warn("Invalid command-line arguments! Use <./ptcracker.config>.");
			configFile = "ptcracker.config";
		} else {
			configFile = args[0];
		}
		
		/* command-line arguments */
		Config.parseConfig(configFile);
		
		logger.info("PT-Cracker started with [MaxRate="+Config.maxRate+"KB/s, MinRate="+Config.minRate+"KB/s, Interval="+Config.interval+"s, Traffic="+Config.total+"MB");
		logger.debug("Torrents files are: "+Config.torrents);

		/* shuffle the torrent files, in order to use every torrent file as fair as possible */
		Collections.shuffle(Config.torrents);
		
		ExecutorService ex = Executors.newFixedThreadPool(Config.maxThreads);
		logger.info("Created! Fixed Thread Pool (size: "+Config.maxThreads+")");
		
		/* use up to 10 torrent files */
		int tSize = Config.torrents.size();
		for(int i=0; i<tSize && i<=Config.maxThreads; i++) {
			/* total divided equally to each SimpleCracker */
			int traffic = Config.total / (tSize > Config.maxThreads ? Config.maxThreads : tSize);
			ex.execute(new SimpleCracker(Config.torrents.get(i), traffic, i));
			logger.info("Start a cracker [Traffic: "+traffic+"MB] of "+ Config.torrents.get(i));
		}
		/* wait for threads to complete */
		ex.shutdown();
	}
}
