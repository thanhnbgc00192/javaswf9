package swf9.types;
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

import swf9.util.PackedBitObj;

public class SHAPERECORD {

	public int TypeFlag;
	public int Flags5;
	public int StraightFlag;
	
	// mixed 
	public int FillBits;
	public int LineBits;
	
	// useful
	public int nextByte;
	public int nextBit;
	public int length;
	
	public SHAPERECORD() {
		
	}
	
	public SHAPERECORD(byte[] data, int offset, int offsetBit) {
		
		PackedBitObj pboTypeFlag = PackedBitObj.readPackedBits(data, offset, offsetBit, 1);
		TypeFlag 				 = pboTypeFlag.value;	
		
		nextByte 	= pboTypeFlag.nextByteIndex;
		nextBit		= pboTypeFlag.nextBitIndex;
		
		// edge
		if(isEdgeShapeRecord()) {
			PackedBitObj pboStraightFlag = PackedBitObj.readPackedBits(data, nextByte, nextBit, 1);
			StraightFlag 				 = pboStraightFlag.value;
		// non edge
		} else {
			PackedBitObj pboFlags5 	= PackedBitObj.readPackedBits(data, nextByte, nextBit, 5);
			Flags5 					= pboFlags5.value;
		}
		
	}
	
	public boolean isEdgeShapeRecord() {
		return TypeFlag==1;
	}
	
	public boolean isStyleChangeRecord() {
		return Flags5!=0;
	}
	
	public boolean isEndShapeRecord() {
		return TypeFlag==0 && Flags5==0;
	}
	
	public boolean isStraightEdgeRecord() {
		return StraightFlag==1;
	}
	
	public boolean isCurvedEdgeRecord() {
		return StraightFlag==0;
	}
	
	public byte[] toByteArray() {
		// must override 
		return null;
	}
	
	public PackedBitObj toByteArray(PackedBitObj pbo) {
		// must override 
		return null;
	}
	
}
