/**
 * 
 */
package examples;

import swf9.SWF9;

/**
 * @author Stefano Cottafavi
 *
 */
public class Example1 {
	
	// build SWF9 from JPEG input frames
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SWF9 swf = new SWF9();
		swf.makeJPEG2s("images/jpeg", "image", "320x240", 25, "swf/exampleJPEG.swf");
		
	}

}
