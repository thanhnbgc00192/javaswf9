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

import swf9.tags.PlaceObject2;
import swf9.types.*;

public class PlaceObject3 extends PlaceObject2 {

	public int 	PlaceFlagHasImage;
	public int 	PlaceFlagHasClassName;
	public int 	PlaceFlagHasCacheAsBitmap;
	public int 	PlaceFlagHasBlendMode;
	public int 	PlaceFlagHasFilterList;
	
	public String 		ClassName;
	public FILTERLIST	SurfaceFilterList;
	public int			BlendMode;
	
	public PlaceObject3() {
		type = PlaceObject3;
	}
	
	public PlaceObject3(byte[] swf, int offset) {
		type = PlaceObject3;
		getTagLength(swf,offset);
	}
	
	public byte[] toByteArray() {
		// TODO to bytes
		return null;
	}
}
