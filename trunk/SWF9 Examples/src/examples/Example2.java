/**
 * 
 */
package examples;

import swf9.SWF9;

/**
 * @author Stefano Cottafavi
 *
 */
public class Example2 {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SWF9 swf = new SWF9("swf/input.swf");
		swf.makeTHUMB("thumbs/example3.png", 50);
	}

}
