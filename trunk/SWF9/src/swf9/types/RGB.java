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

import java.awt.Color;

public class RGB {
	
	public byte Red;
	public byte Green;
	public byte Blue;
	
	public RGB() {
		
	}
	
	public RGB(byte[] rgb) {
		
		Red 	= rgb[0];
		Green 	= rgb[1];
		Blue 	= rgb[2];
		
	}
	
	public byte[] toByteArray() {
		return new byte[]{Red,Green,Blue};
	}
	
	public Color toColor() {
		return new Color(((int)Red)&0xFF,((int)Green)&0xFF,((int)Blue)&0xFF);
	}
}
