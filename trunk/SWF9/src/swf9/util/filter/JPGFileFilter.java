package swf9.util.filter;
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
import java.io.FileFilter;

public class JPGFileFilter implements FileFilter {
	
	private String match = null;
	
	public JPGFileFilter() {
    }
	
	public JPGFileFilter(String toMatch) {
		match = toMatch;
    }
    
	public boolean accept(File f) {
		if(match!=null)
			return f.getName().startsWith(match) && (f.getName().endsWith("jpg") || f.getName().endsWith("jpeg"));
		return f.getName().endsWith("jpg") || f.getName().endsWith("jpeg");
	}
	
}
