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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import swf9.SWF9Compression;
import swf9.util.*;

import swf9.types.*;
import swf9.types.data.*;
import swf9.util.PackedBitObj;


public class Header extends SWF9Compression {
	
	public String 	Signature;
	public byte		Version;
	public int		FileLength;
	public RECT		FrameSize;
	public int		FrameRate;
	public int		FrameCount;
	
	public static String SWF_NOT_COMPRESSED = "F";
	public static String SWF_COMPRESSED 	= "C";
	
	// useful (tag length)
	public int 		length;
	
	public Header() {
		
	}
	
	public Header(byte[] data) {
		
	}
	
	public Header(File file) {

		byte[] tmp 	= new byte[getSize(file)];
		byte[] swf 	= null;

		try {
			FileInputStream fis = new FileInputStream( file );
			fis.read(tmp);
			fis.close();
		} catch ( IOException e ) {
			System.err.println( e );
		}
		
		// signature
		Signature = "" + (char)tmp[0] + (char)tmp[1] + (char)tmp[2];
		if(!isSWF())
			System.out.println( "File is not a SWF." );
        if(isCompressed())
           swf = SWF9Compression.uncompress(tmp);
        else
           swf = tmp;

		// version
		Version = swf[3];

		// file length
		FileLength = getSize(swf);

		// stage dimension
		PackedBitObj pbo;
		FrameSize = new RECT();
		
		FrameSize.Nbits = ((swf[8]&0xff)>>3);
		pbo = PackedBitObj.readPackedBits(swf, 8, 5, FrameSize.Nbits);
		FrameSize.Xmin = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf, pbo.nextByteIndex, pbo.nextBitIndex, FrameSize.Nbits);
		FrameSize.Xmax = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf, pbo.nextByteIndex, pbo.nextBitIndex, FrameSize.Nbits);
		FrameSize.Ymin = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf, pbo.nextByteIndex, pbo.nextBitIndex, FrameSize.Nbits);
		FrameSize.Ymax = pbo.value;
		
		int bytePointer = pbo.nextByteIndex + 2;

		// frame rate
		FrameRate = swf[bytePointer];
		bytePointer++;

		// number of frames
		int fc1 = swf[bytePointer] & 0xFF;
		bytePointer++;
		int fc2 = swf[bytePointer] & 0xFF;
		bytePointer++;
		FrameCount = ( fc2 << 8 ) + fc1;

		// store "full header" length
		length = bytePointer;
		      		              
	}
	
	public Header(String sig, int ver, int width, int height, int fps, int frames) {
		
		Signature 	= sig + "WS";
		Version		= (byte)ver;
		FrameSize 	= new RECT(width,height);
		FrameRate 	= fps;
		FrameCount 	= frames;
		
		length 		= 12 + FrameSize.length;
	}
	
	public void setFileLength(int fileLength) {
		FileLength = fileLength;
	}
	
	public boolean isSWF() {
		if(Signature.equals("FWS") || Signature.equals("CWS"))
			return true;
		return false;
	}
	
	public boolean isCompressed() {
		if (Signature.equals("CWS"))
			return true;
	    return false;
	}
	
	public int getSize(File file) {
		byte[] tmp 	= new byte[8];
		byte[] size = new byte[4];
		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(tmp);
			fis.close();
			
			System.arraycopy(tmp,4,size,0,4);
		} catch( IOException e ) {
			System.err.println( e );
		}
		
		return (int)ByteUtils.byteArrayToLong(ArrayUtils.swapArray(size));
	}
	
	public int getSize(byte[] data) {
		byte[] size = new byte[4];
		
		System.arraycopy(data,4,size,0,4);
				
		return (int)ByteUtils.byteArrayToLong(ArrayUtils.swapArray(size));
	}
	
	public byte[] toByteArray() {
		byte[] bytes = new byte[length];
		
		bytes[3] = Version;
		System.arraycopy(Signature.getBytes(),0,bytes,0,3);
		
		int offset = 4;
		System.arraycopy(ByteUtils.intToByte(FileLength,4,true),0,bytes,offset,4);
		offset += 4;
		System.arraycopy(FrameSize.toByteArray(),0,bytes,offset,FrameSize.length);
		offset += FrameSize.length;
		// TODO fixed point numbers 8.8, 16.16
		byte[] fps = new FIXED8((int)FrameRate).toByteArray();//{0x00,0x19}; //this is 25fps
		System.arraycopy(fps,0,bytes,offset,2);
		offset += 2;
		System.arraycopy(ByteUtils.intToByte(FrameCount,2,true),0,bytes,offset,2);
		
		return bytes;
	}
	
}
