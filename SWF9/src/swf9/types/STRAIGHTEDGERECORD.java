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

public class STRAIGHTEDGERECORD extends SHAPERECORD {

	// flags
	public int NumBits;
	public int GeneralLineFlag;
	public int VertLineFlag;
	public int DeltaX;
	public int DeltaY;
		
	public STRAIGHTEDGERECORD() {
		
	}
	
	public STRAIGHTEDGERECORD( int dx, int dy) {
		
		TypeFlag 		= 1;
		StraightFlag	= 1;
		
		if(dx!=0) {
			GeneralLineFlag = 0;
			VertLineFlag	= 0;
		} else if(dy!=0) {
			GeneralLineFlag = 0;
			VertLineFlag	= 1;
		} else {
			GeneralLineFlag = 1;
			VertLineFlag	= 0;
		}
		DeltaX = dx;
		DeltaY = dy;
		
		setNumBits(dx,dy);
		
	}
	
	public STRAIGHTEDGERECORD(byte[] data, int offset, int offsetBit) {
		
		// flags
		PackedBitObj pboTypeFlag 		= PackedBitObj.readPackedBits(data, offset, offsetBit, 1);
		TypeFlag 						= pboTypeFlag.value;	
		PackedBitObj pboStraightFlag	= PackedBitObj.readPackedBits(data, pboTypeFlag.nextByteIndex, pboTypeFlag.nextBitIndex, 1);
		StraightFlag 					= pboStraightFlag.value;
		PackedBitObj pboNumBits		    = PackedBitObj.readPackedBits(data, pboStraightFlag.nextByteIndex, pboStraightFlag.nextBitIndex, 4);
		NumBits		 					= pboNumBits.value;
		PackedBitObj pboGeneralLineFlag = PackedBitObj.readPackedBits(data, pboNumBits.nextByteIndex, pboNumBits.nextBitIndex, 1);
		GeneralLineFlag 				= pboGeneralLineFlag.value;
		
		nextByte	= pboGeneralLineFlag.nextByteIndex;
		nextBit		= pboGeneralLineFlag.nextBitIndex;
		
		
		if(GeneralLineFlag==0) {
			
			PackedBitObj pboVertLineFlag 	= PackedBitObj.readPackedBits(data, nextByte, nextBit, 1);
			VertLineFlag 					= pboVertLineFlag.value;
			
			nextByte	= pboVertLineFlag.nextByteIndex;
			nextBit 	= pboVertLineFlag.nextBitIndex;
			
		}
		if(GeneralLineFlag==1 || VertLineFlag==0) {
			
			PackedBitObj pboDeltaX 	= PackedBitObj.readPackedBitsS(data, nextByte, nextBit, NumBits+2);
			DeltaX		 			= pboDeltaX.value;
			
			nextByte	= pboDeltaX.nextByteIndex;
			nextBit 	= pboDeltaX.nextBitIndex;
			
		}
		if(GeneralLineFlag==1 || VertLineFlag==1) {
			
			PackedBitObj pboDeltaY 	= PackedBitObj.readPackedBitsS(data, nextByte, nextBit, NumBits+2);
			DeltaY		 			= pboDeltaY.value;
			
			nextByte	= pboDeltaY.nextByteIndex;
			nextBit 	= pboDeltaY.nextBitIndex;
			
		}
		
	}
	
	public void setNumBits(int dx, int dy) {
		NumBits = 0;
		int[] rect = {dx,dy};
		for(int i=0; i<rect.length; i++) {
			// TODO problema del segno!!!!!!
			NumBits = Math.max(NumBits,Integer.toBinaryString(Math.abs(rect[i])).length()-1);
			//NumBits = Math.max(NumBits,Integer.toBinaryString(rect[i]).length()-1);	
		}
	}
	
	public byte[] toByteArray() {
		//TODO
		//PackedBitObj pbo = new PackedBitObj();
		//pbo.writePackedBits(DeltaX,NumBits);
		//pbo.writePackedBits(DeltaY,NumBits);
		return null;//pbo.toByteArray();
	}
	
	public PackedBitObj toByteArray(PackedBitObj pbo) {
		
		pbo.writePackedBits(TypeFlag,1);
		pbo.writePackedBits(StraightFlag,1);
		pbo.writePackedBits(NumBits,4);
		pbo.writePackedBits(GeneralLineFlag,1);
		if(GeneralLineFlag==0)
			pbo.writePackedBits(VertLineFlag,1);
		if(GeneralLineFlag==1 || VertLineFlag==0)
			pbo.writePackedBits(DeltaX,NumBits+2);
		if(GeneralLineFlag==1 || VertLineFlag==1)
			pbo.writePackedBits(DeltaY,NumBits+2);
		
		return pbo;
	}
	
}
