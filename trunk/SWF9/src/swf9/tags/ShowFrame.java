package swf9.tags;
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

import swf9.tags.Tag;

public class ShowFrame extends Tag {
	
	public ShowFrame() {
		
		type 	= ShowFrame;
		length 	= 2;
		
	}
	
	public ShowFrame(byte[] swf, int offset) {
		
		type 	= ShowFrame;
		getTagLength(swf,offset);
		
	}
	
	public byte[] toByteArray() {
		byte[] bytes = new byte[2];
		bytes = writeHeader(bytes,type,length-2);
		
		return bytes;
	}
	
}
