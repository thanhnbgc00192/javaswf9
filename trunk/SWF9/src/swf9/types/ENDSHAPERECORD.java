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

import swf9.util.PackedBitObj;

public class ENDSHAPERECORD extends SHAPERECORD {

	public int EndOfShape;
	
	public ENDSHAPERECORD() {
		TypeFlag 	= 0;
		Flags5 		= 0;
	}
	
	public byte[] toByteArray() {
		byte[] bytes = {0};
		return bytes;
	}
	
	public PackedBitObj toByteArray(PackedBitObj pbo) {
		pbo.writePackedBits(0,6);
		return pbo;
	}
	
}
