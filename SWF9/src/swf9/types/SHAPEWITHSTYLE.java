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

import swf9.util.*;

public class SHAPEWITHSTYLE extends SHAPE {
	
	public FILLSTYLEARRAY FillStyles;
	public LINESTYLEARRAY LineStyles;
	
	public SHAPEWITHSTYLE() {
		FillStyles 		= new FILLSTYLEARRAY();
		LineStyles 		= new LINESTYLEARRAY();
		ShapeRecords 	= new SHAPERECORD[0];
		
		length = 0;
	}

	public SHAPEWITHSTYLE(byte[] data, int offset, Object type) {
		
		FillStyles 		= getFillStyles(data, offset, type);
		offset += FillStyles.length;
		LineStyles 		= getLineStyles(data, offset, type);
		offset += LineStyles.length;
		ShapeRecords 	= getShapeRecords(data,offset,type);
		
		length += FillStyles.length + LineStyles.length;
	}
	
	public void addFillStyles(FILLSTYLE[] styles) {
		for(int i=0; i<styles.length; i++) {
			FillStyles.addFillStyle(styles[i]);
			length +=  styles[i].length;
		}
		setNumFillBits();
	}
	
	public void addLineStyles(LINESTYLE[] styles) {
		for(int i=0; i<styles.length; i++) {
			LineStyles.addLineStyle(styles[i]);
			length +=  styles[i].length;
		}
		setNumLineBits();
	}
	
	public void addShapeRecord(SHAPERECORD shape) {	
		SHAPERECORD[] shapes = new SHAPERECORD[ShapeRecords.length+1];
		System.arraycopy(ShapeRecords,0,shapes,0,ShapeRecords.length);
		shapes[ShapeRecords.length] = shape;
		ShapeRecords = shapes;
	}
	
	public FILLSTYLEARRAY getFillStyles(byte[] data, int offset, Object type) {
		FillStyles = new FILLSTYLEARRAY(data,offset,type);
		return FillStyles;
	}
	
	public LINESTYLEARRAY getLineStyles(byte[] data, int offset, Object type) {
		LineStyles = new LINESTYLEARRAY(data,offset,type);
		return LineStyles;
	}
	
	public void setNumFillBits() {
		// TODO and if it's countextended?!?!?!?
		NumFillBits = Integer.toBinaryString(FillStyles.FillStyleCount).length();	
	}
	public void setNumLineBits() {
		// TODO and if it's countextended?!?!?!?
		NumLineBits = Integer.toBinaryString(LineStyles.LineStyleCount).length();	
	}
	
	public byte[] toByteArray() {
		
		PackedBitObj pbo = new PackedBitObj();
		for(int i=0; i<ShapeRecords.length; i++) {
			
			SHAPERECORD record = (SHAPERECORD)ShapeRecords[i];
			if(record instanceof STYLECHANGERECORD) 		record = (STYLECHANGERECORD)record;
			else if(record instanceof ENDSHAPERECORD)		record = (ENDSHAPERECORD)record;
			else if(record instanceof STRAIGHTEDGERECORD)	record = (STRAIGHTEDGERECORD)record;
			else											record = (CURVEDEDGERECORD)record;
			
			pbo = record.toByteArray(pbo);
		}
		byte[] packed = pbo.bytes;
		
		
		byte[] bytes = new byte[FillStyles.length+LineStyles.length+1+packed.length];
		
		int offset = 0;
		System.arraycopy(FillStyles.toByteArray(),0,bytes,offset,FillStyles.length);
		offset += FillStyles.length;
		System.arraycopy(LineStyles.toByteArray(),0,bytes,offset,LineStyles.length);
		offset += LineStyles.length;
		bytes[offset] = (byte)(NumFillBits<<4 | NumLineBits); 
		offset += 1;
		System.arraycopy(packed,0,bytes,offset,packed.length);
		
		return bytes;
	}
}
