package swf9.util.comparator;
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
import java.util.Comparator;

//compare files index
public class FileIndexComparator implements Comparator<File> {
	public int compare(File file1, File file2) {
		int index1 = file1.getName().indexOf(".", file1.getName().lastIndexOf("_"));
		int index2 = file2.getName().indexOf(".", file2.getName().lastIndexOf("_"));
		int delta = index1 - index2;
		if (delta < 0) return -1;
		else if (delta > 0) return 1;
		return 0;
	}
}
