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

import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import swf9.util.ByteUtils;
import swf9.tags.Tag;

public class DefineBitsLossless extends Tag {
	
	public int 		CharacterID;
	public int 		BitmapFormat;
	public int 		BitmapWidth;
	public int 		BitmapHeight;
	public int 		BitmapColorTableSize;
	public byte[] 	ZlibBitmapData;
	
	public DefineBitsLossless() {
		
		type 		= DefineBitsLossless;
		length		= 0;
				
	}
	
	public DefineBitsLossless(byte[] swf, int offset) {
		
		type 		= DefineBitsLossless;
		length 		= getTagLength(swf,offset);
		
		//byte[] tmp
		//CharacterID = swf[offset+.....] //TODO: if type!=3: 13, else 14 
		ZlibBitmapData = new byte[length-13];
		System.arraycopy(swf,offset+13,ZlibBitmapData,0,ZlibBitmapData.length);
		                                            
	}
	
	public DefineBitsLossless(File file, int id) {
		
		type 		= DefineBitsLossless;
		length 		= 0;
		
		CharacterID = id;
		
		importImage(file);
			
	}
	
	public void importImage(File file) {
		
		int[] rgb;
		
		try {	
	        BufferedImage image = ImageIO.read(file);
	        BitmapWidth 		= image.getWidth();
	        BitmapHeight 		= image.getHeight();
	        // TODO : get BitmapFormat
	        BitmapFormat		= 5;
	        
	        rgb 				= new int[BitmapWidth*BitmapHeight];
	        image.getRGB(0,0,BitmapWidth,BitmapHeight,rgb,0,BitmapWidth);
	        
	        // convert to bytes
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			byte[] tmp;
			for(int i=0; i<rgb.length; i++) {
				tmp = ByteUtils.intToByte(rgb[i],4,false);  
				// force alpha: tmp[0] &= 0x0F;
				data.write(tmp);
			}
			
			// compress       
			ZlibBitmapData = deflate(data.toByteArray());
			
			// set length
			length = 13 + ZlibBitmapData.length;
			if(BitmapFormat==3) length++;
						
		} catch (IOException e) {
		}
	}
	
	public void exportImage(File file) {
		
		// TODO: DEFINE TYPE.... type!!!!
		BufferedImage buf = new BufferedImage(320,240,BufferedImage.TYPE_INT_ARGB);
		
		// convert
		byte[] inArray = inflate(ZlibBitmapData);
		int[] pixels = new int[inArray.length/4];
		
		for(int j=0,k=0; j<inArray.length-4; j+=4,k++) {
			byte[] tmp = new byte[4];
			System.arraycopy( inArray, j, tmp, 0, 4);                     
			pixels[k] = (int)ByteUtils.byteArrayToLong(tmp);
		}
		buf.setRGB(0, 0, 320, 240, pixels, 0, 320);
		
		// write
		try {
			ImageIO.write(buf, "png", file);
			buf = null;
		} catch (IOException e) {
		}
	}
	
	public byte[] toByteArray() {
		
		byte[] bytes = new byte[length];
		
		bytes = writeHeader(bytes,type,length-6);
		
		int offset = 6;
		
		System.arraycopy(ByteUtils.intToByte(CharacterID,2,true),0,bytes,offset,2);
		bytes[offset+2] = (byte)BitmapFormat;
		System.arraycopy(ByteUtils.intToByte(BitmapWidth,2,true),0,bytes,offset+3,2);
		System.arraycopy(ByteUtils.intToByte(BitmapHeight,2,true),0,bytes,offset+5,2);
		
		//TODO
		//if(BitmapFormat==3)
		//	System.arraycopy(Util.swapArray(Util.intToByteArray(BitmapColorTableSize)),0,bytes,offset+5,2);
		System.arraycopy(ZlibBitmapData,0,bytes,offset+7,ZlibBitmapData.length);
		
		return bytes;
	}
	
}
