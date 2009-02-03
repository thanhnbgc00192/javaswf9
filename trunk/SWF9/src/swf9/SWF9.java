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

import javax.swing.event.EventListenerList;

import java.io.*;
import java.util.*;

import swf9.util.comparator.*;
import swf9.util.events.*;
import swf9.util.filter.*;

import swf9.tags.*;
import swf9.tags.bitmaps.*;
import swf9.tags.control.*;
import swf9.tags.shapemorphing.DefineMorphShape;
import swf9.tags.shapes.*;
import swf9.tags.sprites.DefineSprite;
import swf9.tags.text.*;
import swf9.tags.video.*;
import swf9.types.*;

/**
 * @author stefanocottafavi
 */
public class SWF9 {
	
	// Create the listener list
    protected EventListenerList listenerList = new EventListenerList();
    
	FileInputStream fis;
	public byte[] 	swf;
	public int 		length;
	
	public Header 	hdr			= new Header();
	
	FileAttributes	tagFA		= new FileAttributes();
	SetBackgroundColor tagSBC 	= new SetBackgroundColor();
	
	Vector<Tag> tagsDF 	= new Vector<Tag>(0);
	Vector<Tag> tagsDT 	= new Vector<Tag>(0);
	Vector<Tag> tagsDET = new Vector<Tag>(0);

	Vector<Tag> tagsDS 	= new Vector<Tag>(0); 
	Vector<Tag> tagsDS2 = new Vector<Tag>(0);
	Vector<Tag> tagsDS3 = new Vector<Tag>(0);
	Vector<Tag> tagsPO2 = new Vector<Tag>(0);
	Vector<Tag> tagsPO3 = new Vector<Tag>(0);
	Vector<Tag> tagsSF 	= new Vector<Tag>(0);
	
	Vector<Tag> tagsJT 	= new Vector<Tag>(0);
	Vector<Tag> tagsDB 	= new Vector<Tag>(0); 
	Vector<Tag> tagsDB2 = new Vector<Tag>(0);
	Vector<Tag> tagsDB3 = new Vector<Tag>(0); 
	Vector<Tag> tagsDBL = new Vector<Tag>(0);
	Vector<Tag> tagsDBL2= new Vector<Tag>(0);
	Vector<Tag> tagsDVS = new Vector<Tag>(0); 
	Vector<Tag> tagsVF 	= new Vector<Tag>(0);
	
	Vector<Tag> tags = new Vector<Tag>();
	
	// long task variables
	public int totalFrames 	= 0;
	public int currentFrame	= 0;
	
	/**
     * Constructor
     */
	public SWF9() {
		length = 0;
	}
	
	/**
     * SWF9 Constructor.
     * @param file SWF file to parse
     */
	public SWF9(File file) {
		
		length = 0;
		
		hdr = new Header(file);
		
		try {
			swf = new byte[hdr.FileLength];
			fis = new FileInputStream(file);
			fis.read(swf);
			fis.close();
		} catch(IOException e) {
			System.err.println( e );
		}

		parse(swf);

	}
	
	/**
     * SWF9 Constructor.
     * @param data SWF bytes array to parse
     */
	public SWF9(byte[] data) {
		
		length = 0;
		
		swf = data;
		parse(swf);
	}
	    
	/**
     * Make SWF9 from a PNG images sequence.
     * @param srcFolder folder containing the images
     * @param fileIdent substring contained in the images name (allow to isolate one sequence)
     * @param dim string representing SWF frames width x height(ex: '320x240');  
     * @param outFile string output SWF file name 
     */
	public void makePNGs(String srcFolder, String fileIdent, String dim, int fps, String outFile) {
		
		File path 			= new File(srcFolder);
		FileFilter filter 	= new PNGFileFilter(fileIdent);
		String[] size		= dim.split("x");
		File[] frames 		= path.listFiles(filter);
		
		totalFrames 	= frames.length;
		currentFrame 	= 0;
		
		
		// sort by frame index
		Arrays.sort(frames, new FileIndexComparator());
		
		tags.clear();
		tags.add(new FileAttributes(0,1,0));
		tags.add(new SetBackgroundColor());
		for(int Iframe=0,charID=1; Iframe<frames.length; Iframe++,charID+=2) {
			
			currentFrame = Iframe+1;
			
			tags.add(new DefineBitsLossless(frames[Iframe],charID) );
			tags.add(new DefineShape( tags.get(tags.size()-1), charID+1 ));
	        if(charID==1) tags.add(new PlaceObject2( 	1, 
	        											PlaceObject2.HasCharacter |
	        											PlaceObject2.HasMatrix,
	        											charID+1,new MATRIX(),null,0,"",0,null) );
	        else		  tags.add(new PlaceObject2( 	1,
	        											PlaceObject2.HasCharacter |
	        											PlaceObject2.FlagMove,
	        											charID+1,null,null,0,"",0,null) );
	    	tags.add(new ShowFrame()); 
	    	
	    	//dispatch progress event
	    	fireProgressEvent(new ProgressEvent(this,currentFrame*100/totalFrames));
	    		    	
		}
		tags.add(new End());
		
		// SWF header
		hdr = new Header(	Header.SWF_NOT_COMPRESSED, 
							9,
							Integer.parseInt(size[0]),
							Integer.parseInt(size[1]),
							fps, 
							frames.length);
		
		// store total length to Header
		for(int Itag=0; Itag<tags.size(); Itag++) {
			length += tags.get(Itag).getLength();
		}
		length += hdr.length;
		hdr.setFileLength(length);
				
		// write the SWF file
	    write(new File(outFile));
	    
	    // get rid of the munnezza!!!
		Runtime.getRuntime().gc();
		
	}
	
	/**
     * Make SWF9 from a JPEG images sequence (JPEG2 method).
     * @param srcFolder folder containing the images
     * @param fileIdent substring contained in the images name (allow to isolate one sequence)
     * @param dim string representing SWF frames width x height(ex: '320x240');  
     * @param outFile string output SWF file name 
     */
	public void makeJPEG2s(String srcFolder, String fileIdent, String dim, int fps, String outFile) {
		
		File path 			= new File(srcFolder);
		FileFilter filter 	= new JPGFileFilter(fileIdent);
		File[] frames 		= path.listFiles(filter);
			
		makeJPEG2s(frames, dim, fps, outFile);

	}
	
	/**
     * Make SWF9 from a JPEG images sequence (JPEG2 method).
     * @param frames images file array
     * @param dim string representing SWF frames width x height(ex: '320x240');  
     * @param outFile string output SWF file name 
     */
	public void makeJPEG2s(File[] frames, String dim, int fps, String outFile) {
		
		totalFrames 	= frames.length;
		currentFrame 	= 0;
		
		String[] size = dim.split("x");
		
		// sort by frame index
		Arrays.sort(frames, new FileIndexComparator());
					
		tags.clear();
		tags.add(new FileAttributes(0,1,0));
		tags.add(new SetBackgroundColor());
		for(int Iframe=0,charID=1; Iframe<frames.length; Iframe++,charID+=2) {
			
			currentFrame = Iframe+1;
			
			tags.add(new DefineBitsJPEG2(frames[Iframe],charID) );
			tags.add(new DefineShape( tags.get(tags.size()-1), charID+1 ));
	        if(charID==1) tags.add(new PlaceObject2( 	1, 
	        											PlaceObject2.HasCharacter |
	        											PlaceObject2.HasMatrix,
	        											charID+1,new MATRIX(),null,0,"",0,null) );
	        else		  tags.add(new PlaceObject2( 	1,
	        											PlaceObject2.HasCharacter |
	        											PlaceObject2.FlagMove,
	        											charID+1,null,null,0,"",0,null) );
	    	tags.add(new ShowFrame()); 
	    		    	
	    	//dispatch progress event
	    	fireProgressEvent(new ProgressEvent(this,currentFrame*100/totalFrames));
	    		    	
		}
		tags.add(new End());
		
		// SWF header
		hdr = new Header(	Header.SWF_NOT_COMPRESSED, 
							9,
							Integer.parseInt(size[0]),
							Integer.parseInt(size[1]),
							fps, 
							frames.length);
		
		// store total length to Header
		for(int Itag=0; Itag<tags.size(); Itag++)
			length += tags.get(Itag).getLength();

		length += hdr.length;
		hdr.setFileLength(length);
				
		// write the SWF file
	    write(new File(outFile));
	    
	    // get rid of the munnezza!!!
		Runtime.getRuntime().gc();
		
	}
	
	/**
     * Make a SWF file thumbnail.
     * @param file string SWF file name
     * @param frame frame index to take as thumbnail
     */
	public void makeTHUMB(String file, int frame) {
		makeTHUMB(new File(file),frame);
	}
	
	/**
     * Make a SWF file thumbnail.
     * @param file SWF file
     * @param frame frame index to take as thumbnail
     */
	public void makeTHUMB(File file, int frame) {
		
		// TODO: better solution based on PlaceObject tags!!!!!
		frame--;
		if(!tagsDVS.isEmpty()) {

			System.out.print("VIDEO...");
			tagsVF.get(frame).exportImage(file);
			System.out.println("no support yet!");
			return;

		} else if(!tagsDB.isEmpty()) {

			System.out.print("JPEG...");
			tagsDB.get(frame).exportImage(file,(JPEGTables)tagsJT.get(0));
			System.out.println("OK");
			return;

		} else if(!tagsDB2.isEmpty()) {

			System.out.print("JPEG2...");
			tagsDB2.get(frame).exportImage(file);
			System.out.println("OK");
			return;

		} else if(!tagsDB3.isEmpty()) {

			System.out.print("JPEG3...");
			tagsDB3.get(frame).exportImage(file);				
			System.out.println("OK");
			return;

		} else if(!tagsDBL.isEmpty()) {

			System.out.print("PNG...");
			tagsDBL.get(frame).exportImage(file);
			System.out.println("OK");
			return;

		} else if(!tagsDS.isEmpty()) {

			System.out.print("SHAPE...");
			tagsDS.get(frame).exportImage(file);
			System.out.println("OK");
			return;

		} else if(!tagsDS2.isEmpty()) {

			System.out.print("SHAPE2...");
			tagsDS2.get(frame).exportImage(file);
			System.out.println("OK");
			return;

		} else if(!tagsDS3.isEmpty()) {

			System.out.print("SHAPE3...");
			tagsDS3.get(frame).exportImage(file);
			System.out.println("OK");
			return;

		} else {
			System.out.println("WOOT??!!");
			return;
		}		      
		
	}
	
	/**
     * Parse a SWF file.
     * @param data SWF bytes array
     */
	public void parse(byte[] data) {
		
		int i 	= hdr.length;
		Tag tag;
		
		while(i<swf.length-1)
		{		
			tag = new Tag(swf,i);
			
			Class cls = tag.getClass();
			
			// TODO all tags
			switch(tag.getType()) {
				case Tag.FileAttributes		: tags.add(new FileAttributes(swf,i)); 		break;
				case Tag.SetBackgroundColor	: tags.add(new SetBackgroundColor(swf,i));	break;
				case Tag.RemoveObject2		: tags.add(new RemoveObject2(swf,i));		break;
				case Tag.DefineMorphShape	: tags.add(new DefineMorphShape(swf,i));	break;
				case Tag.DefineFont			: tags.add(new DefineFont(swf,i));			break;
				case Tag.DefineFontInfo2	: tags.add(new DefineFontInfo2(swf,i));		break;
				case Tag.DefineText			: tags.add(new DefineText(swf,i));			break;
				case Tag.DefineEditText		: tags.add(new DefineEditText(swf,i));		break;
				case Tag.DefineShape		: tags.add(new DefineShape(swf,i));			break;
				case Tag.DefineShape2		: tags.add(new DefineShape2(swf,i));		break;
				case Tag.DefineShape3		: tags.add(new DefineShape3(swf,i));		break;
				case Tag.PlaceObject		: tags.add(new PlaceObject(swf,i));			break;
				case Tag.PlaceObject2		: tags.add(new PlaceObject2(swf,i));		break;
				case Tag.PlaceObject3		: tags.add(new PlaceObject3(swf,i));		break;
				case Tag.DefineSprite		: tags.add(new DefineSprite(swf,i));		break;
				case Tag.ShowFrame			: tags.add(new ShowFrame(swf,i));			break;
				case Tag.JPEGTables			: tags.add(new JPEGTables(swf,i));
												tagsJT.addElement(tags.lastElement());	break;
				case Tag.DefineBits			: tags.add(new DefineBits(swf,i));
												tagsDB.addElement(tags.lastElement());	break;
				case Tag.DefineBitsJPEG2	: tags.add(new DefineBitsJPEG2(swf,i));
												tagsDB2.addElement(tags.lastElement());	break;
				case Tag.DefineBitsJPEG3	: tags.add(new DefineBitsJPEG3(swf,i));
												tagsDB3.addElement(tags.lastElement());	break;
				case Tag.DefineBitsLossless	: tags.add(new DefineBitsLossless(swf,i));
												tagsDBL.addElement(tags.lastElement());	break;
				case Tag.DefineBitsLossless2: tags.add(new DefineBitsLossless2(swf,i));
												tagsDBL2.addElement(tags.lastElement());break;
				case Tag.DefineVideoStream	: tags.add(new DefineVideoStream(swf,i));
												tagsDVS.addElement(tags.lastElement());	break;
				case Tag.VideoFrame			: tags.add(new VideoFrame(swf,i,(DefineVideoStream)tagsDVS.get(0)));
												tagsVF.addElement(tags.lastElement());	break;
				case Tag.End				: tags.add(new End(swf,i));					break;
				
			}
			i += tag.length;
					
		}
	}
	
	/**
     * Write a SWF file.
     * @param file SWF file to write
     */
	public void write(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(hdr.toByteArray());
			for(int i=0; i<tags.size(); i++)
				fos.write(tags.get(i).toByteArray());
			fos.close();
			
		} catch(IOException e) {	
		}
	}
	
	
	// EVENT
    // This methods allows classes to register for MyEvents
    public void addProgressEventListener(ProgressEventListener listener) {
        listenerList.add(ProgressEventListener.class, listener);
    }
    // This methods allows classes to unregister for MyEvents
    public void removeProgressEventListener(ProgressEventListener listener) {
        listenerList.remove(ProgressEventListener.class, listener);
    }
    
    private void fireProgressEvent(ProgressEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ProgressEventListener.class) {
                ((ProgressEventListener)listeners[i+1]).onProgress(evt);
            }
        }
    }
    
    
	// SERVICE MAIN
	public static void main(String[] args) {	
		//SWF9 swf = new SWF9();
		//swf.makeJPEG2s("E:\\My Videos\\test", "AR","320x240", "testCREATEJPEG2.swf");
		//SWF9 swf = new SWF9(new File("testVIDEO.swf"));
		//swf.makeTHUMB("testTHUMB.jpg",1);
	}

}
