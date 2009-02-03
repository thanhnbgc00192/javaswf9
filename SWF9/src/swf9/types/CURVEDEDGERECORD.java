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


public class CURVEDEDGERECORD extends SHAPERECORD {
	
	public int NumBits;
	public int ControlDeltaX;
	public int ControlDeltaY;
	public int AnchorDeltaX;
	public int AnchorDeltaY;
	
	public CURVEDEDGERECORD() {
		
	}
	
	public CURVEDEDGERECORD(byte[] data, int offset, int offsetBit) {
		
		// flags
		PackedBitObj pboTypeFlag 		= PackedBitObj.readPackedBits(data, offset, offsetBit, 1);
		TypeFlag 						= pboTypeFlag.value;	
		PackedBitObj pboStraightFlag	= PackedBitObj.readPackedBits(data, pboTypeFlag.nextByteIndex, pboTypeFlag.nextBitIndex, 1);
		StraightFlag 					= pboStraightFlag.value;
		PackedBitObj pboNumBits		    = PackedBitObj.readPackedBits(data, pboStraightFlag.nextByteIndex, pboStraightFlag.nextBitIndex, 4);
		NumBits		 					= pboNumBits.value;
		PackedBitObj pboControlDeltaX	= PackedBitObj.readPackedBitsS(data, pboNumBits.nextByteIndex, pboNumBits.nextBitIndex, NumBits+2);
		ControlDeltaX 					= pboControlDeltaX.value;
		PackedBitObj pboControlDeltaY	= PackedBitObj.readPackedBitsS(data, pboControlDeltaX.nextByteIndex, pboControlDeltaX.nextBitIndex, NumBits+2);
		ControlDeltaY 					= pboControlDeltaY.value;
		PackedBitObj pboAnchorDeltaX	= PackedBitObj.readPackedBitsS(data, pboControlDeltaY.nextByteIndex, pboControlDeltaY.nextBitIndex, NumBits+2);
		AnchorDeltaX 					= pboAnchorDeltaX.value;
		PackedBitObj pboAnchorDeltaY	= PackedBitObj.readPackedBitsS(data, pboAnchorDeltaX.nextByteIndex, pboAnchorDeltaX.nextBitIndex, NumBits+2);
		AnchorDeltaY 					= pboAnchorDeltaY.value;
		
		nextByte 	= pboAnchorDeltaY.nextByteIndex;
		nextBit 	= pboAnchorDeltaY.nextBitIndex;
				
	}
	
	public byte[] toByteArray() {
		//TODO
		PackedBitObj pbo = new PackedBitObj();
		pbo.writePackedBits(1,15 );
		return pbo.toByteArray();
	}
	
}
