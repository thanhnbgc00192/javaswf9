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

import java.util.Vector;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.*;

import swf9.types.*;

public class ShapeCanvas extends Canvas {
	
	public FILLSTYLE[] fillStyles;
	public LINESTYLE[] lineStyles;
	
	public LINESTYLE currentLineStyle;
	public FILLSTYLE currentFillStyle;
	
	public Graphics2D 	g2d;
	public GeneralPath 	path;
	public BasicStroke 	stroke;
	//public Shape 		shape;
	
	public ShapeCanvas(Graphics graphics) {
		g2d  = (Graphics2D)graphics;
		path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(0,0);
		
		currentLineStyle 	= null;
		currentFillStyle 	= null;
	}
	
	public void buildPath(Vector<Object> edges) {
		
		GeneralPath path2 = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path2.moveTo((float)path.getCurrentPoint().getX(), (float)path.getCurrentPoint().getY());
		
		for(int i=0; i<edges.size(); i++) {
			if(edges.get(i) instanceof STRAIGHTEDGERECORD) {
				path2.lineTo( 	(float)path2.getCurrentPoint().getX() + ((STRAIGHTEDGERECORD)edges.get(i)).DeltaX/20,
								(float)path2.getCurrentPoint().getY() + ((STRAIGHTEDGERECORD)edges.get(i)).DeltaY/20 );
			} else if(edges.get(i) instanceof CURVEDEDGERECORD) {
				path2.quadTo(	(float)path2.getCurrentPoint().getX() + ((CURVEDEDGERECORD)edges.get(i)).ControlDeltaX/20,
								(float)path2.getCurrentPoint().getY() + ((CURVEDEDGERECORD)edges.get(i)).ControlDeltaY/20,
								(float)path2.getCurrentPoint().getX() + ((CURVEDEDGERECORD)edges.get(i)).ControlDeltaX/20 + ((CURVEDEDGERECORD)edges.get(i)).AnchorDeltaX/20,
								(float)path2.getCurrentPoint().getY() + ((CURVEDEDGERECORD)edges.get(i)).ControlDeltaY/20 + ((CURVEDEDGERECORD)edges.get(i)).AnchorDeltaY/20 );
			}
		}
		g2d.fill(path2);
		
		// border
		if(currentLineStyle!=null) {
			g2d.setColor(currentLineStyle.Color);
			stroke = new BasicStroke(currentLineStyle.Width/20);
			g2d.setStroke(stroke);
		}
		
		path.append(path2, false);
		g2d.draw(path);
		
	}
	
	public boolean isClosedPath(Vector<Object> edges) {
		float totX = 0;
		float totY = 0;
		for(int i=0; i<edges.size(); i++) {
			if(edges.get(i) instanceof STRAIGHTEDGERECORD) {
				totX += ((STRAIGHTEDGERECORD)edges.get(i)).DeltaX;
				totY += ((STRAIGHTEDGERECORD)edges.get(i)).DeltaY;
			} else {
				totX += ((CURVEDEDGERECORD)edges.get(i)).AnchorDeltaX;
				totY += ((CURVEDEDGERECORD)edges.get(i)).AnchorDeltaY;
			}
			
		}
		if(totX==0 && totY==0)
			return true;
		return false;
	}
	
	
}
