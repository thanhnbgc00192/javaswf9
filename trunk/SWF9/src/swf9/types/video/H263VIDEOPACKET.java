package swf9.types.video;
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

public class H263VIDEOPACKET extends VIDEOPACKET {

	public int PictureStartCode;
	public int Version;
	public int TemporalReference;
	public int PictureSize;
	public int CustomWidth;
	public int CustomHeight;
	public int PictureType;
	public int DeblockingFlag;
	public int Quantizer;
	public int ExtraInformationFlag;
	public int ExtraInformation;
	public MACROBLOCK Macroblock;
	public int PictureStuffing;
	
	public H263VIDEOPACKET() {
		
	}
	
	public H263VIDEOPACKET(byte[] swf, int offset) {
		
		PackedBitObj pbo = new PackedBitObj();
		
		pbo = PackedBitObj.readPackedBits(swf,offset,0,17);
		PictureStartCode = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,5);
		Version = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,8);
		TemporalReference = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,3);
		PictureSize = pbo.value;
		if(PictureSize==0) {
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,8);
			CustomWidth = pbo.value;
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,8);
			CustomHeight = pbo.value;
		} else if(PictureSize==1) {
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,16);
			CustomWidth = pbo.value;
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,16);
			CustomHeight = pbo.value;
		}
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,2);
		PictureType = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,1);
		DeblockingFlag = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,5);
		Quantizer = pbo.value;
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,1);
		ExtraInformationFlag = pbo.value;
		if(ExtraInformationFlag==1) {
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,8);
			ExtraInformation = pbo.value;
		}
		
		// TODO macroblock
		
	}
	
	public void exportImage(File file) {
		
	}
	
}
