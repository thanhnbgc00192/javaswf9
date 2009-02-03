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
package swf9.types;

public class STRING {

	public String String;
	public String StringEnd;
	
	public STRING() {
	}
	
	public STRING(String string) {
		String = string;
	}
	
	public byte[] toByteArray() {
		// TODO
		return null;
	}
}
