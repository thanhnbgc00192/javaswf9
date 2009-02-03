package swf9.tags.video;
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

import swf9.util.*;
import swf9.tags.Tag;

public class DefineVideoStream extends Tag {

	public int CharacterID;
	public int NumFrames;
	public int Width;
	public int Height;
	public int VideoFlagReserved;
	public int VideoFlagDeblocking;
	public int VideoFlagSmoothing;
	public int CodecID;
	
	
	public DefineVideoStream() {
		type = DefineVideoStream;		
	}
	
	public DefineVideoStream(byte[] swf, int offset) {
		
		type = DefineVideoStream;
		getTagLength(swf,offset);
		
		if(RecordHeader==RECORDHEADER_SHORT) 	offset += 2;
		else									offset += 6;
		
		getCharacterID(swf,offset);
		offset += 2;
		getNumFrames(swf,offset);
		offset += 2;
		getWidth(swf,offset);
		offset += 2;
		getHeight(swf,offset);
		offset += 2;
		
		VideoFlagReserved 	= (swf[offset]>>4)&0x0F;
		VideoFlagDeblocking = (swf[offset]>>1)&0x07;
		VideoFlagSmoothing 	= swf[offset]&0x01;
		offset += 1;
		
		CodecID = swf[offset];
		
	}
	
	public int getCharacterID(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		CharacterID = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return CharacterID;
	}
	
	public int getNumFrames(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		NumFrames = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return NumFrames;
	}
	
	public int getWidth(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		Width = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return Width;
	}
	
	public int getHeight(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		Height = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return Height;
	}
	
	public void exportImage(File file) {
		
	}
	
	public byte[] toByteArray() {
		return null;
	}
}
