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

public class STYLECHANGERECORD extends SHAPERECORD {
	
	// flags
	public int StateNewStyles;
	public int StateLineStyle;
	public int StateFillStyle1;
	public int StateFillStyle0;
	public int StateMoveTo;
	
	public int MoveBits;
	public int MoveDeltaX;
	public int MoveDeltaY;
	
	public int FillStyle0;
	public int FillStyle1;
	public int LineStyle;
	public FILLSTYLEARRAY FillStyles;
	public LINESTYLEARRAY LineStyles;
	
	public int NumFillBits;
	public int NumLineBits;
		
	// masks
	public static int HasNewStyles 		= 0x40;
	public static int HasLineStyle 		= 0x20;
	public static int HasFillStyle1 	= 0x10;
	public static int HasFillStyle0 	= 0x08;
	public static int HasMoveTo 		= 0x04;
	
	
	public STYLECHANGERECORD() {
		
	}
	
	public STYLECHANGERECORD(	int flags, int mx, int my, int fs0, int fs1, int ls, FILLSTYLEARRAY fillStyles, LINESTYLEARRAY lineStyles,
								int fillBits, int lineBits) {
		TypeFlag 		= 0;
		if((flags&HasNewStyles)==HasNewStyles) {
			StateNewStyles	= 1;
			FillStyles 		= fillStyles;
			LineStyles 		= lineStyles;
		}
		if((flags&HasLineStyle)==HasLineStyle) {
			StateLineStyle	= 1;
			LineStyle 		= ls;
		}
		if((flags&HasFillStyle1)==HasFillStyle1) {
			StateFillStyle1	= 1;
			FillStyle1		= fs1;
		}
		if((flags&HasFillStyle0)==HasFillStyle0) {
			StateFillStyle0	= 1;
			FillStyle0		= fs0;
		}
		if((flags&HasMoveTo)==HasMoveTo) {
			StateMoveTo	= 1;
			setMoveBits(mx,my);
			MoveDeltaX 	= mx;
			MoveDeltaY 	= my;
		}
		
		FillBits = fillBits; 
		LineBits = lineBits;
		// TODO length!!!!!
	}
	
	public STYLECHANGERECORD(byte[] data, int offset, int offsetBit, int FillBits, int LineBits, Object type) {
		
		PackedBitObj pbo = new PackedBitObj(); 
	
		pbo 			= PackedBitObj.readPackedBits(data, offset, offsetBit, 1);
		TypeFlag 		= pbo.value;	
		pbo  			= PackedBitObj.readPackedBits(data, pbo.nextByteIndex, pbo.nextBitIndex, 1);
		StateNewStyles 	= pbo.value;
		pbo				= PackedBitObj.readPackedBits(data, pbo.nextByteIndex, pbo.nextBitIndex, 1);
		StateLineStyle 	= pbo.value;
		pbo 			= PackedBitObj.readPackedBits(data, pbo.nextByteIndex, pbo.nextBitIndex, 1);
		StateFillStyle1 = pbo.value;
		pbo 			= PackedBitObj.readPackedBits(data, pbo.nextByteIndex, pbo.nextBitIndex, 1);
		StateFillStyle0 = pbo.value;
		pbo 			= PackedBitObj.readPackedBits(data, pbo.nextByteIndex, pbo.nextBitIndex, 1);
		StateMoveTo 	= pbo.value;
		
		nextByte	= pbo.nextByteIndex;
		nextBit		= pbo.nextBitIndex;
		
		if(StateMoveTo==1) {
			
			pbo 		= PackedBitObj.readPackedBits(data, nextByte, nextBit, 5);
			MoveBits 	= pbo.value;
			
			pbo 		= PackedBitObj.readPackedBitsS(data, pbo.nextByteIndex, pbo.nextBitIndex, MoveBits);
			MoveDeltaX 	= pbo.value;
			pbo 		= PackedBitObj.readPackedBitsS(data, pbo.nextByteIndex, pbo.nextBitIndex, MoveBits);
			MoveDeltaY 	= pbo.value;
			
			nextByte	= pbo.nextByteIndex;
			nextBit 	= pbo.nextBitIndex;
			
		}
		if(StateFillStyle0==1) {
			
			pbo 		= PackedBitObj.readPackedBits(data, nextByte, nextBit, FillBits);
			FillStyle0 	= pbo.value;
			
			nextByte 	= pbo.nextByteIndex;
			nextBit		= pbo.nextBitIndex;
			
		}
		if(StateFillStyle1==1) {
			
			pbo 		= PackedBitObj.readPackedBits(data, nextByte, nextBit, FillBits);
			FillStyle1 	= pbo.value;
			
			nextByte 	= pbo.nextByteIndex;
			nextBit 	= pbo.nextBitIndex;
			
		}
		if(StateLineStyle==1) {
			
			if(LineBits!=0) {
				pbo 		= PackedBitObj.readPackedBits(data, nextByte, nextBit, LineBits);
				LineStyle 	= pbo.value;
				
				nextByte = pbo.nextByteIndex;
				nextBit = pbo.nextBitIndex;
			} else {
				nextByte++; 
				nextBit = 0;
			}
						
		}
		if(StateNewStyles==1) {
			
			nextByte = pbo.nextByteIndex+1;
			
			FillStyles = new FILLSTYLEARRAY(data, nextByte, type);
			nextByte += FillStyles.length;
			LineStyles = new LINESTYLEARRAY(data, nextByte, type);
			nextByte += LineStyles.length;
			
			NumFillBits = (data[nextByte]>>4) & 0x0F;
			NumLineBits = data[nextByte]	  & 0x0F;
			
			nextByte ++;
			nextBit = 0;
			
		}
				
	}
	
	public void setMoveBits(int mx, int my) {
		MoveBits = 0;
		int[] rect = {mx,my};
		for(int i=0; i<rect.length; i++) {
			MoveBits = Math.max(MoveBits,Integer.toBinaryString(rect[i]).length()+1);	
		}
	}
	
	public void setNumFillBits(int mx, int my) {
		NumFillBits = 0;
		int[] rect = {mx,my};
		for(int i=0; i<rect.length; i++) {
			NumFillBits = Math.max(NumFillBits,Integer.toBinaryString(rect[i]).length()+1);	
		}
	}
	
	public void setNumLineBits(int mx, int my) {
		NumLineBits = 0;
		int[] rect = {mx,my};
		for(int i=0; i<rect.length; i++) {
			NumLineBits = Math.max(NumLineBits,Integer.toBinaryString(rect[i]).length()+1);	
		}
	}
	
	public byte[] toByteArray() {
		
		PackedBitObj pbo = new PackedBitObj();
		
		int flags = TypeFlag|StateNewStyles|StateLineStyle|
					StateFillStyle1|StateFillStyle0|StateMoveTo;
		pbo.writePackedBits(flags,6);
		
		if((flags&HasMoveTo)==HasMoveTo) {
			pbo.writePackedBits(MoveDeltaX,MoveBits);
			pbo.writePackedBits(MoveDeltaY,MoveBits); 	
		}
		if((flags&HasFillStyle0)==HasFillStyle0)
			pbo.writePackedBits(FillStyle0,FillBits);
		if((flags&HasFillStyle1)==HasFillStyle1)
			pbo.writePackedBits(FillStyle1,FillBits);
		if((flags&HasLineStyle)==HasLineStyle)
			pbo.writePackedBits(LineStyle,LineBits);
		
		byte[] bytes = pbo.toByteArray();
		
		// TODO ...
		if((flags&HasNewStyles)==HasNewStyles) {
			
			//byte n = (byte)( (NumFillBits&0x0F)<<4 | (NumLineBits&0x0F) );
		}
		
		return bytes;
	}
	
	public PackedBitObj toByteArray(PackedBitObj pbo) {
		
		int flags = TypeFlag<<7|StateNewStyles<<6|StateLineStyle<<5|
					StateFillStyle1<<4|StateFillStyle0<<3|StateMoveTo<<2;
		pbo.writePackedBits(flags>>2,6);
		
		if((flags&HasMoveTo)==HasMoveTo) {
			pbo.writePackedBits(MoveDeltaX,MoveBits);
			pbo.writePackedBits(MoveDeltaY,MoveBits); 	
		}
		if((flags&HasFillStyle0)==HasFillStyle0)
			pbo.writePackedBits(FillStyle0,FillBits);
		if((flags&HasFillStyle1)==HasFillStyle1)
			pbo.writePackedBits(FillStyle1,FillBits);
		if((flags&HasLineStyle)==HasLineStyle)
			pbo.writePackedBits(LineStyle,LineBits);
		
		if((flags&HasNewStyles)==HasNewStyles) {
			// TODO how can I return a pbo from byte[]?
			//byte[] styles = new byte[1+FillStyles.length+LineStyles.length];
			//styles[0] = (byte)( (NumFillBits&0x0F)<<4 | (NumLineBits&0x0F) );
			//FillStyles.toByteArray()
		}
		
		return pbo;
	}

}
