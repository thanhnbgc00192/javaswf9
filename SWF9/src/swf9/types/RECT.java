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

public class RECT {

	public int Nbits;
	public int Xmin;
	public int Xmax;
	public int Ymin;
	public int Ymax;
	
	public int length;
		
	public RECT() {
		length = 0;		
	}
	
	public RECT(int width, int height) {
		
		Xmin = 0;
		Xmax = pixelsToTwips(width);
		Ymin = 0;
		Ymax = pixelsToTwips(height);
		setNbits(Xmin,Xmax,Ymin,Ymax);
		
		length = (int) Math.ceil((5+(double)Nbits*4)/8);
			
	}
	
	public RECT(int xmin, int xmax, int ymin, int ymax) {
		
		Xmin = pixelsToTwips(xmin);
		Xmax = pixelsToTwips(xmax);
		Ymin = pixelsToTwips(ymin);
		Ymax = pixelsToTwips(ymax);
		setNbits(Xmin,Xmax,Ymin,Ymax);
		
		length = (int) Math.ceil((5+(double)Nbits*4)/8);
		
	}
	
	public RECT(byte[] swf, int offset) {
		
		Nbits = ( ( swf[offset] & 0xff ) >> 3 );
		
		PackedBitObj pbo;
		pbo	= PackedBitObj.readPackedBitsS( swf, offset, 5, Nbits );
		Xmin= pbo.value;
		pbo = PackedBitObj.readPackedBitsS( swf, pbo.nextByteIndex, pbo.nextBitIndex, Nbits );
		Xmax= pbo.value;
		pbo = PackedBitObj.readPackedBitsS( swf, pbo.nextByteIndex, pbo.nextBitIndex, Nbits );
		Ymin= pbo.value;
		pbo = PackedBitObj.readPackedBitsS( swf, pbo.nextByteIndex, pbo.nextBitIndex, Nbits );
		Ymax= pbo.value;

		// RECT length in bytes
		length = pbo.nextByteIndex+1 - offset;
	}
		
	public void setNbits(int xmin, int xmax, int ymin, int ymax) {
		Nbits = 0;
		int[] rect = {xmin,xmax,ymin,ymax};
		for(int i=0; i<rect.length; i++) {
			Nbits = Math.max(Nbits,Integer.toBinaryString(rect[i]).length()+1);	
		}
	}
	
	public int getWidth() {
		return twipsToPixels(Xmax);
	}
	
	public int getHeight() {
		return twipsToPixels(Ymax);
	}
	
	public int twipsToPixels( int twips )
	{
		return twips/20;
	}

	public int pixelsToTwips( int pixels )
	{
		return pixels*20;
	}
	
	public byte[] toByteArray() {
			      
		PackedBitObj pbo = new PackedBitObj();
		pbo.writePackedBits(Nbits,5);
		pbo.writePackedBits(Xmin,Nbits);
		pbo.writePackedBits(Xmax,Nbits);
		pbo.writePackedBits(Ymin,Nbits);
		pbo.writePackedBits(Ymax,Nbits);
		return pbo.bytes;
		
	}
	
}
