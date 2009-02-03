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

import swf9.util.ByteUtils;

public class FILLSTYLEARRAY {
	
	public int FillStyleCount;
	public int FillStyleCountExtended;
	public FILLSTYLE[] FillStyles;
	
	public int length = 0;
	
	public FILLSTYLEARRAY() {
		FillStyleCount 			= 0;
		FillStyleCountExtended 	= 0;
		FillStyles				= new FILLSTYLE[1];
		length					= 1;
	}
	
	public FILLSTYLEARRAY(byte[] data, int offset, Object type) {
		
		FillStyleCount 	= getFillStyleCount(data, offset);
		offset++;
		FillStyles 		= getFillStyles(data, offset, type);
		
		// TODO check this! 
		if(FillStyleCountExtended!=0)	length += 5;
		else							length++;

	}
	
	public void addFillStyle(FILLSTYLE style) {
		
		FILLSTYLE[] styles = new FILLSTYLE[FillStyles.length+1];
		
		for(int i=0; i<FillStyles.length; i++)
			styles[i] = FillStyles[i];
		
		styles[FillStyles.length] 	= style;
		FillStyles 					= styles;
		
		// TODO extended length
		FillStyleCount	= FillStyles.length-1;
		
		for(int i=1; i<=FillStyleCount; i++) 
			length += FillStyles[i].length;
		
	}
	
	public void setFillStyles() {
		
	}
	
	public int getFillStyleCount(byte[] data, int offset) {
		
		if(data[offset]==0xFF) {
			FillStyleCountExtended = getFillStyleCountExtended(data);
		}
		return data[offset];
		
	}

	public int getFillStyleCountExtended(byte[] data) {
		byte[] extCount = new byte[2];
		System.arraycopy(data, 1, extCount, 0, 2);
		return ByteUtils.byteArrayToShort(extCount);
	}
	
	public FILLSTYLE[] getFillStyles(byte[] swf, int offset, Object type) {
		
		FILLSTYLE[] styles;
		length = offset;
		
		if(FillStyleCount==0) {
			styles = new FILLSTYLE[1];
			styles[0] = new FILLSTYLE();
		} else if(FillStyleCount<0xFF) {
			styles = new FILLSTYLE[FillStyleCount+1];
		} else {
			styles = new FILLSTYLE[FillStyleCountExtended+1];
			offset += 2;
		}
				
		for(int i=1; i<styles.length; i++) {
			styles[i] = new FILLSTYLE(swf, offset,type);
			offset += styles[i].length;
		}
		
		length = offset-length;
		
		return styles;
		
	}
	
	public byte[] toByteArray() {
		
		byte[] bytes = new byte[length];
		
		bytes[0] = (byte)FillStyleCount;
		
		if(FillStyleCount!=0)
		{
			int offset = 1;
			int l = FillStyleCount;
			if(bytes[0]==0xFF) {	
				System.arraycopy(ByteUtils.intToByte(FillStyleCountExtended,2,true),0,bytes,1,2);
				offset +=2;
				l = FillStyleCountExtended;
			}
			
			for(int i=1; i<=l; i++) {
				System.arraycopy(FillStyles[i].toByteArray(),0,bytes,offset,FillStyles[i].length);
				offset += FillStyles[i].length;
			}
		}
		
		return bytes;
	}
			
}
