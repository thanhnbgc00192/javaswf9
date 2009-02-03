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


import java.io.*;


public class SWF9Decompressor extends SWF9Compression
{ 
   public SWF9Decompressor() 
   {
      
   }
   
   public SWF9Decompressor( String name )
   {
      File file = new File( name );
      readFile( file );
   }
   
   public SWF9Decompressor( File file )
   {
      readFile( file );
   }
   
   private void readFile( File file ) 
   {
      FileInputStream fis   = null;
      FileOutputStream fout = null;
      byte[] swf            = new byte[ readFullSize( file ) ];
      try
      {
         fis = new FileInputStream( file );
         fis.read( swf );
         fis.close();
         
         byte[] decomp = uncompress( swf );
         
         fout = new FileOutputStream( file.getAbsoluteFile() );
         fout.write( decomp );
         fout.flush();
         fout.close();
         
      }
      catch ( IOException e )
      {
         System.err.println( e );
      }
   }
   
//   public byte[] uncompress( byte[] bytes ) throws DataFormatException
//   {  
//      Inflater decompressor = new Inflater();
//      decompressor.setInput( strip( bytes ) );//feed the Inflater the bytes
//      
//      ByteArrayOutputStream stream = new ByteArrayOutputStream( bytes.length - 8 );//an expandable byte array to store the uncompressed data
//      
//      byte[] buffer = new byte[1024];
//      
//      int count = 1;
//      while ( count!=0)//read until the end of the stream is found
//      {
//    	 try
//         {
//            count = decompressor.inflate( buffer );//decompress the data into the buffer
//            stream.write( buffer, 0, count );
//         }
//         catch( DataFormatException dfe )
//         {
//            dfe.printStackTrace();
//         }
//      }
//      try
//      {
//         stream.close();
//      }
//      catch( IOException e )
//      {
//         e.printStackTrace();
//      }
//      
//      //create an array to hold the header and body bytes
//      byte[] swf = new byte[ 8 + stream.size() ];
//      //copy the first 8 bytes which are uncompressed into the swf array
//      System.arraycopy( bytes, 0, swf, 0, 8 );
//      //copy the uncompressed data into the swf array
//      System.arraycopy( stream.toByteArray(), 0, swf, 8, stream.size() );
//      //the first byte of the swf indicates whether the swf is compressed or not
//      swf[0] = 70;
//      
//      return swf;
//   }
   
   
   public static void main( String[] args )
   {
     if ( args.length != 1 )
     {
        System.err.println( "Usage: swf_file" );
     }
     else
     {
        new SWF9Decompressor( args[0] );
     }
   }
   
}
