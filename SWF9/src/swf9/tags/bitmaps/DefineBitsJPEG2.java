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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import swf9.util.*;
import swf9.tags.bitmaps.DefineBits;

public class DefineBitsJPEG2 extends DefineBits {

	public DefineBitsJPEG2() {
		type 	= DefineBitsJPEG2;
	}
	
	public DefineBitsJPEG2(byte[] swf, int offset) {
			
		type 	= DefineBits;
		length	= getTagLength(swf,offset);
		
		offset += 6;
		getCharacterID(swf,offset);
		offset += 2;
		getJPEGData(swf,offset);
		
	}
	
	public DefineBitsJPEG2(File file, int id) {
		
		type 		= DefineBitsJPEG2;
		length 		= 0;
		
		CharacterID = id;
		
		importImage(file);
			
	}
	
	public int getCharacterID(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,bytes.length);
		CharacterID = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return CharacterID;
	}
	
	public byte[] getJPEGData(byte[] swf, int offset) {
		JPEGData = new byte[length-2];
		System.arraycopy(swf,offset,JPEGData,0,JPEGData.length);
		return JPEGData;
	}
	
	public void exportImage(String file) {
		exportImage(new File(file));
	}
	
	public void importImage(File file) {
		
		try {	
			BufferedImage buf = ImageIO.read(file);
			width 	= buf.getWidth();
			height 	= buf.getHeight();
			buf 	= null;
			
			JPEGData = new byte[(int)file.length()];
	        FileInputStream fis = new FileInputStream(file);
	        fis.read(JPEGData);
	        
	        // set length
			length = 8 + JPEGData.length;
			
		} catch (IOException e) {
		}
	}

	public void exportImage(File file) {
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			// TODO faster check?
			if(JPEGData[0]==(byte)0xFF && JPEGData[1]==(byte)0xD9 && JPEGData[2]==(byte)0xFF && JPEGData[3]==(byte)0xD8)
				fos.write(ArrayUtils.strip(JPEGData,4));
			else
				fos.write(JPEGData);		
			fos.close();
			fos = null;
		} catch (IOException e) {
		}
		
	}
	
	public byte[] toByteArray() {
		
		byte[] bytes = new byte[length];
		
		bytes = writeHeader(bytes,type,length-6);
		
		int offset = 6;
		
		System.arraycopy(ByteUtils.intToByte(CharacterID,2,true),0,bytes,offset,2);
		offset += 2;
		System.arraycopy(JPEGData,0,bytes,offset,JPEGData.length);
		
		return bytes;
	}

}
