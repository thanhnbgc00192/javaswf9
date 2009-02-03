package swf9.util;
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

import java.io.File;
import java.util.BitSet;

import swf9.types.data.*;

public class ByteUtils {
	
	/**
	 * Converts a 2 byte array of unsigned bytes to an short
	 * @param b an array of 4 unsigned bytes
	 * @return a long representing the unsigned int
	 */
	public static short byteArrayToShort(byte[] b) 
	{
	    short l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    return l;
	}
	
	/**
	 * Converts a 4 byte array of unsigned bytes to an long
	 * @param b an array of 4 unsigned bytes
	 * @return a long representing the unsigned int
	 */
	public static long byteArrayToLong(byte[] b) 
	{
	    long l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    return l;
	}
	
	/**
     * Convert the byte array to an int starting from the given offset.
     *
     * @param b The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
    
	/**
	 * Returns a byte array containing the two's-complement representation of the integer.<br>
	 * The byte array will be in big-endian byte-order with a fixes length of 4
	 * (the least significant byte is in the 4th element).<br>
	 * <br>
	 * <b>Example:</b><br>
	 * <code>intToByteArray(258)</code> will return { 0, 0, 1, 2 },<br>
	 * <code>BigInteger.valueOf(258).toByteArray()</code> returns { 1, 2 }. 
	 * @param integer The integer to be converted.
	 * @return The byte array of length 4.
	 */
	public static byte[] intToByteArray (final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];
		
		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		
		return byteArray;
	}

	public static byte[] intToByte(int input, int Nbyte, boolean flip) {
		
		byte[] b = new byte[Nbyte];
		
		for (int i = 0; i < Nbyte; i++) {
			int offset = (Nbyte - 1 - i) * 8;
			
			// bigEndian
			if(!flip) {
				b[i] = (byte) ((input >>> offset) & 0xFF);
			// littleEndian
			} else {
				b[Nbyte-i-1] = (byte) ((input >>> offset) & 0xFF);
			}
			
		}
	    		
		return b;
		
	}
			
	public static BitSet byte2bit(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i=0; i<bytes.length*8; i++) {
            if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
	
	public static byte[] bit2byte(BitSet bits) {
        byte[] bytes = new byte[bits.length()/8+1];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }
	
	public static int readSize( byte[] bytes ) {
		
		int s = 0;
		for (int i=0; i<4; i++) {
			s = ( s << 8 ) + bytes[i + 4];
		}

		s = Integer.reverseBytes( s ) - 1;

		return s;
	}
	
	public static int twipsToPixels( int twips )
	{
		return twips / 20;
	}

	public static int pixelsToTwips( int pixels )
	{
		return pixels * 20;
	}
	
}
