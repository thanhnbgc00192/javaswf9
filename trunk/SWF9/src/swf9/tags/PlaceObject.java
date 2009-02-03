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
import swf9.types.*;

public class PlaceObject extends Tag {

	public int 		CharacterID;
	public int 		Depth;
	public MATRIX	Matrix;
	public CXFORM 	ColorTransform;
	
	public PlaceObject() {
		
	}
	
	public PlaceObject(byte[] swf, int i) {
		
	}
	
	public PlaceObject(int id, int depth) {
		
		CharacterID = id;
		Depth 		= depth;
		
	}
	
	public byte[] toByteArray() {
		// TODO
		byte[] bytes = new byte[0];
		return bytes;
	}
	
}
