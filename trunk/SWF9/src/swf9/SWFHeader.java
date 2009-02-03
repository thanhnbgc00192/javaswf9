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
package swf9;

import java.io.*;

import swf9.util.PackedBitObj;

public class SWFHeader extends SWF9Compression
{

   private String       signature;
   private String       compressionType;
   private int          version;
   private long         size;
   private int          nbits;
   private int          xmax;
   private int          ymax;
   private int          width;
   private int          height;
   private int          frameRate;
   private int          frameCount;
   private int          length;
   private int			rectBytesNum; 

   public static String COMPRESSED   = "compressed";
   public static String UNCOMPRESSED = "uncompressed";
   
   public SWFHeader()
   {
      super();
   }

   public SWFHeader( File file )
   {
      try
      {
         parseHeader( file );
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }


   public SWFHeader( String fileName ) 
   {
      try
      {
         parseHeader( new File( fileName ) );
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }
   
   public String toString() 
   {
      return 	"signature:   " + getSignature() + "\n" +
      			"version:     " + getVersion() + "\n" + 
      			"compression: " + getCompressionType() + "\n" +
      			"size:        " + getSize() + "\n" +
      			"nbits:       " + getNbits() + "\n" +
      			"xmax:        " + getXmax() + "\n" +
      			"ymax:        " + getYmax() + "\n" +
      			"width:       " + getWidth() + "\n" +
      			"height:      " + getHeight() + "\n" +
      			"frameRate:   " + getFrameRate() + "\n" +
      			"frameCount:  " + getFrameCount();
   }
   
   public void parseHeader( File file ) throws Exception
   {  
      FileInputStream fis = null;
      byte[] temp = new byte[readFullSize(file)];//read the whole file in maybe--should maybe read just the header and then determine the true length by looking at the header's size property
      //byte[] temp = new byte[8];//read just the header instead of the whole file
      byte[] swf = null;

      try
      {
         fis = new FileInputStream( file );
         fis.read( temp );
         fis.close();
         
         

         if ( !isSWF( temp ) )
         {
            throw new Exception(
                  "File does not appear to be a swf - incorrect file signature" );
         }
         else
         {
            signature = "" + ( char ) temp[0] + ( char ) temp[1]
                  + ( char ) temp[2];
         }

         if ( isCompressed( temp[0] ) )
         {
            swf = uncompress( temp );
            compressionType = SWFHeader.COMPRESSED;
         }
         else
         {
            swf = temp;
            compressionType = SWFHeader.UNCOMPRESSED;
         }

      }
      catch ( IOException e )
      {
         System.err.println( e );
      }
      
      //System.out.println( "swf byte array length: " + swf.length );
      
      // version is the 4th byte of a swf;
      version = swf[3];

      // bytes 5 - 8 represent the size in bytes of a swf
      size = readSize( swf );

      // Stage dimensions are stored in a rect

      nbits = ( ( swf[8] & 0xff ) >> 3 );

      PackedBitObj pbo = readPackedBits( swf, 8, 5, nbits );
      
      PackedBitObj pbo2 = readPackedBits( swf, pbo.nextByteIndex,
            pbo.nextBitIndex, nbits );

      PackedBitObj pbo3 = readPackedBits( swf, pbo2.nextByteIndex,
            pbo2.nextBitIndex, nbits );

      PackedBitObj pbo4 = readPackedBits( swf, pbo3.nextByteIndex,
            pbo3.nextBitIndex, nbits );

      xmax = pbo2.value;
      ymax = pbo4.value;

      width = convertTwipsToPixels( xmax );
      height = convertTwipsToPixels( ymax );

      int bytePointer = pbo4.nextByteIndex + 2;
      
      // RECT length in bytes
      rectBytesNum = bytePointer-8;
      
      frameRate = swf[bytePointer];
      bytePointer++;
      
      
      int fc1 = swf[bytePointer] & 0xFF;
      bytePointer++;
      
      int fc2 = swf[bytePointer] & 0xFF;
      bytePointer++;
      
      frameCount = ( fc2 << 8 ) + fc1;
      
      // store "full header" length
      length = bytePointer;
      
      
      /*System.out.println( "signature:   " + getSignature() );
      System.out.println( "version:     " + getVersion() );
      System.out.println( "compression: " + getCompressionType() );
      System.out.println( "size:        " + getSize() );
      System.out.println( "nbits:       " + getNbits() );
      System.out.println( "xmax:        " + getXmax() );
      System.out.println( "ymax:        " + getYmax() );
      System.out.println( "width:       " + getWidth() );
      System.out.println( "height:      " + getHeight() );
      System.out.println( "frameRate:   " + getFrameRate() );
      System.out.println( "frameCount:  " + getFrameCount() );*/
      
   }

   public void read( byte[] output, byte[] input, int offset )
   {
      System.arraycopy( input, offset, output, 0, output.length - offset );
   }

   public PackedBitObj readPackedBits( byte[] bytes, int byteMarker,
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

   public int convertTwipsToPixels( int twips )
   {
      return twips / 20;
   }

   public int convertPixelsToTwips( int pixels )
   {
      return pixels * 20;
   }

   public boolean isSWF( byte[] signature )
   {
      String sig = "" + ( char ) signature[0] + ( char ) signature[1]
            + ( char ) signature[2];

      if ( sig.equals( "FWS" ) || sig.equals( "CWS" ) )
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   public boolean isCompressed( int firstByte )
   {
      if ( firstByte == 67 )
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   public boolean isCompressed()
   {
      boolean result = false;
      if ( signature.equalsIgnoreCase( "CWS" ) )
      {
         result = true;
      }
      return result;
   }

   
   /**
    * @param args
    */
   public static void main( String[] args )
   {
      if ( args.length != 1 )
      {
         System.err.println( "usage: swf_file" );
      }
      else
      {
         try
         {
            new SWFHeader( args[0] );
         }
         catch ( Exception e )
         {
            System.err.println( e.getMessage() );
         }
      }

   }

   /**
    * @return the frameCount
    */
   public int getFrameCount()
   {
      return frameCount;
   }

   /**
    * @return the frameRate
    */
   public int getFrameRate()
   {
      return frameRate;
   }

   /**
    * @return the nbits
    */
   public int getNbits()
   {
      return nbits;
   }

   /**
    * @return the signature
    */
   public String getSignature()
   {
      return signature;
   }

   /**
    * @return the size
    */
   public long getSize()
   {
      return size;
   }

   /**
    * @return the version
    */
   public int getVersion()
   {
      return version;
   }

   /**
    * @return the xmax
    */
   public int getXmax()
   {
      return xmax;
   }

   /**
    * @return the ymax
    */
   public int getYmax()
   {
      return ymax;
   }

   /**
    * @return the compressionType
    */
   public String getCompressionType()
   {
      return compressionType;
   }

   /**
    * @return the height
    */
   public int getHeight()
   {
      return height;
   }

   /**
    * @return the width
    */
   public int getWidth()
   {
      return width;
   }
   
   /**
    * @return the SWF Body start index
    */
   public int getLength()
   {
      return length;
   }
   
   /**
    * @return the rectBytesNum
    */
   public int getRectBytesNum()
   {
      return rectBytesNum;
   }

}
