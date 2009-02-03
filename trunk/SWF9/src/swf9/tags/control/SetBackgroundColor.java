package swf9.tags.control;
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

import swf9.tags.Tag;


public class SetBackgroundColor extends Tag {

	public int r;
	public int g;
	public int b;
	
	public SetBackgroundColor() {
		
		type 	= Tag.SetBackgroundColor;
		length 	= 5;
		
		r = 0xFF;
		g = 0xFF;
		b = 0xFF;
		
	}
	
	public SetBackgroundColor(byte[] swf, int offset) {
		
		type 	= Tag.SetBackgroundColor;
		length 	= getTagLength(swf,offset);
		
		r = swf[offset+2]&0xFF;
		g = swf[offset+3]&0xFF;
		b = swf[offset+4]&0xFF;
				
	}
	
	public void setColor(Color rgb) {
		
		r = rgb.getRed();
		g = rgb.getGreen();
		b = rgb.getBlue();
		
	}
	
	public Color getColor() {
		return new Color(r,g,b);
	}
	
	public byte[] toByteArray() {
		
		byte[] bytes = new byte[length];
		bytes = writeHeader(bytes,type,length-2);
		bytes[2] = (byte)r;
		bytes[3] = (byte)g;
		bytes[4] = (byte)b;
		
		return bytes;
	}
	
}
