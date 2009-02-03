package swf9.types;
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


public class LINESTYLE2 extends LINESTYLE {

	public LINESTYLE2() {
		
	}

	public LINESTYLE2(byte[] data, int offset, Object type) {
		
		Width = getWidth(data, offset);
		length+=2;
		Color = getColor(data, offset+length, type);
		if(((Tag)type).type == Tag.DefineShape3)
			length += 4; 
		else
			length += 3;				
	}
	
}
