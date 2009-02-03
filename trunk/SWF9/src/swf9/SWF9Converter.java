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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import swf9.util.ByteUtils;

public class SWF9Converter {
					
	public SWF9Converter(String inName, String outName) {
		
		new SWF9Converter(new File(inName), new File(outName));
				
	}
	
	public SWF9Converter(File inFile, File outFile) {
		
		// check
		if(outFile == null) {
			outFile	= inFile;
		}
		
		// read swf
		byte[] swf 	= new byte[ readFullSize(inFile) ];
		try
		{
			FileInputStream fis = new FileInputStream( inFile );
			fis.read( swf );
			fis.close();
		}
		catch ( IOException e )
		{
			System.err.println( e );
		}
		
		// convert
		byte[] swf9 = converter(swf, new SWFHeader(inFile));
				
	    // write the SWF9 file
	    try {
	    		
		      FileOutputStream fos = new FileOutputStream(outFile);
		      fos.write(swf9);
		      fos.close();
		        
		} catch (IOException e) {
		      e.printStackTrace();
		}
		
	}
	
	public static byte[] converter(byte[] swf, SWFHeader hdr) {
		
		byte[] swf9;
		int fileSize	= (int)hdr.getSize();
		int hdrLength 	= hdr.getLength();
		
		if(hdr.getVersion()<=7 && hdr.getVersion()!=9) {
			
			swf9 = new byte[fileSize+6];
			
			// create header
			System.arraycopy( swf, 0, swf9, 0, hdrLength );
			// update size
			byte[] size9 = ByteUtils.intToByte(fileSize+6, 4, true);
			System.arraycopy( size9, 0, swf9, 4, size9.length );
			
			// create FileAttributes tag
			byte[] attrib = {0x44,0x11,0x08,0x00,0x00,0x00};
			System.arraycopy( attrib, 0, swf9, hdrLength, attrib.length);
			
			// create body
			System.arraycopy(strip(swf,hdrLength), 0, swf9, hdrLength+6, swf.length-hdrLength);
						
		} else {
			
			swf9 = swf;
			
		}
		
		swf9[3] = 0x09;
		return swf9;
				
	}
		
	public static int readFullSize( File name )
	   {
		  byte[] temp = new byte[8];
	      
	      try
	      {
	         FileInputStream fis = new FileInputStream( name );
	         fis.read( temp );
	         fis.close();
	      }
	      catch ( IOException e )
	      {
	         System.err.println( e );
	      }
	      return readSize( temp );
	   }
	
	public static int readSize( byte[] bytes )
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
	    * @param bytes
	    * @param n
	    * @return an array of bytes representing a swf file minus n leading bytes
	    */
	   public static byte[] strip( byte[] bytes, int n )
	   {
	      byte[] stripped = new byte[bytes.length - n];
	      System.arraycopy( bytes, n, stripped, 0, bytes.length - n );//fills a byte array with data needing decompression
	      return stripped;
	   }
	   
	   
	   public static void main(String args[]) {
			
			new SWF9Converter(new File(args[0]),null); 
			
		}
	
}
