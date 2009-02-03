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


public class SWF9Compressor extends SWF9Compression {

   /**
    * 
    */
   public SWF9Compressor()
   {
      
   }
   
   public SWF9Compressor( String name )
   {
      File file = new File( name );
      readFile( file );
   }
   
   public SWF9Compressor( File file )
   {
      readFile( file );
   }
   
   private void readFile( File file )
   {
      FileInputStream fis = null;
      FileOutputStream fout = null;
      
      byte[] swf  = new byte[ readFullSize( file ) ];
      
      try
      {
         fis = new FileInputStream( file );
         fis.read( swf );
         fis.close();

         byte[] temp = compress( swf );

         fout = new FileOutputStream( file.getAbsoluteFile() );
         fout.write( temp );
         fout.flush();
         fout.close();
      }
      catch ( IOException e )
      {
         e.printStackTrace();
      }
   }
   
   /**
    * @param args
    */
   public static void main( String[] args )
   {
      if ( args.length != 1 )
      {
         System.err.println( "Usage: swf_file" );
      }
      else
      {
         new SWF9Compressor( args[0] );  
      }
   }

}

