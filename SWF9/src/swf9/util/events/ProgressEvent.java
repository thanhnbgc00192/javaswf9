package swf9.util.events;
/**
 * Copyright (c) 2008  Stefano Cottafavi and .:: A Quarter to Seven ::.
 * 
 * Some rights reserved.
 * 
 * Licensed under the CREATIVE COMMONS Attribution-Noncommercial-Share Alike 3.0
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: http://creativecommons.org/licenses/by-nc-sa/3.0/us/
 * 
 * Please visit http://www.stefanocottafavi.com for more information
**/

import java.util.EventObject;

public class ProgressEvent extends EventObject {
	
	private Number _progress;
	
	public ProgressEvent(Object source, Number progress) {
		super(source);
		_progress = progress;
	}
	
	public Number progress() {
		return _progress;
	}
	
}
