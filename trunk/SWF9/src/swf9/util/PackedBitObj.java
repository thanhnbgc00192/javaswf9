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
package swf9.util;

public class PackedBitObj
{

	public int    bitIndex         = 0;
	public int    byteIndex        = 0;
	public int    value            = 0;
	public int    nextBitIndex     = 0;
	public int    nextByteIndex    = 0;
	public int    nextByteBoundary = 0;
   
	// useful 
	public byte[] bytes = new byte[1];
	public int 	 length = 1;
      
   	public PackedBitObj() { 		
   	}
   	
   	/**
     * @param bitMarker
     *           The index of the last bit read
     * @param byteMarker
     *           The index of the last byte read
     * @param decimalValue
     *           The decimal value of the packed bit sequence          
     */
   	public PackedBitObj( int bitMarker, int byteMarker, int decimalValue )
   	{
   		bitIndex = bitMarker;
   		byteIndex = byteMarker;
   		value = decimalValue;
   		nextBitIndex = bitMarker;

   		if ( bitMarker <= 7 ) {
   			nextBitIndex++;
   			nextByteIndex = byteMarker;
   			nextByteBoundary = byteMarker++;
   		} else {
   			nextBitIndex = 0;
   			nextByteIndex++;
   			nextByteBoundary = nextByteIndex;
   		}
   	}
   
   	public static PackedBitObj readPackedBits( byte[] bytes, int byteMarker,
   			int bitMarker, int length )
   	{
   		int total = 0;
   		int shift = 7 - bitMarker;
   		int counter = 0;
   		int bitIndex = bitMarker;
   		int byteIndex = byteMarker;

   		while ( counter < length )
   		{
   			for ( int i = bitMarker; i < 8; i++ )
   			{
   				int bit = ( ( bytes[byteMarker] & 0xff ) >> shift ) & 1;
   				total = ( total << 1 ) + bit;
   				bitIndex = i;
   				shift--;
   				counter++;

   				if ( counter == length )
   				{
   					break;
   				}
   			}
   			byteIndex = byteMarker;
   			byteMarker++;
   			bitMarker = 0;
   			shift = 7;
   		}
   		return new PackedBitObj( bitIndex, byteIndex, total );
   	}

   	public static PackedBitObj readPackedBitsS( byte[] bytes, int byteMarker,
   			int bitMarker, int length )
   	{
   		int total = 0;
   		int shift = 7 - bitMarker;
   		int counter = 0;
   		int bitIndex = bitMarker;
   		int byteIndex = byteMarker;

   		while ( counter < length )
   		{
   			for ( int i = bitMarker; i < 8; i++ )
   			{
   				int bit = ( ( bytes[byteMarker] & 0xff ) >> shift ) & 1;
   				total = ( total << 1 ) + bit;

   				bitIndex = i;
   				shift--;
   				counter++;

   				if ( counter == length )
   				{
   					break;
   				}
   			}
   			byteIndex = byteMarker;
   			byteMarker++;
   			bitMarker = 0;
   			shift = 7;

   		}

   		// sign extension
   		if(Integer.highestOneBit(total)==(int)Math.pow(2, length-1)) {
   			total|=(0xFFFFFFFF<<length);
   		}

   		return new PackedBitObj( bitIndex, byteIndex, total );
   	}

   	// TODO fixed point....all!!!!
   	public static PackedBitObj readPackedBitsF( byte[] bytes, int byteMarker,
   			int bitMarker, int length )
   	{
   		int total = 0;
   		int shift = 7 - bitMarker;
   		int counter = 0;
   		int bitIndex = bitMarker;
   		int byteIndex = byteMarker;

   		while ( counter < length )
   		{
   			for ( int i = bitMarker; i < 8; i++ )
   			{
   				int bit = ( ( bytes[byteMarker] & 0xff ) >> shift ) & 1;
   				total = ( total << 1 ) + bit;

   				bitIndex = i;
   				shift--;
   				counter++;

   				if ( counter == length )
   				{
   					break;
   				}
   			}
   			byteIndex = byteMarker;
   			byteMarker++;
   			bitMarker = 0;
   			shift = 7;

   		}

   		// sign extension
   		if(Integer.highestOneBit(total)==(int)Math.pow(2, length-1)) {
   			total|=(0xFFFFFFFF<<length);
   		}

   		return new PackedBitObj( bitIndex, byteIndex, total );
   	}

   	public void writePackedBits( int val, int NBits ) {

   		int shift = 7-bitIndex;

   		for(int i=0; i<NBits; i++ ) {
   			bytes[length-1] |= ((val>>(NBits-i-1))&0x01) <<shift;
   			shift--;
   			bitIndex++;

   			if(bitIndex==8) {

   				byte[] tmp = new byte[length+1];
   				System.arraycopy(bytes,0,tmp,0,bytes.length);
   				bytes = tmp;
   				length++;

   				bitIndex = 0;
   				shift = 7;
   			}
   		}

   	}

   	public static byte[] writePackedBits(int[] val)
   	{
   		int Nbits = 0;
   		for(int i=0; i<val.length; i++) {
   			Nbits = Math.max(Nbits,Integer.toBinaryString(val[i]).length()+1);	
   		}

   		byte[] bytes = new byte[(int)Math.ceil((5+(double)Nbits*4)/8)];

   		bytes[0] = (byte)(Nbits<<3);

   		int v = 0;
   		int bitMarker 	= 5;
   		int byteMarker 	= 0;
   		int Ibits = 0;

   		while(byteMarker<bytes.length) {
   			while(bitMarker<8) {
   				if(Ibits<Nbits) {
   					if(v>=val.length)
   						break;
   					bytes[byteMarker] |= ( (val[v]>>((Nbits-1)-Ibits))&1 )<<(7-bitMarker);
   				} else {
   					Ibits=0;
   					v++;
   				}
   				Ibits++;
   				bitMarker++;
   			}
   			byteMarker++;
   			bitMarker = 0;
   		}
   		return bytes;
   	}

   	public byte[] toByteArray() {
   		return bytes;
   	}

   	/**
   	 * @param args
   	 */
   	public static void main(String[] args) {	
   		PackedBitObj pbo = new PackedBitObj();
   		pbo.writePackedBits(0x1F, 5);
   		pbo.writePackedBits(0x0F, 5);
   		//byte[] bytes = pbo.toByteArray();
   	}

}
