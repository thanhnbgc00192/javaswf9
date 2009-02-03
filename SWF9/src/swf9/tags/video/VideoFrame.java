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
import swf9.tags.video.DefineVideoStream;
import swf9.types.video.*; 

public class VideoFrame extends Tag {

	public int 			StreamID;
	public int 			FrameNum;
	public VIDEOPACKET 	VideoData;
		
	public VideoFrame() {
		
		type = VideoFrame;		
		
	}
	
	public VideoFrame(byte[] swf, int offset, DefineVideoStream dvs) {
		
		type = VideoFrame;
		getTagLength(swf,offset);
		
		if(RecordHeader==RECORDHEADER_SHORT) 	offset += 2;
		else									offset += 6;
		
		getStreamID(swf,offset);
		offset += 2;
		getFrameNum(swf,offset);
		offset += 2;
		
		switch(dvs.CodecID) {
			case 2 :
				VideoData = new H263VIDEOPACKET(swf,offset);
				break;
			case 3 :
				VideoData = new SCREENVIDEOPACKET(swf,offset);
				break;
			case 4 :
				VideoData = new VP6SWFVIDEOPACKET(swf,offset);
				break;
			case 5 :
				VideoData = new VP6SWFALPHAVIDEOPACKET(swf,offset);
				break;
			case 6 :
				VideoData = new SCREENV2VIDEOPACKET(swf,offset);
		}
		
	}
	
	
	public int getStreamID(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		StreamID = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return StreamID;
	}
	
	public int getFrameNum(byte[] swf, int offset) {
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		FrameNum = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return FrameNum;
	}
		
	public void exportImage(File file) {
			
		
	}
	
	public byte[] toByteArray() {
		//TODO
		return null;
	}
	
}
