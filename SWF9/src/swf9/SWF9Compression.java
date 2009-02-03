package swf9;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import swf9.util.*;

public class SWF9Compression
{
	/**
     * Write SWF tag header.
     * @param bytes tag bytes array
     * @return tag bytes array with header/long header 
     */
	protected static byte[] writeHeader(byte[] tag, int type, int length)
	{
		if(length<63)
			return writeHeaderShort(tag,type,length);
		else
			return writeHeaderLong(tag,type,length);
	}
	
	/**
     * Write SWF tag header to bytes array.
     * @param bytes tag bytes array
     * @return tag bytes array with header 
     */
	protected static byte[] writeHeaderShort(byte[] tag, int type, int length)
	{
		byte[] RecordHeader = ByteUtils.intToByte( (type<<6)&0xFFC0 | (((type<<6)&0xC0)|(length&0x3F)), 2, true);
		System.arraycopy(RecordHeader,0,tag,0,2);
		
		return tag;
	}
	
	/**
     * Write SWF long tag header to bytes array.
     * @param bytes tag bytes array
     * @return tag bytes array with long header 
     */
	protected static byte[] writeHeaderLong(byte[] tag, int type, int length)
	{
		tag = writeHeaderShort(tag, type, 63);
		System.arraycopy(ByteUtils.intToByte(length,4,true),0,tag,2,4);
		
		return tag;
	}
	   
	/**
     * Read type from a SWF tag bytes array.
     * @param bytes tag bytes array
     * @return integer type (see Tag)
     */
	protected static int readTagType( byte[] bytes )
	{
		int s = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return (s >> 6) & 0xFF;
	}
	
	/**
     * Read length from a SWF tag bytes array.
     * @param bytes tag bytes array
     * @return integer size
     */
	protected static int readTagLength( byte[] bytes )
	{
		int s = ByteUtils.byteArrayToShort(ArrayUtils.swapArray(bytes));
		return s & 0x3F;
	}
	
	/**
     * Read extended length from a SWF tag bytes array.
     * @param bytes tag bytes array
     * @return integer size
     */
	protected static int readTagLengthExt( byte[] bytes )
	{
		int s = (int)ByteUtils.byteArrayToLong(ArrayUtils.swapArray(bytes));
		return s;
	}
	
	/**
     * Read size from a SWF file.
     * @param file SWF file
     * @return integer size
     */
	public int readFullSize( File file )
	{
		byte[] temp = new byte[8];

		try
		{
			FileInputStream fis = new FileInputStream( file );
			fis.read( temp );
			fis.close();
		}
		catch ( IOException e )
		{
			System.err.println( e );
		}
		return readSize( temp );
	}
	
	/**
     * Read size from a SWF bytes array.
     * @param bytes SWF byte array
     * @return integer size
     */
	public int readSize( byte[] bytes )
	{
		int s = 0;
		for ( int i = 0; i < 4; i++ )
		{
			s = ( s << 8 ) + bytes[i + 4];
		}

		s = Integer.reverseBytes( s ) - 1;

		return s;
	}

	/**
	 * Strips the uncompressed header bytes from a swf file byte array
	 * @param bytes of the swf
	 * @return bytes array minus the uncompressed header bytes
	 */
	public static byte[] strip( byte[] bytes )
	{
		byte[] compressable = new byte[bytes.length-8];
		System.arraycopy(bytes,8,compressable,0,bytes.length-8);
		return compressable;
	}

	/**
	 * Strips n leading bytes from a byte array.
	 * @param bytes input bytes array
	 * @param n number of leading bytes to strip
	 * @return input array minus n leading bytes
	 */
	public static byte[] strip( byte[] bytes, int n )
	{
		byte[] compressable = new byte[bytes.length-n];
		System.arraycopy(bytes,n,compressable,0,bytes.length-n);
		return compressable;
	}
	
	/**
	 * Compress SWF bytes array using Zlib deflater.
	 * @param bytes input bytes array
	 * @return compressed bytes array
	 */
	public static byte[] compress( byte[] bytes )
	{
		Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
		compressor.setInput(strip(bytes));
		compressor.finish();

		ByteArrayOutputStream stream = new ByteArrayOutputStream(bytes.length-8);

		byte[] buffer = new byte[1024];
		while(!compressor.finished()) {
			int count = compressor.deflate( buffer );
			stream.write( buffer, 0, count );
		}
		try {
			stream.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		//create a byte array that will contain the uncompressed header and the compressed data
		byte[] swf = new byte[8 + stream.size()];
		//the first 8 bytes of the header are uncompressed
		System.arraycopy( bytes, 0, swf, 0, 8 );
		//copy the compressed data from the temporary byte array to its new byte array
		System.arraycopy( stream.toByteArray(), 0, swf, 8, stream.size() );
		//the first byte of the swf indicates whether the swf is compressed or not
		swf[0] = 67;

		return swf;
	}
	
	/**
	 * Uncompress SWF bytes array using Zlib inflater.
	 * @param bytes input bytes array
	 * @return uncompressed bytes array
	 */
	public static byte[] uncompress( byte[] bytes )
	{  
		Inflater decompressor = new Inflater();
		decompressor.setInput( strip( bytes ) );//feed the Inflater the bytes

		ByteArrayOutputStream stream = new ByteArrayOutputStream( bytes.length - 8 );//an expandable byte array to store the uncompressed data

		byte[] buffer = new byte[1024];

		int count = 1;
		while ( count!=0 )//read until the end of the stream is found
		{
			try
			{
				count = decompressor.inflate( buffer );//decompress the data into the buffer
				stream.write( buffer, 0, count );
				if(decompressor.finished()||count==0) break;
			}
			catch( DataFormatException dfe )
			{
				dfe.printStackTrace();
			}
			
		}
		try
		{
			stream.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		//create an array to hold the header and body bytes
		byte[] swf = new byte[ 8 + stream.size() ];
		//copy the first 8 bytes which are uncompressed into the swf array
		System.arraycopy( bytes, 0, swf, 0, 8 );
		//copy the uncompressed data into the swf array
		System.arraycopy( stream.toByteArray(), 0, swf, 8, stream.size() );
		//the first byte of the swf indicates whether the swf is compressed or not
		swf[0] = 70;

		return swf;
	}
	
	/**
	 * Compress generic bytes array using Zlib deflater.
	 * @param bytes input bytes array
	 * @return compressed bytes array
	 */
	public static byte[] deflate(byte[] bytes)
	{
		Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
		compressor.setInput(bytes);
		compressor.finish();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		while(!compressor.finished()) {
			int count = compressor.deflate( buffer );
			stream.write( buffer, 0, count );
		}
		try {
			stream.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return stream.toByteArray();
	}
	
	/**
	 * Decompress generic bytes array using Zlib inflater.
	 * @param bytes input bytes array
	 * @return uncompressed bytes array
	 */
	public static byte[] inflate( byte[] bytes )
	{  
		Inflater decompressor = new Inflater();
		decompressor.setInput(bytes);

		ByteArrayOutputStream stream = new ByteArrayOutputStream(bytes.length);

		byte[] buffer = new byte[1024];

		int count = 1;
		while(count!=0) {
			try {
				count = decompressor.inflate( buffer );
				stream.write( buffer, 0, count );
			} catch(DataFormatException dfe) {
				dfe.printStackTrace();
			}
		}
		try	{
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		return stream.toByteArray();
	}
	
}

