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

import swf9.tags.*;

public class LINESTYLE {

	public int 		Width;
	public Color 	Color;
	
	public int length = 0;
	
	public LINESTYLE() {
		length = 0;
	}
	
	public LINESTYLE(byte[] data, int offset, Object type) {
		
		Width = getWidth(data, offset);
		length+=2;
		Color = getColor(data, offset+length, type);
		if(((Tag)type).type == Tag.DefineShape3)
			length += 4; 
		else
			length += 3; 
						
	}
	
	public int getWidth(byte[] data, int offset) {
		return data[offset];
	}
	
	public Color getColor(byte[] data, int offset, Object type) {
		if(((Tag)type).type == Tag.DefineShape3)
			return new Color((int)data[offset]&0xFF,(int)data[offset+1]&0xFF,(int)data[offset+2]&0xFF,(int)data[offset+3]&0xFF);
		else
			return new Color((int)data[offset]&0xFF,(int)data[offset+1]&0xFF,(int)data[offset+2]&0xFF);
		
	}
	
	public byte[] toByteArray() {
		byte[] bytes = new byte[length];
		// TODO
		return bytes;
	}
			
}
