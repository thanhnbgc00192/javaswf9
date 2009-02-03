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

public class MATRIX {

	public int 		HasScale;
	public int 		NScaleBits;
	public float	ScaleX;
	public float 	ScaleY;
	public int 		HasRotate;
	public int 		NRotateBits;
	public float 	RotateSkew0;
	public float 	RotateSkew1;
	public int 		NTranslateBits;
	public int 		TranslateX;
	public int 		TranslateY;
	
	//useful
	public int length;
	
	
	public MATRIX() {
		HasScale 		= 0;
		HasRotate		= 0;
		NTranslateBits	= 0;
		
		length 			= 1;
	}
	
	public MATRIX(byte[] swf, int offset) {
		
		HasScale = (swf[offset]>>7)&0x01;
		
		PackedBitObj pbo = new PackedBitObj();
		
		if(HasScale==1) {
			NScaleBits = (swf[offset]>>2)&0x1F;
			pbo = PackedBitObj.readPackedBits(swf,offset,6,NScaleBits);
			ScaleX = pbo.value;
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,NScaleBits);
			ScaleY = pbo.value;
		}
		
		pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,1);
		HasRotate = pbo.value;
		
		if(HasRotate==1) {
			// TODO with float
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,5);
			NRotateBits = pbo.value;
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,NRotateBits);
			RotateSkew0 = pbo.value;
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,NRotateBits);
			RotateSkew1 = pbo.value;
			
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,5);
			NTranslateBits = pbo.value;
			if(NTranslateBits!=0) {
				pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,NTranslateBits);
				TranslateX = pbo.value;
				pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,NTranslateBits);
				TranslateY = pbo.value;
			}
		} else {
			pbo = PackedBitObj.readPackedBits(swf,pbo.nextByteIndex,pbo.nextBitIndex,5);
			NTranslateBits = pbo.value;
			if(NTranslateBits!=0) {
				pbo = PackedBitObj.readPackedBitsS(swf,pbo.nextByteIndex,pbo.nextBitIndex,NTranslateBits);
				TranslateX = pbo.value;
				pbo = PackedBitObj.readPackedBitsS(swf,pbo.nextByteIndex,pbo.nextBitIndex,NTranslateBits);
				TranslateY = pbo.value;
			}
		}
				
		length = pbo.nextByteIndex+1 - offset;
	}
	
	public MATRIX(int scale, float sx, float sy, int rotate, int rs0, int rs1, int tx, int ty) {
		
		length = 1;
		
		HasScale 	= scale;
		HasRotate	= rotate;
		
		if(HasScale==1) { 
			ScaleX = sx;
			ScaleY = sy;
			setNScaleBits(ScaleX,ScaleY);
			length = 7;
		}
		if(HasRotate==1) {
			//TODO
			RotateSkew0 = rs0;
			RotateSkew1 = rs1;
		}
				
	}
	
	public void setNScaleBits(float sx, float sy) {
		// TODO not sure of this! check <0!?!?
		NScaleBits = 0;
		int[] scale = {(int)sx,(int)sy};
		for(int i=0; i<scale.length; i++) {
			NScaleBits = Math.max(NScaleBits,Integer.toBinaryString(scale[i]).length()+17);	
		}
	}
	
	public void setNRotateBits(float sx, float sy) {
		// TODO check this
		NRotateBits = 0;
		int[] rotate = {(int)sx,(int)sy};
		for(int i=0; i<rotate.length; i++) {
			NRotateBits = Math.max(NRotateBits,Integer.toBinaryString(rotate[i]).length()+17);	
		}
	}
	
	public void setNTranslateBits(int sx, int sy) {
		// TODO in twips
		NTranslateBits = 0;
		int[] scale = {sx,sy};
		for(int i=0; i<scale.length; i++) {
			NTranslateBits = Math.max(NTranslateBits,Integer.toBinaryString(scale[i]).length()+1);	
		}
	}
	
	public byte[] toByteArray() {
		// TODO
		byte[] bytes = {(byte)0xD9,0x40,0x00,0x05,0x00,0x00,0x00};
		if(HasScale==1) {
			// fixed to scaling 20.0
			//System.arraycopy(PackedBitObj.writePackedBits({ScaleX,ScaleY});
		} else {
			bytes = new byte[1];
			bytes[0] = 0;
		}
		return bytes;
	}
	
}
