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

public class ArrayUtils {
	
	public static byte[] swapArray(byte[] in) {
		byte[] out = new byte[in.length];
		for(int i=0; i<in.length; i++) {
			out[i] = in[in.length-i-1];
		}
		return out;
	}
	
	/**
	 * Strips n lead bytes from byte array
	 * @param bytes
	 * @param n
	 * @return an array of bytes representing array minus n leading bytes
	 */
	public static byte[] strip( byte[] bytes, int n )
	{
		byte[] stripped = new byte[bytes.length - n];
		System.arraycopy( bytes, n, stripped, 0, bytes.length - n );//fills a byte array with data needing decompression
		return stripped;
	}
	
}
