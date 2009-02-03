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

import swf9.tags.Tag;

public class JPEGTables extends Tag {

	public byte[] JPEGData;
	
	public JPEGTables() {
		
	}
	
	public JPEGTables(byte[] swf, int offset) {
		
		type 	= JPEGTables;
		length	= getTagLength(swf,offset);
		
		if(RecordHeader==RECORDHEADER_LONG) {
			JPEGData = new byte[length-6];
			System.arraycopy(swf,offset+6,JPEGData,0,JPEGData.length);
		} else {
			JPEGData = new byte[length-2];
			System.arraycopy(swf,offset+2,JPEGData,0,JPEGData.length);
		}
		
	}
	
	public JPEGTables(File file) {
		// TODO getTable from an image file 		
	}
	
	public byte[] toByteArray() {
		return null;
	}
	
}
