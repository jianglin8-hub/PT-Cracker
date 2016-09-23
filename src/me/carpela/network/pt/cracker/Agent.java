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

package me.carpela.network.pt.cracker;

import me.carpela.network.pt.cracker.bean.TrackerRequestParameter;

/**
 * Agent performs the function that deliver a packet to the tracker server.
 * @author Hover Winter
 */
public interface Agent {

	/* send packet to tracker */
	boolean sendPacket(String url, TrackerRequestParameter param);
	
}
