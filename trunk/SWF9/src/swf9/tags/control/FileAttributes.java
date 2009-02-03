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

import swf9.tags.Tag;


public class FileAttributes extends Tag {
	
	public int HasMetadata;
	public int ActionScript3;
	public int UseNetwork;
	
	public FileAttributes() {
		HasMetadata		= 0;
		ActionScript3	= 0;
		UseNetwork		= 0;
		length 			= 6;
	}
	
	public FileAttributes(byte[] swf, int offset) {
		
		type 		= Tag.FileAttributes;
		length 		= getTagLength(swf,offset);
		
		HasMetadata 	= (swf[offset+2]>>4) & 0x01;
		ActionScript3	= (swf[offset+2]>>3) & 0x01;
		UseNetwork 		= (swf[offset+2]) & 0x01;
		
	}
	
	public FileAttributes(int mdata, int as3, int net) {
		
		HasMetadata	  	= mdata;
		ActionScript3 	= as3;
		UseNetwork		= net;
		
		type 		= Tag.FileAttributes;
		length 		= 6;
		
	}
	
	public byte[] toByteArray() {
		
		byte[] bytes = new byte[length];
		bytes 		= writeHeader(bytes,type,length-2);
		bytes[2] 	= 	(byte)(HasMetadata<<4);
		bytes[2] 	|=	(byte)(ActionScript3<<3);
		bytes[2] 	|=	(byte)(UseNetwork);
		
		return bytes;
		
	}

}
