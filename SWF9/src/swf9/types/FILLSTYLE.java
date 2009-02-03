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

import java.awt.Color;

import swf9.util.*;

import swf9.tags.*;
import swf9.types.gradients.*;

public class FILLSTYLE {

	public int 		FillStyleType;
	public Color 	Color;
	public MATRIX 	GradientMatrix;
	public GRADIENT	Gradient;
	public int 		BitmapID;
	public MATRIX	BitmapMatrix;
	
	// useful
	public int length = 0;
	
	public final static byte SOLID_FILL 					= 0x00;
	public final static byte LINEAR_GRADIENT_FILL 			= 0x10;
	public final static byte RADIAL_GRADIENT_FILL 			= 0x12;
	public final static byte FOOCAL_RADIAL_GRADIENT_FILL 	= 0x13;
	public final static byte REPEATING_BITMAP_FILL	 		= 0x40;
	public final static byte CLIPPED_BITMAP_FILL 			= 0x41;
	public final static byte NONSMOOTHED_REPEATING_BITMAP 	= 0x42;
	public final static byte NONSMOOTHED_CLIPPED_BITMAP 	= 0x43;
	
	public FILLSTYLE() {
		
	}
	
	public FILLSTYLE(byte[] swf, int offset, Object type) {
		
		length = offset;
		
		FillStyleType = getFillStyleType(swf, offset);
		offset++;
		
		switch(FillStyleType) {
			case SOLID_FILL :
				Color = getColor(swf,offset,type);
				if(((Tag)type).type == Tag.DefineShape3) 	offset += 4;
				else 										offset += 3;
				break;
			case LINEAR_GRADIENT_FILL : //TODO gradient fill
			case RADIAL_GRADIENT_FILL : 
			case FOOCAL_RADIAL_GRADIENT_FILL :
				break;
			case REPEATING_BITMAP_FILL :				
			case CLIPPED_BITMAP_FILL : 
			case NONSMOOTHED_REPEATING_BITMAP :
			case NONSMOOTHED_CLIPPED_BITMAP : 
				getBitmapID(swf,offset);
				offset += 2;
				BitmapMatrix 	= new MATRIX(swf,offset);
				offset += BitmapMatrix.length;
						
		}
		
		length = offset - length;
				
	}
	
	public FILLSTYLE(int fillStyleType, int bitmapID) {
		
		FillStyleType 	= fillStyleType;
		
		// TODO
		switch(FillStyleType) {
			case REPEATING_BITMAP_FILL 	:
			case CLIPPED_BITMAP_FILL 	:
			case NONSMOOTHED_CLIPPED_BITMAP :
				BitmapID		= bitmapID;
				BitmapMatrix	= new MATRIX(1,20,20,0,0,0,0,0);
				length			= 3+BitmapMatrix.length;
		}
		
	}
		
	public int getFillStyleType(byte[] data, int offset) {
		return data[offset];
	}
	
	public Color getColor(byte[] data, int offset, Object type) {
		if(((Tag)type).type == Tag.DefineShape3)
			return new Color((int)data[offset]&0xFF,(int)data[offset+1]&0xFF,(int)data[offset+2]&0xFF,(int)data[offset+3]&0xFF);
		else
			return new Color((int)data[offset]&0xFF,(int)data[offset+1]&0xFF,(int)data[offset+2]&0xFF);
	}
	
	public int getBitmapID(byte[] swf, int offset) {
		
		byte[] bytes = new byte[2];
		System.arraycopy(swf,offset,bytes,0,2);
		BitmapID = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return BitmapID;
		
	}
	public byte[] toByteArray() {
		byte[] bytes = new byte[length];
		bytes[0] = (byte)FillStyleType;
		
		// options TODO verify wich is parent type and adapt conditions!!!!!!!!
		int offset = 1;
		switch(FillStyleType) {
			// TODO all cases
			case SOLID_FILL :
				System.arraycopy(ByteUtils.intToByte(Color.getRGB(),3,false),0,bytes,offset,3);
				offset += 3;
				break;
			case LINEAR_GRADIENT_FILL 			:
			case RADIAL_GRADIENT_FILL 			:
			case FOOCAL_RADIAL_GRADIENT_FILL 	:
				break;
			case REPEATING_BITMAP_FILL	 		:
			case NONSMOOTHED_REPEATING_BITMAP 	:
			case NONSMOOTHED_CLIPPED_BITMAP		:
			case CLIPPED_BITMAP_FILL 			:
				System.arraycopy(ByteUtils.intToByte(BitmapID,2,true),0,bytes,offset,2);
				offset += 2;
				System.arraycopy(BitmapMatrix.toByteArray(),0,bytes,offset,BitmapMatrix.length);
				offset += BitmapMatrix.length;
		}
		
		return bytes;
	}
	
}
