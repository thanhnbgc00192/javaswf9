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
import swf9.tags.bitmaps.DefineBitsJPEG2;

public class DefineBitsJPEG3 extends DefineBitsJPEG2 {

	public int 		AlphaDataOffset;
	public byte[] 	BitmapAlphaData;
	
	public DefineBitsJPEG3() {
		type 	= DefineBitsJPEG3;
	}
	
	public DefineBitsJPEG3(byte[] swf, int offset) {
			
		type 	= DefineBitsJPEG3;
		getTagLength(swf,offset);
		
		offset += 6;
		getCharacterID(swf,offset);
		offset += 2;
		getAlphaDataOffset(swf,offset);
		offset += 4;
		getJPEGData(swf,offset);
		offset += JPEGData.length;
		getBitmapAlphaData(swf,offset);
		
	}
	
	public int getAlphaDataOffset(byte[] swf, int offset) {
		byte[] bytes = new byte[4];
		System.arraycopy(swf,offset,bytes,0,bytes.length);
		AlphaDataOffset = (int)ByteUtils.byteArrayToLong(ArrayUtils.swapArray(bytes));
		return AlphaDataOffset;
	}
	
	public byte[] getJPEGData(byte[] swf, int offset) {
		JPEGData = new byte[AlphaDataOffset];
		System.arraycopy(swf,offset,JPEGData,0,JPEGData.length);
		return JPEGData;
	}
	
	public byte[] getBitmapAlphaData(byte[] swf, int offset) {
		BitmapAlphaData = new byte[length-(12+AlphaDataOffset)];
		System.arraycopy(swf,offset,BitmapAlphaData,0,BitmapAlphaData.length);
		return BitmapAlphaData;
	}
	
	public void exportImage(File file) {
		
		// TODO export with alpha!!!
		try {
			FileOutputStream fos = new FileOutputStream(file);
			if(JPEGData[0]==(byte)0xFF && JPEGData[1]==(byte)0xD9 && JPEGData[2]==(byte)0xFF && JPEGData[3]==(byte)0xD8) {
				fos.write(ArrayUtils.strip(JPEGData, 4));
			} else {
				fos.write(JPEGData);
			}			
			fos.close();
			fos = null;
		} catch(IOException e) {
		}
	}
	
}
