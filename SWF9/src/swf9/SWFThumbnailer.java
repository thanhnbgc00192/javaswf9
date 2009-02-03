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

// TODO: deprecated ?!?!?! remove this and update other code with SWF9.makeTHUMB

public class SWFThumbnailer {
	
	public SWFThumbnailer() {
	}

	public SWFThumbnailer( File file, String thumb )
	{
		thumbnailer(file, thumb, 1);
	}
	
	public SWFThumbnailer( File file, String thumb, int frame )
	{
		thumbnailer(file, thumb, frame);
	}


	public SWFThumbnailer( String name, String thumb ) 
	{
		thumbnailer(new File(name), thumb, 1);
	}
	
	public SWFThumbnailer( String name, String thumb, int frame ) 
	{
		thumbnailer(new File(name), thumb, frame);
	}
	
	private void thumbnailer(File file, String thumb, int frame) {
		
		// parse SWF
		SWF9 swf = new SWF9(file);

		// VideoFrame
		if(!swf.tagsDVS.isEmpty()){

			System.out.print("VIDEO...");
			//swf.exportVideoFrame(frame);
			System.out.println("no support yet!");
			return;

			// DefineBits
		} else if(!swf.tagsDB.isEmpty()) {

			System.out.print("JPEG...");
			//swf.exportDefineBits(frame,thumb);
			System.out.println("OK");
			return;

			// DefineBitsJPEG2 tag found!
		} else if(!swf.tagsDB2.isEmpty()){

			System.out.print("JPEG2...");
			//swf.exportDefineBitsJPEG2(frame,thumb);
			System.out.println("OK");
			return;

		} else if(!swf.tagsDB3.isEmpty()){

			System.out.print("JPEG3...");
			//swf.exportDefineBits3(frame,thumb);				
			System.out.println("OK");
			return;

			// PNG sequence
		} else if(!swf.tagsDBL.isEmpty()){

			System.out.print("PNG...");
			//swf.exportDefineBitsLossless(frame,thumb);				
			System.out.println("OK");
			return;

			// SHAPES ONLY	
		} else if(!swf.tagsDS.isEmpty()){

			System.out.print("SHAPE...");
			//swf.exportDefineShape(frame,thumb);
			System.out.println("OK");
			return;

		} else if(!swf.tagsDS2.isEmpty()){

			System.out.print("SHAPE2...");
			//swf.exportDefineShape2(frame,thumb);
			System.out.println("OK");
			return;

		} else if(!swf.tagsDS3.isEmpty()){

			System.out.print("SHAPE3...");
			//swf.exportDefineShape3(frame,thumb);
			System.out.println("OK");
			return;

		} else {
			System.out.println("WOOT??!!");
			return;
		}
	}


//	private void clean() {
//		// clean
//		hdr 			= null;
//		fis				= null;
//		fos				= null;	
//		swf				= null;
//		img				= null;
//		decompressed	= null;
//		buffer			= null;
//		tag				= null;
//	}
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		new SWFThumbnailer( args[0], args[1], Integer.parseInt(args[2]) );  
	}
	
}
