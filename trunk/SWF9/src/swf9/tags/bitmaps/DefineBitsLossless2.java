package swf9.tags.bitmaps;
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

import java.io.File;

import swf9.tags.bitmaps.DefineBitsLossless;

public class DefineBitsLossless2 extends DefineBitsLossless {

	//TODO test
	byte[] check;
	
	public DefineBitsLossless2() {
		
		type 		= DefineBitsLossless2;
		length		= 0;
				
	}
	
	public DefineBitsLossless2(File file, int id) {
		
		type 		= DefineBitsLossless2;
		length 		= 0;
		
		CharacterID = id;
		
		importImage(file);
		
		check = toByteArray();
		
	}
	
	public DefineBitsLossless2(byte[] swf, int offset) {
		
		type 		= DefineBitsLossless2;
		length 		= getTagLength(swf,offset);
		// TODO
		
	}
	
}
