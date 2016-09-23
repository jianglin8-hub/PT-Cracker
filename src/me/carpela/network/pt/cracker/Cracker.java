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

import me.carpela.network.pt.cracker.bean.*;

/**
 * Cracker perfoms the function that cracks the PT tracker server following the PT Protocol.
 * @author Hover Winter
 *
 */
public interface Cracker {

	/* initial */
	boolean startup(TrackerRequestParameter trp);
	/* crack actions */
	boolean doCrack(TrackerRequestParameter trp);
	/* destory */
	boolean shutdown(TrackerRequestParameter trp);
	
}
