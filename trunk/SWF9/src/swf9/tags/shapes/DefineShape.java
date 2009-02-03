package swf9.tags.shapes;
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

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import swf9.util.*;
import swf9.tags.Tag;
import swf9.tags.bitmaps.DefineBits;
import swf9.tags.bitmaps.DefineBitsJPEG2;
import swf9.tags.bitmaps.DefineBitsLossless;
import swf9.types.*;
import swf9.util.ShapeCanvas;


public class DefineShape extends Tag {
	
	public int ShapeID;
	public RECT ShapeBounds;
	public SHAPEWITHSTYLE Shapes;
	
	public DefineShape() {
		
		type = DefineShape;
		
		ShapeID		= 0;
		ShapeBounds	= new RECT();
		Shapes 		= new SHAPEWITHSTYLE();
		
	}
	
	public DefineShape(byte[] swf, int offset) {
		
		type = DefineShape;
		getTagLength(swf,offset);
		
		if(RecordHeader==RECORDHEADER_SHORT) 	offset += 2;
		else									offset += 6; 
		getShapeID(swf,offset);
		offset += 2;
		getShapeBounds(swf,offset);
		offset += ShapeBounds.length;
		getShapes(swf,offset,this);
		
	}
	
	public DefineShape(Tag linkedTag, int shapeID) {
		
		type 	= DefineShape;
		ShapeID = shapeID;
				
		if(linkedTag instanceof DefineBitsLossless) {
			// TODO improve!!!
			ShapeBounds	= new RECT( ((DefineBitsLossless)linkedTag).BitmapWidth, ((DefineBitsLossless)linkedTag).BitmapHeight);
			Shapes 		= defineBitsLosslessShape((DefineBitsLossless)linkedTag);
		}
		if(linkedTag instanceof DefineBits) {
			//TODO all cases
		}
		if(linkedTag instanceof DefineBitsJPEG2) {
			ShapeBounds	= new RECT( ((DefineBitsJPEG2)linkedTag).width, ((DefineBitsJPEG2)linkedTag).height);
			Shapes 		= defineBitsJPEG2Shape((DefineBitsJPEG2)linkedTag);
		}
		
		// tag length (+6 is recordheader(long))
		length = 6 + 2 + ShapeBounds.toByteArray().length + Shapes.toByteArray().length;
		
		
	}
	
	public int getShapeID(byte[] swf, int offset) {
		
		byte[] tmp = new byte[2];
		System.arraycopy( swf, offset, tmp, 0, 2);
		ShapeID = (int)ByteUtils.byteArrayToShort(ArrayUtils.swapArray(tmp));
		return ShapeID;
		
	}
	
	public RECT getShapeBounds(byte[] swf, int offset) {
		
		ShapeBounds = new RECT(swf,offset);
		return ShapeBounds;
		
	}
	
	private SHAPEWITHSTYLE defineBitsJPEG2Shape(DefineBitsJPEG2 linkedTag) {
		
		SHAPEWITHSTYLE shapes 	= new SHAPEWITHSTYLE();
		FILLSTYLE[] styles 		= {new FILLSTYLE( FILLSTYLE.NONSMOOTHED_CLIPPED_BITMAP, linkedTag.CharacterID)};
		
		//TODO
		shapes.addFillStyles(styles);
		shapes.addShapeRecord(new STYLECHANGERECORD(STYLECHANGERECORD.HasFillStyle1,
													0,0,
													1,1,0,
													null,null,
													1,0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(ByteUtils.pixelsToTwips(linkedTag.width),0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(0,ByteUtils.pixelsToTwips(linkedTag.height)));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(ByteUtils.pixelsToTwips(-linkedTag.width),0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(0,ByteUtils.pixelsToTwips(-linkedTag.height)));
		shapes.addShapeRecord(new ENDSHAPERECORD());
		
		return shapes;
	}

	private SHAPEWITHSTYLE defineBitsLosslessShape(DefineBitsLossless linkedTag) {
		
		SHAPEWITHSTYLE shapes 	= new SHAPEWITHSTYLE();
		FILLSTYLE[] styles 		= {new FILLSTYLE( FILLSTYLE.NONSMOOTHED_CLIPPED_BITMAP, linkedTag.CharacterID)};
		
		shapes.addFillStyles(styles);
		shapes.addShapeRecord(new STYLECHANGERECORD(STYLECHANGERECORD.HasFillStyle1,
													0,0,
													1,1,0,
													null,null,
													1,0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(ByteUtils.pixelsToTwips(linkedTag.BitmapWidth),0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(0,ByteUtils.pixelsToTwips(linkedTag.BitmapHeight)));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(ByteUtils.pixelsToTwips(-linkedTag.BitmapWidth),0));
		shapes.addShapeRecord(new STRAIGHTEDGERECORD(0,ByteUtils.pixelsToTwips(-linkedTag.BitmapHeight)));
		shapes.addShapeRecord(new ENDSHAPERECORD());
		
		return shapes;
	}
		
	public SHAPEWITHSTYLE getShapes(byte[] swf, int offset, Object type) {
						
		Shapes = new SHAPEWITHSTYLE(swf,offset,type);
		return Shapes;
		
	}
	
	public ShapeCanvas draw(ShapeCanvas canvas) {
		
		// styles
		canvas.fillStyles = Shapes.FillStyles.FillStyles;
		canvas.lineStyles = Shapes.LineStyles.LineStyles;
		
		// edges sequence
		Vector<Object> edges = new Vector<Object>();
		
		for(int i=0; i<Shapes.ShapeRecords.length; i++) {
			
			Object rec = Shapes.ShapeRecords[i];
			
			if(rec instanceof STYLECHANGERECORD) {
				canvas 	= drawStyleChange((STYLECHANGERECORD)rec, edges, canvas);
				edges 	= new Vector<Object>();
			} else if(rec instanceof ENDSHAPERECORD) {
				canvas 	= drawEnd(edges, canvas);
			} else {
				edges.add(rec);
			}		
		}		
		return canvas;
	}
		
	public ShapeCanvas drawStyleChange(	STYLECHANGERECORD styleChange,
										Vector<Object> edges,
										ShapeCanvas canvas) {
		
		if(!edges.isEmpty()) {
			
			if(canvas.currentFillStyle!=null)
				canvas.g2d.setColor(canvas.currentFillStyle.Color);
			else
				canvas.g2d.setColor(null);
			canvas.buildPath(edges);
			
		}		
		
		// NEXT SHAPE
		if(styleChange.StateNewStyles==1) {
			canvas.fillStyles = styleChange.FillStyles.FillStyles;
			canvas.lineStyles = styleChange.LineStyles.LineStyles;
		}
		if(styleChange.StateLineStyle==1) {
			canvas.currentLineStyle = canvas.lineStyles[styleChange.LineStyle];
			if(canvas.currentLineStyle!=null) {
				canvas.g2d.setStroke(new BasicStroke(canvas.currentLineStyle.Width/20));
			}
		}
		if(styleChange.StateFillStyle1==1) {
			canvas.currentFillStyle = canvas.fillStyles[styleChange.FillStyle1];
		}
		if(styleChange.StateFillStyle0==1) {
			canvas.currentFillStyle = canvas.fillStyles[styleChange.FillStyle0];
		}
		if(styleChange.StateMoveTo==1) {
			canvas.path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
			canvas.path.moveTo(	styleChange.MoveDeltaX/20, 
								styleChange.MoveDeltaY/20 );
		}
		
		return canvas;
	}
		
	public ShapeCanvas drawEnd(Vector<Object> edges, ShapeCanvas canvas) {
		
		if(!edges.isEmpty()) {
			
			if(canvas.currentFillStyle!=null)
				canvas.g2d.setColor(canvas.currentFillStyle.Color);
			else
				canvas.g2d.setColor(null);
			canvas.buildPath(edges);
						
		}
		
		return canvas;
		
	}
	
	public void exportImage(File file) {
		
		BufferedImage buf 	= new BufferedImage(ShapeBounds.getWidth(),ShapeBounds.getHeight(),BufferedImage.TYPE_INT_ARGB);
		ShapeCanvas canvas 	= new ShapeCanvas(buf.getGraphics());
		
		canvas.setSize(new Dimension(ShapeBounds.getWidth(),ShapeBounds.getHeight()));
		//canvas.g2d.setPaint(tagSBC.getColor());
		canvas.g2d.fillRect(0,0,buf.getWidth(), buf.getHeight());
		// draw shapes
		canvas = draw(canvas);
		
		try {
			ImageIO.write(buf, "png", file);
			buf = null;
		} catch (IOException e) {
		}
	}

	public byte[] toByteArray() {
		
		
		byte[] bounds = ShapeBounds.toByteArray();
		byte[] shapes = Shapes.toByteArray();
		
		//TODO is always long????
		byte[] bytes = new byte[length];
		bytes = writeHeaderLong(bytes,type,length-6);
		
		int offset = 6; 
		System.arraycopy(ByteUtils.intToByte(ShapeID,2,true),0,bytes,offset,2);
		offset += 2;
		System.arraycopy(bounds,0,bytes,offset,bounds.length);
		offset += bounds.length;
		System.arraycopy(shapes,0,bytes,offset,shapes.length);
		
		return bytes;
	}
	
}
