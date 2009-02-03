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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swf9.tags.Tag;
import swf9.tags.shapes.DefineShape2;
import swf9.util.ShapeCanvas;

public class DefineShape3 extends DefineShape2 {
	
	public DefineShape3() {
		
		type = Tag.DefineShape3;		
		
	}
	
	public DefineShape3(byte[] swf, int offset) {
		
		type = Tag.DefineShape3;
		
		getTagLength(swf,offset);
		
		getShapeID(swf,offset);
		getShapeBounds(swf,offset);
		getShapes(swf,offset,this);
		
	}
	
	// TODO move to the class
	public void exportImage(File file) {
		
		BufferedImage buf 	= new BufferedImage(ShapeBounds.getWidth(),ShapeBounds.getHeight(),BufferedImage.TYPE_INT_ARGB);
		ShapeCanvas canvas 	= new ShapeCanvas(buf.getGraphics());
		
		canvas.setSize(new Dimension(ShapeBounds.getWidth(),ShapeBounds.getHeight()));
		// TODO background
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
	
}
