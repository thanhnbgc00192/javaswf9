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
import java.io.FileOutputStream;
import java.io.IOException;

import swf9.util.*;
import swf9.tags.Tag;
import swf9.tags.bitmaps.JPEGTables;

public class DefineBits extends Tag {

	public int 		CharacterID;
	public byte[] 	JPEGData;
	
	// useful
	public int width;
	public int height;
	
	public DefineBits() {
		type 	= DefineBits;
	}
	
	public DefineBits(byte[] swf, int offset) {
			
		type 	= DefineBits;
		length	= getTagLength(swf,offset);
		
		offset += 6;
		getCharacterID(swf,offset);
		offset += 2;
		getJPEGData(swf,offset);
				
	}
	
	public int getCharacterID(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,bytes.length);
		CharacterID = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return CharacterID;
	}
	
	public byte[] getJPEGData(byte[] swf, int offset) {
		JPEGData = new byte[length-8];
		System.arraycopy(swf,offset,JPEGData,0,JPEGData.length);
		return JPEGData;
	}
	
	public void exportImage(String file) {
		exportImage(new File(file));
	}
		
	public void exportImage(File file, JPEGTables table) {
				
		byte[] SOF = {(byte)0xFF,(byte)0xDA};
		int tagSOF = 0;
		
		for(int j=0; j<JPEGData.length; j++) {
			if(JPEGData[j]==SOF[0] && JPEGData[j+1]==SOF[1] && tagSOF==0) {
				tagSOF = j;
				break;
			}
		}
		
		byte[] bytes = new byte[ table.JPEGData.length + JPEGData.length ];
		System.arraycopy( JPEGData,0,bytes,0,tagSOF);
		System.arraycopy( table.JPEGData,2,bytes,tagSOF,table.JPEGData.length-4);
		System.arraycopy( JPEGData,tagSOF,bytes,tagSOF+table.JPEGData.length-4, JPEGData.length-tagSOF);
		
		// write output file
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
			fos = null;
		} catch (IOException e) {
		}
	}
}
